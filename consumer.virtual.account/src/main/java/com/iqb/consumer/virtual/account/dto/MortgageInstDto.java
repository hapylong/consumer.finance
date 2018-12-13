package com.iqb.consumer.virtual.account.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstDetail;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MortgageInstDto {

    private String orderId;// 订单号
    private String regId;// 注册用户
    private String openId;// 开户对应的行业ID(质押车,以租代售,抵押车,易安家,医美)
    private String orderDate;// 订单日期
    private String merchantNo;// 商户号
    private String beginDate;// 起息日(账单生成日)
    private BigDecimal contractAmt;// 核准金额
    private BigDecimal installSumAmt;// 分期总金额本金
    private BigDecimal installAmt;// 分期金额本金
    private BigDecimal takePaymentAmt;// 上收月供金额
    private BigDecimal interestAmt;// 剩余利息
    private int installTerms;// 分期期数
    private int takePayment;// 上收月供数
    private int takeInterest;// 是否上收利息(1,是 2,否)
    private int takeMonth;// 是否上收月供(1,是 2,否)
    private long planId;// 计划ID
    private List<InstDetail> instDetails;// 分期详情单
    private String sourcesFunding;// 资金来源
    private BigDecimal otherAmt;// 其他费用，这个费用需要添加到每个月月供中
    private String installUnit;// 分期单位月份的详情太大，可以通过该字段节省instDetails大小
    private String fundId;// 上标ID
    private int interestTerms;// 剩余利息分摊期数
    private int takeOtherAmt;// 是否上收其他费用,上收，则最后一期无，不上收则包含最后一期

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

    public String getInstallUnit() {
        return installUnit;
    }

    public void setInstallUnit(String installUnit) {
        this.installUnit = installUnit;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
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

    public BigDecimal getTakePaymentAmt() {
        return takePaymentAmt;
    }

    public void setTakePaymentAmt(BigDecimal takePaymentAmt) {
        this.takePaymentAmt = takePaymentAmt;
    }

    public BigDecimal getInterestAmt() {
        return interestAmt;
    }

    public void setInterestAmt(BigDecimal interestAmt) {
        this.interestAmt = interestAmt;
    }

    public int getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(int installTerms) {
        this.installTerms = installTerms;
    }

    public int getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(int takePayment) {
        this.takePayment = takePayment;
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

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public List<InstDetail> getInstDetails() {
        return instDetails;
    }

    public void setInstDetails(List<InstDetail> instDetails) {
        this.instDetails = instDetails;
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
}
