/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 利息计算方式表
 * @date 2016年11月07日 下午7:29:31
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.domain.product;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
public class Interest extends BaseEntity {

    private long id;// 利息计算方式唯一ID
    private double ratio;// 手续费率
    private int ratioType;// 计算方式
    private BigDecimal minAmt;// 下限金额
    private BigDecimal maxAmt;// 上限金额
    private int enable;// 是否启用上下限
    private int day2year;// 年天数
    private int day2month;// 月天数
    private String remark;// 备注
    private Integer version;// 版本号
    private Date createTime;// 创建时间
    private Date updateTime;// 修改时间

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getRatioType() {
        return ratioType;
    }

    public void setRatioType(int ratioType) {
        this.ratioType = ratioType;
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

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getDay2year() {
        return day2year;
    }

    public void setDay2year(int day2year) {
        this.day2year = day2year;
    }

    public int getDay2month() {
        return day2month;
    }

    public void setDay2month(int day2month) {
        this.day2month = day2month;
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
