package com.iqb.consumer.common.utils.sign;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class SignUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    @SuppressWarnings("rawtypes")
    public static Object chatDecode(JSONObject requestMessage, Class c, String publicKey) {
        try {
            String PUBLIC_KEY = publicKey;
            String sign = requestMessage.getString("sign");
            String data = requestMessage.getString("data");
            byte[] byteData = Base64.decodeBase64(data);
            boolean signFlag = RSAUtils.verify(byteData, PUBLIC_KEY, sign);
            if (signFlag) {
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, PUBLIC_KEY);
                String jsonStr = new String(decodedData);
                return JSONObject.parseObject(jsonStr, c);
            } else {
                throw new RuntimeException("SignUtil.chatDecode error 1.");
            }
        } catch (Throwable e) {
            logger.error("SignUtil.chatDecode error 2." + e);
            throw new RuntimeException("SignUtil.chatDecode error 2.");
        }
    }

    public static Map<String, Object> chatEncode(String json, String privateKey) throws Exception {
        String PRIVATE_KEY = privateKey;
        byte[] data = json.getBytes();
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, PRIVATE_KEY);
        String data1 = Base64.encodeBase64String(encodedData);
        String sign = RSAUtils.sign(encodedData, PRIVATE_KEY);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data", data1);
        params.put("sign", sign);
        return params;
    }

}
