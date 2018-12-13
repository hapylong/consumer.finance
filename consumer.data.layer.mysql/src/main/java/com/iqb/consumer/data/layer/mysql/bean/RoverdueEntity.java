package com.iqb.consumer.data.layer.mysql.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * @Description:Mysql Table: finance.r_overdue
 * @author adam
 * @date 2017年10月18日 下午5:02:04
 * @version
 */
public class RoverdueEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String merchantNo;

    private Date schedueDate;

    private BigDecimal overdueMonthMd5;

    private BigDecimal overdueInterestMd5;

    private BigDecimal fixedOverdueAmtMd5;

    private BigDecimal curRepayAmtMd5;

    private BigDecimal overdueRateMd5;

    private BigDecimal overdueMonthMb1;

    private BigDecimal overdueInterestMb1;

    private BigDecimal fixedOverdueAmtMb1;

    private BigDecimal curRepayAmtMb1;

    private BigDecimal outOverdueRateMb1;

    private BigDecimal overduePrincipalMb1;

    private BigDecimal stockAmtMb1;

    private BigDecimal overdueRateMb1;

    private BigDecimal overduePrincipalMb2;

    private BigDecimal stockAmtMb2;

    private BigDecimal overdueRateMb2;

    private BigDecimal overduePrincipalMb3;

    private BigDecimal stockAmtMb3;

    private BigDecimal overdueRateMb3;

    private BigDecimal overduePrincipalMb4;

    private BigDecimal stockAmtMb4;

    private BigDecimal overdueRateMb4;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Date getSchedueDate() {
        return schedueDate;
    }

    public void setSchedueDate(Date schedueDate) {
        this.schedueDate = schedueDate;
    }

    public BigDecimal getOverdueMonthMd5() {
        return overdueMonthMd5;
    }

    public void setOverdueMonthMd5(BigDecimal overdueMonthMd5) {
        this.overdueMonthMd5 = overdueMonthMd5;
    }

    public BigDecimal getOverdueInterestMd5() {
        return overdueInterestMd5;
    }

    public void setOverdueInterestMd5(BigDecimal overdueInterestMd5) {
        this.overdueInterestMd5 = overdueInterestMd5;
    }

    public BigDecimal getFixedOverdueAmtMd5() {
        return fixedOverdueAmtMd5;
    }

    public void setFixedOverdueAmtMd5(BigDecimal fixedOverdueAmtMd5) {
        this.fixedOverdueAmtMd5 = fixedOverdueAmtMd5;
    }

    public BigDecimal getCurRepayAmtMd5() {
        return curRepayAmtMd5;
    }

    public void setCurRepayAmtMd5(BigDecimal curRepayAmtMd5) {
        this.curRepayAmtMd5 = curRepayAmtMd5;
    }

    public BigDecimal getOverdueRateMd5() {
        return overdueRateMd5;
    }

    public void setOverdueRateMd5(BigDecimal overdueRateMd5) {
        this.overdueRateMd5 = overdueRateMd5;
    }

    public BigDecimal getOverdueMonthMb1() {
        return overdueMonthMb1;
    }

    public void setOverdueMonthMb1(BigDecimal overdueMonthMb1) {
        this.overdueMonthMb1 = overdueMonthMb1;
    }

    public BigDecimal getOverdueInterestMb1() {
        return overdueInterestMb1;
    }

    public void setOverdueInterestMb1(BigDecimal overdueInterestMb1) {
        this.overdueInterestMb1 = overdueInterestMb1;
    }

    public BigDecimal getFixedOverdueAmtMb1() {
        return fixedOverdueAmtMb1;
    }

    public void setFixedOverdueAmtMb1(BigDecimal fixedOverdueAmtMb1) {
        this.fixedOverdueAmtMb1 = fixedOverdueAmtMb1;
    }

    public BigDecimal getCurRepayAmtMb1() {
        return curRepayAmtMb1;
    }

    public void setCurRepayAmtMb1(BigDecimal curRepayAmtMb1) {
        this.curRepayAmtMb1 = curRepayAmtMb1;
    }

    public BigDecimal getOutOverdueRateMb1() {
        return outOverdueRateMb1;
    }

    public void setOutOverdueRateMb1(BigDecimal outOverdueRateMb1) {
        this.outOverdueRateMb1 = outOverdueRateMb1;
    }

    public BigDecimal getOverduePrincipalMb1() {
        return overduePrincipalMb1;
    }

    public void setOverduePrincipalMb1(BigDecimal overduePrincipalMb1) {
        this.overduePrincipalMb1 = overduePrincipalMb1;
    }

    public BigDecimal getStockAmtMb1() {
        return stockAmtMb1;
    }

    public void setStockAmtMb1(BigDecimal stockAmtMb1) {
        this.stockAmtMb1 = stockAmtMb1;
    }

    public BigDecimal getOverdueRateMb1() {
        return overdueRateMb1;
    }

    public void setOverdueRateMb1(BigDecimal overdueRateMb1) {
        this.overdueRateMb1 = overdueRateMb1;
    }

    public BigDecimal getOverduePrincipalMb2() {
        return overduePrincipalMb2;
    }

    public void setOverduePrincipalMb2(BigDecimal overduePrincipalMb2) {
        this.overduePrincipalMb2 = overduePrincipalMb2;
    }

    public BigDecimal getStockAmtMb2() {
        return stockAmtMb2;
    }

    public void setStockAmtMb2(BigDecimal stockAmtMb2) {
        this.stockAmtMb2 = stockAmtMb2;
    }

    public BigDecimal getOverdueRateMb2() {
        return overdueRateMb2;
    }

    public void setOverdueRateMb2(BigDecimal overdueRateMb2) {
        this.overdueRateMb2 = overdueRateMb2;
    }

    public BigDecimal getOverduePrincipalMb3() {
        return overduePrincipalMb3;
    }

    public void setOverduePrincipalMb3(BigDecimal overduePrincipalMb3) {
        this.overduePrincipalMb3 = overduePrincipalMb3;
    }

    public BigDecimal getStockAmtMb3() {
        return stockAmtMb3;
    }

    public void setStockAmtMb3(BigDecimal stockAmtMb3) {
        this.stockAmtMb3 = stockAmtMb3;
    }

    public BigDecimal getOverdueRateMb3() {
        return overdueRateMb3;
    }

    public void setOverdueRateMb3(BigDecimal overdueRateMb3) {
        this.overdueRateMb3 = overdueRateMb3;
    }

    public BigDecimal getOverduePrincipalMb4() {
        return overduePrincipalMb4;
    }

    public void setOverduePrincipalMb4(BigDecimal overduePrincipalMb4) {
        this.overduePrincipalMb4 = overduePrincipalMb4;
    }

    public BigDecimal getStockAmtMb4() {
        return stockAmtMb4;
    }

    public void setStockAmtMb4(BigDecimal stockAmtMb4) {
        this.stockAmtMb4 = stockAmtMb4;
    }

    public BigDecimal getOverdueRateMb4() {
        return overdueRateMb4;
    }

    public void setOverdueRateMb4(BigDecimal overdueRateMb4) {
        this.overdueRateMb4 = overdueRateMb4;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
