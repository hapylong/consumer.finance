/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账户业务调度
 * @date 2016年7月22日 下午2:35:06
 * @version V1.0
 */

package com.iqb.consumer.virtual.account.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant.RemindSmsTypeConstant;
import com.iqb.consumer.service.account.BizSchedulerOverdueService;
import com.iqb.consumer.service.sms.SendSmsService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class BusinessScheduler {

    protected static final Logger logger = LoggerFactory.getLogger(BusinessScheduler.class);
    @Resource
    private BizSchedulerOverdueService bizSchedulerOverdueService;
    @Resource
    private SendSmsService sendSmsServiceImpl;

    /**
     * 滞纳金计算调度
     */
    // @Scheduled(cron = "0 30 0 * * ?")
    public void calculeDelay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        logger.debug("滞纳金调度开始时间:{}", sdf.format(new Date()));
        bizSchedulerOverdueService.calOverdueInterest(null);
        logger.debug("滞纳金调度结束时间:{}", sdf.format(new Date()));
    }

    /**
     * 
     * Description: 还款短信提醒调度
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年1月11日 下午1:45:35
     */
    // @Scheduled(cron = "0 0 8 * * ?")
    public void repayRemindSms() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        logger.debug("还款短信提醒调度开始时间:{}", sdf.format(new Date()));
        JSONObject objs = new JSONObject();
        objs.put("dayAdv", this.getDealDays());
        Object o = sendSmsServiceImpl.smsRemind(objs,
                RemindSmsTypeConstant.COM_IQIANBANG_SMS_TYPE_01);
        logger.debug("发送短信数量:{}", o.toString());
        logger.debug("还款短信提醒调度结束时间:{}", sdf.format(new Date()));
    }

    // @Scheduled(cron = "0 0 8 * * ?")
    public void billOverdueMSGRemind() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        logger.info("BusinessScheduler[billOverdueMSGRemind] start:{}", sdf.format(new Date()));

        Integer o = sendSmsServiceImpl.billOverdueMSGRemind(RemindSmsTypeConstant.COM_IQIANBANG_SMS_TYPE_03);

        logger.info("BusinessScheduler[billOverdueMSGRemind] MSG count:{}", o.toString());
        logger.info("BusinessScheduler[billOverdueMSGRemind] end:{}", sdf.format(new Date()));
    }

    /**
     * 
     * Description: 获取所需提前天数
     * 
     * @param
     * @return String[]
     * @throws
     * @Author wangxinbang Create Date: 2017年1月11日 下午2:08:18
     */
    private String[] getDealDays() {
        return new String[] {"1", "3", "7"};
    }

}
