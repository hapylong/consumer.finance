/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月14日 上午11:50:48
 * @version V1.0
 */

package com.iqb.consumer.virtual.account.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Configuration
public class AccountConifg {

    // 账户公钥地址
    @Value("${Account.PublicKey}")
    private String publicKey;
    /*************************** 功能回调地址 ***************************************/
    // 生成账单地址
    @Value("${NOTITY_CREATEBILL_URL}")
    private String notifyBillUrl;
    /*************************** 短信通知地址 ******************************************/
    // 分期短信发送地址
    @Value("${SMS_INSTALL}")
    private String installSMSUrl;
    // 开户短信发送
    @Value("${SMS_OPENACCURL}")
    private String smsUrl;
    /*************************** 短信内容 **********************************************/
    // 开户短信信息
    @Value("${openAccSuccInfo}")
    private String smsOpenAcc;
    // 分期短信内容
    @Value("${installInfo}")
    private String smsInstallInfo;
    // 还款提醒短信内容
    @Value("${smsRepayInfo}")
    private String smsRepayInfo;
    // 催收短信内容
    @Value("${smsOverDueInfo}")
    private String smsOverDueInfo;
    // 罚息利率
    @Value("${interestRate}")
    private String interestRate;

    public String getSmsRepayInfo() {
        return smsRepayInfo;
    }

    public void setSmsRepayInfo(String smsRepayInfo) {
        this.smsRepayInfo = smsRepayInfo;
    }

    public String getSmsOverDueInfo() {
        return smsOverDueInfo;
    }

    public void setSmsOverDueInfo(String smsOverDueInfo) {
        this.smsOverDueInfo = smsOverDueInfo;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getInstallSMSUrl() {
        return installSMSUrl;
    }

    public void setInstallSMSUrl(String installSMSUrl) {
        this.installSMSUrl = installSMSUrl;
    }

    public String getSmsInstallInfo() {
        return smsInstallInfo;
    }

    public void setSmsInstallInfo(String smsInstallInfo) {
        this.smsInstallInfo = smsInstallInfo;
    }

    public String getSmsOpenAcc() {
        return smsOpenAcc;
    }

    public void setSmsOpenAcc(String smsOpenAcc) {
        this.smsOpenAcc = smsOpenAcc;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getNotifyBillUrl() {
        return notifyBillUrl;
    }

    public void setNotifyBillUrl(String notifyBillUrl) {
        this.notifyBillUrl = notifyBillUrl;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

}
