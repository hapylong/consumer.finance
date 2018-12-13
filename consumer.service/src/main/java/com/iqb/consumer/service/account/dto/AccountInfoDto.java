/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月28日 下午6:12:22
 * @version V1.0
 */

package com.iqb.consumer.service.account.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInfoDto {

    private long id;
    private String conUUID;// 用户ID
    private String realName;// 真实姓名
    private String regId;// 注册号
    private String idNo;// 身份证件号
    private String bankCard;// 银行卡号
    private int accStatus;// 账户状态(0、正常,1、冻结,2、注销)
    private BigDecimal saveAmt;// 存放金额
    private String currType = "RMB";// 币种
    private String industryType;// 车贷，房贷，医美贷
    private Date openDay;// 开户日期
    private Date cancelDay;// 销户日期
    private int freeInterestCount;// 免息卷总数
    private long pointSum;// 累计积分
    private Integer publicNo;// 公众号()
    private String sign;// 验签
    private Date createTime = new Date();
    private Date updateTime = new Date();

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Integer getPublicNo() {
        return publicNo;
    }

    public void setPublicNo(Integer publicNo) {
        this.publicNo = publicNo;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConUUID() {
        return conUUID;
    }

    public void setConUUID(String conUUID) {
        this.conUUID = conUUID;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public int getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(int accStatus) {
        this.accStatus = accStatus;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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
