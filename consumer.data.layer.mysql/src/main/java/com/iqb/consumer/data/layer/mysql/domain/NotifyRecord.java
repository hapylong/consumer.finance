/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月21日 下午1:17:03
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.domain;

import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class NotifyRecord extends BaseEntity {
    private Date lastNotifyTime;// 最后通知时间
    private int notifyTimes = 0;;// 通知次数
    private int limitNotifyTimes = 5;// 通知上线次数
    private String url;// 通知地址
    private String msg;// 内容
    private String merchantNo;// 商户号
    private String merchantOrderNo;// 订单号
    private int status = 101;;// 状态
    private int notifyType = 0;// 通知类型

    public Date getLastNotifyTime() {
        return lastNotifyTime;
    }

    public void setLastNotifyTime(Date lastNotifyTime) {
        this.lastNotifyTime = lastNotifyTime;
    }

    public int getNotifyTimes() {
        return notifyTimes;
    }

    public void setNotifyTimes(int notifyTimes) {
        this.notifyTimes = notifyTimes;
    }

    public int getLimitNotifyTimes() {
        return limitNotifyTimes;
    }

    public void setLimitNotifyTimes(int limitNotifyTimes) {
        this.limitNotifyTimes = limitNotifyTimes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    @Override
    public String toString() {
        return "订单号:{" + this.merchantOrderNo + "}商户号:{" + this.merchantNo + "}通知地址:{" + this.url + "}通知次数{"
                + this.notifyTimes + "}";
    }

}
