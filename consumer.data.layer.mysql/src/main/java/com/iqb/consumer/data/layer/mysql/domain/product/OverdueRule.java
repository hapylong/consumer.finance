package com.iqb.consumer.data.layer.mysql.domain.product;

import java.math.BigDecimal;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * 逾期计算规则
 * 
 * @author <a href="zhuyaoming@aliyun.com">Yeoman</a>
 * 
 */
@SuppressWarnings("serial")
public class OverdueRule extends BaseEntity {
    private Integer poverdueId;
    private Integer baseAmtType;// 金额来源
    private Double ratio;// 逾期费率
    private Double floatRatio;// 浮动比例
    private BigDecimal minAmt; // 最小金额
    private Integer overdueMinDays;// 逾期最少天数
    private Integer overdueMaxDays;// 逾期最多天数
    private String remark;

    public int getPoverdueId() {
        return poverdueId;
    }

    public void setPoverdueId(int poverdueId) {
        this.poverdueId = poverdueId;
    }

    public int getBaseAmtType() {
        return baseAmtType;
    }

    public void setBaseAmtType(int baseAmtType) {
        this.baseAmtType = baseAmtType;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public double getFloatRatio() {
        return floatRatio;
    }

    public void setFloatRatio(double floatRatio) {
        this.floatRatio = floatRatio;
    }

    public BigDecimal getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(BigDecimal minAmt) {
        this.minAmt = minAmt;
    }

    public int getOverdueMinDays() {
        return overdueMinDays;
    }

    public void setOverdueMinDays(int overdueMinDays) {
        this.overdueMinDays = overdueMinDays;
    }

    public int getOverdueMaxDays() {
        return overdueMaxDays;
    }

    public void setOverdueMaxDays(int overdueMaxDays) {
        this.overdueMaxDays = overdueMaxDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
