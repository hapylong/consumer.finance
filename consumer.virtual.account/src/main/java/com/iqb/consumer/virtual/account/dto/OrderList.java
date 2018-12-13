/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月16日 下午2:09:23
 * @version V1.0
 */
package com.iqb.consumer.virtual.account.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class OrderList implements Comparable {
    private int repayNo;// 还款序号
    private String regId;// 手机号
    private BigDecimal repayAmt;// 还款金额
    private String lastRepayDate;// 最后还款日期

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    @Override
    public int compareTo(Object o) {
        OrderList ol = (OrderList) o;
        if (this.repayNo > ol.repayNo) {
            return 1;
        } else {
            return -1;
        }
    }

    // public static void main(String[] args) {
    // List<OrderList> list = new ArrayList<>();
    // OrderList ol1 = new OrderList();
    // ol1.setRepayNo(1);
    // OrderList ol2 = new OrderList();
    // ol2.setRepayNo(3);
    // OrderList ol3 = new OrderList();
    // ol3.setRepayNo(2);
    // list.add(ol1);
    // list.add(ol2);
    // list.add(ol3);
    // Collections.sort(list);
    // for(OrderList l: list){
    // System.out.println(l.getRepayNo());
    // }
    // }

}
