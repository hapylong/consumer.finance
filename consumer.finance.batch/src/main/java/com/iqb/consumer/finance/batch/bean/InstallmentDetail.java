/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月8日 下午6:59:17
 * @version V1.0
 */
package com.iqb.consumer.finance.batch.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * 分期分摊信息表
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentDetail extends BaseEntity {
    private static final long serialVersionUID = 1905925120516783141L;
    private String orderId;// 订单ID
    private Date orderDate;// 订单时间
    private int repayNo;// 还款序号
    private long installInfoId;// 分期ID--->对应installInfo主键
    private String regId;// 注册号
    private String openId;// 对应行业ID
    private Date installDate;// 分期日期
    private BigDecimal principal;// 每期本金
    private BigDecimal remainPrincipal;// 剩余本金
    private BigDecimal realPayamt;// 还款分摊金额
    private long planId;// 计划ID
    private BigDecimal contractAmt;// 合同金额
    private BigDecimal installSumAmt;// 分期总金额
    private BigDecimal installAmt;// 分期金额
    private BigDecimal interestAmt;// 剩余利息
    private BigDecimal takePaymentAmt;// 上收月供金额
    private int takePayment;// 上收月供数
    private int installTerms;// 分期期数
    private String merchantNo;// 商户号
    private int instOrder;// 分期次数
    private int status;
    private int useFreeInterest;// 是否使用了免息卷
    private int takeInterest;// 是否上收利息
    private int takeMonth;// 是否上收月供

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

    public BigDecimal getInstallSumAmt() {
        return installSumAmt;
    }

    public void setInstallSumAmt(BigDecimal installSumAmt) {
        this.installSumAmt = installSumAmt;
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

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public long getInstallInfoId() {
        return installInfoId;
    }

    public void setInstallInfoId(long installInfoId) {
        this.installInfoId = installInfoId;
    }

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

    public Date getInstallDate() {
        return installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getRemainPrincipal() {
        return remainPrincipal;
    }

    public void setRemainPrincipal(BigDecimal remainPrincipal) {
        this.remainPrincipal = remainPrincipal;
    }

    public BigDecimal getRealPayamt() {
        return realPayamt;
    }

    public void setRealPayamt(BigDecimal realPayamt) {
        this.realPayamt = realPayamt;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
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

}
