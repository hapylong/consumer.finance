/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 下午4:12:14
 * @version V1.0
 */

package com.iqb.consumer.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Configuration
public class ParamConfig {

    /*********************** 短信类参数 *********************************/
    @Value("${vCodeContent}")
    private String vCodeContent;// 验证码

    @Value("${regSuccInfo}")
    private String regSuccInfo;// 注册成功

    @Value("${openAccSuccInfo}")
    private String openAccSuccInfo;// 开户成功

    @Value("${consumerInfo}")
    private String consumerInfo;// 消费信息

    @Value("${installInfo}")
    private String installInfo;// 分期信息

    @Value("${repayInfo}")
    private String repayInfo;// 账单文件信息

    /*************************** 账户公钥地址 ***************************************/
    @Value("${Account.PublicKey}")
    private String publicKey;

    public String getvCodeContent() {
        return vCodeContent;
    }

    public void setvCodeContent(String vCodeContent) {
        this.vCodeContent = vCodeContent;
    }

    public String getRegSuccInfo() {
        return regSuccInfo;
    }

    public void setRegSuccInfo(String regSuccInfo) {
        this.regSuccInfo = regSuccInfo;
    }

    public String getOpenAccSuccInfo() {
        return openAccSuccInfo;
    }

    public void setOpenAccSuccInfo(String openAccSuccInfo) {
        this.openAccSuccInfo = openAccSuccInfo;
    }

    public String getConsumerInfo() {
        return consumerInfo;
    }

    public void setConsumerInfo(String consumerInfo) {
        this.consumerInfo = consumerInfo;
    }

    public String getInstallInfo() {
        return installInfo;
    }

    public void setInstallInfo(String installInfo) {
        this.installInfo = installInfo;
    }

    public String getRepayInfo() {
        return repayInfo;
    }

    public void setRepayInfo(String repayInfo) {
        this.repayInfo = repayInfo;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}
