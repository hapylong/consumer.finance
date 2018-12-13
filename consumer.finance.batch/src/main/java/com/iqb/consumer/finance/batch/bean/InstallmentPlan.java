/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月8日 下午7:09:29
 * @version V1.0
 */
package com.iqb.consumer.finance.batch.bean;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * 分期方案表
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentPlan extends BaseEntity {
    private static final long serialVersionUID = 8565461915813207933L;
    private int repayMethod;// 还款方式
    private int instMethod;// 分期方式
    private int interestId;// 利息计算规则
    private int overdueMethod;// 逾期方式
    private int ovedueId;// 逾期对应ID
    private int repayDateId;// 最后还款日规则
    private int secondRepayDateId;// 多次放款规则
    private int prepayMent;// 提前还款
    private int partpayMent;// 部分还款
    private int breachContract;// 还款违约
    private int breachContractId;// 还款违约关联id
    private int refundCost;// 退费
    private int refundCostId;// 退费关联id
    private int refundInterest;// 退息
    private int freeId;// 退息关联id
    private int lockRepayDate;// 锁定标识
    private int lockRepayDateId;// 锁定关联id
    private int graceDays;// 宽限天数
    private int status;// 状态

    public int getSecondRepayDateId() {
        return secondRepayDateId;
    }

    public void setSecondRepayDateId(int secondRepayDateId) {
        this.secondRepayDateId = secondRepayDateId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRepayMethod() {
        return repayMethod;
    }

    public void setRepayMethod(int repayMethod) {
        this.repayMethod = repayMethod;
    }

    public int getInstMethod() {
        return instMethod;
    }

    public void setInstMethod(int instMethod) {
        this.instMethod = instMethod;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }

    public int getOverdueMethod() {
        return overdueMethod;
    }

    public void setOverdueMethod(int overdueMethod) {
        this.overdueMethod = overdueMethod;
    }

    public int getOvedueId() {
        return ovedueId;
    }

    public void setOvedueId(int ovedueId) {
        this.ovedueId = ovedueId;
    }

    public int getRepayDateId() {
        return repayDateId;
    }

    public void setRepayDateId(int repayDateId) {
        this.repayDateId = repayDateId;
    }

    public int getPrepayMent() {
        return prepayMent;
    }

    public void setPrepayMent(int prepayMent) {
        this.prepayMent = prepayMent;
    }

    public int getPartpayMent() {
        return partpayMent;
    }

    public void setPartpayMent(int partpayMent) {
        this.partpayMent = partpayMent;
    }

    public int getBreachContract() {
        return breachContract;
    }

    public void setBreachContract(int breachContract) {
        this.breachContract = breachContract;
    }

    public int getBreachContractId() {
        return breachContractId;
    }

    public void setBreachContractId(int breachContractId) {
        this.breachContractId = breachContractId;
    }

    public int getRefundCost() {
        return refundCost;
    }

    public void setRefundCost(int refundCost) {
        this.refundCost = refundCost;
    }

    public int getRefundCostId() {
        return refundCostId;
    }

    public void setRefundCostId(int refundCostId) {
        this.refundCostId = refundCostId;
    }

    public int getRefundInterest() {
        return refundInterest;
    }

    public void setRefundInterest(int refundInterest) {
        this.refundInterest = refundInterest;
    }

    public int getFreeId() {
        return freeId;
    }

    public void setFreeId(int freeId) {
        this.freeId = freeId;
    }

    public int getLockRepayDate() {
        return lockRepayDate;
    }

    public void setLockRepayDate(int lockRepayDate) {
        this.lockRepayDate = lockRepayDate;
    }

    public int getLockRepayDateId() {
        return lockRepayDateId;
    }

    public void setLockRepayDateId(int lockRepayDateId) {
        this.lockRepayDateId = lockRepayDateId;
    }

    public int getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(int graceDays) {
        this.graceDays = graceDays;
    }
}
