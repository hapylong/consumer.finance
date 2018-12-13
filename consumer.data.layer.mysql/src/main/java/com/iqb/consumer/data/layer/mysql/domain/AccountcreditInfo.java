package com.iqb.consumer.data.layer.mysql.domain;

import java.math.BigDecimal;

import com.iqb.consumer.common.domain.BaseEntity;

public class AccountcreditInfo extends BaseEntity {

    private Long accountId;//
    private String industryType;//
    private BigDecimal creditLimit;//
    private BigDecimal availLimit;//
    private int activate;//

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getActivate() {
        return activate;
    }

    public void setActivate(int activate) {
        this.activate = activate;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
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

}
