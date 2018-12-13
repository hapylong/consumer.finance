/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月7日 下午4:40:27
 * @version V1.0
 */
package com.iqb.consumer.virtual.account.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 分期数据传输类
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstallmentDto {

    private String orderId;// 订单号
    private String orderDate;// 订单日期
    private String regId;// 注册用户
    private String openId;// 开户对应的行业ID(质押车,以租代售,抵押车,易安家,医美)
    private String merchantNo;// 商户号
    private String beginDate;// 起息日(账单生成日)
    private BigDecimal contractAmt;// 合同金额
    private BigDecimal installSumAmt;// 分期总金额本金
    private BigDecimal installAmt;// 分期金额本金
    private BigDecimal interestAmt;// 剩余利息
    private BigDecimal takePaymentAmt;// 上收月供金额
    private BigDecimal otherAmt;// 其他费用，这个费用需要添加到每个月月供中
    private int takeOtherAmt;// 是否上收其他费用,上收，则最后一期无，不上收则包含最后一期
    private int interestTerms;// 剩余利息分摊期数
    private int takePayment;// 上收月供数
    private int takeInterest;// 是否上收利息
    private int takeMonth;// 是否上收月供
    private int installTerms;// 分期期数
    private long planId;// 计划ID
    private String sourcesFunding;// 资金来源
    private String fundId;// 上标ID

    private String smsMobile;// 接收短信手机号码

    public int getTakeOtherAmt() {
        return takeOtherAmt;
    }

    public void setTakeOtherAmt(int takeOtherAmt) {
        this.takeOtherAmt = takeOtherAmt;
    }

    public int getInterestTerms() {
        return interestTerms;
    }

    public void setInterestTerms(int interestTerms) {
        this.interestTerms = interestTerms;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
    }

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

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getInstallSumAmt() {
        return installSumAmt;
    }

    public void setInstallSumAmt(BigDecimal installSumAmt) {
        this.installSumAmt = installSumAmt;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
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

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getSmsMobile() {
        return smsMobile;
    }

    public void setSmsMobile(String smsMobile) {
        this.smsMobile = smsMobile;
    }

}
