/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月15日 上午11:00:03
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
public class InstallmentPlanDto {
    private long id;
    private String merchantNo;// 商户号
    private double feeRate;// 手续费费率(50.2% 50.2)
    private int feeType;// 手续费费率类型(0y,1m,2d)
    private double overdueRate;// 逾期费率(滞纳金)
    private BigDecimal overdueAmount;// 逾期固定费用
    private int periods;// 分期周期(非必要字段)
    private BigDecimal fixedAmount;// 固定金额
    private int installType;// 分期类型(固定日期账单,月对月账单,隔日账单,实时账单)
    private String planName;// 分期计划名称
    private BigDecimal minAmt;// 每期最少分期金额
    private int chkAccStatus;// 分期是否检查用户状态(0、检查,1、不检查)
    private int graceDays;// 宽限天数
    private int needShare;// 需后台分摊(0、不需要,1、需要)
    private String sign;// 验签
    private Date createTime = new Date();
    private Date updateTime = new Date();

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public double getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(double feeRate) {
        this.feeRate = feeRate;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public double getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(double overdueRate) {
        this.overdueRate = overdueRate;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public int getInstallType() {
        return installType;
    }

    public void setInstallType(int installType) {
        this.installType = installType;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(BigDecimal minAmt) {
        this.minAmt = minAmt;
    }

    public int getChkAccStatus() {
        return chkAccStatus;
    }

    public void setChkAccStatus(int chkAccStatus) {
        this.chkAccStatus = chkAccStatus;
    }

    public int getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(int graceDays) {
        this.graceDays = graceDays;
    }

    public int getNeedShare() {
        return needShare;
    }

    public void setNeedShare(int needShare) {
        this.needShare = needShare;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
