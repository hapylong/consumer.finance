package com.iqb.consumer.service.account.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvancedPayBillsDto {

    private List<InstallmentBillInfo> payBills;// 提前还款实际账单
    private BigDecimal breachContract;// 违约金

    public List<InstallmentBillInfo> getPayBills() {
        return payBills;
    }

    public void setPayBills(List<InstallmentBillInfo> payBills) {
        this.payBills = payBills;
    }

    public BigDecimal getBreachContract() {
        return breachContract;
    }

    public void setBreachContract(BigDecimal breachContract) {
        this.breachContract = breachContract;
    }

}
