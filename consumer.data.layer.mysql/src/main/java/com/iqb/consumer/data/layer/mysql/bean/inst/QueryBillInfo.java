package com.iqb.consumer.data.layer.mysql.bean.inst;

import java.util.List;

/**
 * { "orderId": "90001", "orderList": [ { "repayno": "1", "regId": "18515262578", . . . "repayAmt":
 * "50", "lastRepayDate": "20161126" }, { "repayno": "2", "regId": "18515262578", . . . "repayAmt":
 * "50", "lastRepayDate": "20161126" } ] }
 */
public class QueryBillInfo {

    private String orderId;// 订单id
    private List<InstallmentBillInfo> billList;// 账单列表

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<InstallmentBillInfo> getBillList() {
        return billList;
    }

    public void setBillList(List<InstallmentBillInfo> billList) {
        this.billList = billList;
    }

}
