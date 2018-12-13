package com.iqb.consumer.data.layer.mysql.bean.inst;

import java.math.BigDecimal;

public class InstDetail {
    private Integer repayNo; // 期数
    private Integer days; // 天数
    private BigDecimal interest;// 每期利息

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
