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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/acc")
public class AccController {

    private static final Logger logger = LoggerFactory.getLogger(AccController.class);
    @Resource
    private ParamConfig paramConfig;
    @Autowired
    private EncryptUtils encryptUtils;

    /**
     * 开户 { "regId": "手机号", "idNo": "身份证号", "realName": "真实姓名", "openId": "行业号", "bankCardNo":"银行卡号"
     * }
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/openAcc.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object openAcc(HttpServletRequest request, HttpServletResponse response) {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("开户:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/account/openAccount",
                            params);
            return JSONObject.parse(resultStr);
        } catch (BizException e) {
            logger.error("开户异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("开户异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 销户 { "regId": "手机号", "idNo": "身份证号", "openId": "行业号", "flag": "销户类型:1.消除主账户 2.消除行业账户" }
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/destroyAcc.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object destroyAcc(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("销户:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/account/destroyAccount",
                            params);
            return JSONObject.parse(resultStr);
        } catch (BizException e) {
            logger.error("销户异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("销户异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 查询是否开户 { "regId": "手机号", "openId": "行业号" }
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryAcc.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryAcc(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("查询是否开户:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/account/queryAccount",
                            params);
            return JSONObject.parse(resultStr);
        } catch (BizException e) {
            logger.error("查询是否开户异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("查询是否开户异常", e);
            return Msg.error(e);
        }
    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }
}
