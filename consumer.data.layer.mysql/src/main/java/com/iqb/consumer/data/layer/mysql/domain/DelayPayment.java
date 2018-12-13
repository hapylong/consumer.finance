/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 违约金(滞纳金)
 * @date 2016年7月25日 下午5:07:23
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class DelayPayment extends BaseEntity {
    private long billID;// 账单号
    private BigDecimal principalAmt;// 剩余本金
    private double overdueRate;// 违约金费率
    private BigDecimal delayAmt;// 违约金
    private Date delayDate;// 违约时间

    public long getBillID() {
        return billID;
    }

    public void setBillID(long billID) {
        this.billID = billID;
    }

    public BigDecimal getPrincipalAmt() {
        return principalAmt;
    }

    public void setPrincipalAmt(BigDecimal principalAmt) {
        this.principalAmt = principalAmt;
    }

    public double getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(double overdueRate) {
        this.overdueRate = overdueRate;
    }

    public BigDecimal getDelayAmt() {
        return delayAmt;
    }

    public void setDelayAmt(BigDecimal delayAmt) {
        this.delayAmt = delayAmt;
    }

    public Date getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }

}
