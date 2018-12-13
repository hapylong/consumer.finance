package com.iqb.consumer.data.layer.mysql.bean.acc;

import java.math.BigDecimal;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * 对应acc_baseinfo表
 */
public class AccBaseInfo extends BaseEntity {

    private String realName;// 真实姓名
    private String idNo;// 身份证号
    private BigDecimal amt;// 账户余额
    private int freeInterestCount;// 免息券
    private int pointSum;// 积分
    private int status;// 账户状态

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public int getFreeInterestCount() {
        return freeInterestCount;
    }

    public void setFreeInterestCount(int freeInterestCount) {
        this.freeInterestCount = freeInterestCount;
    }

    public int getPointSum() {
        return pointSum;
    }

    public void setPointSum(int pointSum) {
        this.pointSum = pointSum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
