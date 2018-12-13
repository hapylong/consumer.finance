package com.iqb.consumer.service.consumer.dto;

/**
 * 计划类
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public final class InstPlan {

    private String merchantNo; // 商户号
    private Double downPaymentRatio; // 首付比例，(不含%)
    private Double serviceFeeRatio;// 服务费比例
    private Double marginRatio;// 押金比例
    private Double feeRatio;// 费率(月)
    private int feeYear;// 上收息(月数)
    private Double upInterestFee;// (上收息对应的利率)
    private int takePayment;// 上收月供对应的(月数)
    private int installPeriods;// 分期期数
    private String remark;// 备注
    private int planId;// 账务ID
    private Double reCharge;// 充值费率
    private double floatServiceFeeRatio;// 上浮服务费比例

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Double getDownPaymentRatio() {
        return downPaymentRatio;
    }

    public void setDownPaymentRatio(Double downPaymentRatio) {
        this.downPaymentRatio = downPaymentRatio;
    }

    public Double getServiceFeeRatio() {
        return serviceFeeRatio;
    }

    public void setServiceFeeRatio(Double serviceFeeRatio) {
        this.serviceFeeRatio = serviceFeeRatio;
    }

    public Double getMarginRatio() {
        return marginRatio;
    }

    public void setMarginRatio(Double marginRatio) {
        this.marginRatio = marginRatio;
    }

    public Double getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(Double feeRatio) {
        this.feeRatio = feeRatio;
    }

    public int getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(int feeYear) {
        this.feeYear = feeYear;
    }

    public Double getUpInterestFee() {
        return upInterestFee;
    }

    public void setUpInterestFee(Double upInterestFee) {
        this.upInterestFee = upInterestFee;
    }

    public int getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(int takePayment) {
        this.takePayment = takePayment;
    }

    public int getInstallPeriods() {
        return installPeriods;
    }

    public void setInstallPeriods(int installPeriods) {
        this.installPeriods = installPeriods;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public Double getReCharge() {
        return reCharge;
    }

    public void setReCharge(Double reCharge) {
        this.reCharge = reCharge;
    }

    public double getFloatServiceFeeRatio() {
        return floatServiceFeeRatio;
    }

    public void setFloatServiceFeeRatio(double floatServiceFeeRatio) {
        this.floatServiceFeeRatio = floatServiceFeeRatio;
    }

}
