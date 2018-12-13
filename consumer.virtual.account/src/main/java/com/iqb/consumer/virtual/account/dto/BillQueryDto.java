/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月17日 上午11:33:31
 * @version V1.0
 */
package com.iqb.consumer.virtual.account.dto;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class BillQueryDto {

    private String regId;// 手机号
    private String orderId;// 订单号
    private String lastRepayDate;// 最后还款时间
    private String merchantNos;// 商户号,隔开
    private String status;// 账单状态
    private String openId;// 行业号

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public String getMerchantNos() {
        return merchantNos;
    }

    public void setMerchantNos(String merchantNos) {
        this.merchantNos = merchantNos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
