/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账单
 * @date 2016年7月15日 上午9:37:07
 * @version V1.0
 */

package com.iqb.consumer.virtual.account.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant;
import com.iqb.consumer.common.enums.RetCodeEnum;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.exception.RequestException;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.request.RepayAuthenticateRequestMessage;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.sign.DecryptUtils;
import com.iqb.consumer.common.utils.sign.RSAUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfoNormalRepay;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.ShouldDebtDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.StockStatisticsBean;
import com.iqb.consumer.data.layer.mysql.bean.inst.ToRiskBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.request.FinishBillRequestMessage;
import com.iqb.consumer.data.layer.mysql.bean.request.OverDueBillPojo;
import com.iqb.consumer.data.layer.mysql.bean.request.OverDueBillQueryRequestMessage;
import com.iqb.consumer.data.layer.mysql.bean.request.OverDueBillQueryResponseMessage;
import com.iqb.consumer.data.layer.mysql.bean.request.QueryCondition;
import com.iqb.consumer.service.account.AccRepayHistoryService;
import com.iqb.consumer.service.account.AccountInfoService;
import com.iqb.consumer.service.account.AcctInfoService;
import com.iqb.consumer.service.account.BillBizService;
import com.iqb.consumer.service.account.BizSchedulerOverdueService;
import com.iqb.consumer.service.account.BizSchedulerService;
import com.iqb.consumer.service.inst.IInstInfoService;
import com.iqb.consumer.virtual.account.config.AccountConifg;
import com.iqb.consumer.virtual.account.constant.Constant;
import com.iqb.consumer.virtual.account.dto.refund.PaymentDto;
import com.iqb.consumer.virtual.account.dto.refund.RepayList;
import com.iqb.consumer.virtual.account.utils.ConvertObjectUtils;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Controller
@RequestMapping("/bill")
public class BillController {

    protected static final Logger logger = LoggerFactory.getLogger(BillController.class);

    /* 还款类型 */
    private static final String REPAY_MODEL_NORMAL = "normal";// 正常
    private static final String REPAY_MODEL_ALL = "all";// 提前还款
    private static final String REPAY_MODEL_PART = "part";// 部分还款
    private static final String UTF_8 = "utf-8";

    @Resource
    private AccRepayHistoryService accRepayHistoryService;
    @Resource
    private BillBizService billBizService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private BizSchedulerService bizSchedulerService;
    @Resource
    private AcctInfoService acctInfoService;
    @Resource
    private AccountConifg accountConifg;
    @Resource
    private BizSchedulerOverdueService bizSchedulerOverdueService;
    @Resource
    private DecryptUtils decryptUtils;
    @Resource
    private IInstInfoService iInstInfoService;
    @Resource
    private ConvertObjectUtils convertObjectUtils;

    /**
     * 存量统计查询(分页)
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listStockStatisticsPage", method = {RequestMethod.GET, RequestMethod.POST})
    public Object listStockStatisticsPage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("存量统计查询接口，解析传参:{}", jsonStr);
            } else {
                logger.info("存量统计查询接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("存量统计查询接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            PageBean pageBean = billBizService.listStockStatisticsPage(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("result", pageBean);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            logger.error("存量统计查询接口出现异常:{}", e);
        }
        return result;
    }

    /**
     * 存量统计报表(不分页)
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listStockStatistics", method = {RequestMethod.GET, RequestMethod.POST})
    public Object listStockStatistics(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("存量统计查询接口，解析传参:{}", jsonStr);
            } else {
                logger.info("存量统计查询接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("存量统计查询接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            List<StockStatisticsBean> list = billBizService.listStockStatistics(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("result", list);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            logger.error("存量统计查询接口出现异常:{}", e);
        }
        return result;
    }

    /**
     * 财务查询应收明细列表(不分页)
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectShouldDebtDetail2", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectShouldDebtDetail2(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("财务查询应收明细列表接口，解析传参:{}", jsonStr);
            } else {
                logger.info("财务查询应收明细列表接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("财务查询应收明细列表接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            List<ShouldDebtDetail> list = billBizService.selectShouldDebtDetail2(params);
            BigDecimal totalRepayAmt = billBizService.getTotalRepayAmt(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("result", list);
            result.put("totalAmt", totalRepayAmt);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            logger.error("财务查询应收明细列表接口出现异常:{}", e);
        }
        return result;
    }

    /**
     * 财务查询应收明细列表(分页)
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectShouldDebtDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectShouldDebtDetail(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("财务查询应收明细列表接口，解析传参:{}", jsonStr);
            } else {
                logger.info("财务查询应收明细列表接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("财务查询应收明细列表接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            PageBean pageBean = billBizService.selectShouldDebtDetail(params);
            BigDecimal totalRepayAmt = billBizService.getTotalRepayAmt(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("result", pageBean);
            result.put("totalAmt", totalRepayAmt);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            logger.error("财务查询应收明细列表接口出现异常:{}", e);
        }
        return result;
    }

    /**
     * 给风控所需的账单信息 入参：{regId:"手机号", status:"账单状态(非必须)"}
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toRiskBillInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Object toRiskBillInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("给风控所需的账单信息接口，解析传参:{}", jsonStr);
            } else {
                logger.info("给风控所需的账单信息接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("给风控所需的账单信息接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        if (params.get("regId") == null || "".equals(params.get("regId"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "手机号为空，数据不合法");
        } else {
            result.put("retCode", Constant.SUCCESS);
        }
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                List<ToRiskBillInfo> list = billBizService.getToRiskBillInfo(params);
                result.put("billList", list);
            } catch (Exception e) {
                logger.error("给风控所需的账单信息接口出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * (对外接口)提前清算获取相关信息
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/balanceAdvance", method = {RequestMethod.GET, RequestMethod.POST})
    public Object balanceAdvance(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询提前清算接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询提前清算接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询提前清算接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        if (params.get("regId") == null || "".equals(params.get("regId"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "手机号为空，数据不合法");
        } else {
            result.put("retCode", Constant.SUCCESS);
        }
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                result = billBizService.balanceAdvance(params);
            } catch (Exception e) {
                logger.error("查询提前清算接口出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * (对外接口)查询流水
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectRepayHistoryByPages", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectRepayHistoryByPages(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询流水接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询流水接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询流水接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        result = validateParams(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                PageBean pageBean = accRepayHistoryService.selectRepayHistoryByPages(params);
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "查询流水成功");
                result.put("result", pageBean);
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "查询流水出现异常");
                logger.error("查询流水接口出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * (对外接口)查询账单信息(正常还款,待还款,逾期还款)
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryBillInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryBillInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询账单数据接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询查询账单数据接口接口验签失败");
                throw RequestException.BILLQUERY_SIGN_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询提前还款应还金额接口解密参数异常:{}", e);
            throw RequestException.BILLQUERY_DECRYPTION_EXCEPTION;
        }
        PageBean pageBean = null;
        try {
            JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
            // 支持批量订单查询
            String orderId = params.getString("orderId");
            List<String> orderIdS = str2List(orderId);
            params.put("orderId", orderIdS);
            // 格式效验
            result = validateQueryParam(params);
            if (Constant.SUCCESS.equals(result.get("retCode"))) {
                logger.info("查询账单的参数为:{}", params);
                pageBean = billBizService.queryBillByParams(params);
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "查询账单成功");
                result.put("result", pageBean);
            }
        } catch (Exception e) {
            logger.error("查询账单失败", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "查询账单失败");
            result.put("result", pageBean);
        }
        return result;
    }

    /**
     * 通过orderId包含,转化为list
     * 
     * @param orderId
     * @return
     */
    private List<String> str2List(String orderId) {
        List<String> list = null;
        if (orderId != null && orderId.contains(",")) {
            list = Arrays.asList(orderId.split(","));
        } else {
            if (StringUtils.isBlank(orderId)) {
                return null;
            }
            list = new ArrayList<>(1);
            list.add(orderId);
        }
        return list;
    }

    private Map<String, Object> validateQueryParam(JSONObject params) {
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", Constant.SUCCESS);
        if (StringUtils.isBlank(params.getString("pageNum"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "开始页数不能为空");
        } else if (StringUtils.isBlank(params.getString("numPerPage"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "每页显示个数不能为空");
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
            Date date = formatter.parse(str);
            return str.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * (对外接口)查询提前还款应还金额
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectCost4Advance", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectCost4Advance(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询提前还款金额接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询提前还款应还金额接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询提前还款应还金额接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        if (params.get("regId") == null || "".equals(params.get("regId"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "手机号为空，数据不合法");
        } else {
            result.put("retCode", Constant.SUCCESS);
        }
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                result = billBizService.selectCost4Advance(params);
            } catch (Exception e) {
                logger.error("查询提前还款应还金额接口出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * (对外接口) 验证支付的合法性:正常支付必须顺序支付，且前端金额必须与后台计算金额完全一致
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/verifyPayment", method = RequestMethod.POST)
    public Object verifyPayment(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String verifyInfo = null;
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
                verifyInfo = new String(decodedData);
            } else {
                throw RequestException.REPAYPARAMS_SIGN__EXCEPTION;
            }
        } catch (Exception e) {
            throw RequestException.REPAYPARAMS_DECRYPTION_EXCEPTION;
        }
        // 还款参数
        List<PaymentDto> repayVerifyList = null;
        try {
            repayVerifyList = JSONObject.parseArray(verifyInfo, PaymentDto.class);
        } catch (Exception e) {
            throw RequestException.REPAYPARAMS_CONVER2BEAN_EXCEPTION;
        }
        // 效验参数合法性
        result = verifyParams(repayVerifyList);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            return result;
        }
        return result;
    }

    /**
     * @method 效验参数方法
     * @param repayVerifyList
     * @return
     */
    private Map<String, Object> verifyParams(List<PaymentDto> paymentDtoList) {
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", Constant.SUCCESS);
        if (paymentDtoList.size() > 0) {
            BigDecimal allSumAmt = new BigDecimal(0);
            for (PaymentDto paymentDto : paymentDtoList) {
                String orderId = paymentDto.getOrderId();
                BigDecimal sumAmt = getSumAmt(paymentDto.getRepayList());
                List<Integer> repayNoList = getRepayNo(paymentDto.getRepayList());
                if (!judgeSort(repayNoList)) {
                    result.put("retCode", Constant.ERROR);
                    result.put("retMsg", "订单" + orderId + "跨期还款");
                    return result;
                }
                // 每笔订单预还款金额是否一致 1,订单号,序号
                Map<String, Object> params = new HashMap<>();
                params.put("orderId", orderId);
                params.put("list", repayNoList);
                BigDecimal curRepayAmt = billBizService.getSAmtByOrderIdAndRepayNo(params);
                // 金额差在0.5分以内默认为正常还款
                BigDecimal diffAmt = BigDecimalUtil.sub(curRepayAmt, sumAmt).abs();
                if (diffAmt.compareTo(new BigDecimal(0.5)) > 0) {
                    // }
                    // if (sumAmt.compareTo(curRepayAmt) != 0) {
                    result.put("retCode", Constant.ERROR);
                    result.put("retMsg", "订单" + orderId + "还款金额与实际不符");
                    return result;
                }
                // 上期账单确认已经还款完毕
                params = new HashMap<>();
                params.put("orderId", orderId);
                params.put("repayNo", repayNoList.get(0) - 1);
                InstallmentBillInfo instBill = billBizService.getInstBillByOrderIdAndRepayNo(params);
                if (instBill != null) {
                    result.put("retCode", Constant.ERROR);
                    result.put("retMsg", "订单" + orderId + "跨期还款");
                    return result;
                }
                allSumAmt = BigDecimalUtil.add(allSumAmt, sumAmt);
            }
            result.put("retMsg", allSumAmt);
        } else {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "还款验证参数为空");
        }
        return result;
    }

    /**
     * @method 判断连续顺序方法
     * @param repayNoList
     * @return
     */
    private static boolean judgeSort(List<Integer> repayNoList) {
        Collections.sort(repayNoList);
        for (int i = 1; i <= repayNoList.size(); i++) {
            if (i == repayNoList.size()) return true;
            if (repayNoList.get(i) - repayNoList.get(i - 1) != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * @method 放大100倍后还款总金额方法
     * @param list
     * @return
     */
    private static BigDecimal getSumAmt(List<RepayList> list) {
        BigDecimal sumAmt = new BigDecimal(0);
        for (RepayList rl : list) {
            sumAmt = BigDecimalUtil.add(sumAmt, rl.getAmt());
        }
        return BigDecimalUtil.mul(sumAmt, new BigDecimal(100));
    }

    /**
     * @method 获取还款序号方法
     * @param list
     * @return
     */
    private static List<Integer> getRepayNo(List<RepayList> list) {
        List<Integer> resultList = new ArrayList<>();
        for (RepayList rl : list) {
            resultList.add(rl.getRepayNo());
        }
        return resultList;
    }

    /**
     * (对外接口)平账还款接口
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refund", method = {RequestMethod.GET, RequestMethod.POST})
    public Object refund(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", Constant.SUCCESS);
        /* 接收参数 */
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.debug("平账还款接口验签成功,参数为:{}", jsonStr);
            } else {
                logger.error("平账还款接口验签失败");
                throw RequestException.REFUND_SIGN_EXCEPTION;
            }
        } catch (Exception e) {
            logger.error("账单还款接口解密参数异常:{}", e);
            throw RequestException.REFUND_DECRYPTION_EXCEPTION;
        }
        List<PaymentDto> list = null;
        try {
            list = JSONObject.parseArray(jsonStr, PaymentDto.class);
        } catch (Exception e) {
            logger.error("平账还款接口数据转Bean出现异常", e);
            throw RequestException.REFUND_CONVER2BEAN_EXCEPTION;
        }
        if (list != null) {

            Map<String, List<InstallmentBillInfo>> map = ConvertObjectUtils.listDto2ListBean(list);
            String tradeNo = list.get(0).getTradeNo();// 交易流水
            String orderId = list.get(0).getOrderId();// 订单号
            Date repayDate = list.get(0).getRepayDate();// 还款时间
            String merchantNo = list.get(0).getMerchantNo();// 商户号
            String repayModel = list.get(0).getRepayModel();// 还款方式
            String bankCardNo = list.get(0).getBankCardNo();// 银行卡号
            String bankName = list.get(0).getBankName();// 银行名称
            String repayType = list.get(0).getRepayType() == null ? "1" : list.get(0).getRepayType();// 还款模式
            BigDecimal sumAmt = list.get(0).getSumAmt() != null ? list.get(0).getSumAmt() : BigDecimal.ZERO;
            sumAmt = BigDecimalUtil.expand(sumAmt);
            if (REPAY_MODEL_NORMAL.equals(repayModel)) {
                // 正常还款逻辑
                // result = validateInstallInfo(list);
                if (Constant.SUCCESS.equals(result.get("retCode"))) {
                    result = billBizService.normalPrepayment(map, tradeNo, repayType, bankCardNo, bankName);
                }
            } else if (REPAY_MODEL_ALL.equals(repayModel)) {
                // 提前全部还款逻辑
                result =
                        billBizService.allPrepayment(orderId, tradeNo, repayDate, merchantNo, repayType, bankCardNo,
                                bankName, sumAmt);
            } else if (REPAY_MODEL_PART.equals(repayModel)) {
                // 提前部分还款逻辑
                map = convertObjectUtils.listDto2ListBeanForPay(list);
                result = billBizService.partPrepayment(map, tradeNo, repayType, bankCardNo, bankName);
            } else {
                result.put("retCode", Constant.ERROR);
                result.put("retMsg", "不支持该还款模式[" + list.get(0).getRepayModel() + "]");
            }
        }
        return result;
    }

    /**
     * (对外接口)平账接口
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/payment", method = {RequestMethod.GET, RequestMethod.POST})
    public Object payment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();

        /* 接收参数 */
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("账单还款接口，解析传参:{}", jsonStr);
            } else {
                logger.error("账单还款接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.error("账单还款接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        try {
            // result = billBizService.payment(jsonStr);
        } catch (Exception e) {
            logger.error("还款过程出现异常:{}", e);
        }
        return result;
    }

    /**
     * @description 查询还款账单
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/repayBill", method = RequestMethod.GET)
    public Object repayBill(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        // 根据用户手机号，行业号
        String openId = request.getParameter("openId");
        String regId = request.getParameter("regId");
        // 判断用户是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("openId", openId);
        params.put("regId", regId);
        Map<String, String> retMap = acctInfoService.queryAccount(params);
        if (!"00".equals(retMap.get("retCode"))) {
            result.put("retCode", RetCodeEnum.OPENACCNOTEXIST.getValue());
            result.put("retMsg", RetCodeEnum.OPENACCNOTEXIST.getDesc());
            return result;
        }
        // 查询账单

        return result;
    }

    /**
     * @description (对内接口)生成账单
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/genMortgageBills", method = RequestMethod.GET)
    public Object genMortgageBills(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
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
        result = billBizService.createMortgageBill(installmentInfo);
        return result;
    }

    /**
     * @description (对内接口)生成账单
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/generatedBills", method = RequestMethod.GET)
    public Object generatedBills(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
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
        result = billBizService.createBill(installmentInfo);
        return result;
    }

    /**
     * @description (对内接口)生成账单
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/genLSBills", method = RequestMethod.GET)
    public Object genLSBills(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
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
        result = billBizService.createLSBill(installmentInfo);
        return result;
    }

    // /**
    // * @description 批量滞纳金
    // * @param request
    // * @param response
    // * @return
    // */
    // @ResponseBody
    // @RequestMapping(value = "/batchDealyAmt", method = RequestMethod.GET)
    // public Object batchDealyAmt(HttpServletRequest request, HttpServletResponse response) {
    // Map<String, Object> result = new HashMap<String, Object>();
    // String endDate = request.getParameter("endDate");
    // bizSchedulerService.batchCalDelay(endDate);
    // return result;
    // }

    /**
     * (对外接口)查询所有账单接口 分页查询
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/selectBillsByPage", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectBillsByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询账单接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询账单接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询账单接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        result = validateParamsOrderId(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                PageBean pageBean = billBizService.selectBillsByPage(params);
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "查询账单成功");
                result.put("result", pageBean);
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "查询账单出现异常");
                logger.error("查询账单出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * (对外接口)查询最近三期未还款账单
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/selectCurrBills", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectCurrBills(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询当期账单接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询当期账单接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询当期账单接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        result = validateParams(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            String regId = (String) params.get("regId");
            String openId = (String) params.get("openId");
            Object bills = null;
            try {
                bills = billBizService.selectCurrBills(regId, openId);
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "查询当期账单失败");
                logger.error("查询当期账单出现异常:{}", e);
            }
            if (bills != null) {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "查询当期账单成功");
                result.put("result", bills);
            } else {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "未查询到账单");
            }
        }
        return result;
    }

    /**
     * (对外接口)查询一期未还款账单和全部逾期账单
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/selectSpeCurrBills", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectSpeCurrBills(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询当期账单接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询当期账单接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询当期账单接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        result = validateParams(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            String regId = (String) params.get("regId");
            String openId = (String) params.get("openId");
            Object bills = null;
            try {
                bills = billBizService.selectSpeCurrBills(regId, openId);
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "查询当期账单失败");
                logger.error("查询当期账单出现异常:{}", e);
            }
            if (bills != null) {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "查询当期账单成功");
                result.put("result", bills);
            } else {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "未查询到账单");
            }
        }
        return result;
    }

    /**
     * (对外接口)获得当前订单最后还款期数
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRepayNo", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getRepayNo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("获得当前订单最后还款期数接口，解析传参:{}", jsonStr);
            } else {
                logger.info("获得当前订单最后还款期数接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("获得当前订单最后还款期数接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        if (params.get("orderId") == null || "".equals(params.get("orderId"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "订单号为空，数据不合法");
        } else {
            result.put("retCode", Constant.SUCCESS);
        }
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                result = billBizService.getRepayNo((String) params.get("orderId"));
            } catch (Exception e) {
                logger.error("获得当前订单最后还款期数接口出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * @description 批量逾期
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bizSchedulerOverdue", method = {RequestMethod.GET, RequestMethod.POST})
    public Object bizSchedulerOverdue(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        JSONObject params = JSONObject.parseObject(decryptUtils.DecryptFromRequest(request));// 解析信息
        Map<String, Object> cal = new HashMap<>();
        cal.put("startTime", System.currentTimeMillis());
        bizSchedulerOverdueService.calOverdueInterest(params);
        result.put("retCode", AccountConstant.SUCCESS);
        cal.put("endTime", System.currentTimeMillis());
        result.put("retMsg", cal);
        return result;
    }

    /**
     * 
     * Description: 逾期费用计算非复利测试
     * 
     * @param
     * @return Object
     * @throws @Author wangxinbang Create Date: 2016年11月16日 下午3:42:35
     */
    @ResponseBody
    @RequestMapping(value = "/calOverdueInterest4Test", method = {RequestMethod.GET, RequestMethod.POST})
    public Object calOverdueInterest4Test(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        JSONObject params = JSONObject.parseObject(decryptUtils.DecryptFromRequest(request));// 解析信息
        Map<String, Object> cal = new HashMap<>();
        cal.put("startTime", System.currentTimeMillis());
        bizSchedulerOverdueService.calOverdueInterest4Test(params);
        result.put("retCode", AccountConstant.SUCCESS);
        cal.put("endTime", System.currentTimeMillis());
        result.put("retMsg", cal);
        return result;
    }

    /**
     * 
     * Description: (对外接口)终止分期
     * 
     * @param
     * @return Object
     * @throws @Author wangxinbang Create Date: 2016年11月16日 上午10:16:22
     */
    @ResponseBody
    @RequestMapping(value = "/stopPeriodization", method = {RequestMethod.GET, RequestMethod.POST})
    public Object stopPeriodization(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        JSONObject params = JSONObject.parseObject(decryptUtils.DecryptFromRequest(request));// 解析信息
        try {
            billBizService.stopPeriodization(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "终止分期成功");
        } catch (BizException e) {
            logger.error("终止分期异常", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "终止分期异常->" + e.getMsg());
        }
        return result;
    }

    /**
     * 
     * Description: (对外接口)重置分期成功
     * 
     * @param
     * @return Object
     * @throws @Author wangxinbang Create Date: 2016年11月16日 上午11:14:50
     */
    @ResponseBody
    @RequestMapping(value = "/resetPeriodization", method = {RequestMethod.GET, RequestMethod.POST})
    public Object resetPeriodization(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        JSONObject params = JSONObject.parseObject(decryptUtils.DecryptFromRequest(request));// 解析信息
        try {
            // 更新分期信息表中BEGINDATE
            InstallmentInfo installmentInfo = JSONObject.toJavaObject(params, InstallmentInfo.class);
            iInstInfoService.updateInstallmentInfoByCondition(installmentInfo);

            billBizService.resetPeriodization(JSONObject.parseObject(JSONObject.toJSONString(params)));
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "重置分期成功");
        } catch (BizException e) {
            logger.error("终止分期异常", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "重置分期异常->" + e.getMsg());
        }
        return result;
    }

    @SuppressWarnings("unused")
    private Map<String, Object> validateInstallInfo(List<PaymentDto> paymentDtoList) {
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", Constant.SUCCESS);
        // int repayNo = 0; // 还款序号
        for (PaymentDto paymentDto : paymentDtoList) {
            BigDecimal sumAmt = paymentDto.getSumAmt();
            if (BigDecimal.ZERO.compareTo(sumAmt) >= 0) {
                result.put("retCode", Constant.ERROR);
                result.put("retMsg", "总金额必须大于0");
            } else {
                for (RepayList repay : paymentDto.getRepayList()) {
                    BigDecimal amt = repay.getAmt();
                    if (BigDecimal.ZERO.compareTo(amt) >= 0) {
                        result.put("retCode", Constant.ERROR);
                        result.put("retMsg", "账单金额必须大于0");
                    } else {
                        sumAmt = BigDecimalUtil.sub(sumAmt, amt);
                        // repayNo = Math.min(repayNo, repay.getRepayNo());// 获取最小序号
                    }
                }
                if (sumAmt.compareTo(BigDecimal.ZERO) != 0) {
                    result.put("retCode", Constant.ERROR);
                    result.put("retMsg", "总金额与账单金额不符");
                }
            }
            // 不允许跨期还款
            // if (billBizService.checkIntertemporal(paymentDto.getOrderId(),
            // Integer.toString(repayNo)) > 0) {
            // result.put("retCode", Constant.ERROR);
            // result.put("retMsg", "不允许跨期还款");
            // }
        }
        return result;
    }

    private Map<String, Object> validateParams(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        if (params.get("regId") == null || "".equals(params.get("regId"))) {// 校验手机号是否为空
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "手机号为空，数据不合法");
        } else if (params.get("openId") == null || "".equals(params.get("openId"))) {// 校验行业号是否为空
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "行业号为空，数据不合法");
        } else {
            result.put("retCode", Constant.SUCCESS);
        }
        return result;
    }

    private Map<String, Object> validateParamsForOpenId(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        if (params.get("openId") == null || "".equals(params.get("openId"))) {// 校验行业号是否为空
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "行业号为空，数据不合法");
        } else {
            result.put("retCode", Constant.SUCCESS);
        }
        return result;
    }

    /**
     * 
     * Description: 罚息减免接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月14日 上午9:58:45
     */
    @ResponseBody
    @RequestMapping(value = "/deratePenalty", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deratePenalty(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        JSONObject params = JSONObject.parseObject(decryptUtils.DecryptFromRequest(request));// 解析信息
        logger.info("罚息减免接口传入数据:" + JSONObject.toJSONString(params));
        try {
            this.billBizService.deratePenalty(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "罚息减免成功");
        } catch (BizException e) {
            logger.error("罚息减免异常", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "罚息减免异常->" + e.getMsg());
        } catch (Exception e) {
            logger.error("罚息减免异常", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "罚息减免异常->" + e);
        }
        return result;
    }

    /**
     * (对外接口)抵押车业务分成-查询账单信息
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryBillByParamsForMortgage", method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryBillByParamsForMortgage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        /*
         * try { request.setCharacterEncoding("UTF-8"); } catch (UnsupportedEncodingException e) {
         * e.printStackTrace(); }
         */
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("抵押车业务分成-查询账单数据接口，解析传参:{}", jsonStr);
            } else {
                logger.info("抵押车业务分成-查询账单数据接口接口验签失败");
                throw RequestException.BILLQUERY_SIGN_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("抵押车业务分成-查询账单数据接口解密参数异常:{}", e);
            throw RequestException.BILLQUERY_DECRYPTION_EXCEPTION;
        }
        PageBean pageBean = null;
        try {
            JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
            // 格式效验
            result = validateQueryParam(params);
            if (Constant.SUCCESS.equals(result.get("retCode"))) {
                pageBean = billBizService.queryBillByParamsForMortgage(params);
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "抵押车业务分成-查询账单成功");
                result.put("result", pageBean);
            }
        } catch (Exception e) {
            logger.error("抵押车业务分成-查询账单失败", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "抵押车业务分成-查询账单失败");
            result.put("result", pageBean);
        }
        return result;
    }

    /**
     * 查询账户系统-订单要素 违约金 最迟还款日期 当期期数 已还金额
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFactors", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getFactors(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询账户系统-查询订单要素数据接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询账户系统-查询订单要素数据接口接口验签失败");
                throw RequestException.BILLQUERY_SIGN_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询账户系统-查询订单要素数据接口解密参数异常:{}", e);
            throw RequestException.BILLQUERY_DECRYPTION_EXCEPTION;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
            // 格式效验
            if (params.get("orderId") == null || "".equals(params.get("orderId"))) {
                result.put("retCode", Constant.ERROR);
                result.put("retMsg", "订单号为空，数据不合法");
                return result;
            }
            return billBizService.getPrepaymentResponsePojo(params.getString("orderId"),
                    accountConifg.getInterestRate());

        } catch (Exception e) {
            logger.error("查询账户系统-查询订单要素数据失败", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "查询账户系统-查询订单要素数据失败");
            result.put("result", map);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = {"/repay_authenticate"}, method = {RequestMethod.POST})
    public Object repayAuthenticate(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", AccountConstant.ERROR);
        try {
            RepayAuthenticateRequestMessage rarm =
                    (RepayAuthenticateRequestMessage) verify(requestMessage, RepayAuthenticateRequestMessage.class);
            if (rarm == null) {
                response.put("errCode", -1);
                response.put("errMsg", "请求信息错误.");
                return response;
            }
            int result = billBizService.repayAuthenticate(rarm);
            response.put("errCode", result);
            switch (result) {
                case 0:
                    response.put("status", AccountConstant.SUCCESS);
                    break;
                case -1:
                    response.put("errMsg", "请求信息错误.");
                    break;
                case -2:
                    response.put("errMsg", "订单不存在.");
                    break;
                case -3:
                    response.put("errMsg", "请及时还清逾期账单.");
                    break;
                case -4:
                    response.put("errMsg", "请在当期还款日过后再申请，谢谢.");
                    break;
                case -5:
                    response.put("errMsg", "暂无提前还款订单.");
                    break;
                case -6:
                    response.put("errMsg", "未知异常.");
                    break;
                default:
                    response.put("errMsg", "未知异常.");
                    response.put("errCode", -7); // 未知异常 2.
            }
        } catch (RequestException e) {
            response.put("errCode", -8); // 签名异常
            response.put("errMsg", e.getMsg());
        } catch (Throwable e) {
            logger.error("BillController[repayAuthenticate]", e);
            response.put("errCode", -9); // 未知异常 3.
            response.put("errMsg", "未知异常.");
        }
        return response;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object verify(JSONObject requestMessage, Class c) {
        try {
            // 验签
            String sign = requestMessage.getString("sign");
            String data = requestMessage.getString("data");
            byte[] byteData = Base64.decodeBase64(data);
            boolean signFlag = RSAUtils.verify(byteData, accountConifg.getPublicKey(), sign);
            if (signFlag) {
                // 解密
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, accountConifg.getPublicKey());
                String jsonStr = new String(decodedData);
                return JSONObject.parseObject(jsonStr, c);
            } else {
                throw RequestException.BILLQUERY_SIGN_EXCEPTION;
            }
        } catch (Exception e) {
            throw RequestException.BILLQUERY_DECRYPTION_EXCEPTION;
        }
    }

    /**
     * 项目初审---转租回调接口 1:将转租订单用户迁移为新用户。如果包含管理费需要在月供中减掉月供费用 2:将新订单解绑。
     * 3:将账户系统转租账单切换为新用户(inst_info,inst_detail,inst_billinfo,acc_repay_history)
     * 4:有展期的，需要添加对应展期期数，有管理费用的，需要在月供中减掉对应的月供。
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSubletInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getSubletInfo(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("账务系统-以租代购转租，解析传参:{}", jsonStr);
            } else {
                logger.info("账务系统-以租代购转租接口验签失败");
                throw RequestException.BILLQUERY_SIGN_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("账务系统-以租代购转租接口解密参数异常:{}", e);
            throw RequestException.BILLQUERY_DECRYPTION_EXCEPTION;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
            // 格式效验
            if (params.get("subletOrderId") == null || "".equals(params.get("subletOrderId"))) {
                result.put("retCode", Constant.ERROR);
                result.put("retMsg", "订单号为空，数据不合法");
            } else {
                this.billBizService.getSubletInfo(params);
                result.put("retCode", Constant.SUCCESS);
            }
        } catch (Exception e) {
            logger.error("账务系统-以租代购转租数据失败", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "账务系统-以租代购转租数据失败");
            result.put("result", map);
        }
        return result;
    }

    /**
     * 校验账单是否逾期
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/validateBillinfoOverdue", method = {RequestMethod.GET, RequestMethod.POST})
    public Object validateBillinfoOverdue(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> result = new HashMap<>();
        Map<String, String> param = new HashMap<>();

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("账务系统-校验账单是否逾期，解析传参:{}", jsonStr);
            } else {
                logger.info("账务系统-校验账单是否逾期接口验签失败");
                throw RequestException.BILLQUERY_SIGN_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("账务系统-校验账单是否逾期接口解密参数异常:{}", e);
            throw RequestException.BILLQUERY_DECRYPTION_EXCEPTION;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息

            String overdueFlag = billBizService.getOverdueInstallmentBillInfo(params);
            result.put("result", overdueFlag);
        } catch (Exception e) {
            logger.error("账务系统-校验账单是否逾期数据失败", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "账务系统-校验账单是否逾期数据失败");
            result.put("result", map);
        }
        return result;
    }

    /**
     * 查询所有账单接口(中阁使用) 分页查询
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/selectAllBillsByPage", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectAllBillsByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("中阁使用-查询账单接口，解析传参:{}", jsonStr);
            } else {
                logger.info("中阁使用-查询账单接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("中阁使用-查询账单接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        result = validateParamsForOpenId(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                PageBean pageBean = billBizService.listInstallmentBillInfoPage(params);
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "中阁使用-查询账单成功");
                result.put("result", pageBean);
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "中阁使用-查询账单出现异常");
                logger.error("中阁使用-查询账单出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * 
     * Description:校验是否上送订单号
     * 
     * @param objs
     * @param request
     * @return
     */
    private Map<String, Object> validateParamsOrderId(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        if (params.get("orderId") == null || "".equals(params.get("orderId"))) {// 校验订单号是否为空
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "订单号为空，数据不合法");
        } else {
            result.put("retCode", Constant.SUCCESS);
        }
        return result;
    }

    /**
     * 
     * Description:根据orderId repayno获取账单应还总金额
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCurRepayAmtByRepayno", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getCurRepayAmtByRepayno(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("中阁使用-获取指定账单指定期数应还总金额，解析传参:{}", jsonStr);
            } else {
                logger.info("中阁使用-获取指定账单指定期数应还总金额接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("中阁使用-查询账单接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息

        result = validateParamsForOpenId(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                InstallmentBillInfo billInfo = billBizService.getCurRepayAmtByRepayno(params);
                if (billInfo != null) {
                    result.put("retCode", AccountConstant.SUCCESS);
                    result.put("retMsg", "中阁使用-获取指定账单指定期数应还总金额成功");
                    result.put("curRepayAmt", billInfo.getCurRepayAmt());
                    result.put("curRealRepayamt", billInfo.getCurRealRepayamt());
                } else {
                    result.put("retCode", AccountConstant.ERROR);
                    result.put("retMsg", "账户信息为空");
                }
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "中阁使用-获取指定账单指定期数应还总金额出现异常");
                logger.error("中阁使用-获取指定账单指定期数应还总金额出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * 
     * Description:根据订单号 手机号码 还款期数 查询账单信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectBillsByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectBillsByCondition(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("根据订单号 手机号码 还款期数 查询账单信息接口，解析传参:{}", jsonStr);
            } else {
                logger.info("根据订单号 手机号码 还款期数 查询账单信息接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("根据订单号 手机号码 还款期数 查询账单信息接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        result = validateParamsOrderId(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            try {
                List<InstallmentBillInfoNormalRepay> list = billBizService.selectBillsByCondition(params);
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "查询账单成功");
                result.put("result", list);
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "根据订单号 手机号码 还款期数 查询账单信息出现异常");
                logger.error("根据订单号 手机号码 还款期数 查询账单信息出现异常:{}", e);
            }
        }
        return result;
    }

    /**
     * 
     * Description: 通过订单号和，期数查询 逾期订单 供中阁使用
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月20日 下午5:25:59
     */
    @ResponseBody
    @RequestMapping(value = "/overdue_bill_query", method = {RequestMethod.POST})
    public Object overdueBillQuery(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        OverDueBillQueryRequestMessage odbq = null;
        try {
            odbq = (OverDueBillQueryRequestMessage) SignUtil.
                    chatDecode(requestMessage, OverDueBillQueryRequestMessage.class, accountConifg.getPublicKey());
        } catch (Exception e) {
            logger.error("invalid request Msg.", e);
            return new Response("fail", "请求参数解密失败.");
        }

        try {
            List<QueryCondition> conditions = odbq.getConditions();
            if (conditions == null || conditions.size() == 0) {
                return new Response("fail", "请求参数非法.");
            }
            OverDueBillQueryResponseMessage responseMessage = new OverDueBillQueryResponseMessage();
            List<OverDueBillPojo> bills = new ArrayList<>();
            for (QueryCondition qc : conditions) {
                try {
                    OverDueBillPojo odb = billBizService.overdueBillQuery(qc);
                    if (odb == null) {
                        responseMessage.appendMsg(qc.getOrderId()).appendMsg(";");
                        continue;
                    }
                    bills.add(odb);
                } catch (Throwable e) {
                    logger.error("overdueBillQuery db error.", e);
                    responseMessage.appendMsg(qc.getOrderId()).appendMsg(";");
                }

            }
            responseMessage.setBills(bills);
            return new Response("ok", responseMessage);
        } catch (Exception e) {
            logger.error("overdueBillQuery error:", e);
            return new Response("fail", "未知异常.");
        }
    }

    /**
     * 
     * Description:查询每日到期账单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectEveryDayExpireBill", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectEveryDayExpireBill(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询每日到期账单信息接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询每日到期账单息接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询每日到期账单信息接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            PageBean pageBean = billBizService.selectEveryDayExpireBill(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "查询每日到期账单成功");
            result.put("result", pageBean);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "查询每日到期账单息出现异常");
            logger.error("查询每日到期账单信息出现异常:{}", e);
        }
        return result;
    }

    /**
     * (api标准接口使用)查询最近三期未还款账单
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getLastThreeOrderInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getLastThreeOrderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询当期账单接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询当期账单接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询当期账单接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        result = validateParams(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            String regId = (String) params.get("regId");
            String openId = (String) params.get("openId");
            String orderId = (String) params.get("orderId");
            Object bills = null;
            try {
                bills = billBizService.selectCurrBills(regId, openId, orderId);
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "查询当期账单失败");
                logger.error("查询当期账单出现异常:{}", e);
            }
            if (bills != null) {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "查询当期账单成功");
                result.put("result", bills);
            } else {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "未查询到账单");
            }
        }
        return result;
    }
    /**
     * 
     * Description: FINANCE-2249 车辆状态跟踪---已出售的车辆流程结束后，订单状态更新为已结清（以流程结束时间为准）
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年11月6日 下午3:05:48
     */
    @ResponseBody
    @RequestMapping(value = "/finishBill", method = {RequestMethod.POST})
    public Object finishBill(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        FinishBillRequestMessage fbrm = null;
        try {
            fbrm = (FinishBillRequestMessage) SignUtil.chatDecode(requestMessage,
                    FinishBillRequestMessage.class, accountConifg.getPublicKey());
            if (fbrm == null || !fbrm.check()) {
                return new Response("fail", "请求参数解密失败.");
            }
        } catch (Exception e) {
            logger.error("invalid request Msg.", e);
            return new Response("fail", "请求参数解密失败.");
        }

        try {
            return new Response("ok", billBizService.finishBill(fbrm));
        } catch (InvalidParameterException e) {
            logger.error("finishBill error:", e);
            return new Response("fail", e.getMessage());
        } catch (Exception e) {
            logger.error("finishBill error:", e);
            return new Response("fail", "未知异常.");
        }
    }

    class Response {
        String status;
        String errMsg;
        Object result;

        public Response(String status, String errMsg) {
            this.status = status;
            this.errMsg = errMsg;
        }

        public Response(String status, Object result) {
            this.status = status;
            this.result = result;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

    }

    /**
     * 
     * Description:查询房贷分期详细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月14日
     */
    @ResponseBody
    @RequestMapping(value = "/getMorgateOrderInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMorgateOrderInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("查询房贷分期详细信息接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询房贷分期详细信息接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询房贷分期详细信息接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        result = validateParamsForOrderId(params);
        if (Constant.SUCCESS.equals(result.get("retCode"))) {
            Map<String, Object> map = new HashMap<>();

            String orderId = (String) params.get("orderId");
            map.put("orderId", orderId);
            List<Map<String, Object>> bills = null;
            try {
                bills = billBizService.getMorgateOrderInfo(map);
            } catch (Exception e) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("retMsg", "查询房贷分期详细信息账单失败");
                logger.error("查询房贷分期详细信息出现异常:{}", e);
            }
            if (bills != null) {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "查询房贷分期详细信息成功");
                result.put("result", bills);
            } else {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("retMsg", "未查询到房贷分期详细信息");
            }
        }
        return result;
    }

    /**
     * 
     * Description:校验订单号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月14日
     */
    private Map<String, Object> validateParamsForOrderId(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        if (params.get("orderId") == null || "".equals(params.get("orderId"))) {// 校验订单号是否为空
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "订单号为空，数据不合法");
        } else {
            result.put("retCode", Constant.SUCCESS);
        }
        return result;
    }

    /**
     * 
     * Description:根据订单获取订单账单状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月8日
     */
    @ResponseBody
    @RequestMapping(value = "/getBillStatusByOrderIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getBillStatusByOrderIds(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("根据订单号获取账单状态接口，解析传参:{}", jsonStr);
            } else {
                logger.info("根据订单号获取账单状态接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("根据订单号获取账单状态接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            List<Map<String, Object>> list = billBizService.getBillStatusByOrderIds(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "根据订单号获取账单状态接口成功");
            result.put("result", list);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "根据订单号获取账单状态接口出现异常");
            logger.error("根据订单号获取账单状态接口出现异常:{}", e);
        }
        return result;
    }

    /**
     * 
     * Description:已逾期且逾期天数小于等于5天的账单
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月23日 16:12:13
     */
    @ResponseBody
    @RequestMapping(value = "/queryBillLastDateThree", method = {RequestMethod.GET, RequestMethod.POST})
    private Object queryBillLastDateThree(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<InstallmentBillInfo> billQueryAllDtoList = billBizService.queryBillLastDateThree(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "已逾期且逾期天数小于等于5天的账单成功");
            result.put("result", billQueryAllDtoList);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "根据订单号获取账单状态接口出现异常");
            logger.error("已逾期且逾期天数小于等于5天的账单出现异常:{}", e);
        }
        return result;
    }

    /**
     * 
     * Description:批量根据订单号还有当前期数查询账单信息
     * 
     * @author chengzhen
     * @param objs
     * @param 2018年4月25日 10:36:12
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryBillInfoByOrderIdPage", method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryBillInfoByOrderIdPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("批量根据订单号还有当前期数查询账单信息接口，解析传参:{}", jsonStr);
            } else {
                logger.info("批量根据订单号还有当前期数查询账单信息接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("批量根据订单号还有当前期数查询账单信息接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            List<InstallmentBillInfo> billQueryAllDtoList = billBizService.selectEveryDayExpireBill2(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "获取账单数据成功");
            result.put("result", billQueryAllDtoList);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "批量根据订单号还有当前期数查询账单信息出现异常");
            logger.error("批量根据订单号还有当前期数查询账单信息出现异常:{}", e);
        }
        return result;
    }

    /**
     * 
     * Description:取已逾期且逾期天数大于5天的订单号
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月2日 14:55:11
     */
    @ResponseBody
    @RequestMapping(value = "/queryBillLastDateFive", method = {RequestMethod.GET, RequestMethod.POST})
    private Object queryBillLastDateGreaterThanFive(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<InstallmentBillInfo> billQueryAllDtoList = billBizService.queryBillLastDateGreaterThanFive(params);
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            for (InstallmentBillInfo installmentBillInfo : billQueryAllDtoList) {
                map.put("orderId", installmentBillInfo.getOrderId());
                list.add(map);
            }
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "取已逾期且逾期天数大于5天的订单号开始");
            result.put("result", billQueryAllDtoList);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "取已逾期且逾期天数大于5天的订单号出现异常");
            logger.error("取已逾期且逾期天数大于5天的订单号出现异常:{}", e);
        }
        return result;
    }

    /**
     * 
     * 获取以租代购所有待还款账单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    @ResponseBody
    @RequestMapping(value = "/selectLatelyThreeDaysBill", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectLatelyThreeDaysBill(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("获取以租代购所有待还款账单，解析传参:{}", jsonStr);
            } else {
                logger.info("获取以租代购所有待还款账单接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("获取以租代购所有待还款账单接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            PageBean pageBean = billBizService.selectLatelyThreeDaysBill(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "获取以租代购所有待还款账单成功");
            result.put("result", pageBean);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "获取以租代购所有待还款账单出现异常");
            logger.error("获取距当前系统时间三天的账单息出现异常:{}", e);
        }
        return result;
    }

    /**
     * 
     * Description:根据订单号 期数查询账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月2日
     */
    @ResponseBody
    @RequestMapping(value = "/getInstallmentBillInfoByOrderId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getInstallmentBillInfoByOrderId(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("根据订单号 期数查询账单信息，解析传参:{}", jsonStr);
            } else {
                logger.info("根据订单号 期数查询账单信息接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("根据订单号 期数查询账单信息接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            InstallmentBillInfo bean = billBizService.getInstallmentBillInfoByOrderId(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "根据订单号 期数查询账单信息成功");
            result.put("result", bean);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "根据订单号 期数查询账单信息出现异常");
            logger.error("根据订单号 期数查询账单信息出现异常:{}", e);
        }
        return result;
    }

    /**
     * 
     * Description:根据订单号修改账单信息表客户接收短信号码
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月24日
     */
    @ResponseBody
    @RequestMapping(value = "/batchUpdateBillSmsMobile", method = {RequestMethod.GET, RequestMethod.POST})
    public Object batchUpdateBillSmsMobile(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("根据订单号修改账单信息表客户接收短信号码，解析传参:{}", jsonStr);
            } else {
                logger.info("根据订单号修改账单信息表客户接收短信号码接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("根据订单号修改账单信息表客户接收短信号码接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            int resultValue = billBizService.batchUpdateBillSmsMobile(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "根据订单号修改账单信息表客户接收短信号码成功");
            result.put("result", resultValue);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "根据订单号修改账单信息表客户接收短信号码出现异常");
            logger.error("根据订单号修改账单信息表客户接收短信号码出现异常:{}", e);
        }
        return result;
    }

    /**
     * 
     * Description:根据订单列表获取每个订单最大已还期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    @ResponseBody
    @RequestMapping(value = "/getMaxRepayNosByOrderId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMaxRepayNosByOrderId(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("根据订单列表获取每个订单最大已还期数，解析传参:{}", jsonStr);
            } else {
                logger.info("根据订单列表获取每个订单最大已还期数接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("根据订单列表获取每个订单最大已还期数接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            List<Map<String, String>> resultValue = billBizService.getMaxRepayNosByOrderId(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "根据订单列表获取每个订单最大已还期数成功");
            result.put("result", resultValue);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "根据订单列表获取每个订单最大已还期数出现异常");
            logger.error("根据订单列表获取每个订单最大已还期数出现异常:{}", e);
        }
        return result;
    }

    /**
     * 
     * Description:根据订单号查询首次还款时间
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    @ResponseBody
    @RequestMapping(value = "/getFirstLastRepayDateByOrderId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getFirstLastRepayDateByOrderId(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("根据订单号查询首次还款时间，解析传参:{}", jsonStr);
            } else {
                logger.info("根据订单号查询首次还款时间接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("根据订单号查询首次还款时间解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            InstallmentBillInfo resultValue = billBizService.getFirstLastRepayDateByOrderId(params);
            String lastRepayDate = DateUtil.format(resultValue.getLastRepayDate(), DateUtil.SHORT_DATE_FORMAT);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "根据订单号查询首次还款时间成功");
            result.put("result", lastRepayDate);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "根据订单号查询首次还款时间出现异常");
            logger.error("根据订单号查询首次还款时间出现异常:{}", e);
        }
        return result;
    }
    
    /**
     * 
     * Description:按订单分组汇总账单信息
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月19日
     */
    @ResponseBody
    @RequestMapping(value = "/collectBillDataByOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public Object collectBillDataByOrder(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("按订单分组汇总账单信息，解析传参:{}", jsonStr);
            } else {
                logger.info("按订单分组汇总账单信息接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("按订单分组汇总账单信息接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
        	List<Map<String, Object>> cList = billBizService.collectBillDataByOrder(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "按订单分组汇总账单信息成功");
            result.put("result", cList);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "按订单分组汇总账单信息出现异常");
            logger.error("按订单分组汇总账单信息出现异常:{}", e);
        }
        return result;
    }
    
    /**
     * 
     * Description:根据订单号 放款时间查询当前期数、剩余未还本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月17日
     */
    @ResponseBody
    @RequestMapping(value = "/getAllocationInfoOrderId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getAllocationInfoOrderId(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 接收参数
        Map<String, String> param = new HashMap<>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String jsonStr = null;
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
                jsonStr = new String(decodedData);
                logger.info("根据订单号 放款时间查询当前期数、剩余未还本金，解析传参:{}", jsonStr);
            } else {
                logger.info("根据订单号 放款时间查询当前期数、剩余未还本金接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("根据订单号 放款时间查询当前期数、剩余未还本金接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }

        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            Map<String, Object> map = billBizService.getAllocationInfoOrderId(params);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "根据订单号 放款时间查询当前期数、剩余未还本金成功");
            result.put("result", map);
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "根据订单号 放款时间查询当前期数、剩余未还本金出现异常");
            logger.error("根据订单号 放款时间查询当前期数、剩余未还本金出现异常:{}", e);
        }
        return result;
    }
}
