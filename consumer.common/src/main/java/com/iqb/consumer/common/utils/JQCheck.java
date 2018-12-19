/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月2日 上午11:49:09
 * @version V1.0
 */

package com.iqb.consumer.common.utils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class JQCheck {
    private static Logger logger = LoggerFactory.getLogger(JQCheck.class);

    public static void main(String[] args) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("idNO", "510125198301102828");
        map.put("userName", "鲁康容");
        map.put("mobile", "13438270868");
        map.put("recard", "6223450010041072303");
        // hswq(map,"F:/secretkey/weishangweiquan/zszh_private_key_pkcs8.pem","cuptest.iqianbang.cn","80");
        hswq(map, "D:/usr/local/tomcat/conf/zszh_private_key_pkcs8.pem", "192.168.1.148", "80");
    }

    public static boolean hswq(Map<String, Object> map, String pkSuffix,
            String host, String port) {
        String url = "/K";
        // String host = "cuptest.iqianbang.cn";
        HttpClient httpClient = new HttpClient();
        httpClient.getHostConfiguration().setHost(host, Integer.parseInt(port), "http");

        HttpMethod method;
        try {
            method = postMethod(url, map, pkSuffix);
            httpClient.executeMethod(method);
            String response = method.getResponseBodyAsString();
            Map<String, Object> regRetMap = (Map<String, Object>) JSONObject
                    .parse(response);

            String code = regRetMap.get("success") + "";
            logger.debug("jq return code ==========" + code);
            if ("1".equals(code)) {
                return true;
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

    }

    public static HttpMethod postMethod(String url, Map<String, Object> map,
            String pkSuffix) throws IOException {
        PostMethod post = new PostMethod(url);

        String str = getValuesByKeySorted(map);
        String sign = null;
        try {
            logger.debug("str==" + str + "pkSuffix" + pkSuffix);
            sign = SignUtil.sign(str, pkSuffix);
        } catch (Exception e) {
            logger.debug("签名异常==" + e.getMessage());
            e.printStackTrace();
        }
        post.setRequestHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=utf-8");
        logger.debug("signStr" + sign);
        NameValuePair[] param = {
                new NameValuePair("idcard", (String) map.get("idNO")),
                new NameValuePair("userName", (String) map.get("userName")),
                new NameValuePair("userPhone", (String) map.get("mobile")),
                new NameValuePair("bankcardid", (String) map.get("recard")),
                new NameValuePair("source", "1"),
                new NameValuePair("signStr", sign)};
        post.setRequestBody(param);
        post.releaseConnection();
        return post;
    }

    private static String getValuesByKeySorted(Map<String, Object> map) {
        Map<String, Object> sortedMap = map;
        if (!(map instanceof TreeMap)) {
            sortedMap = new TreeMap<String, Object>();
            sortedMap.putAll(map);
        }
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Object> ent : sortedMap.entrySet()) {
            sb.append(ent.getValue() == null ? "" : ent.getValue().toString());
        }
        return sb.toString();
    }
}
