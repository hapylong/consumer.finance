/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月21日 下午1:17:29
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.bean;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class NotifyRecordLog extends BaseEntity {
    private static final long serialVersionUID = 5457186116355663248L;
    private long notifyID;//
    private String request;
    private String response;
    private String merchantNo;
    private String merchantOrderNo;
    private int httpStatus;

    public long getNotifyID() {
        return notifyID;
    }

    public void setNotifyID(long notifyID) {
        this.notifyID = notifyID;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
