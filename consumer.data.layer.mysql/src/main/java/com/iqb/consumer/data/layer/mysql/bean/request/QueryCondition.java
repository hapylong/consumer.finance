package com.iqb.consumer.data.layer.mysql.bean.request;

public class QueryCondition {
    String orderId; // 订单号
    String repayNo;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(String repayNo) {
        this.repayNo = repayNo;
    } // 期数
}
