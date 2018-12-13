/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 下午2:02:05
 * @version V1.0
 */

package com.iqb.consumer.service.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Configuration
public class SmsConfig {

    @Value("${smsUrl}")
    private String url;
    @Value("${smsNameInfo}")
    private String account;
    @Value("${smsPswdInfo}")
    private String pswd;
    @Value("${smsNameFHH}")
    private String accountByHH;
    @Value("${smsPswdFHH}")
    private String pswdByHH;
    @Value("${smsNeedStatusInfo}")
    private String needstatus;

    public String getAccountByHH() {
        return accountByHH;
    }

    public void setAccountByHH(String accountByHH) {
        this.accountByHH = accountByHH;
    }

    public String getPswdByHH() {
        return pswdByHH;
    }

    public void setPswdByHH(String pswdByHH) {
        this.pswdByHH = pswdByHH;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getNeedstatus() {
        return needstatus;
    }

    public void setNeedstatus(String needstatus) {
        this.needstatus = needstatus;
    }

}
