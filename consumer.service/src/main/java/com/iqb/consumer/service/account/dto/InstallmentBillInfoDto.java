/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 上午11:16:42
 * @version V1.0
 */

package com.iqb.consumer.service.account.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstallmentBillInfoDto {

    private long id;
    private String conUUID;// 用户ID
    private String regID;// 注册号
    private String merchantNo;// 商户号
    private String orderID;// 订单ID
    private BigDecimal currentRepayAmt;// 本期累计还款金额

    public BigDecimal getCurrentRepayAmt() {
        return currentRepayAmt;
    }

    public void setCurrentRepayAmt(BigDecimal currentRepayAmt) {
        this.currentRepayAmt = currentRepayAmt;
    }

    private String sign;// 验签

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConUUID() {
        return conUUID;
    }

    public void setConUUID(String conUUID) {
        this.conUUID = conUUID;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
