/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月9日 下午5:32:08
 * @version V1.0
 */
package com.iqb.consumer.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.iqb.consumer.common.utils.httpclient.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.RSAUtils;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class TestInteface {

    public static void main(String[] args) throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        String source =
                "{\"orderId\":\"90000001\",\"regId\":\"15117923307\",\"orderDate\":\"20161110\",\"beginDate\":\"20161217\",\"openId\":\"10101\",\"merchantNo\":\"yianjia\",\"installSumAmt\":\"50023.12\",\"installAmt\":\"10000.00\",\"installTerms\":\"12\",\"planId\":\"1\"}";
        System.out.println("\r\n原文字：\r\n" + source);
        byte[] data = source.getBytes();
        // 私钥加密
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
        String data1 = encodeBase64(encodedData);
        System.err.println("私钥签名——公钥验证签名");
        String sign = RSAUtils.sign(encodedData, privateKey);
        System.err.println("签名:\r" + sign);
        System.out.println(data1);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data", data1);
        params.put("sign", sign);

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        // String resultStr =
        // SimpleHttpUtils.httpPost("http://101.201.151.38:8081/consumer.virtual.account/install/paymentByInstll",
        // params);
        String resultStr =
                SimpleHttpUtils.httpPost("http://localhost:9080/consumer.virtual.account/install/paymentByInstll",
                        params);
        System.out.println(resultStr);
    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");

    }

}
