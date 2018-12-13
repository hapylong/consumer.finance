package com.iqb.consumer.data.layer.mysql.bean.pojo;

import java.util.Date;

public class PrepaymentAnalysisPojo {

    /** 当期是否已还 **/
    private Boolean mark = false;
    /** 已还期数 **/
    private Integer repayNo;

    private Date lastRepayDate;
    private Long id;

    public Boolean getMark() {
        return mark;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
