/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月9日 下午1:58:15
 * @version V1.0
 */

package com.iqb.consumer.service.account.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class NoticeBeanDto {

    private String regID;// 注册号

    private BigDecimal sumShareAmount;// 预分期总额
    private int realPeriods;// 实际分期期数
    private BigDecimal firstShareAmount;// 分期分摊金额
    private BigDecimal lastShareAmount;// 分期分摊金额

    private Date billDate;// 账单日
    private BigDecimal currentRepayAmt;// 还款金额
    private int publicNo;// 公众号

    public int getPublicNo() {
        return publicNo;
    }

    public void setPublicNo(int publicNo) {
        this.publicNo = publicNo;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    public BigDecimal getSumShareAmount() {
        return sumShareAmount;
    }

    public void setSumShareAmount(BigDecimal sumShareAmount) {
        this.sumShareAmount = sumShareAmount;
    }

    public int getRealPeriods() {
        return realPeriods;
    }

    public void setRealPeriods(int realPeriods) {
        this.realPeriods = realPeriods;
    }

    public BigDecimal getFirstShareAmount() {
        return firstShareAmount;
    }

    public void setFirstShareAmount(BigDecimal firstShareAmount) {
        this.firstShareAmount = firstShareAmount;
    }

    public BigDecimal getLastShareAmount() {
        return lastShareAmount;
    }

    public void setLastShareAmount(BigDecimal lastShareAmount) {
        this.lastShareAmount = lastShareAmount;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getCurrentRepayAmt() {
        return currentRepayAmt;
    }

    public void setCurrentRepayAmt(BigDecimal currentRepayAmt) {
        this.currentRepayAmt = currentRepayAmt;
    }

}
