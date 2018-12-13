package com.iqb.consumer.common.utils;

import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpsClientUtil {

    private static class SingletonHolder {
        private static final HttpsClientUtil INSTANCE = new HttpsClientUtil();
    }

    /**
     * Singleton! This class can be created only once!
     * 
     * @return
     */
    public static final HttpsClientUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 
     * @param url
     * @param map
     * @param charset
     * @return
     */
    public String doPostold(String url, String jsonStr, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(URLEncoder.encode(jsonStr, charset));
            entity.setContentEncoding("utf-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);

            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    // result =
                    // URLDecoder.decode(EntityUtils.toString(resEntity,charset));
                    result = EntityUtils.toString(resEntity, "ISO8859-1");
                    result = java.net.URLDecoder.decode(result, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * @param url
     * @param map
     * @param charset
     * @return
     */
    public static String doPost(String url, String jsonStr, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient(); // 一般用法
            httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 50000);
            httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 50000);
            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonStr, charset);
            httpPost.setEntity(entity);
            entity.setContentEncoding("utf-8");
            entity.setContentType("application/json");
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * @param url
     * @param map
     * @param charset
     * @return
     */
    public String doPostHttps(String url, String jsonStr) {
        return doPost(url, jsonStr, "utf-8");
    }

    static class SSLClient extends DefaultHttpClient {
        public SSLClient() throws Exception
        {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {}

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {}

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] {tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
        }
    }
}
