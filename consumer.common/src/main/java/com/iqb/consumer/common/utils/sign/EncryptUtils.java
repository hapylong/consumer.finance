package com.iqb.consumer.common.utils.sign;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.exception.RequestException;

/**
 * 
 * Description: 加密
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月18日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class EncryptUtils {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtils.class);

    @Resource
    private ConsumerConfig consumerConfig;

    /**
     * 
     * Description: 加密
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:56:48
     */
    public Map<String, Object> encrypt(JSONObject objs) {
        String source = objs.toJSONString();
        byte[] data = source.getBytes();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            byte[] encodedData = RSAUtils.encryptByPrivateKey(data, consumerConfig.getCommonPrivateKey());
            String data1 = encodeBase64(encodedData);
            String sign = RSAUtils.sign(encodedData, consumerConfig.getCommonPrivateKey());
            params.put("data", data1);
            params.put("sign", sign);
        } catch (Exception e) {
            logger.error("加密异常：" + objs.toJSONString(), e);
        }
        return params;
    }

    /**
     * 
     * Description: 加密
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2016年12月6日 上午11:21:06
     */
    public Map<String, Object> encrypt(Map<String, Object> map) {
        String source = objToJson(map);
        byte[] data = source.getBytes();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            byte[] encodedData = RSAUtils.encryptByPrivateKey(data, consumerConfig.getCommonPrivateKey());
            String data1 = encodeBase64(encodedData);
            String sign = RSAUtils.sign(encodedData, consumerConfig.getCommonPrivateKey());
            params.put("data", data1);
            params.put("sign", sign);
        } catch (Exception e) {
            logger.error("加密异常：" + source, e);
        }
        return params;
    }

    /**
     * 加密
     * 
     * @param source
     * @return
     */
    public Map<String, Object> encrypt(String source) {
        byte[] data = source.getBytes();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            byte[] encodedData = RSAUtils.encryptByPrivateKey(data, consumerConfig.getCommonPrivateKey());
            String data1 = encodeBase64(encodedData);
            String sign = RSAUtils.sign(encodedData, consumerConfig.getCommonPrivateKey());
            params.put("data", data1);
            params.put("sign", sign);
        } catch (Exception e) {
            logger.error("加密异常：" + source, e);
        }
        return params;
    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }

    /**
     * 解密数据
     * 
     * @param request
     * @param publicKey
     * @return
     */
    public static String decrypt(HttpServletRequest request, String publicKey) throws RequestException {
        // 接收参数
        Map<String, String> param = new HashMap<String, String>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        String calculateInfo = null;
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
            boolean signFlag = RSAUtils.verify(byteData, publicKey, sign);
            if (signFlag) {
                // 解密
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, publicKey);
                calculateInfo = new String(decodedData);
            } else {
                throw RequestException.COMMONSIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            throw RequestException.DECRYPTION_EXCEPTION;
        }
        return calculateInfo;
    }

    /**
     * 解密
     * 
     * @param request
     * @return
     */
    public String decode(HttpServletRequest request) {
        Map<String, String> param = new HashMap<String, String>();
        String jsonStr = null;
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
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
            boolean signFlag = RSAUtils.verify(byteData, consumerConfig.getCommonPublicKey(), sign);
            if (signFlag) {
                // 解密
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, consumerConfig.getCommonPublicKey());
                jsonStr = new String(decodedData);
                logger.info("存量统计查询接口，解析传参:{}", jsonStr);
            } else {
                logger.info("存量统计查询接口验签失败");
                throw new RuntimeException();
            }
        } catch (Exception e) {
            logger.info("存量统计查询接口解密参数异常:{}", e);
            throw new RuntimeException();
        }
        return jsonStr;
    }

    public static String objToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSONObject.toJSONString(obj,
                new SerializerFeature[] {SerializerFeature.SortField});
    }
}
