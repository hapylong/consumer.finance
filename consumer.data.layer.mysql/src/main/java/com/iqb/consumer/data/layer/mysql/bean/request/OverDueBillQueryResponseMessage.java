package com.iqb.consumer.data.layer.mysql.bean.request;

import java.util.List;

public class OverDueBillQueryResponseMessage {

    private StringBuilder financeMsg = new StringBuilder("账务系统查询无效订单：【");
    List<OverDueBillPojo> bills;

    public OverDueBillQueryResponseMessage appendMsg(String msg) {
        this.financeMsg.append(msg);
        return this;
    }

    public void setFinanceMsg(StringBuilder msg) {
        this.financeMsg = msg;
    }

    public StringBuilder getFinanceMsg() {
        return financeMsg.append("】");
    }

    public List<OverDueBillPojo> getBills() {
        return bills;
    }

    public void setBills(List<OverDueBillPojo> bills) {
        this.bills = bills;
    }
}
