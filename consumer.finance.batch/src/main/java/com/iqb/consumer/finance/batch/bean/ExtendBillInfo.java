package com.iqb.consumer.finance.batch.bean;

public class ExtendBillInfo extends InstallmentBillInfo {

    private static final long serialVersionUID = 6479360402952111164L;
    // 真实姓名
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}
