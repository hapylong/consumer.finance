/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 分期控制层
 * @date 2016年7月13日 下午5:07:55
 * @version V1.0
 */

package com.iqb.consumer.virtual.account.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant.RedisLockConstant;
import com.iqb.consumer.common.enums.RetCodeEnum;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.exception.RequestException;
import com.iqb.consumer.common.utils.sign.EncryptUtils;
import com.iqb.consumer.common.utils.sign.RSAUtils;
import com.iqb.consumer.data.layer.mysql.bean.acc.OpenInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentPlan;
import com.iqb.consumer.finance.cache.kv.KVCache;
import com.iqb.consumer.service.account.AcctInfoService;
import com.iqb.consumer.service.consumer.dto.CalculateAmt;
import com.iqb.consumer.service.consumer.dto.fastBill.FastBillBean;
import com.iqb.consumer.service.inst.IInstInfoService;
import com.iqb.consumer.service.inst.IInstPlanService;
import com.iqb.consumer.virtual.account.config.AccountConifg;
import com.iqb.consumer.virtual.account.constant.Constant;
import com.iqb.consumer.virtual.account.dto.InstallmentDto;
import com.iqb.consumer.virtual.account.dto.MortgageInstDto;
import com.iqb.consumer.virtual.account.utils.ConvertObjectUtils;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@RestController
@RequestMapping("/install")
public class InstallmentController {

    protected static final Logger logger = LoggerFactory.getLogger(InstallmentController.class);
    private static final String UTF_8 = "utf-8";
    @Resource
    private AccountConifg accountConifg;
    @Resource
    private IInstInfoService installmentBizService;
    @Resource
    private AcctInfoService acctInfoService;
    @Resource
    private IInstPlanService instPlanService;
    @Resource
    private KVCache<String> lockLockedCache;

    /**
     * (对外接口) 计算金额明细
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/calculateAmt", method = RequestMethod.POST)
    public Object calculateAmt(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String decryptData;
        try {
            decryptData = EncryptUtils.decrypt(request, accountConifg.getPublicKey());
        } catch (RequestException e) {
            result.put("retCode", e.getCode());
            result.put("retMsg", e.getMsg());
            logger.error("计算解密异常", e);
            return result;
        }
        CalculateAmt calculateAmt = JSONObject.parseObject(decryptData, CalculateAmt.class);
        try {
            result = validateCAmt(calculateAmt);
            // 计划ID存在。
            InstallmentPlan installmentPlan = instPlanService.getByPlanId(calculateAmt.getInstPlan().getPlanId());
            if (installmentPlan == null) {
                throw BizException.CALCULATE_NOTEXIT_PLAN;
            }
            logger.info("计算参数为:{}", JSONObject.toJSONString(calculateAmt));
            Map<String, BigDecimal> returnResult = installmentBizService.calculateAmt(calculateAmt);
            logger.info("计算后的金额:{}", returnResult);
            result.put("result", returnResult);
        } catch (BizException e) {
            result.put("retCode", e.getCode());
            result.put("retMsg", e.getMsg());
            logger.error("计算发送错误", e);
            return result;
        }
        return result;
    }

    /**
     * (对外接口)房贷分期
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/instByMortgage", method = RequestMethod.POST)
    public Object instByMortgage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String decryptData;
        try {
            decryptData = EncryptUtils.decrypt(request, accountConifg.getPublicKey());
        } catch (RequestException e) {
            result.put("retCode", e.getCode());
            result.put("retMsg", e.getMsg());
            logger.error("房贷分期解密异常", e);
            return result;
        }
        logger.info("分期参数为:{}", decryptData);
        MortgageInstDto mortgageInstDto = JSONObject.parseObject(decryptData, MortgageInstDto.class);
        // 判断分期参数是否满足分期要求
        result = valMortgageInfo(mortgageInstDto);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            // 判断是否可分期
            // 行业号存在，即已经开户。
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("openId", mortgageInstDto.getOpenId());
            params.put("regId", mortgageInstDto.getRegId());
            OpenInfo openInfo = acctInfoService.queryAccByParams(params);
            if (openInfo == null) {
                result.put("retCode", RetCodeEnum.OPENACCNOTEXIST.getValue());
                result.put("retMsg", RetCodeEnum.OPENACCNOTEXIST.getDesc());
                return result;
            }
            // 计划ID存在。
            InstallmentPlan installmentPlan = instPlanService.getByPlanId(mortgageInstDto.getPlanId());
            if (installmentPlan == null) {
                result.put("retCode", RetCodeEnum.PLANNOTEXIST.getValue());
                result.put("retMsg", RetCodeEnum.PLANNOTEXIST.getDesc());
                return result;
            }
            // 订单是否存在，如果存在金额之和不能大于总订单金额
            InstallmentInfo installmentInfo = (InstallmentInfo) ConvertObjectUtils.dto2Bean(mortgageInstDto);
            params = new HashMap<String, Object>();
            params.put("orderId", mortgageInstDto.getOrderId());
            params.put("openId", mortgageInstDto.getOpenId());
            installmentInfo = installmentBizService.repeatInstInfo(params, installmentInfo);
            if (installmentInfo != null) {
                // 满足分期条件
                try {
                    installmentInfo.setBaseId(openInfo.getBid());
                    if (installmentPlan.getRepayMethod() == 2) {
                        // 等本等息
                        installmentBizService.saveInstalInfo(installmentInfo);
                    } else {
                        // 先息后本,动态计息
                        installmentBizService.insertMortgageInstInfo(installmentInfo);
                    }
                } catch (Exception e) {
                    logger.error("分期发生异常", e);
                    throw RequestException.INSTDATA_SAVE_EXCEPTION;
                }
            } else {
                result.put("retCode", RetCodeEnum.REPEATINST.getValue());
                result.put("retMsg", RetCodeEnum.REPEATINST.getDesc());
            }
        }
        return result;
    }

    /**
     * (对外接口)分期付款
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/paymentByInstll", method = RequestMethod.POST)
    public Object paymentByInstll(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String lockFlag = lockLockedCache.get(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK_KEY, String.class);
        if (RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK.equals(lockFlag)) {
            result.put("retCode", RetCodeEnum.LOCKINST.getValue());
            result.put("retMsg", RetCodeEnum.LOCKINST.getDesc());
            return result;
        }
        // 接收参数
        Map<String, String> param = new HashMap<String, String>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String instInfo = null;
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement();
            String para = request.getParameter(paraName);
            param.put(paraName, para.trim());
        }
        try {
            // 验签
            String sign = param.get("sign");
            String data = param.get("data");
            byte[] byteData = Base64.decodeBase64(data);
            boolean signFlag = RSAUtils.verify(byteData, accountConifg.getPublicKey(), sign);
            if (signFlag) {
                // 解密
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, accountConifg.getPublicKey());
                instInfo = new String(decodedData);
            } else {
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        logger.info("分期参数为:{}", instInfo);
        InstallmentDto installmentDto = JSONObject.parseObject(instInfo, InstallmentDto.class);
        // 判断分期参数是否满足分期要求
        result = validateInstallInfo(installmentDto);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            // 判断是否可分期
            // 行业号存在，即已经开户。
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("openId", installmentDto.getOpenId());
            params.put("regId", installmentDto.getRegId());
            OpenInfo openInfo = acctInfoService.queryAccByParams(params);
            if (openInfo == null) {
                result.put("retCode", RetCodeEnum.OPENACCNOTEXIST.getValue());
                result.put("retMsg", RetCodeEnum.OPENACCNOTEXIST.getDesc());
                return result;
            }
            // 计划ID存在。
            InstallmentPlan installmentPlan = instPlanService.getByPlanId(installmentDto.getPlanId());
            if (installmentPlan == null) {
                result.put("retCode", RetCodeEnum.PLANNOTEXIST.getValue());
                result.put("retMsg", RetCodeEnum.PLANNOTEXIST.getDesc());
                return result;
            }
            // 订单是否存在，如果存在金额之和不能大于总订单金额
            InstallmentInfo installmentInfo = (InstallmentInfo) ConvertObjectUtils.dto2Bean(installmentDto);
            params = new HashMap<String, Object>();
            params.put("orderId", installmentDto.getOrderId());
            params.put("openId", installmentDto.getOpenId());
            installmentInfo = installmentBizService.repeatInstInfo(params, installmentInfo);
            if (installmentInfo != null) {
                // 满足分期条件
                try {
                    installmentInfo.setBaseId(openInfo.getBid());
                    installmentBizService.insertInstalInfo(installmentInfo);
                } catch (Exception e) {
                    logger.error("分期发生异常", e);
                    throw RequestException.INSTDATA_SAVE_EXCEPTION;
                }
            } else {
                result.put("retCode", RetCodeEnum.REPEATINST.getValue());
                result.put("retMsg", RetCodeEnum.REPEATINST.getDesc());
            }
        }
        logger.info("---分期结果为:{}", result);
        return result;
    }

    /**
     * (对外接口) 保存爱钱帮发送的还款计划
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createRepayPlan", method = RequestMethod.POST)
    public Object createRepayPlan(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 接收参数
        Map<String, String> param = new HashMap<String, String>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String instInfo = null;
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement();
            String para = request.getParameter(paraName);
            param.put(paraName, para.trim());
        }
        try {
            // 验签
            String sign = param.get("sign");
            String data = param.get("data");
            byte[] byteData = Base64.decodeBase64(data);
            boolean signFlag = RSAUtils.verify(byteData, accountConifg.getPublicKey(), sign);
            if (signFlag) {
                // 解密
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, accountConifg.getPublicKey());
                instInfo = new String(decodedData);
            } else {
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        logger.info("分期参数为:{}", instInfo);
        List<FastBillBean> fbbs = JSONObject.parseArray(instInfo, FastBillBean.class);

        for (FastBillBean fbb : fbbs) {
            Map<String, Object> result = new HashMap<String, Object>();
            try {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("openId", fbb.getOpenId());
                params.put("regId", fbb.getRegId());
                OpenInfo openInfo = acctInfoService.queryAccByParams(params);
                if (openInfo == null) {
                    // 默认自动开户
                }
                // 参数效验
                result = legalEfficacy(fbb);
                if (Constant.SUCCESS.equals(result.get("retCode"))) {
                    params.put("orderId", fbb.getOrderId());
                    InstallmentInfo installmentInfo = new InstallmentInfo();
                    installmentInfo.setInstallAmt(fbb.getInstallAmt());
                    installmentInfo = installmentBizService.repeatInstInfo(params, installmentInfo);
                    if (installmentInfo != null) {
                        // 满足分期条件
                        try {
                            result = installmentBizService.fastGenRepayBill(fbb);
                            if (Constant.SUCCESS.equals(result.get("retCode"))) {
                                result.put("orderId", fbb.getOrderId());
                                result.put("retCode", Constant.SUCCESS);
                                result.put("retMsg", Constant.SUCCESS);
                            } else {
                                result.put("orderId", fbb.getOrderId());
                                result.put("retCode", Constant.ERROR);
                            }
                        } catch (Exception e) {
                            logger.error("分期发生异常", e);
                            throw RequestException.INSTDATA_SAVE_EXCEPTION;
                        }
                    } else {
                        result.put("retCode", RetCodeEnum.REPEATINST.getValue());
                        result.put("retMsg", RetCodeEnum.REPEATINST.getDesc());
                    }
                }
            } catch (Exception e) {
                result.put("orderId", fbb.getOrderId());
                result.put("retCode", Constant.ERROR);
            }
            list.add(result);
        }
        return list;
    }

    /**
     * (对内接口)房贷生成分期分摊详情
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/genMortgageDetails", method = RequestMethod.GET)
    public Object genMortgageDetails(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String msgBase64 = null;
        try {
            msgBase64 = URLDecoder.decode(request.getParameter("msg"), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("账单生成参数[msg]编码异常");
        }
        if (StringUtils.isBlank(msgBase64)) {
            throw new RuntimeException("通知请求参数[msg]异常");
        }
        String json = new String(Base64.decodeBase64(msgBase64));
        InstallmentInfo installmentInfo = JSONObject.parseObject(json, InstallmentInfo.class);
        result = installmentBizService.genMortgageInstDetail(installmentInfo);
        return result;
    }

    /**
     * (对内接口)生成分期分摊详情
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/generateInstDetails", method = RequestMethod.GET)
    public Object generateInstDetails(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String msgBase64 = null;
        try {
            msgBase64 = URLDecoder.decode(request.getParameter("msg"), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("账单生成参数[msg]编码异常");
        }
        if (StringUtils.isBlank(msgBase64)) {
            throw new RuntimeException("通知请求参数[msg]异常");
        }
        String json = new String(Base64.decodeBase64(msgBase64));
        InstallmentInfo installmentInfo = JSONObject.parseObject(json, InstallmentInfo.class);
        result = installmentBizService.genInstDetail(installmentInfo);
        return result;
    }

    /**
     * (对内接口)生成分期分摊详情
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/genLSInstDetails", method = RequestMethod.GET)
    public Object generateLSInstDetails(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String msgBase64 = null;
        try {
            msgBase64 = URLDecoder.decode(request.getParameter("msg"), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("账单生成参数[msg]编码异常");
        }
        if (StringUtils.isBlank(msgBase64)) {
            throw new RuntimeException("通知请求参数[msg]异常");
        }
        String json = new String(Base64.decodeBase64(msgBase64));
        InstallmentInfo installmentInfo = JSONObject.parseObject(json, InstallmentInfo.class);
        result = installmentBizService.genLSInstDetail(installmentInfo);
        return result;
    }

    private Map<String, Object> validateCAmt(CalculateAmt calculateAmt) throws BizException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("retCode", Constant.SUCCESS);
        if (calculateAmt == null) {
            throw BizException.CALCULATE_NULL_PARAM;
        } else if (calculateAmt.getInstPlan() == null) {
            throw BizException.CALCULATE_NULL_PLAN;
        } else if (calculateAmt.getAmt() == null || BigDecimal.ZERO.compareTo(calculateAmt.getAmt()) >= 0) {
            throw BizException.CALCULATE_ZERO_AMT;
        }
        return result;
    }

    private Map<String, Object> legalEfficacy(FastBillBean fbb) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("retCode", Constant.SUCCESS);
        if (StringUtils.isBlank(fbb.getOrderId())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "订单号不能为空");
        } else if (StringUtils.isBlank(fbb.getRegId())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "用户手机号不能为空");
        } else if (StringUtils.isBlank(fbb.getOpenId())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "行业号不能为空");
        } else if (StringUtils.isBlank(fbb.getMerchantNo())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "商户号不能为空");
        } else if (BigDecimal.ZERO.compareTo(fbb.getInstallSumAmt()) >= 0) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "放款金额必须大于0");
        } else if (BigDecimal.ZERO.compareTo(fbb.getInstallAmt()) >= 0) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "放款金额必须大于0");
        } else if (BigDecimal.ZERO.compareTo(fbb.getContractAmt()) >= 0) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "合同金额必须大于0");
        } else if (fbb.getInstallTerms() < 1) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "分期期数至少一期");
        }
        return result;
    }

    /** 房贷分期参数效验 **/
    private Map<String, Object> valMortgageInfo(MortgageInstDto mortgageInstDto) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("retCode", Constant.SUCCESS);
        if (StringUtils.isBlank(mortgageInstDto.getRegId())) {//
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "手机号不能为空");
        } else if (StringUtils.isBlank(mortgageInstDto.getOrderId())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "订单号不能为空");
        } else if (StringUtils.isBlank(mortgageInstDto.getOpenId())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "对应行业不能为空");
        } else if (StringUtils.isBlank(mortgageInstDto.getMerchantNo())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "商户号不能为空");
        } else if (mortgageInstDto.getInstDetails() == null || mortgageInstDto.getInstDetails().size() == 0) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "分期详情不能为空");
        } else if (BigDecimal.ZERO.compareTo(mortgageInstDto.getInstallAmt()) >= 0) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "分期金额必须大于0");
        } else if (mortgageInstDto.getInstallTerms() < 1) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "分期期数至少一期");
        } else if (!isValidDate(mortgageInstDto.getOrderDate())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "订单日期格式不对,yyyyMMdd");
        } else if (!isValidDate(mortgageInstDto.getBeginDate())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "起息格式不对,yyyyMMdd");
        } else if (mortgageInstDto.getTakeInterest() != 1 && mortgageInstDto.getTakeInterest() != 2) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "上收息 必须为数字 1,是2,否");
        } else if (mortgageInstDto.getTakeMonth() != 1 && mortgageInstDto.getTakeMonth() != 2) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "上收月供是否为数字 1,是 2,否");
        }
        return result;
    }

    private Map<String, Object> validateInstallInfo(InstallmentDto installmentDto) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("retCode", Constant.SUCCESS);
        if (StringUtils.isBlank(installmentDto.getRegId())) {//
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "手机号不能为空");
        } else if (StringUtils.isBlank(installmentDto.getOrderId())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "订单号不能为空");
        } else if (StringUtils.isBlank(installmentDto.getOpenId())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "对应行业不能为空");
        } else if (StringUtils.isBlank(installmentDto.getMerchantNo())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "商户号不能为空");
        } else if (BigDecimal.ZERO.compareTo(installmentDto.getInstallAmt()) >= 0) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "分期金额必须大于0");
        } else if (installmentDto.getInstallTerms() < 1) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "分期期数至少一期");
        } else if (!isValidDate(installmentDto.getOrderDate())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "订单日期格式不对,yyyyMMdd");
        } else if (!isValidDate(installmentDto.getBeginDate())) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "起息格式不对,yyyyMMdd");
        } else if (installmentDto.getTakeInterest() != 1 && installmentDto.getTakeInterest() != 2) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "上收息 必须为数字 1,是2,否");
        } else if (installmentDto.getTakeMonth() != 1 && installmentDto.getTakeMonth() != 2) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "上收月供是否为数字 1,是 2,否");
        }
        return result;
    }

    /**
     * 判断时间格式 格式必须为“YYYY-MM-dd” 2004-2-30 是无效的 2003-2-29 是无效的
     * 
     * @param sDate
     * @return
     */
    public boolean isValidDate(String str) {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = (Date) formatter.parse(str);
            return str.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

}
