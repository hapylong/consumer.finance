package com.iqb.consumer.data.layer.mysql.bean.product;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * 
 * Description: 还款日
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月7日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class RepayDate extends BaseEntity {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 8476195771411535208L;

    /**
     * 类型
     */
    private String type;

    /**
     * 指定日期
     */
    private String specifiedDate;

    /**
     * 期间天数
     */
    private String billShortDay;

    /**
     * 账单日
     */
    private String billDate;

    /**
     * 宽限天数
     */
    private String graceDays;

    /**
     * 备注
     */
    private String remark;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecifiedDate() {
        return specifiedDate;
    }

    public void setSpecifiedDate(String specifiedDate) {
        this.specifiedDate = specifiedDate;
    }

    public String getBillShortDay() {
        return billShortDay;
    }

    public void setBillShortDay(String billShortDay) {
        this.billShortDay = billShortDay;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(String graceDays) {
        this.graceDays = graceDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
