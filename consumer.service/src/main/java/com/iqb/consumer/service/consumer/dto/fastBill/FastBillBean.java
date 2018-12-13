/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月17日 上午11:33:31
 * @version V1.0
 */
package com.iqb.consumer.service.consumer.dto.fastBill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class FastBillBean {

    private String orderId;
    private Date orderDate;
    private String regId;
    private Date beginDate;
    private String openId;
    private String merchantNo;
    private BigDecimal installSumAmt;
    private BigDecimal installAmt;
    private BigDecimal contractAmt;// 合同金额
    private int installTerms;
    private int planId;
    private String fundId;
    private String sourcesFunding;
    private List<Plan> plan;

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
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

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
    }

    public List<Plan> getPlan() {
        return plan;
    }

    public void setPlan(List<Plan> plan) {
        this.plan = plan;
    }

}
