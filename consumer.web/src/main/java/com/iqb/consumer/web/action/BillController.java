package com.iqb.consumer.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.web.config.ParamConfig;
import com.iqb.consumer.web.exception.BizException;
import com.iqb.consumer.web.util.Msg;
import com.iqb.consumer.web.util.RequestUtil;
import com.iqb.consumer.web.util.http.SimpleHttpUtils;
import com.iqb.consumer.web.util.sign.EncryptUtils;
import com.iqb.consumer.web.util.sign.RSAUtils;

@RestController
@Controller
@RequestMapping("/bill")
public class BillController {

    @Resource
    private ParamConfig paramConfig;
    @Autowired
    private EncryptUtils encryptUtils;
    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    /**
     * 查询流水分页 { "regId": "手机号", "openId": "行业号", "orderId":"订单号(可选)", "merchantNo":"商户号(可选)" }
     * 
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryLSByPage.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryLSByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("查询流水分页:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/selectRepayHistoryByPages",
                            params);
            return JSONObject.parse(resultStr);
        } catch (BizException e) {
            logger.error("查询流水分页异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("查询流水分页异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 查询账单分页 { "regId": "手机号", "openId": "行业号", "orderId":"订单号(可选)", "merchantNo":"商户号(可选)",
     * "status":"状态: 1代表查询未还款账单.2代表查询已还款账单.0代表全部都查", "pageNum":"当前页", "numPerPage":"每页条数" }
     * 
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryBillsByPage.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryBillsByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("查询账单分页:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/selectBillsByPage",
                            params);
            return JSONObject.parse(resultStr);
        } catch (BizException e) {
            logger.error("查询账单分页异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("查询账单分页异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 查询最近三期未还款账单 { "regId": "手机号", "openId": "行业号" }
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryCurrBills.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryCurrBills(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("查询当前账单:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/selectCurrBills",
                            params);
            return JSONObject.parse(resultStr);
        } catch (BizException e) {
            logger.error("查询当前账单异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("查询当前账单异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 提前还款还款应付金额 { "regId": "手机号", "orderId": "订单号" }
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/repay4Tiqian.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object repay4Tiqian(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("提前还款:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/selectCost4Advance",
                            params);
            return JSONObject.parse(resultStr);
        } catch (BizException e) {
            logger.error("提前还款异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("提前还款异常", e);
            return Msg.error(e);
        }
    }

    /**
     * [{\
     * "orderId\":111111,\"openId\":2323,\"repayModel\":\"normal\",\"regId\":\"110\",\"merchantNo\":\"111\",\"tradeNo\":\"123\",\"repayDate\":\"2016\",\"sumAmt\":\"27\",\"repayList\":[{\"repayNo\":1,\"amt\":\"13\"},{\"repayNo\":2,\"amt\":\"14\"}]},{\"orderId\":90000001,\"openId\":2323,\"repayModel\":\"normal\",\"regId\":\"119\",\"merchantNo\":\"111\",\"tradeNo\":\"2016\",\"repayDate\":\"2017\",\"sumAmt\":\"55\",\"repayList\":[{\"repayNo\":2,\"amt\":\"23\"},{\"repayNo\":3,\"amt\":
     * \ " 3 2 \ " } ] } ]
     * 
     * type: normal 正常还款 、 advance 提前还款
     * 
     * @param obj
     * @param type
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/go2payment/{type}.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object go2payment(@RequestBody Object obj, @PathVariable("type") String type,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Object objs = JSONObject.toJSON(obj);
        String uri1 = paramConfig.getRequestBaseUrl() + "/bill/verifyPayment";
        String uri2 = paramConfig.getRequestBaseUrl() + "/bill/refund";

        String privateKey = paramConfig.getCommonPrivateKey();
        String publicKey = paramConfig.getCommonPublicKey();

        String source = JSON.toJSONString(objs);
        System.out.println("\r\n原文字：\r\n" + source);
        byte[] data = source.getBytes();
        // 私钥加密
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
        String data1 = encodeBase64(encodedData);
        System.err.println("私钥签名——公钥验证签名");
        String sign = RSAUtils.sign(encodedData, privateKey);
        System.err.println("签名:\r" + sign);
        System.out.println(data1);

        /* 接收参数 */
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data", data1);
        params.put("sign", sign);

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = null;
        if (type.equals("normal")) {
            // 数据校验
            resultStr = SimpleHttpUtils.httpPost(uri1, params);
            System.out.println(resultStr);
            // 还款
            Map<String, String> map = (Map<String, String>) JSON.parse(resultStr);
            String retCode = map.get("retCode");
            if (retCode != null && "success".equals(retCode)) {
                resultStr = SimpleHttpUtils.httpPost(uri2, params);
            }
            return resultStr;
        }
        resultStr = SimpleHttpUtils.httpPost(uri2, params);
        return resultStr;
    }

    /**
     * 
     * Description: 停止分期 { "regId": "注册号", "openId": "开户号", "orderId":"订单号"}
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年12月1日 下午6:00:43
     */
    @ResponseBody
    @RequestMapping(value = "/stopPeriodization.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object stopPeriodization(HttpServletRequest request, HttpServletResponse response) {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("测试:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/stopPeriodization",
                            params);
            return Msg.info(resultStr);
        } catch (BizException e) {
            logger.error("测试异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("测试异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 
     * Description: 重置分期 { "regId": "注册号", "openId": "开户号", "orderId":"订单号"}
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年12月1日 下午6:00:43
     */
    @ResponseBody
    @RequestMapping(value = "/resetPeriodization.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object resetPeriodization(HttpServletRequest request, HttpServletResponse response) {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("测试:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/resetPeriodization",
                            params);
            return Msg.info(resultStr);
        } catch (BizException e) {
            logger.error("测试异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("测试异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 
     * Description: 获得当前订单最后还款期数
     * 
     * @param
     * @return Object
     * @throws
     * @Author gxy Create Date: 2016年12月1日 下午6:00:43
     */
    @ResponseBody
    @RequestMapping(value = "/getRepayNo.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getRepayNo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("测试:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/getRepayNo",
                            params);
            return Msg.info(resultStr);
        } catch (BizException e) {
            logger.error("测试异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("测试异常", e);
            return Msg.error(e);
        }
    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }
}
