package com.iqb.consumer.data.layer.mysql.bean.product;

import java.math.BigDecimal;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * 
 * Description: 违约金bean
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class BreachContract extends BaseEntity {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 3552769517692849335L;

    /**
     * 固定金额
     */
    private BigDecimal fixedAmount;

    /**
     * 下限
     */
    private BigDecimal minAmt;

    /**
     * 上限
     */
    private BigDecimal maxAmt;

    /**
     * 上下限是否可用
     */
    private String enable;

    /**
     * 违约比例基数
     */
    private String baseAmtType;

    /**
     * 违约计算比例
     */
    private BigDecimal ratio;

    /**
     * 违约固定期数
     */
    private Integer fixedNper;

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public BigDecimal getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(BigDecimal minAmt) {
        this.minAmt = minAmt;
    }

    public BigDecimal getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(BigDecimal maxAmt) {
        this.maxAmt = maxAmt;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getBaseAmtType() {
        return baseAmtType;
    }

    public void setBaseAmtType(String baseAmtType) {
        this.baseAmtType = baseAmtType;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Integer getFixedNper() {
        return fixedNper;
    }

    public void setFixedNper(Integer fixedNper) {
        this.fixedNper = fixedNper;
    }

}
