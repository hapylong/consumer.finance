/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 下午1:23:22
 * @version V1.0
 */

package com.iqb.consumer.service.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerInfoDto {
    private String regId;
    private String passWord;
    private String vcode;// 验证码

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
