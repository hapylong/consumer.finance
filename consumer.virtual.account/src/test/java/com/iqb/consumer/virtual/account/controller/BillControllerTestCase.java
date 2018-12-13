package com.iqb.consumer.virtual.account.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.httpclient.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.RSAUtils;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;

public class BillControllerTestCase {

    private String uri = "http://localhost:8080/consumer.virtual.account";

    /**
     * 查询全部还款应付金额 { "regId": "手机号", "orderId": "订单号" }
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void balanceAdvance() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/balanceAdvance";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map map = new HashMap();
        map.put("orderId", "20160607-873501");
        map.put("regId", "15117923307");
        String source = JSON.toJSONString(map);
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
     * 查询流水分页 { "regId": "手机号", "openId": "行业号", "orderId":"订单号(可选)", "merchantNo":"商户号(可选)" }
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void selectRepayHistoryByPages() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/selectRepayHistoryByPages";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map map = new HashMap();
        map.put("openId", "10101");
        map.put("regId", "15117923307");
        map.put("orderId", "111112");
        map.put("merchantNo", null);
        map.put("pageNum", 1);
        map.put("numPerPage", 500);
        String source = JSON.toJSONString(map);
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
     * 查询还款分页条件:{merchantNos=[test01, yianjia], lastRepayDate=null, status=1, numPerPage=500,
     * orderId=null, regId=null, pageNum=1, openId=10101} 输出参数:
     * 
     * @throws Exception
     */
    @Test
    public void testQueryBillByParams() throws Exception {
        uri = "http://localhost:9080/consumer.virtual.account/bill/queryBillInfo";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("openId", null);
        map.put("regId", null);
        map.put("lastRepayDate", null);
        List<String> list = new ArrayList<String>();
        list.add("test01");
        list.add("yianjia");
        map.put("merchantNos", null);
        map.put("orderId", null);
        map.put("startDate", "20170201");
        map.put("endDate", "20170216");
        map.put("status", null);
        map.put("realName", "小测");
        map.put("pageNum", 1);
        map.put("numPerPage", 10);
        String source = JSON.toJSONString(map);
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
     * 查询账单分页 { "regId": "手机号", "openId": "行业号", "orderId":"订单号(可选)", "merchantNo":"商户号(可选)",
     * "status":"状态: 1代表查询未还款账单.2代表查询已还款账单.0代表全部都查", "pageNum":"当前页", "numPerPage":"每页条数" }
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void selectBillsByPage() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/selectBillsByPage";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map map = new HashMap();
        map.put("openId", "10101");
        map.put("regId", "15117923307");
        map.put("status", 1);
        map.put("pageNum", 1);
        map.put("numPerPage", 500);
        String source = JSON.toJSONString(map);
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
     * 查询最近三期未还款账单 { "regId": "手机号", "openId": "行业号" }
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void selectCurrBills() throws Exception {
        uri = "http://test.ishandian.cn:8081/consumer.virtual.account/bill/selectCurrBills";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map map = new HashMap();
        map.put("openId", "10101");
        map.put("regId", "15117923307");
        String source = JSON.toJSONString(map);
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
     * 查询全部还款应付金额 { "regId": "手机号", "orderId": "订单号" }
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void selectCost4Advance() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/selectCost4Advance";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map map = new HashMap();
        map.put("orderId", "111112");
        map.put("regId", "15117923307");
        String source = JSON.toJSONString(map);
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

    @Test
    public void bizSchedulerOverdue() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("orderId", "ASXR2002170627001");
        sourceMap.put("instBillId", "173618");
        sourceMap.put("endDate", "20171129");

        String source = JSONObject.toJSONString(sourceMap);
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

        String resultStr =
                SimpleHttpUtils.httpPost(
                        "http://localhost:9080/consumer.virtual.account/bill/bizSchedulerOverdue", params);
        System.out.println(resultStr);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testResetPeriodization() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        uri = uri + "/bill/resetPeriodization";

        Map map = new HashMap();
        map.put("regId", "13681486576");
        map.put("openId", "10102");
        map.put("orderId", "SYQS2002170602002");
        map.put("beginDate", "2017-06-28");

        String source = JSONObject.toJSONString(map);
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

        String resultStr =
                SimpleHttpUtils.httpPost(uri, params);
        System.out.println("------------------" + resultStr);
    }

    @Test
    public void testBillPaymentNormal() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("id", "2");
        sourceMap.put("ratio", "1");

        String source = "[\r\n" +
                "    {\r\n" +
                "        \"orderId\": \"20160530-625226\",\r\n" +
                "        \"openId\": 10102,\r\n" +
                "        \"repayModel\": \"all\",\r\n" +
                "        \"regId\": \"15108258619\",\r\n" +
                "        \"merchantNo\": \"dlxszb\",\r\n" +
                "        \"orderId\": \"CDHTC2001170215005\",\r\n" +
                "        \"openId\": 10102,\r\n" +
                "        \"repayModel\": \"all\",\r\n" +
                "        \"regId\": \"18161008336\",\r\n" +
                "        \"merchantNo\": \"cdhtc\",\r\n" +

                "        \"tradeNo\": \"123\",\r\n" +
                "        \"repayDate\": \"2016\",\r\n" +
                "        \"sumAmt\": \"27\",\r\n" +
                "        \"repayList\": [\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 1,\r\n" +
                "                \"amt\": \"13\"\r\n" +
                "            },\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 2,\r\n" +
                "                \"amt\": \"14\"\r\n" +
                "            }\r\n" +
                "        ]\r\n" +
                "    },\r\n" +
                "    {\r\n" +
                "        \"orderId\": 90000001,\r\n" +
                "        \"openId\": 2323,\r\n" +
                "        \"repayModel\": \"normal\",\r\n" +
                "        \"regId\": \"119\",\r\n" +
                "        \"merchantNo\": \"111\",\r\n" +
                "        \"tradeNo\": \"2016\",\r\n" +
                "        \"repayDate\": \"2017\",\r\n" +
                "        \"sumAmt\": \"55\",\r\n" +
                "        \"repayList\": [\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 2,\r\n" +
                "                \"amt\": \"23\"\r\n" +
                "            },\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 3,\r\n" +
                "                \"amt\": \"32\"\r\n" +
                "            }\r\n" +
                "        ]\r\n" +
                "    }\r\n" +
                "]";
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

        String resultStr =
                SimpleHttpUtils.httpPost(
                        "http://localhost:8080/consumer.virtual.account/bill/refund", params);
        System.out.println(resultStr);
    }

    /**
     * 验证支付合法性
     * 
     * @throws Exception
     */
    @Test
    public void testVerifyPayment() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("id", "2");
        sourceMap.put("ratio", "1");

        String source = "[\r\n" +
                "    {\r\n" +
                "        \"orderId\": 111111,\r\n" +
                "        \"openId\": 2323,\r\n" +
                "        \"repayModel\": \"normal\",\r\n" +
                "        \"regId\": \"110\",\r\n" +
                "        \"merchantNo\": \"111\",\r\n" +
                "        \"tradeNo\": \"123\",\r\n" +
                "        \"repayDate\": \"2016\",\r\n" +
                "        \"sumAmt\": \"27\",\r\n" +
                "        \"repayList\": [\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 1,\r\n" +
                "                \"amt\": \"13\"\r\n" +
                "            },\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 2,\r\n" +
                "                \"amt\": \"4987\"\r\n" +
                "            }\r\n" +
                "        ]\r\n" +
                "    },\r\n" +
                "    {\r\n" +
                "        \"orderId\": 90000001,\r\n" +
                "        \"openId\": 2323,\r\n" +
                "        \"repayModel\": \"normal\",\r\n" +
                "        \"regId\": \"119\",\r\n" +
                "        \"merchantNo\": \"111\",\r\n" +
                "        \"tradeNo\": \"2016\",\r\n" +
                "        \"repayDate\": \"2017\",\r\n" +
                "        \"sumAmt\": \"55\",\r\n" +
                "        \"repayList\": [\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 2,\r\n" +
                "                \"amt\": \"23\"\r\n" +
                "            },\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 3,\r\n" +
                "                \"amt\": \"32\"\r\n" +
                "            }\r\n" +
                "        ]\r\n" +
                "    }\r\n" +
                "]";
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

        String resultStr =
                SimpleHttpUtils.httpPost(
                        "http://localhost:9080/consumer.virtual.account/bill/verifyPayment", params);
        System.out.println(resultStr);
    }

    @Test
    public void testBillPaymentAll() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("id", "2");
        sourceMap.put("ratio", "1");

        String source = "[\r\n" +
                "    {\r\n" +
                "        \"orderId\": 111112,\r\n" +
                "        \"openId\": 2323,\r\n" +
                "        \"repayModel\": \"all\",\r\n" +
                "        \"regId\": \"120\",\r\n" +
                "        \"merchantNo\": \"111\",\r\n" +
                "        \"TRADENO\": \"11111111\",\r\n" +
                "        \"repayDate\": \"2017\",\r\n" +
                "        \"sumAmt\": \"23\",\r\n" +
                "        \"repayList\": [\r\n" +
                "        ]\r\n" +
                "    }\r\n" +
                "]";
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

        String resultStr =
                SimpleHttpUtils.httpPost(
                        "http://localhost:8080/consumer.virtual.account/bill/refund", params);
        System.out.println(resultStr);
    }

    /**
     * 查询还款期数
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void getRepayNo() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/getRepayNo";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map map = new HashMap();
        map.put("orderId", "20161219001");
        String source = JSON.toJSONString(map);
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
     * 查询还款分页条件:{merchantNos=[test01, yianjia], lastRepayDate=null, status=1,
     * numPerPage=500,realName=null, orderId=null, regId=null, pageNum=1, openId=10101} 输出参数:
     * 
     * @throws Exception
     */
    @Test
    public void testQueryBillByParamsForMortgage() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/queryBillByParamsForMortgage";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("regId", null);
        map.put("lastRepayDate", null);
        map.put("merchantNos", "[htqb,dlhl]");
        map.put("orderId", null);
        map.put("lastRepayDate", "null");
        map.put("status", null);
        map.put("realName", "杨文晶");
        map.put("pageNum", 1);
        map.put("numPerPage", 10);
        String source = JSON.toJSONString(map);
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
     * 查询账户系统-订单要素 违约金 最迟还款日期 当期期数 已还金额:{orderId=null} 输出参数:
     * 
     * @throws Exception
     */
    @Test
    public void testGetFactors() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/getFactors";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("orderId", "20160613-286373");

        String source = JSON.toJSONString(map);
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

    @Test
    public void testGetSubletInfo() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/getSubletInfo";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("orderId", "20160707-580511");

        String source = JSON.toJSONString(map);
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

        String resultStr = SimpleHttpUtils.httpPostRequest(uri, map);
        System.out.println(resultStr);
    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }

    @Test
    public void testValidateBillinfoOverdue() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/validateBillinfoOverdue";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        List<String> allOrderIds = new ArrayList<>();
        allOrderIds.add("CDHTC2001170313003");
        allOrderIds.add("AAAAA202012365475");
        allOrderIds.add("CDHTC2001170316009");
        map.put("orderIdList", allOrderIds);

        String source = JSON.toJSONString(map);
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

    @Test
    public void testGetCurRepayAmtByRepayno() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/getCurRepayAmtByRepayno";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("orderId", "20160613-132325");
        map.put("repayNo", "1,2");

        String source = JSON.toJSONString(map);
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

        System.out.println("入参-----" + JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);

        System.out.println("...." + resultStr);
    }

    @Test
    public void testSelectBillsByCondition() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/selectBillsByCondition";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("orderId", "SYQS2002170608002");
        map.put("regId", "15311464963");
        map.put("repayNo", "5,6,7");

        String source = JSON.toJSONString(map);
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

        System.out.println("入参-----" + JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);

        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) retMap.get("result");
        Map<String, Object> result = null;
        if (recordList != null && recordList.size() > 0) {
            result = recordList.get(0);
        }
        System.out.println(result);
        System.out.println("...." + resultStr);
    }

    @Test
    public void testBillPaymentPart() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("id", "2");
        sourceMap.put("ratio", "1");

        String source = "[\r\n" +
                "    {\r\n" +
                "        \"orderId\": \"CDHTC2001170317052\",\r\n" +
                "        \"openId\": 10103,\r\n" +
                "        \"repayModel\": \"part\",\r\n" +
                "        \"regId\": \"13515160001\",\r\n" +
                "        \"merchantNo\": \"shxd\",\r\n" +
                "        \"tradeNo\": \"123\",\r\n" +
                "        \"repayDate\": \"2016\",\r\n" +
                "        \"sumAmt\": \"27\",\r\n" +
                "        \"repayList\": [\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 5,\r\n" +
                "                \"amt\": \"10\"\r\n" +
                "            },\r\n" +
                "            {\r\n" +
                "                \"repayNo\": 5,\r\n" +
                "                \"amt\": \"10\"\r\n" +
                "            }\r\n" +
                "        ]\r\n" +
                "    }\r\n" +
                "]";

        String a = "";
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

        String resultStr =
                SimpleHttpUtils.httpPost(
                        "http://localhost:8080/consumer.virtual.account/bill/refund", params);
        System.out.println(resultStr);
    }

    /**
     * 存量统计查询 Description:
     * 
     * @param objs
     * @param request
     * @return
     */
    @Test
    public void testListStockStatisticsPage() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/listStockStatisticsPage";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("curRepayDate", "2017-06-22");
        map.put("numPerPage", "10");
        map.put("pageNum", "1");

        String source = JSON.toJSONString(map);
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

        System.out.println("入参-----" + JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);

        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) retMap.get("result");
        Map<String, Object> result = null;
        if (recordList != null && recordList.size() > 0) {
            result = recordList.get(0);
        }
        System.out.println(result);
        System.out.println("...." + resultStr);
    }

    /**
     * 
     * Description:测试-查询每日到期账单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    @Test
    public void testSelectEveryDayExpireBill() throws Exception {
        uri = "http://localhost:8080/consumer.virtual.account/bill/selectEveryDayExpireBill";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("numPerPage", "100");
        map.put("pageNum", "1");

        String source = JSON.toJSONString(map);
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

        System.out.println("入参-----" + JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);

        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) retMap.get("result");
        Map<String, Object> result = null;
        if (recordList != null && recordList.size() > 0) {
            result = recordList.get(0);
        }
        System.out.println(result);
        System.out.println("...." + resultStr);
    }

    /**
     * 查询最近三期未还款账单 { "regId": "手机号", "openId": "行业号" }
     * 
     * @throws Exception
     */
    @Test
    public void getLastThreeOrderInfo() throws Exception {
        uri = "http://127.0.0.1:8080/consumer.virtual.account/bill/getLastThreeOrderInfo";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("openId", "10102");
        map.put("regId", "13718147368");
        map.put("orderId", "RYQC2001170629001");
        String source = JSON.toJSONString(map);
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

        System.out.println(JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);

        System.out.println(resultStr);
    }

    /*
     * 查询房贷分期详细信息
     */
    @Test
    public void getMorgateOrderInfo() throws Exception {
        uri = "http://127.0.0.1:8080/consumer.virtual.account/bill/getMorgateOrderInfo";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("orderId", "FFFFF202012360005");
        String source = JSON.toJSONString(map);
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

        System.out.println(JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println("---" + JSONObject.toJSONString(resultStr));

    }

    @Test
    public void testSelectLatelyThreeDaysBill() throws Exception {
        uri = "http://127.0.0.1:8080/consumer.virtual.account/bill/selectLatelyThreeDaysBill";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("numPerPage", "1");
        map.put("pageNum", "10");
        String source = JSON.toJSONString(map);
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

        System.out.println(JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println("---" + JSONObject.toJSONString(resultStr));
    }

    public static void main(String[] args) throws Exception {

        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("openId", "10102");
        map.put("regId", "13718147368");
        map.put("orderId", "RYQC2001170629001");
        String source = JSON.toJSONString(map);
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

        System.out.println(JSONObject.toJSONString(params));
    }

    @Test
    public void getQueryBillInfoByOrderIdPage() throws Exception {
        uri = "http://127.0.0.1:8080/consumer.virtual.account/bill/queryBillInfoByOrderIdPage";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> map = new HashMap<String, Object>();

        List<InstallmentBillInfo> list = new ArrayList<>();
        InstallmentBillInfo bean1 = new InstallmentBillInfo();
        bean1.setOrderId("20160907-719944");
        bean1.setRepayNo(20);
        list.add(bean1);

        map.put("haList", list);
        String source = JSON.toJSONString(map);
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

        System.out.println(JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println("---" + JSONObject.toJSONString(resultStr));

    }

    @Test
    public void batchUpdateBillSmsMobile() throws Exception {
        uri = "http://127.0.0.1:8080/consumer.virtual.account/bill/batchUpdateBillSmsMobile";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        List<InstallmentBillInfo> list = new ArrayList<>();
        InstallmentBillInfo bean1 = new InstallmentBillInfo();
        bean1.setOrderId("AAAAA202012365480");

        InstallmentBillInfo bean2 = new InstallmentBillInfo();
        bean2.setOrderId("AAAAA202012365499");

        list.add(bean1);
        list.add(bean2);

        JSONObject sourceMap = new JSONObject();
        sourceMap.put("orderList", list);
        sourceMap.put("smsMobile", "13681486576");

        String source = JSON.toJSONString(sourceMap);
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

        System.out.println(JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println("---" + JSONObject.toJSONString(resultStr));

    }

    @Test
    public void getMaxRepayNosByOrderId() throws Exception {
        uri = "http://127.0.0.1:8080/consumer.virtual.account/bill/getMaxRepayNosByOrderId";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, List<String>> paramsMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("CDHTC2002171106007");
        list.add("CDHTC2002171019001");
        list.add("CDHTC2002171106007");

        JSONObject sourceMap = new JSONObject();
        sourceMap.put("orderList", list);

        String source = JSON.toJSONString(sourceMap);
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

        System.out.println(JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println("---" + JSONObject.toJSONString(resultStr));

    }

    @Test
    public void getAllocationInfoOrderId() throws Exception {
        uri = "http://127.0.0.1:8080/consumer.virtual.account/bill/getAllocationInfoOrderId";
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        JSONObject sourceMap = new JSONObject();
        sourceMap.put("orderId", "TYLD2002170901001");
        sourceMap.put("loanDate", "2018-10-16");

        String source = JSON.toJSONString(sourceMap);
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

        System.out.println(JSONObject.toJSONString(params));

        byte[] data2 = Base64.decodeBase64(data1);
        boolean flag = RSAUtils.verify(data2, publicKey, sign);
        System.out.println(flag);

        byte[] tB = RSAUtils.decryptByPublicKey(data2, publicKey);
        System.out.println(tB);

        String resultStr = SimpleHttpUtils.httpPost(uri, params);
        System.out.println("---" + JSONObject.toJSONString(resultStr));

    }
}
