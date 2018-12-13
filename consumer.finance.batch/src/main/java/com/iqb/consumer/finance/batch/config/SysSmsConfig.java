/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 下午2:44:22
 * @version V1.0
 */
package com.iqb.consumer.finance.batch.config;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class SysSmsConfig extends BaseEntity {

    private static final long serialVersionUID = 1117515709305584097L;
    private String openId;
    private int wechatNo;
    private String wechatRemark;
    private String smsUrl;
    private String smsName;
    private String smsPswd;
    private int smsNeedStatus;// 1 TRUE 2 FALSE

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(int wechatNo) {
        this.wechatNo = wechatNo;
    }

    public String getWechatRemark() {
        return wechatRemark;
    }

    public void setWechatRemark(String wechatRemark) {
        this.wechatRemark = wechatRemark;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getSmsName() {
        return smsName;
    }

    public void setSmsName(String smsName) {
        this.smsName = smsName;
    }

    public String getSmsPswd() {
        return smsPswd;
    }

    public void setSmsPswd(String smsPswd) {
        this.smsPswd = smsPswd;
    }

    public int getSmsNeedStatus() {
        return smsNeedStatus;
    }

    public void setSmsNeedStatus(int smsNeedStatus) {
        this.smsNeedStatus = smsNeedStatus;
    }

    @Override
    public String toString() {
        return "[openId]" + this.openId + "[smsName]" + this.smsName + "[smsPswd]" + this.smsPswd + "[wechatNo]"
                + this.wechatNo + "[wechatRemark]" + this.wechatRemark;
    }

}
