package com.iqb.consumer.finance.batch.bean;

import java.math.BigDecimal;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年10月18日下午1:58:41 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstBillInfoBean extends BaseEntity {
    private static final long serialVersionUID = -8052061778243436750L;
    /** 订单id **/
    private String orderId;
    /** 商户号 **/
    private String merchantNo;
    /** 批处理日期 **/
    private String schedueDate;
    /** 逾期月供 **/
    private BigDecimal overdueMonthMD5 = BigDecimal.ZERO;
    /** 逾期罚息 **/
    private BigDecimal overdueInterestMD5 = BigDecimal.ZERO;
    /** 违约金-逾期 **/
    private BigDecimal fixedOverdueAmtMD5 = BigDecimal.ZERO;
    /** 本期应还总额 **/
    private BigDecimal curRepayAmtMD5 = BigDecimal.ZERO;
    /** 逾期率(%) **/
    private BigDecimal overdueRateMD5 = BigDecimal.ZERO;

    /** MB1逾期月供 **/
    private BigDecimal overdueMonthMB1 = BigDecimal.ZERO;
    /** MB1逾期罚息 **/
    private BigDecimal overdueInterestMB1 = BigDecimal.ZERO;
    /** MB1逾期违约金 **/
    private BigDecimal fixedOverdueAmtMB1 = BigDecimal.ZERO;
    /** MB1本期应还总额 **/
    private BigDecimal curRepayAmtMB1 = BigDecimal.ZERO;
    /** 外部逾期率(%) **/
    private BigDecimal outOverdueRateMB1 = BigDecimal.ZERO;
    /** MB1逾期应还本金 **/
    private BigDecimal overduePincipalMB1 = BigDecimal.ZERO;
    /** MB1资产总存量 **/
    private BigDecimal stockAmtMB1 = BigDecimal.ZERO;
    /** MB1内部逾期率(%) **/
    private BigDecimal overdueRateMB1 = BigDecimal.ZERO;

    /** MB2逾期应还本金 **/
    private BigDecimal overduePincipalMB2 = BigDecimal.ZERO;
    /** MB2资产总存量 **/
    private BigDecimal stockAmtMB2 = BigDecimal.ZERO;
    /** MB2内部逾期率(%) **/
    private BigDecimal overdueRateMB2 = BigDecimal.ZERO;

    /** MB3逾期剩余本金 **/
    private BigDecimal overduePincipalMB3 = BigDecimal.ZERO;
    /** MB3资产总存量 **/
    private BigDecimal stockAmtMB3 = BigDecimal.ZERO;
    /** MB3内部逾期率(%) **/
    private BigDecimal overdueRateMB3 = BigDecimal.ZERO;

    /** MB3逾期剩余本金 **/
    private BigDecimal overduePincipalMB4 = BigDecimal.ZERO;
    /** MB3资产总存量 **/
    private BigDecimal stockAmtMB4 = BigDecimal.ZERO;
    /** MB3内部逾期率(%) **/
    private BigDecimal overdueRateMB4 = BigDecimal.ZERO;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getSchedueDate() {
        return schedueDate;
    }

    public void setSchedueDate(String schedueDate) {
        this.schedueDate = schedueDate;
    }

    public BigDecimal getOverdueMonthMD5() {
        return overdueMonthMD5;
    }

    public void setOverdueMonthMD5(BigDecimal overdueMonthMD5) {
        this.overdueMonthMD5 = overdueMonthMD5;
    }

    public BigDecimal getOverdueInterestMD5() {
        return overdueInterestMD5;
    }

    public void setOverdueInterestMD5(BigDecimal overdueInterestMD5) {
        this.overdueInterestMD5 = overdueInterestMD5;
    }

    public BigDecimal getFixedOverdueAmtMD5() {
        return fixedOverdueAmtMD5;
    }

    public void setFixedOverdueAmtMD5(BigDecimal fixedOverdueAmtMD5) {
        this.fixedOverdueAmtMD5 = fixedOverdueAmtMD5;
    }

    public BigDecimal getCurRepayAmtMD5() {
        return curRepayAmtMD5;
    }

    public void setCurRepayAmtMD5(BigDecimal curRepayAmtMD5) {
        this.curRepayAmtMD5 = curRepayAmtMD5;
    }

    public BigDecimal getOverdueRateMD5() {
        return overdueRateMD5;
    }

    public void setOverdueRateMD5(BigDecimal overdueRateMD5) {
        this.overdueRateMD5 = overdueRateMD5;
    }

    public BigDecimal getOverdueMonthMB1() {
        return overdueMonthMB1;
    }

    public void setOverdueMonthMB1(BigDecimal overdueMonthMB1) {
        this.overdueMonthMB1 = overdueMonthMB1;
    }

    public BigDecimal getOverdueInterestMB1() {
        return overdueInterestMB1;
    }

    public void setOverdueInterestMB1(BigDecimal overdueInterestMB1) {
        this.overdueInterestMB1 = overdueInterestMB1;
    }

    public BigDecimal getFixedOverdueAmtMB1() {
        return fixedOverdueAmtMB1;
    }

    public void setFixedOverdueAmtMB1(BigDecimal fixedOverdueAmtMB1) {
        this.fixedOverdueAmtMB1 = fixedOverdueAmtMB1;
    }

    public BigDecimal getCurRepayAmtMB1() {
        return curRepayAmtMB1;
    }

    public void setCurRepayAmtMB1(BigDecimal curRepayAmtMB1) {
        this.curRepayAmtMB1 = curRepayAmtMB1;
    }

    public BigDecimal getOutOverdueRateMB1() {
        return outOverdueRateMB1;
    }

    public void setOutOverdueRateMB1(BigDecimal outOverdueRateMB1) {
        this.outOverdueRateMB1 = outOverdueRateMB1;
    }

    public BigDecimal getOverduePincipalMB1() {
        return overduePincipalMB1;
    }

    public void setOverduePincipalMB1(BigDecimal overduePincipalMB1) {
        this.overduePincipalMB1 = overduePincipalMB1;
    }

    public BigDecimal getStockAmtMB1() {
        return stockAmtMB1;
    }

    public void setStockAmtMB1(BigDecimal stockAmtMB1) {
        this.stockAmtMB1 = stockAmtMB1;
    }

    public BigDecimal getOverdueRateMB1() {
        return overdueRateMB1;
    }

    public void setOverdueRateMB1(BigDecimal overdueRateMB1) {
        this.overdueRateMB1 = overdueRateMB1;
    }

    public BigDecimal getOverduePincipalMB2() {
        return overduePincipalMB2;
    }

    public void setOverduePincipalMB2(BigDecimal overduePincipalMB2) {
        this.overduePincipalMB2 = overduePincipalMB2;
    }

    public BigDecimal getStockAmtMB2() {
        return stockAmtMB2;
    }

    public void setStockAmtMB2(BigDecimal stockAmtMB2) {
        this.stockAmtMB2 = stockAmtMB2;
    }

    public BigDecimal getOverdueRateMB2() {
        return overdueRateMB2;
    }

    public void setOverdueRateMB2(BigDecimal overdueRateMB2) {
        this.overdueRateMB2 = overdueRateMB2;
    }

    public BigDecimal getOverduePincipalMB3() {
        return overduePincipalMB3;
    }

    public void setOverduePincipalMB3(BigDecimal overduePincipalMB3) {
        this.overduePincipalMB3 = overduePincipalMB3;
    }

    public BigDecimal getStockAmtMB3() {
        return stockAmtMB3;
    }

    public void setStockAmtMB3(BigDecimal stockAmtMB3) {
        this.stockAmtMB3 = stockAmtMB3;
    }

    public BigDecimal getOverdueRateMB3() {
        return overdueRateMB3;
    }

    public void setOverdueRateMB3(BigDecimal overdueRateMB3) {
        this.overdueRateMB3 = overdueRateMB3;
    }

    public BigDecimal getOverduePincipalMB4() {
        return overduePincipalMB4;
    }

    public void setOverduePincipalMB4(BigDecimal overduePincipalMB4) {
        this.overduePincipalMB4 = overduePincipalMB4;
    }

    public BigDecimal getStockAmtMB4() {
        return stockAmtMB4;
    }

    public void setStockAmtMB4(BigDecimal stockAmtMB4) {
        this.stockAmtMB4 = stockAmtMB4;
    }

    public BigDecimal getOverdueRateMB4() {
        return overdueRateMB4;
    }

    public void setOverdueRateMB4(BigDecimal overdueRateMB4) {
        this.overdueRateMB4 = overdueRateMB4;
    }
}
