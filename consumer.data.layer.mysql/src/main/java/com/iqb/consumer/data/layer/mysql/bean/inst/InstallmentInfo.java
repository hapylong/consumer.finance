/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月8日 下午6:49:25
 * @version V1.0
 */
package com.iqb.consumer.data.layer.mysql.bean.inst;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * 分期信息原始总表
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentInfo extends BaseEntity {
    private String orderId;// 订单ID
    private String subOrderId;// 子订单号
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
    private BigDecimal otherAmt;// 其他费用，这个费用需要添加到每个月月供中
    private int takePayment;// 上收月供期数
    private String merchantNo;// 商户号
    private long planId;// 分期计划编号
    private int instOrder;// 放款顺序
    private int status;// 分期状态
    private int useFreeInterest;// 是否使用了免息卷(0、否 1、是)
    private int freeInterestCount;// 免息卷张数(必须小于<=分期期数)
    private int takeInterest;// 是否上收利息
    private int takeMonth;// 是否上收月供
    private String instDetails;// 分期详情 addBy yeoman 20170802
    private String installUnit;// 分期单位月份的详情太大，可以通过该字段节省instDetails大小
    private String sourcesFunding;// 资金来源
    private String fundId;// 上标ID
    private int takeOtherAmt;// 是否上收其他费用,上收，则最后一期无，不上收则包含最后一期
    private int interestTerms;// 剩余利息分摊期数
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

    public String getInstallUnit() {
        return installUnit;
    }

    public void setInstallUnit(String installUnit) {
        this.installUnit = installUnit;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getInstDetails() {
        return instDetails;
    }

    public void setInstDetails(String instDetails) {
        this.instDetails = instDetails;
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

    public String getSmsMobile() {
        return smsMobile;
    }

    public void setSmsMobile(String smsMobile) {
        this.smsMobile = smsMobile;
    }

}
