package com.iqb.consumer.finance.batch.bean;

import java.math.BigDecimal;

/**
 * 
 * Description: 提醒短信bean
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年1月11日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class RemindSmsBean {

    private String regId;

    private BigDecimal repayAmount;

    private BigDecimal overDueAmount;

    private String repayMonth;

    private String repayDay;

    private String openId;

    private Integer type;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public BigDecimal getOverDueAmount() {
        return overDueAmount;
    }

    public void setOverDueAmount(BigDecimal overDueAmount) {
        this.overDueAmount = overDueAmount;
    }

    public String getRepayMonth() {
        return repayMonth;
    }

    public void setRepayMonth(String repayMonth) {
        this.repayMonth = repayMonth;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
