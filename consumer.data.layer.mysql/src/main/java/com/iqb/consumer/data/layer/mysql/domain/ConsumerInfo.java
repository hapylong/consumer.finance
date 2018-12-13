/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 消费者
 * @date 2016年5月25日 下午3:15:55
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.domain;

import java.sql.Timestamp;
import java.util.Date;

import com.iqb.consumer.common.domain.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class ConsumerInfo extends BaseEntity {

    private String regId;
    private String password;
    private String idCard;
    private String realName;
    private int status;
    private String loginIp;
    private Timestamp loginTime;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

}
