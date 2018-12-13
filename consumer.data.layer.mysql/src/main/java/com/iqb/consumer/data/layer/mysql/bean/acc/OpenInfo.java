package com.iqb.consumer.data.layer.mysql.bean.acc;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * 对应acct_openinfo表，开户信息表
 */
public class OpenInfo extends BaseEntity {

    private String uuid;// uuid
    private String pid;// 父id
    private long bid;// baseinfo表的id
    private String regId;// 用户号
    private String realName;// 真实姓名
    private String idNo;// 身份证号
    private String bankCardNo;// 银行卡号
    private int industry;// 资产包含(1 车贷 2 房贷 3 医美) 资金(1 饭饭 2 爱钱帮)
    private String description;// 描述
    private Date openDay;// 开户日
    private Date cancelDay;// 销户日
    private BigDecimal creditLimit;// 额度
    private BigDecimal availLimit;// 可用额度
    private int enable = 1;// 默认不使用(1,不使用 2,使用)
    private int status;// 状态(备用字段)
    /**
     * 添加每个行业类型对应的短信渠道
     * 
     * @author Yeoman
     * @date 20161122
     */
    private int smsChannel;// 短信渠道

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public int getSmsChannel() {
        return smsChannel;
    }

    public void setSmsChannel(int smsChannel) {
        this.smsChannel = smsChannel;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
