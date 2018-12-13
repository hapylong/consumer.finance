package com.iqb.consumer.finance.batch.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.InstallmentBillInfo;
import com.iqb.consumer.finance.batch.bean.LockRepayDate;
import com.iqb.consumer.finance.batch.cache.KVCache;
import com.iqb.consumer.finance.batch.constant.AccountConstant.RedisLockConstant;
import com.iqb.consumer.finance.batch.dao.DelayPaymentDao;
import com.iqb.consumer.finance.batch.dao.InstallmentBillInfoDao;
import com.iqb.consumer.finance.batch.dao.InstallmentInfoDao;
import com.iqb.consumer.finance.batch.dao.InstallmentPlanDao;
import com.iqb.consumer.finance.batch.page.PageBean;
import com.iqb.consumer.finance.batch.page.PageParam;
import com.iqb.consumer.finance.batch.util.DateUtil;
import com.iqb.consumer.finance.batch.util.StringUtil;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年10月19日下午3:15:37 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class ConrurrentBizSchedulerOverdueServiceImpl implements ConcurrentBizSchedulerOverdueService {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(BizSchedulerOverdueServiceImpl.class);

    /** 每页最大数量 **/
    private int numPerPage = 500;

    @Resource
    private InstallmentBillInfoDao installmentBillInfoDao;

    @Resource
    private OverdueIterestService overdueIterestService;

    @Resource
    private InstallmentPlanDao installmentPlanDao;

    @Resource
    private InstallmentInfoDao installmentInfoDao;

    @Resource
    private DelayPaymentDao delayPaymentDao;

    @Resource
    private KVCache<LockRepayDate> lockRepayDateCache;

    @Resource
    private KVCache<Integer> lockLockedCache;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年10月19日
     */
    @Override
    public void calOverdueInterest(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            objs = new JSONObject();
        }
        this.calOverdueInterest(objs.getString("orderId"), objs.getString("instBillId"), objs.getString("startDate"),
                objs.getString("endDate"));
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年10月19日
     */
    @Override
    public void calOverdueInterest(String orderId, String instBillId, String startDate, String endDate) {
        try {
            /** 加逾期调度redis锁 **/
            lockLockedCache.putForever(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK_KEY,
                    Integer.parseInt(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK));

            Long startT = System.currentTimeMillis();

            if (StringUtil.isEmpty(endDate)) {
                /** 每日调度 **/
                endDate = DateUtil.getDateString(new Date(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            } else {
                /** 添加跑批时间 加入redis控制 **/
                lockLockedCache.putForever(RedisLockConstant.BIZ_LOCKED_DAY_LOCK_KEY, Integer.parseInt(endDate));
            }
            if (StringUtil.isEmpty(startDate)) {
                startDate = DateUtil.format(DateUtil.getYesterdayDate(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            }
            logger.info("逾期调度服务开始：{}", DateUtil.format(new Date(startT), DateUtil.SIMPLE_DATE_FORMAT));
            List<Date> dateList =
                    DateUtil.getDateListBetween(DateUtil.parseDate(startDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH),
                            DateUtil.parseDate(endDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH));
            if (dateList == null || dateList.size() == 0) {
                logger.error("日期区间为空！！！");
            } else {
                dateList.remove(dateList.size() - 1);
                if (!CollectionUtils.isEmpty(dateList)) {
                    for (Date d : dateList) {
                        this.calOverdueInterestEve(orderId, instBillId,
                                DateUtil.getDateString(d, DateUtil.SHORT_DATE_FORMAT_NO_DASH));
                    }
                }
            }
            Long endT = System.currentTimeMillis();
            logger.info("---逾期调度服务完成：{}", DateUtil.format(new Date(endT), DateUtil.SIMPLE_DATE_FORMAT));
            logger.info(endDate + "---逾期调度服务完成所花时间：{} 秒", (endT - startT) / 1000);
        } catch (Exception e) {
            logger.error("逾期调度服务异常：", e);
        } finally {
            /** 清除逾期调度redis锁 **/
            lockLockedCache.remove(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK_KEY);
        }
    }

    private void calOverdueInterestEve(String orderId, String instBillId, String calDate) {
        // CPU个数
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        // 创建无界队列
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor pool =
                new ThreadPoolExecutor(availableProcessors, availableProcessors, 1, TimeUnit.SECONDS, queue);

        /** 该时间段需要进行库锁,通过Redis变量控制进行封库 **/
        /** 页数 **/
        int pageNum = 1;
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        /** 根据查询参数查询账单期的账单 **/
        Date delayDate = null;
        if (StringUtil.isEmpty(calDate)) {
            delayDate = DateUtil.getYesterdayDate();
        } else {
            delayDate = DateUtil.parseDate(calDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);
        paramMap.put("instBillId", instBillId);
        paramMap.put("delayDate", delayDate);
        /** 处理账单期账单 **/
        PageBean pageBean = this.installmentBillInfoDao.listOverdueInterestPageRepayNoDesc(pageParam, paramMap);
        try {
            int counter = 0;
            while (pageNum <= pageBean.getEndPageIndex()) {
                List<Object> list = pageBean.getRecordList();

                for (int i = 0; i < list.size(); i++) {
                    counter++;
                    InstallmentBillInfo installmentBillInfo = (InstallmentBillInfo) list.get(i);
                    try {
                        DealInstallmentBillInfoTask billInfoTask =
                                new DealInstallmentBillInfoTask(installmentBillInfo, delayDate);
                        pool.submit(billInfoTask);
                        logger.info("线程池中的线程数目：" + pool.getPoolSize() + ",队列中等待执行的任务数量："
                                + pool.getQueue().size() + ",已执行完的任务数目：" + pool.getCompletedTaskCount());

                    } catch (Exception e) {
                        logger.error("账单计算异常：" + JSONObject.toJSONString(installmentBillInfo), e);
                    }
                }
                pageNum++;
                pageParam.setPageNum(pageNum);
                pageBean = this.installmentBillInfoDao.listOverdueInterestPageRepayNoDesc(pageParam, paramMap);
                logger.info("查询账单期的账单:pageBean->" + JSONObject.toJSONString(pageBean));
                logger.info("当前账单页数:pageNum->" + pageNum);
            }
            logger.info("---总计处理逾期账单{}笔", counter);
        } catch (Exception e) {
            logger.error("执行每日逾期跑批异常--{}", e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
