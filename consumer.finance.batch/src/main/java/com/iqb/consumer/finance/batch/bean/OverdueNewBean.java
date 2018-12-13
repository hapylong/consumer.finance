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
 * 2017年12月5日下午5:16:50 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class OverdueNewBean extends BaseEntity {
    /** 订单id **/
    private String orderId;
    /** 商户号 **/
    private String merchantNo;
    /** 批处理日期 **/
    private String schedueDate;
    /** 逾期个数 **/
    private String overdueNum;
    /** 订单总数 **/
    private String overdueStockNum;
    /** 逾期月供-总量 **/
    private BigDecimal overdueMonth = BigDecimal.ZERO;
    /** 逾期剩余本金-总量 **/
    private BigDecimal overduePincipal = BigDecimal.ZERO;
    /** 资产总存量 **/
    private BigDecimal stockAmt = BigDecimal.ZERO;
    /** 逾期率 **/
    private BigDecimal overdueRate = BigDecimal.ZERO;

    /** MD5逾期个数 **/
    private String overdueNumMD5;
    /** MD5逾期月供 **/
    private BigDecimal overdueMonthMD5 = BigDecimal.ZERO;
    /** MD5逾期剩余本金 **/
    private BigDecimal overduePincipalMD5 = BigDecimal.ZERO;
    /** MD5资产总存量 **/
    private BigDecimal stockAmtMD5 = BigDecimal.ZERO;
    /** MD5逾期率(%) **/
    private BigDecimal overdueRateMD5 = BigDecimal.ZERO;

    /** MB1逾期个数 **/
    private String overdueNumMB1;
    /** MB1逾期剩余本金 **/
    private BigDecimal overduePincipalMB1 = BigDecimal.ZERO;
    /** MB1逾期月供 **/
    private BigDecimal overdueMonthMB1 = BigDecimal.ZERO;
    /** MB1资产总存量 **/
    private BigDecimal stockAmtMB1 = BigDecimal.ZERO;
    /** MB1内部逾期率(%) **/
    private BigDecimal overdueRateMB1 = BigDecimal.ZERO;

    /** MB2逾期个数 **/
    private String overdueNumMB2;
    /** MB2逾期剩余本金 **/
    private BigDecimal overduePincipalMB2 = BigDecimal.ZERO;
    /** MB2逾期月供 **/
    private BigDecimal overdueMonthMB2 = BigDecimal.ZERO;
    /** MB2资产总存量 **/
    private BigDecimal stockAmtMB2 = BigDecimal.ZERO;
    /** MB2内部逾期率(%) **/
    private BigDecimal overdueRateMB2 = BigDecimal.ZERO;

    /** MB3逾期个数 **/
    private String overdueNumMB3;
    /** MB3逾期剩余本金 **/
    private BigDecimal overduePincipalMB3 = BigDecimal.ZERO;
    /** MB3逾期月供 **/
    private BigDecimal overdueMonthMB3 = BigDecimal.ZERO;
    /** MB3资产总存量 **/
    private BigDecimal stockAmtMB3 = BigDecimal.ZERO;
    /** MB3内部逾期率(%) **/
    private BigDecimal overdueRateMB3 = BigDecimal.ZERO;

    /** MB4逾期个数 **/
    private String overdueNumMB4;
    /** MB3逾期剩余本金 **/
    private BigDecimal overduePincipalMB4 = BigDecimal.ZERO;
    /** MB4逾期月供 **/
    private BigDecimal overdueMonthMB4 = BigDecimal.ZERO;
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

    public String getOverdueNum() {
        return overdueNum;
    }

    public void setOverdueNum(String overdueNum) {
        this.overdueNum = overdueNum;
    }

    public String getOverdueStockNum() {
        return overdueStockNum;
    }

    public void setOverdueStockNum(String overdueStockNum) {
        this.overdueStockNum = overdueStockNum;
    }

    public BigDecimal getOverdueMonth() {
        return overdueMonth;
    }

    public void setOverdueMonth(BigDecimal overdueMonth) {
        this.overdueMonth = overdueMonth;
    }

    public BigDecimal getOverduePincipal() {
        return overduePincipal;
    }

    public void setOverduePincipal(BigDecimal overduePincipal) {
        this.overduePincipal = overduePincipal;
    }

    public BigDecimal getStockAmt() {
        return stockAmt;
    }

    public void setStockAmt(BigDecimal stockAmt) {
        this.stockAmt = stockAmt;
    }

    public BigDecimal getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public String getOverdueNumMD5() {
        return overdueNumMD5;
    }

    public void setOverdueNumMD5(String overdueNumMD5) {
        this.overdueNumMD5 = overdueNumMD5;
    }

    public BigDecimal getOverdueMonthMD5() {
        return overdueMonthMD5;
    }

    public void setOverdueMonthMD5(BigDecimal overdueMonthMD5) {
        this.overdueMonthMD5 = overdueMonthMD5;
    }

    public BigDecimal getOverduePincipalMD5() {
        return overduePincipalMD5;
    }

    public void setOverduePincipalMD5(BigDecimal overduePincipalMD5) {
        this.overduePincipalMD5 = overduePincipalMD5;
    }

    public BigDecimal getStockAmtMD5() {
        return stockAmtMD5;
    }

    public void setStockAmtMD5(BigDecimal stockAmtMD5) {
        this.stockAmtMD5 = stockAmtMD5;
    }

    public BigDecimal getOverdueRateMD5() {
        return overdueRateMD5;
    }

    public void setOverdueRateMD5(BigDecimal overdueRateMD5) {
        this.overdueRateMD5 = overdueRateMD5;
    }

    public String getOverdueNumMB1() {
        return overdueNumMB1;
    }

    public void setOverdueNumMB1(String overdueNumMB1) {
        this.overdueNumMB1 = overdueNumMB1;
    }

    public BigDecimal getOverduePincipalMB1() {
        return overduePincipalMB1;
    }

    public void setOverduePincipalMB1(BigDecimal overduePincipalMB1) {
        this.overduePincipalMB1 = overduePincipalMB1;
    }

    public BigDecimal getOverdueMonthMB1() {
        return overdueMonthMB1;
    }

    public void setOverdueMonthMB1(BigDecimal overdueMonthMB1) {
        this.overdueMonthMB1 = overdueMonthMB1;
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

    public String getOverdueNumMB2() {
        return overdueNumMB2;
    }

    public void setOverdueNumMB2(String overdueNumMB2) {
        this.overdueNumMB2 = overdueNumMB2;
    }

    public BigDecimal getOverduePincipalMB2() {
        return overduePincipalMB2;
    }

    public void setOverduePincipalMB2(BigDecimal overduePincipalMB2) {
        this.overduePincipalMB2 = overduePincipalMB2;
    }

    public BigDecimal getOverdueMonthMB2() {
        return overdueMonthMB2;
    }

    public void setOverdueMonthMB2(BigDecimal overdueMonthMB2) {
        this.overdueMonthMB2 = overdueMonthMB2;
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

    public String getOverdueNumMB3() {
        return overdueNumMB3;
    }

    public void setOverdueNumMB3(String overdueNumMB3) {
        this.overdueNumMB3 = overdueNumMB3;
    }

    public BigDecimal getOverduePincipalMB3() {
        return overduePincipalMB3;
    }

    public void setOverduePincipalMB3(BigDecimal overduePincipalMB3) {
        this.overduePincipalMB3 = overduePincipalMB3;
    }

    public BigDecimal getOverdueMonthMB3() {
        return overdueMonthMB3;
    }

    public void setOverdueMonthMB3(BigDecimal overdueMonthMB3) {
        this.overdueMonthMB3 = overdueMonthMB3;
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

    public String getOverdueNumMB4() {
        return overdueNumMB4;
    }

    public void setOverdueNumMB4(String overdueNumMB4) {
        this.overdueNumMB4 = overdueNumMB4;
    }

    public BigDecimal getOverduePincipalMB4() {
        return overduePincipalMB4;
    }

    public void setOverduePincipalMB4(BigDecimal overduePincipalMB4) {
        this.overduePincipalMB4 = overduePincipalMB4;
    }

    public BigDecimal getOverdueMonthMB4() {
        return overdueMonthMB4;
    }

    public void setOverdueMonthMB4(BigDecimal overdueMonthMB4) {
        this.overdueMonthMB4 = overdueMonthMB4;
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
