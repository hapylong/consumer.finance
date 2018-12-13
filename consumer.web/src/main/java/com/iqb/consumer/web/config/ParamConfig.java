/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 下午4:12:14
 * @version V1.0
 */

package com.iqb.consumer.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Description: 配置
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
@Configuration
public class ParamConfig {

    /*************************** 公钥地址 ***************************************/
    @Value("${common.publicKey}")
    private String commonPublicKey;

    /*************************** 私钥地址 ***************************************/
    @Value("${common.privateKey}")
    private String commonPrivateKey;

    /*************************** 请求地址 ***************************************/
    @Value("${http.interface.request.base.url}")
    private String requestBaseUrl;

    public String getCommonPublicKey() {
        return commonPublicKey;
    }

    public void setCommonPublicKey(String commonPublicKey) {
        this.commonPublicKey = commonPublicKey;
    }

    public String getCommonPrivateKey() {
        return commonPrivateKey;
    }

    public void setCommonPrivateKey(String commonPrivateKey) {
        this.commonPrivateKey = commonPrivateKey;
    }

    public String getRequestBaseUrl() {
        return requestBaseUrl;
    }

    public void setRequestBaseUrl(String requestBaseUrl) {
        this.requestBaseUrl = requestBaseUrl;
    }

}
