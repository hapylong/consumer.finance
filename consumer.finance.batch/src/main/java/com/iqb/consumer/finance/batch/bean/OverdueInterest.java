/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 利息计算方式表
 * @date 2016年11月07日 下午7:29:31
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * 违约金计算Bean
 * 
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
public class OverdueInterest extends BaseEntity {

    private static final long serialVersionUID = -2322700632899924790L;
    private int fixedType; // 违约金类型(1,固定 2,比例)
    private int fixedBaseAmtType;// 金额来源
    private double fixedRatio; // 违约金比例
    private BigDecimal fixedAmount;// 逾期固定金额
    private BigDecimal minAmt;// 下限金额
    private String remark;// 备注
    private Integer version;// 版本号
    private Date createTime;// 创建时间
    private Date updateTime;// 修改时间

    public int getFixedType() {
        return fixedType;
    }

    public void setFixedType(int fixedType) {
        this.fixedType = fixedType;
    }

    public int getFixedBaseAmtType() {
        return fixedBaseAmtType;
    }

    public void setFixedBaseAmtType(int fixedBaseAmtType) {
        this.fixedBaseAmtType = fixedBaseAmtType;
    }

    public double getFixedRatio() {
        return fixedRatio;
    }

    public void setFixedRatio(double fixedRatio) {
        this.fixedRatio = fixedRatio;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
