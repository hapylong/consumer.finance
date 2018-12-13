/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月8日 下午7:27:06
 * @version V1.0
 */
package com.iqb.consumer.finance.batch.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * 给风控的账单信息
 */
public class ToRiskBillInfo extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 3903692535950738742L;
    private String realName;// 真实姓名
    private String orderId;// 订单id
    private String merchantNo;// 商户号
    private int repayNo;// 还款顺序
    private String regId;// 注册号
    private Date lastRepayDate;// 最后还款日
    private Date delayBeginDate;// 滞纳金计算时间
    private BigDecimal fixedOverdueAmt;// 逾期固定收取金额
    private int overdueDays;// 逾期天数
    private Date curRepayDate;// 还款时间
    private BigDecimal curRepayAmt;// 本期应还总金额
    private BigDecimal curRealRepayAmt;// 已经还款金额
    private BigDecimal curRepayOverdueInterest;// 本期应还逾期金额
    private int status;// 账单状态

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public Date getDelayBeginDate() {
        return delayBeginDate;
    }

    public void setDelayBeginDate(Date delayBeginDate) {
        this.delayBeginDate = delayBeginDate;
    }

    public BigDecimal getFixedOverdueAmt() {
        return fixedOverdueAmt;
    }

    public void setFixedOverdueAmt(BigDecimal fixedOverdueAmt) {
        this.fixedOverdueAmt = fixedOverdueAmt;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Date getCurRepayDate() {
        return curRepayDate;
    }

    public void setCurRepayDate(Date curRepayDate) {
        this.curRepayDate = curRepayDate;
    }

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public BigDecimal getCurRealRepayAmt() {
        return curRealRepayAmt;
    }

    public void setCurRealRepayAmt(BigDecimal curRealRepayAmt) {
        this.curRealRepayAmt = curRealRepayAmt;
    }

    public BigDecimal getCurRepayOverdueInterest() {
        return curRepayOverdueInterest;
    }

    public void setCurRepayOverdueInterest(BigDecimal curRepayOverdueInterest) {
        this.curRepayOverdueInterest = curRepayOverdueInterest;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
