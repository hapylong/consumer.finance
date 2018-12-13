package com.iqb.consumer.service.consumer.dto;

import java.math.BigDecimal;

/**
 * 金额计算计划类
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class CalculateAmt {

    public InstPlan instPlan;
    public BigDecimal amt;

    public InstPlan getInstPlan() {
        return instPlan;
    }

    public void setInstPlan(InstPlan instPlan) {
        this.instPlan = instPlan;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
}
