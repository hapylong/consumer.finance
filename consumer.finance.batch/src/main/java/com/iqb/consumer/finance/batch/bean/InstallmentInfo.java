/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月8日 下午6:49:25
 * @version V1.0
 */
package com.iqb.consumer.finance.batch.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * 分期信息原始总表
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentInfo extends BaseEntity {
    private static final long serialVersionUID = -6795860388674840007L;
    private String orderId;// 订单ID
    private Date orderDate;// 订单时间
    private String regId;// 注册号
    private long baseId;// 总开户号
    private String openId;// 行业开户号
    private Date beginDate;// 账单开始时间
    private BigDecimal contractAmt;// 合同金额
    private BigDecimal installSumAmt;// 分期总金额
    private BigDecimal installAmt;// 分期实际总额
    private int installTerms;// 分期期数
    private BigDecimal interestAmt;// 剩余利息
    private BigDecimal takePaymentAmt;// 上收月供金额
    private int takePayment;// 上收月供期数
    private String merchantNo;// 商户号
    private long planId;// 分期计划编号
    private int instOrder;// 放款顺序
    private int status;// 分期状态
    private int useFreeInterest;// 是否使用了免息卷(0、否 1、是)
    private int freeInterestCount;// 免息卷张数(必须小于<=分期期数)
    private int takeInterest;// 是否上收利息
    private int takeMonth;// 是否上收月供
    private String sourcesFunding;// 资金来源
    private String fundId;// 上标ID

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public int getTakeInterest() {
        return takeInterest;
    }

    public void setTakeInterest(int takeInterest) {
        this.takeInterest = takeInterest;
    }

    public int getTakeMonth() {
        return takeMonth;
    }

    public void setTakeMonth(int takeMonth) {
        this.takeMonth = takeMonth;
    }

    public BigDecimal getInterestAmt() {
        return interestAmt;
    }

    public void setInterestAmt(BigDecimal interestAmt) {
        this.interestAmt = interestAmt;
    }

    public BigDecimal getTakePaymentAmt() {
        return takePaymentAmt;
    }

    public void setTakePaymentAmt(BigDecimal takePaymentAmt) {
        this.takePaymentAmt = takePaymentAmt;
    }

    public int getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(int takePayment) {
        this.takePayment = takePayment;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getInstOrder() {
        return instOrder;
    }

    public void setInstOrder(int instOrder) {
        this.instOrder = instOrder;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public long getBaseId() {
        return baseId;
    }

    public void setBaseId(long baseId) {
        this.baseId = baseId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public BigDecimal getInstallSumAmt() {
        return installSumAmt;
    }

    public void setInstallSumAmt(BigDecimal installSumAmt) {
        this.installSumAmt = installSumAmt;
    }

    public BigDecimal getInstallAmt() {
        return installAmt;
    }

    public void setInstallAmt(BigDecimal installAmt) {
        this.installAmt = installAmt;
    }

    public int getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(int installTerms) {
        this.installTerms = installTerms;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUseFreeInterest() {
        return useFreeInterest;
    }

    public void setUseFreeInterest(int useFreeInterest) {
        this.useFreeInterest = useFreeInterest;
    }

    public int getFreeInterestCount() {
        return freeInterestCount;
    }

    public void setFreeInterestCount(int freeInterestCount) {
        this.freeInterestCount = freeInterestCount;
    }
}
