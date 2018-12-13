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

import com.iqb.consumer.common.config.ParamConfig;
import com.iqb.consumer.common.exception.RequestException;

/**
 * 
 * Description: 解密工具
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月17日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class DecryptUtils {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DecryptUtils.class);

    @Resource
    private ParamConfig paramConfig;

    /**
     * 
     * Description: request请求
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:56:48
     */
    public String DecryptFromRequest(HttpServletRequest request) {
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
            boolean signFlag = RSAUtils.verify(byteData, paramConfig.getPublicKey(), sign);
            if (signFlag) {
                // 解密
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, paramConfig.getPublicKey());
                jsonStr = new String(decodedData);
                logger.info("查询当期账单接口，解析传参:{}", jsonStr);
                return jsonStr;
            } else {
                logger.info("查询当期账单接口验签失败");
                throw RequestException.SIGN_DECRYPTION_EXCEPTION;
            }
        } catch (Exception e) {
            logger.info("查询当期账单接口解密参数异常:{}", e);
            throw RequestException.GET_DECRYPTION_EXCEPTION;
        }
    }

}
