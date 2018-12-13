/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月17日 上午11:33:31
 * @version V1.0
 */
package com.iqb.consumer.service.consumer.dto.fastBill;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class Plan {

    private String number;
    private String capital;
    private String interest;
    private String finalrepaymentdate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getFinalrepaymentdate() {
        return finalrepaymentdate;
    }

    public void setFinalrepaymentdate(String finalrepaymentdate) {
        this.finalrepaymentdate = finalrepaymentdate;
    }

}
