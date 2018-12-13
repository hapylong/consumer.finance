/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 违约金(滞纳金)
 * @date 2016年7月25日 下午5:07:23
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class DelayPayment extends BaseEntity {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 4994325106862463288L;

    /**
     * 注册号
     */
    private String regId;

    /**
     * 开户号
     */
    private String openId;

    /**
     * 账单号
     */
    private long billId;

    /**
     * 还款顺序号
     */
    private long repayNo;

    /**
     * 剩余本金
     */
    private BigDecimal principalAmt;

    /**
     * 违约金费率
     */
    private double overdueRate;

    /**
     * 违约金
     */
    private BigDecimal delayAmt;

    /**
     * 逾期日期
     */
    private Date delayDate;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public long getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(long repayNo) {
        this.repayNo = repayNo;
    }

    public BigDecimal getPrincipalAmt() {
        return principalAmt;
    }

    public void setPrincipalAmt(BigDecimal principalAmt) {
        this.principalAmt = principalAmt;
    }

    public double getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(double overdueRate) {
        this.overdueRate = overdueRate;
    }

    public BigDecimal getDelayAmt() {
        return delayAmt;
    }

    public void setDelayAmt(BigDecimal delayAmt) {
        this.delayAmt = delayAmt;
    }

    public Date getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
