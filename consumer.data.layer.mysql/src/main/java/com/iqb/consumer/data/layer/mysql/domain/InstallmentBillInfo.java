/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账单表
 * @date 2016年7月11日 下午2:29:42
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentBillInfo extends BaseEntity {
    private int repayNo;// 还款顺序号
    private long installID;// 分期信息唯一ID
    private String conUUID;// 用户唯一ID
    private String regID;// 注册号
    private long accID;// 账户号-->对应AccountInfo主键
    private Date billDate;// 账单日
    private Date lastGraceDate;// 最后还款日
    private Date delayDate;// 滞纳金开始时间 = 账单日+宽限期
    private BigDecimal sumBillAmt;// 本期应还总金额=(本期分摊+上期未还总额+本期累计预算滞纳金)
    private BigDecimal minRepayAmt;// 本期最低还款金额=(本期应还*最低还款比例)
    private BigDecimal currentAmt;// 本期分摊金额
    private BigDecimal remainAmt;// 剩余本金金额
    private BigDecimal preSumBillAmt;// 上期未应还总金额
    private BigDecimal preAmt;// 上期分摊金额
    private BigDecimal preLateAmt;// 上期累计滞纳金
    private BigDecimal preRepayAmt;// 上期还款金额
    private BigDecimal currentLateFee;// 本期预算滞纳金(每天的)
    private BigDecimal curSumLateFee;// 本期累计预算滞纳金
    private BigDecimal currentRepayAmt;// 本期累计还款金额
    private String merchantNo;// 商户号
    private String orderID;// 订单ID
    private Date lastPayDay;// 最后还款日期(yyyyMMdd) 宽限期最后一天
    private int status;// 账单状态
    private int delayStatus;// 延期状态(0,未延期 1,延期)
    private double minRate;// 最低还款比例(当还款金额>最低还款金额,不计算滞纳金,否则剩余金额计算滞纳金)
    private int installNo;

    public Date getLastGraceDate() {
        return lastGraceDate;
    }

    public void setLastGraceDate(Date lastGraceDate) {
        this.lastGraceDate = lastGraceDate;
    }

    public int getDelayStatus() {
        return delayStatus;
    }

    public void setDelayStatus(int delayStatus) {
        this.delayStatus = delayStatus;
    }

    public int getInstallNo() {
        return installNo;
    }

    public void setInstallNo(int installNo) {
        this.installNo = installNo;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public BigDecimal getRemainAmt() {
        return remainAmt;
    }

    public void setRemainAmt(BigDecimal remainAmt) {
        this.remainAmt = remainAmt;
    }

    public Date getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public long getInstallID() {
        return installID;
    }

    public void setInstallID(long installID) {
        this.installID = installID;
    }

    public String getConUUID() {
        return conUUID;
    }

    public void setConUUID(String conUUID) {
        this.conUUID = conUUID;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    public long getAccID() {
        return accID;
    }

    public void setAccID(long accID) {
        this.accID = accID;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getSumBillAmt() {
        return sumBillAmt;
    }

    public void setSumBillAmt(BigDecimal sumBillAmt) {
        this.sumBillAmt = sumBillAmt;
    }

    public BigDecimal getMinRepayAmt() {
        return minRepayAmt;
    }

    public void setMinRepayAmt(BigDecimal minRepayAmt) {
        this.minRepayAmt = minRepayAmt;
    }

    public BigDecimal getCurrentAmt() {
        return currentAmt;
    }

    public void setCurrentAmt(BigDecimal currentAmt) {
        this.currentAmt = currentAmt;
    }

    public BigDecimal getPreSumBillAmt() {
        return preSumBillAmt;
    }

    public void setPreSumBillAmt(BigDecimal preSumBillAmt) {
        this.preSumBillAmt = preSumBillAmt;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public BigDecimal getPreLateAmt() {
        return preLateAmt;
    }

    public void setPreLateAmt(BigDecimal preLateAmt) {
        this.preLateAmt = preLateAmt;
    }

    public BigDecimal getPreRepayAmt() {
        return preRepayAmt;
    }

    public void setPreRepayAmt(BigDecimal preRepayAmt) {
        this.preRepayAmt = preRepayAmt;
    }

    public BigDecimal getCurrentLateFee() {
        return currentLateFee;
    }

    public void setCurrentLateFee(BigDecimal currentLateFee) {
        this.currentLateFee = currentLateFee;
    }

    public BigDecimal getCurSumLateFee() {
        return curSumLateFee;
    }

    public void setCurSumLateFee(BigDecimal curSumLateFee) {
        this.curSumLateFee = curSumLateFee;
    }

    public BigDecimal getCurrentRepayAmt() {
        return currentRepayAmt;
    }

    public void setCurrentRepayAmt(BigDecimal currentRepayAmt) {
        this.currentRepayAmt = currentRepayAmt;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Date getLastPayDay() {
        return lastPayDay;
    }

    public void setLastPayDay(Date lastPayDay) {
        this.lastPayDay = lastPayDay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getMinRate() {
        return minRate;
    }

    public void setMinRate(double minRate) {
        this.minRate = minRate;
    }

}
