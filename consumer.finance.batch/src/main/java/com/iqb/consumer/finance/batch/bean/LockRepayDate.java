package com.iqb.consumer.finance.batch.bean;

import com.iqb.consumer.finance.batch.domain.BaseEntity;

/**
 * 锁定日期
 */
public class LockRepayDate extends BaseEntity {

    private static final long serialVersionUID = 4240726569759125073L;
    private int lockDays;// 锁定时间
    private int lockrepaytype;// 锁定类型 1.月 2.日

    public int getLockDays() {
        return lockDays;
    }

    public void setLockDays(int lockDays) {
        this.lockDays = lockDays;
    }

    public int getLockrepaytype() {
        return lockrepaytype;
    }

    public void setLockrepaytype(int lockrepaytype) {
        this.lockrepaytype = lockrepaytype;
    }

}
