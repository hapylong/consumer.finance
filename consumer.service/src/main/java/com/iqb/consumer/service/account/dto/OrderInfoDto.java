/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 下午5:31:29
 * @version V1.0
 */

package com.iqb.consumer.service.account.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInfoDto {
    private long id;
    private String regID;//
    private String orderNo;// 唯一订单号
    private BigDecimal sumAmount;// 账单总金额
    private BigDecimal sumShareAmount;// 实际分期总额
    private int sumPeriods;// 总分期周期
    private int realPeriods;// 实际分期期数
    private BigDecimal eachAmount;// 每期金额
    private BigDecimal eachShareAmount;// 分期分摊金额
    private int needShare;// 需后台分摊(0、不需要,1、需要)
    private BigDecimal prePayment;// 首付金额
    private BigDecimal margin;// 保证金
    private BigDecimal serviceFee;// 服务费
    private BigDecimal downPayment;// 定金
    private BigDecimal finalPayment;// 尾款
    private BigDecimal refundAmount;// 退款金额
    private int refundStatus;// 退款状态
    private int feeType;// 费率类型(年费率,月费率,日费率)
    private int installType;// 分期类型(固定日期账单,月对月账单,隔日账单,实时账单)
    private int orderStatus;// 订单状态
    private int useFreeInterest = 0;// 使用免息卷
    private int freeInterestCount;// 免息卷张数(必须小于<=分期期数)
    private int installNo;// 分期计划
    private String merchantNo;// 商户ID
    private String merchantRemark;// 商户备注
    private String orderInfo;// 订单详情
    private String conUuid;// 用户
    private String conRemark;// 用户备注

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    private String sign;// 签名

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public BigDecimal getSumShareAmount() {
        return sumShareAmount;
    }

    public void setSumShareAmount(BigDecimal sumShareAmount) {
        this.sumShareAmount = sumShareAmount;
    }

    public int getSumPeriods() {
        return sumPeriods;
    }

    public void setSumPeriods(int sumPeriods) {
        this.sumPeriods = sumPeriods;
    }

    public int getRealPeriods() {
        return realPeriods;
    }

    public void setRealPeriods(int realPeriods) {
        this.realPeriods = realPeriods;
    }

    public BigDecimal getEachAmount() {
        return eachAmount;
    }

    public void setEachAmount(BigDecimal eachAmount) {
        this.eachAmount = eachAmount;
    }

    public BigDecimal getEachShareAmount() {
        return eachShareAmount;
    }

    public void setEachShareAmount(BigDecimal eachShareAmount) {
        this.eachShareAmount = eachShareAmount;
    }

    public int getNeedShare() {
        return needShare;
    }

    public void setNeedShare(int needShare) {
        this.needShare = needShare;
    }

    public BigDecimal getPrePayment() {
        return prePayment;
    }

    public void setPrePayment(BigDecimal prePayment) {
        this.prePayment = prePayment;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public BigDecimal getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(BigDecimal finalPayment) {
        this.finalPayment = finalPayment;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public int getInstallType() {
        return installType;
    }

    public void setInstallType(int installType) {
        this.installType = installType;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
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

    public int getInstallNo() {
        return installNo;
    }

    public void setInstallNo(int installNo) {
        this.installNo = installNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantRemark() {
        return merchantRemark;
    }

    public void setMerchantRemark(String merchantRemark) {
        this.merchantRemark = merchantRemark;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getConUuid() {
        return conUuid;
    }

    public void setConUuid(String conUuid) {
        this.conUuid = conUuid;
    }

    public String getConRemark() {
        return conRemark;
    }

    public void setConRemark(String conRemark) {
        this.conRemark = conRemark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
