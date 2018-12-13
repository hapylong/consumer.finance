/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月9日 下午3:21:46
 * @version V1.0
 */
package com.iqb.consumer.finance.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Configuration
public class MsgConfig {
    @Value("${MQ.BASEURL}")
    private String baseUrl;

    // 中阁车辆贷后信息接口
    @Value("${zrzx.queryManagecarInfo.url}")
    private String queryManagecarInfoUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getQueryManagecarInfoUrl() {
        return queryManagecarInfoUrl;
    }

    public void setQueryManagecarInfoUrl(String queryManagecarInfoUrl) {
        this.queryManagecarInfoUrl = queryManagecarInfoUrl;
    }

}
