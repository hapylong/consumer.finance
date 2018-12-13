/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账户业务调度
 * @date 2016年7月22日 下午2:35:06
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.constant.AccountConstant.RemindSmsTypeConstant;
import com.iqb.consumer.finance.batch.service.BizSchedulerOverdueService;
import com.iqb.consumer.finance.batch.service.ConcurrentBizSchedulerOverdueService;
import com.iqb.consumer.finance.batch.service.InstBillInfoService;
import com.iqb.consumer.finance.batch.service.OverdueNewService;
import com.iqb.consumer.finance.batch.service.SendSmsService;
import com.iqb.consumer.finance.batch.util.DateUtil;

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
    @Resource
    private InstBillInfoService InstBillInfoServiceImpl;
    @Resource
    private OverdueNewService overdueNewServiceImpl;
    @Resource
    private ConcurrentBizSchedulerOverdueService concurrentBizSchedulerOverdueService;

    /**
     * 0 30 0 * * ?
     * 滞纳金计算调度
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void calculeDelay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
     * @throws @Author wangxinbang Create Date: 2017年1月11日 下午1:45:35
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void repayRemindSms() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        logger.info("还款短信提醒调度开始时间:{}", sdf.format(new Date()));
        JSONObject objs = new JSONObject();
        objs.put("dayAdv", this.getDealDays());
        Object o = sendSmsServiceImpl.smsRemind(objs,
                RemindSmsTypeConstant.COM_IQIANBANG_SMS_TYPE_01);
        logger.info("发送短信数量:{}", o.toString());
        logger.info("还款短信提醒调度结束时间:{}", sdf.format(new Date()));
    }

    /**
     * 
     * Description:逾期账单短息提醒
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月13日
     */
    @Scheduled(cron = "0 0 8 * * ?")
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
     * @throws @Author wangxinbang Create Date: 2017年1月11日 下午2:08:18
     */
    private String[] getDealDays() {
        // return new String[] {"1", "3", "7"};
        return new String[] {"1", "5"};
    }

    /**
     * 每日资产逾期数据保存 每天晚上2点执行 0 0 2 * * ?
     */
    // @Scheduled(cron = "0 0 2 * * ?")
    public void everyOverdueAssetInsert() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logger.debug("每日资产逾期数据保存:{}", sdf.format(new Date()));
        Map<String, Object> paramMap = new HashMap<>();

        long result = 0;
        long days = getIntervalDays(new Date());

        for (int i = 0; i <= days; i++) {
            paramMap.put("currentDate", getDate(i + 1));
            result = InstBillInfoServiceImpl.mergeList(paramMap);
        }

        logger.debug("---插入{}条资产逾期数据", result);
        logger.debug("每日资产逾期数据保存:{}", sdf.format(new Date()));
    }

    /**
     * 
     * Description:获取当前时间到2017-1-1的间隔时间
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月25日
     */
    private static long getIntervalDays(Date date) {
        Calendar cal = DateUtil.now();
        cal.set(2017, 12, 1);
        long intervalDays = DateUtil.diffDays(cal.getTime(), date);
        return intervalDays;
    }

    /**
     * 
     * Description:获取距离2017-1-1日指定天数的时间
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月25日
     */
    private static String getDate(int intervalDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = DateUtil.now();
        cal.set(2017, 11, 1);
        cal.add(Calendar.DATE, intervalDays);
        return sdf.format(cal.getTime());
    }

    /**
     * 
     * Description：每日资产逾期第二版
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月5日
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void everyOverdueNewAssetInsert() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logger.info("每日资产逾期第二版数据保存:{}", sdf.format(new Date()));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("currentDate", sdf.format(new Date()));
        long result = overdueNewServiceImpl.mergeList(paramMap);
        logger.info("---插入{}条资产逾期数据", result);
        logger.info("每日资产逾期第二版数据保存:{}", sdf.format(new Date()));
    }
}
