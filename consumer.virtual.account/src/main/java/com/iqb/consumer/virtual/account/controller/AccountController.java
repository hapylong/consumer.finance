/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月20日 下午3:38:37
 * @version V1.0
 */

package com.iqb.consumer.virtual.account.controller;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
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
import com.iqb.consumer.common.exception.RequestException;
import com.iqb.consumer.common.utils.sign.RSAUtils;
import com.iqb.consumer.service.account.AccountInfoService;
import com.iqb.consumer.service.account.AcctInfoService;
import com.iqb.consumer.virtual.account.config.AccountConifg;
import com.iqb.consumer.virtual.account.constant.Constant;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    protected static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private AccountConifg accountConifg;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private AcctInfoService acctInfoService;

    /**
     * (对外接口)开户接口
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/openAccount", method = RequestMethod.POST)
    public Object openAccount(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
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
                byte[] decodedData = RSAUtils.decryptByPublicKey(byteData, accountConifg.getPublicKey());
                jsonStr = new String(decodedData);
                logger.info("开户接口，解析传参:{}", jsonStr);
            } else {
                logger.info("开户接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("开户接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            result = validateAccount(params);// 校验开户信息
            if (Constant.SUCCESS.equals(result.get("retCode"))) {
                try {
					result = acctInfoService.openAccount(params);// 开户
				} catch (Exception e) {
					logger.error("开户异常",e);
					e.printStackTrace();
				}
            } else {
                result.put("retCode", AccountConstant.ERROR);
                result.put("msg", "验签失败,数据非法");
            }
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "开户发生异常,请确认接口数据");
        }
        return result;
    }

    /**
     * (对外接口)销毁账户(逻辑注销)
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/destroyAccount", method = RequestMethod.POST)
    public Object destroyAccount(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
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
                byte[] decodedData = RSAUtils.decryptByPublicKey(byteData, accountConifg.getPublicKey());
                jsonStr = new String(decodedData);
                logger.info("销毁账户接口，解析传参:{}", jsonStr);
            } else {
                logger.info("销毁账户接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("销毁账户接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            result = acctInfoService.destroyAccount(params);// 销毁账户信息
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "销毁账户发生异常,请确认接口数据");
        }

        return result;
    }

    /**
     * (对外接口)查询开户信息
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryAccount", method = RequestMethod.POST)
    public Object queryAccount(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> param = new HashMap<String, String>();
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
                byte[] decodedData = RSAUtils.decryptByPublicKey(byteData, accountConifg.getPublicKey());
                jsonStr = new String(decodedData);
                logger.info("查询开户信息接口，解析传参:{}", jsonStr);
            } else {
                logger.info("查询开户信息接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询开户信息接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
        JSONObject params = JSONObject.parseObject(jsonStr);// 解析信息
        try {
            result = acctInfoService.queryAccount(params);// 查询开户信息
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "查询开户信息发生异常,请确认接口数据");
        }

        return result;
    }

    /**
     * (对外接口)账户信息修改
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
    public Object updateAccount(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    /**
     * 账户信息校验
     * 
     * @method 类方法
     * @param param
     * @return
     */
    private Map<String, String> validateAccount(Map<String, Object> param) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("retCode", Constant.SUCCESS);
        if (StringUtils.isBlank((String) param.get("idNo"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "用户身份证为空");
        } else if (StringUtils.isBlank((String) param.get("regId"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "用户手机号为空");
        } else if (StringUtils.isBlank((String) param.get("realName"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "用户姓名为空");
        } else if (StringUtils.isBlank((String) param.get("openId"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "行业号为空");
        } else if (param.get("bankCardNo") == null || ((String) param.get("bankCardNo")).length() == 0) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "用户银行卡号为空");
        } else if (((String) param.get("idNo")).length() != 18 && ((String) param.get("idNo")).length() != 15) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "身份证格式不对");
        } else if (((String) param.get("regId")).length() != 11) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "手机号格式不对");
        } else if (!acctInfoService.isOpenIdEnabled((String) param.get("openId"))) {
            result.put("retCode", Constant.ERROR);
            result.put("retMsg", "该行业号不存在");
        }

        return result;
    }

}
