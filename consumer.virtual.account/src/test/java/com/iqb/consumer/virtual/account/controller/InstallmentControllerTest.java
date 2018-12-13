/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月16日 下午4:44:13
 * @version V1.0
 */
package com.iqb.consumer.virtual.account.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.httpclient.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.EncryptUtils;
import com.iqb.consumer.common.utils.sign.RSAUtils;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstDetail;
import com.iqb.consumer.service.consumer.dto.InstPlan;
import com.iqb.consumer.service.consumer.dto.fastBill.FastBillBean;
import com.iqb.consumer.service.consumer.dto.fastBill.Plan;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentControllerTest {
    @Resource
    private EncryptUtils encryptUtils;

    /**
     * 房贷一次性还本付息
     * 
     * @throws Exception
     */
    @Test
    public void testMortgage2Inst() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> sourceMap = new HashMap<String, Object>();
        List<InstDetail> instDetails = new ArrayList<InstDetail>();
        InstDetail instDetail = new InstDetail();
        instDetail.setRepayNo(1);
        instDetail.setDays(50);
        instDetail.setInterest(new BigDecimal(5000));
        instDetails.add(instDetail);
        sourceMap.put("orderId", "FFFFF202012360004");
        sourceMap.put("regId", "13500458320");
        sourceMap.put("orderDate", "20161217");
        sourceMap.put("beginDate", "20170809");
        sourceMap.put("openId", "10301");
        sourceMap.put("merchantNo", "test01");
        sourceMap.put("contractAmt", "100000");// 核准金额
        sourceMap.put("installSumAmt", "100000");
        sourceMap.put("installAmt", "50000");
        sourceMap.put("otherAmt", "");
        sourceMap.put("takeInterest", "2");
        sourceMap.put("interestAmt", "");
        sourceMap.put("takeMonth", "2");
        sourceMap.put("takePaymentAmt", "0");
        sourceMap.put("takePayment", "0");
        sourceMap.put("installTerms", "1");
        sourceMap.put("instDetails", instDetails);// 分期详情规则
        sourceMap.put("planId", "15");
        sourceMap.put("sourcesFunding", "1");
        sourceMap.put("fundId", "1");
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

        // String resultStr =
        // SimpleHttpUtils.httpPost("http://101.201.151.38:8081/consumer.virtual.account/install/paymentByInstll",
        // params);
        String resultStr =
                SimpleHttpUtils.httpPost("http://localhost:9080/consumer.virtual.account/install/instByMortgage",
                        params);
        System.out.println(resultStr);
    }

    /**
     * 房贷上收月供分期测试
     */
    @Test
    public void testMortgageInst() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> sourceMap = new HashMap<String, Object>();
        List<InstDetail> instDetails = new ArrayList<InstDetail>();
        InstDetail instDetail = new InstDetail();
        instDetail.setRepayNo(1);
        instDetail.setDays(10);
        instDetail.setInterest(new BigDecimal(500));
        instDetails.add(instDetail);
        instDetails.add(instDetail);
        sourceMap.put("orderId", "GGG202012360006");
        sourceMap.put("regId", "13500458320");
        sourceMap.put("orderDate", "20161217");
        sourceMap.put("beginDate", "20170803");
        sourceMap.put("openId", "10301");
        sourceMap.put("merchantNo", "test01");
        sourceMap.put("contractAmt", "100000");// 核准金额
        sourceMap.put("installSumAmt", "100000");
        sourceMap.put("installAmt", "50000");
        sourceMap.put("otherAmt", "10");// 每月添加的附加费
        sourceMap.put("takeInterest", "2");
        sourceMap.put("interestAmt", "");
        sourceMap.put("takeMonth", "2");
        sourceMap.put("takePaymentAmt", "0");
        sourceMap.put("takePayment", "0");
        sourceMap.put("installTerms", "37");
        sourceMap.put("installUnit", "2");// 分期单位 1,天数 2,月份
        sourceMap.put("instDetails", instDetails);// 分期详情规则
        sourceMap.put("planId", "15");
        sourceMap.put("sourcesFunding", "1");
        sourceMap.put("fundId", "1");
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

        // String resultStr =
        // SimpleHttpUtils.httpPost("http://101.201.151.38:8081/consumer.virtual.account/install/paymentByInstll",
        // params);
        String resultStr =
                SimpleHttpUtils.httpPost("http://localhost:9080/consumer.virtual.account/install/instByMortgage",
                        params);
        System.out.println(resultStr);
    }

    /**
     * 分期测试
     * 
     * @throws Exception
     */
    @Test
    public void testGo2Inst() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";

        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("orderId", "AAAAA202012365478");
        sourceMap.put("regId", "13500458320");
        sourceMap.put("orderDate", "20161217");
        sourceMap.put("beginDate", "20170131");
        sourceMap.put("openId", "10101");
        sourceMap.put("merchantNo", "test01");
        sourceMap.put("installSumAmt", "100000");
        sourceMap.put("installAmt", "50000");
        sourceMap.put("otherAmt", "100000");
        sourceMap.put("takeInterest", "2");
        sourceMap.put("interestAmt", "");
        sourceMap.put("takeMonth", "2");
        sourceMap.put("takePaymentAmt", "");
        sourceMap.put("takePayment", "");
        sourceMap.put("installTerms", "12");
        sourceMap.put("planId", "7");
        sourceMap.put("sourcesFunding", "1");
        sourceMap.put("fundId", "1");
        sourceMap.put("smsMobile", "13681486576");

        // String source =
        // "{\"orderId\":\"90000001\",\"regId\":\"15117923307\",\"orderDate\":\"20161110\",\"beginDate\":\"20161217\","
        // + "\"openId\":\"10101\",\"merchantNo\":\"yianjia\",\"installSumAmt\":\"50023.12\","
        // + "\"installAmt\":\"10000.00\",\"installTerms\":\"12\",\"planId\":\"1\"}";
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

        // String resultStr =
        // SimpleHttpUtils.httpPost("http://101.201.151.38:8081/consumer.virtual.account/install/paymentByInstll",
        // params);
        String resultStr =
                SimpleHttpUtils.httpPost("http://47.94.240.53:8081/consumer.virtual.account/install/paymentByInstll",
                        params);
        System.out.println(resultStr);
    }

    @Test
    public void testCalculateAmt() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        Map<String, Object> sourceMap = new HashMap<String, Object>();
        InstPlan instPlan = new InstPlan();
        instPlan.setMerchantNo("");
        instPlan.setDownPaymentRatio(0.12);
        instPlan.setServiceFeeRatio(0.20);
        instPlan.setMarginRatio(0.32);
        instPlan.setFeeRatio(0.65);
        instPlan.setFeeYear(24);
        instPlan.setInstallPeriods(36);
        instPlan.setTakePayment(1);
        instPlan.setPlanId(3);
        instPlan.setUpInterestFee(null);
        sourceMap.put("InstPlan", instPlan);
        sourceMap.put("amt", 20.0);
        // String source =
        // "{\"orderId\":\"90000001\",\"regId\":\"15117923307\",\"orderDate\":\"20161110\",\"beginDate\":\"20161217\","
        // + "\"openId\":\"10101\",\"merchantNo\":\"yianjia\",\"installSumAmt\":\"50023.12\","
        // + "\"installAmt\":\"10000.00\",\"installTerms\":\"12\",\"planId\":\"1\"}";
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

        // String resultStr =
        // SimpleHttpUtils.httpPost("http://101.201.151.38:8081/consumer.virtual.account/install/paymentByInstll",
        // params);
        String resultStr =
                SimpleHttpUtils.httpPost("http://localhost:9080/consumer.virtual.account/install/calculateAmt",
                        params);
        System.out.println(resultStr);
    }

    @Test
    public void testFast2Inst() throws Exception {
        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpxOOibrkx/ZKk9VmF1JmkrGIPRcIG9TuO2veNqBIJw3QaIBFJov6I6mcnLmIrTiIWDB0uHHp59ce1dePmsROn/3c2SKk2Ph7+VbfNYQFfoN9NLPKB4uGhoQfEmzQvYeOndlOuK9Fzv/XbnSO3KYWifCvlZ3skNQkQJcErNvvk/wIDAQAB";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<FastBillBean> list = new ArrayList<>();
        FastBillBean fbb = new FastBillBean();
        fbb.setOrderId("1234568002001");
        fbb.setOrderDate(new Date());
        fbb.setRegId("13500458320");
        fbb.setBeginDate(new Date());
        fbb.setOpenId("10101");
        fbb.setMerchantNo("test");
        fbb.setInstallAmt(new BigDecimal("20"));
        fbb.setInstallSumAmt(new BigDecimal("20"));
        fbb.setContractAmt(new BigDecimal("20"));// 合同金额
        fbb.setInstallTerms(12);
        fbb.setPlanId(1);
        fbb.setFundId("1");
        fbb.setSourcesFunding("1");
        Plan plan = new Plan();
        plan.setNumber("1");
        plan.setCapital("1.0");
        plan.setInterest("0.5");
        plan.setFinalrepaymentdate("20160406");
        List<Plan> plans = new ArrayList<Plan>();
        plans.add(plan);
        fbb.setPlan(plans);
        list.add(fbb);
        // String source =
        // "{\"orderId\":\"90000001\",\"regId\":\"15117923307\",\"orderDate\":\"20161110\",\"beginDate\":\"20161217\","
        // + "\"openId\":\"10101\",\"merchantNo\":\"yianjia\",\"installSumAmt\":\"50023.12\","
        // + "\"installAmt\":\"10000.00\",\"installTerms\":\"12\",\"planId\":\"1\"}";
        String source = JSONObject.toJSONString(list);
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
                SimpleHttpUtils.httpPost("http://localhost:9080/consumer.virtual.account/install/createRepayPlan",
                        params);
        System.out.println(resultStr);
    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }

    public void testAA() {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject sourceMap = new JSONObject();
        sourceMap.put("orderId", "GMLD4001180516002");
        sourceMap.put("regId", "13261541870");
        sourceMap.put("orderDate", "20180516");
        sourceMap.put(
                "beginDate", "20180517");
        sourceMap.put("openId", "10501");
        sourceMap.put("merchantNo", "gmld");
        sourceMap.put("installSumAmt", "15600.00");
        sourceMap.put("installAmt", "15600.00");// 分期金额本金
        sourceMap.put("interestAmt", "4080.96");// 剩余利息
        sourceMap.put("takeInterest", "1");// 是否上收利息
        sourceMap.put("takeMonth", "2");// 是否上收月供
        sourceMap.put("takePaymentAmt", new BigDecimal(1640.08));// 上收月供金额
        sourceMap.put("takePayment", 0);// 上收月供数
        sourceMap.put("installTerms", "12");
        sourceMap.put("planId", 4);
        sourceMap.put("otherAmt", 0); // 其他费用
        try {
            String resultStr =
                    SimpleHttpUtils.httpPost("http://localhost:9080/consumer.virtual.account/install/createRepayPlan",
                            encryptUtils.encrypt(sourceMap));
            // 根据平账结果返回成功与否
            result = JSONObject.parseObject(resultStr);
        } catch (Exception e) {

        }
    }
}
