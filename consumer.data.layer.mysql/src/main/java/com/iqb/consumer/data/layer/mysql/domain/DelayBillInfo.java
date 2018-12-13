/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月26日 下午5:46:04
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.domain;

import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class DelayBillInfo extends BaseEntity {
    private String conUUID;// 用户唯一ID
    private long accID;// 账户ID
    private Date delayDate;// 账单延期日期
    private Date repayDate;// 账单还款日期
    private int status;// 延期状态(0,延期1,支付)

    public String getConUUID() {
        return conUUID;
    }

    public void setConUUID(String conUUID) {
        this.conUUID = conUUID;
    }

    public long getAccID() {
        return accID;
    }

    public void setAccID(long accID) {
        this.accID = accID;
    }

    public Date getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
