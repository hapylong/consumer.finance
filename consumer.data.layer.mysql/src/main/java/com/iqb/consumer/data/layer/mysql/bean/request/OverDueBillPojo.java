package com.iqb.consumer.data.layer.mysql.bean.request;

import java.math.BigDecimal;

public class OverDueBillPojo {
    private String orderId;
    private Integer repayNo; // 应还期数
    private BigDecimal Amt; // 应还金额

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public BigDecimal getAmt() {
        return Amt;
    }

    public void setAmt(BigDecimal amt) {
        Amt = amt;
    }
}
