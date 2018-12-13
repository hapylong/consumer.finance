/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账户表
 * @date 2016年7月1日 下午4:45:22
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class AccountInfo extends BaseEntity {

    private String conUUID;// 用户ID
    private String regID;// 注册号
    private String IDNO;// 身份证件号
    private int accStatus;// 账户状态(0、正常,1、冻结,2、注销)
    private BigDecimal creditLimit;// 授信额度
    private BigDecimal availLimit;// 可用额度
    private BigDecimal saveAmt;// 存放金额
    private String currType = "RMB";// 币种
    private Date openDay;// 开户日期
    private Date cancelDay;// 销户日期
    private int freeInterestCount;// 免息卷总数
    private long pointSum;// 累计积分
    private Integer publicNo;// 公众号()

    public Integer getPublicNo() {
        return publicNo;
    }

    public void setPublicNo(Integer publicNo) {
        this.publicNo = publicNo;
    }

    public String getIDNO() {
        return IDNO;
    }

    public void setIDNO(String iDNO) {
        IDNO = iDNO;
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

    public int getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(int accStatus) {
        this.accStatus = accStatus;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAvailLimit() {
        return availLimit;
    }

    public void setAvailLimit(BigDecimal availLimit) {
        this.availLimit = availLimit;
    }

    public BigDecimal getSaveAmt() {
        return saveAmt;
    }

    public void setSaveAmt(BigDecimal saveAmt) {
        this.saveAmt = saveAmt;
    }

    public String getCurrType() {
        return currType;
    }

    public void setCurrType(String currType) {
        this.currType = currType;
    }

    public Date getOpenDay() {
        return openDay;
    }

    public void setOpenDay(Date openDay) {
        this.openDay = openDay;
    }

    public Date getCancelDay() {
        return cancelDay;
    }

    public void setCancelDay(Date cancelDay) {
        this.cancelDay = cancelDay;
    }

    public int getFreeInterestCount() {
        return freeInterestCount;
    }

    public void setFreeInterestCount(int freeInterestCount) {
        this.freeInterestCount = freeInterestCount;
    }

    public long getPointSum() {
        return pointSum;
    }

    public void setPointSum(long pointSum) {
        this.pointSum = pointSum;
    }
}
