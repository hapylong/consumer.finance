/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月14日上午10:27:53 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.data.layer.mysql.bean.inst;

/**
 * @author haojinlong
 * 
 */
public class InstallmentBillInfoNormalRepay {

    private String orderId;// 订单ID
    private String regId;// 注册号
    private String openId;// 行业开户号
    private String merchantNo;// 商户号
    private String repayNo;// 还款序号
    private String amt;// 当前应还金额

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the regId
     */
    public String getRegId() {
        return regId;
    }

    /**
     * @param regId the regId to set
     */
    public void setRegId(String regId) {
        this.regId = regId;
    }

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return the merchantNo
     */
    public String getMerchantNo() {
        return merchantNo;
    }

    /**
     * @param merchantNo the merchantNo to set
     */
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    /**
     * @return the repayNo
     */
    public String getRepayNo() {
        return repayNo;
    }

    /**
     * @param repayNo the repayNo to set
     */
    public void setRepayNo(String repayNo) {
        this.repayNo = repayNo;
    }

    /**
     * @return the amt
     */
    public String getAmt() {
        return amt;
    }

    /**
     * @param amt the amt to set
     */
    public void setAmt(String amt) {
        this.amt = amt;
    }

}
