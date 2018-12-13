/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月15日 下午7:49:58
 * @version V1.0
 */
package com.iqb.consumer.virtual.account.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class RepayVerifyDto {
    private String orderId;
    private String repayModel;
    private List<OrderList> orderList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRepayModel() {
        return repayModel;
    }

    public void setRepayModel(String repayModel) {
        this.repayModel = repayModel;
    }

    public List<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }

}
