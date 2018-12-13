package com.iqb.consumer.virtual.account.controller;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.iqb.consumer.common.utils.httpclient.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.RSAUtils;

public class AccountControllerTestCase {

    private String uri = "http://localhost:9080/consumer.virtual.account";

    /**
     * 测试开户接口 入参: { "regId": "手机号", "idNo": "身份证号", "realName": "真实姓名", "openId": "行业号",
     * "bankCardNo": "银行卡号" }
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void openAccount() throws Exception {
        uri = uri + "/account/openAccount";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map param = new HashMap();
        param.put("regId", "15117923307");
        param.put("idNo", "120556200012120023");
        param.put("realName", "测试1");
        param.put("bankCardNo", "622848562166632545");
        param.put("openId", "10102");
        String source = JSON.toJSONString(param);
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

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println(resultStr);
    }

    /**
     * 查询账户是否存在 入参 { "regId": "手机号", "openId": "行业号" }
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void queryAccount() throws Exception {
        uri = uri + "/account/queryAccount";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map param = new HashMap();
        param.put("regId", "18600006666");
        param.put("openId", "10101");
        String source = JSON.toJSONString(param);
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

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println(resultStr);
    }

    /**
     * 销户测试 入参 { "regId": "手机号", "idNo": "身份证号", "openId": "行业号", "flag": "销户类型:1.消除主账户 2.消除行业账户" }
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void destroyAccount() throws Exception {
        uri = uri + "/account/destroyAccount";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map param = new HashMap();
        param.put("regId", "18600006666");
        param.put("openId", "10101");
        param.put("idNo", "120556200012120023");
        param.put("flag", "1");
        String source = JSON.toJSONString(param);
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

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println(resultStr);
    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }
}
