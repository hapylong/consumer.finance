/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 下午1:42:02
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.config.SysSmsConfig;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface SendSmsService {

    /**
     * 动态发送短信
     * 
     * @param smsConfig
     * @param msg
     * @return
     * @throws Exception
     */
    String sendSms(SysSmsConfig sysSmsConfig, String regId, String msg) throws Exception;

    /**
     * 
     * Description: 还款短信提醒
     * 
     * @param i
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年1月11日 下午1:48:46
     */
    public Object smsRemind(JSONObject objs, Integer i);

    public Integer billOverdueMSGRemind(Integer type);

    /**
     * 校验车辆状态是否已入库(status==30) Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月22日
     */
    public boolean validateCarStatusStoraged(String orderId);

}
