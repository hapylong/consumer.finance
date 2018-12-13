package com.iqb.consumer.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Description: consumer配置文件
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月27日    wangxinbang       1.0        1.0 Version
 *          </pre>
 */
@Configuration
public class ConsumerConfig {

    /******************************************** 加密相关 ***********************************************/
    /** 公钥地址 **/
    @Value("${COMMON.PUBLICKEY}")
    private String commonPublicKey;
    /** 私钥地址 **/
    @Value("${COMMON.PRIVATEKEY}")
    private String commonPrivateKey;

    /******************************************** 请求地址 ***********************************************/

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
}
