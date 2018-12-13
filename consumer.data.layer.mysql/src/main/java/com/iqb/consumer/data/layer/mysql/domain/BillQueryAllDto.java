package com.iqb.consumer.data.layer.mysql.domain;

import java.math.BigDecimal;

/**
 * chengzhen 2018年4月23日 16:31:15
 */
public class BillQueryAllDto {

    private String orderId;// 订单号
    private String realName;// 姓名
    private String regId;// 手机号
    private String merchantFullName;// 商户
    private String repayNo;// 当前账单期数
    private String orderItems;// 总期数
    private String lastRepayDate;// 最后还款时间
    private BigDecimal curRepayAmt;// 本期应还
    private BigDecimal curRepayPrincipalX;// 日逾期金额
    private String status;// 账单状态
    private String openId;// 行业号

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMerchantFullName() {
        return merchantFullName;
    }

    public void setMerchantFullName(String merchantFullName) {
        this.merchantFullName = merchantFullName;
    }

    public String getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(String repayNo) {
        this.repayNo = repayNo;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public BigDecimal getCurRepayPrincipalX() {
        return curRepayPrincipalX;
    }

    public void setCurRepayPrincipalX(BigDecimal curRepayPrincipalX) {
        this.curRepayPrincipalX = curRepayPrincipalX;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
