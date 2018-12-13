package com.iqb.consumer.finance.batch.bean;

import java.math.BigDecimal;

/**
 * 存量统计bean
 */
public class StockStatisticsBean {

    private String orderId;// 订单Id
    private String realName;// 真实姓名
    private String regId;// 手机号
    private String openId;// 账户号
    private String merchantNo;// 商户号
    private String sourcesFunding;// 资金来源
    private String fundId;// 上标ID
    private int installTerms;// 分期期数
    private int repayNo;// 还款顺序
    private BigDecimal contractAmt;// 合同金额
    private BigDecimal installSumAmt;// 分期总金额
    private BigDecimal curRepayPrincipal;// 本期应还本金
    private BigDecimal remainPrincipal;// 剩余本金

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public int getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(int installTerms) {
        this.installTerms = installTerms;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
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

    public BigDecimal getCurRepayPrincipal() {
        return curRepayPrincipal;
    }

    public void setCurRepayPrincipal(BigDecimal curRepayPrincipal) {
        this.curRepayPrincipal = curRepayPrincipal;
    }

    public BigDecimal getRemainPrincipal() {
        return remainPrincipal;
    }

    public void setRemainPrincipal(BigDecimal remainPrincipal) {
        this.remainPrincipal = remainPrincipal;
    }

}
