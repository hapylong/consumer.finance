package com.iqb.consumer.data.layer.mysql.bean.product;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * 
 * Description: 退费bean
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月10日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class RefundCost extends BaseEntity {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 4809405778566782539L;

    /**
     * 类型 ：1,退费 2，退利息
     */
    private String type;

    /**
     * 费用类型 ：1,剩余期数退费 2，实际占用资金退费
     */
    private String refundMethod;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefundMethod() {
        return refundMethod;
    }

    public void setRefundMethod(String refundMethod) {
        this.refundMethod = refundMethod;
    }

}
