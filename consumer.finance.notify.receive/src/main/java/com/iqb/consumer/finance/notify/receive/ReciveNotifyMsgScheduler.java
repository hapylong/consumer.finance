/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月1日 上午10:54:40
 * @version V1.0
 */

package com.iqb.consumer.finance.notify.receive;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.domain.NotifyRecord;
import com.iqb.consumer.finance.notify.receive.core.NotifyQueue;
import com.iqb.consumer.finance.notify.receive.core.NotifyTask;
import com.iqb.consumer.finance.notify.receive.domain.NotifyParam;
import com.iqb.consumer.service.notify.NotifyPersistService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class ReciveNotifyMsgScheduler {

    protected static final Logger logger = LoggerFactory.getLogger(ReciveNotifyMsgScheduler.class);

    public static DelayQueue<NotifyTask> tasks = new DelayQueue<NotifyTask>();

    @Resource
    private ThreadPoolTaskExecutor threadPool;
    @Resource
    private NotifyPersistService notifyPersistService;
    @Resource
    private NotifyParam notifyParam;

    // @Scheduled(initialDelay = 1000L, fixedDelay = 5000L)
    public void test() {
        System.out.println(threadPool);
        for (int i = 1; i < 10; i++) {
            NotifyTask task = new NotifyTask(null, null, null, notifyPersistService);
            tasks.put(task);
        }
        System.out.println("------------定时器--------");
    }

    @PostConstruct
    public void init() {
        startInitFromDB();
        dealDelayQueue();
    }

    private void startInitFromDB() {
        logger.info("get data from database");

        int pageNum = 1;
        int numPerPage = 500;
        PageParam pageParam = new PageParam(pageNum, numPerPage);

        // 查询状态和通知次数符合以下条件的数据进行通知
        String[] status = new String[] {"101", "102", "200", "201"};
        Integer[] notifyTime = new Integer[] {0, 1, 2, 3, 4};
        // 组装查询条件
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("statusList", status);
        paramMap.put("notifyTimeList", notifyTime);

        PageBean pageBean = notifyPersistService.queryNotifyRecordListPage(pageParam, paramMap);
        NotifyQueue notifyQueue = new NotifyQueue(notifyParam, notifyPersistService);
        while (pageNum <= pageBean.getEndPageIndex()) {
            List<Object> list = pageBean.getRecordList();
            for (int i = 0; i < list.size(); i++) {
                NotifyRecord notifyRecord = (NotifyRecord) list.get(i);
                notifyRecord.setLastNotifyTime(new Date());
                notifyQueue.addElementToList(notifyRecord);
            }
            pageNum++;
            logger.info(String.format("调用通知服务.notifyFacade.notiFyReCordListPage(%s, %s, %s)", pageNum, numPerPage,
                    paramMap));
            pageBean = notifyPersistService.queryNotifyRecordListPage(pageParam, paramMap);
        }
    }

    private void dealDelayQueue() {
        threadPool.execute(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        // 如果当前活动线程等于最大线程，那么不执行
                        if (threadPool.getActiveCount() < threadPool.getMaxPoolSize()) {
                            final NotifyTask task = tasks.poll();
                            if (task != null) {
                                threadPool.execute(new Runnable() {
                                    public void run() {
                                        tasks.remove(task);
                                        task.run();
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
