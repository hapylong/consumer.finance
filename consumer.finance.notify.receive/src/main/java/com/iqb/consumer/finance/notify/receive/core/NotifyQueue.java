/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月1日 上午11:53:25
 * @version V1.0
 */

package com.iqb.consumer.finance.notify.receive.core;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqb.consumer.common.enums.NotifyStatusEnum;
import com.iqb.consumer.data.layer.mysql.domain.NotifyRecord;
import com.iqb.consumer.finance.notify.receive.ReciveNotifyMsgScheduler;
import com.iqb.consumer.finance.notify.receive.domain.NotifyParam;
import com.iqb.consumer.service.notify.NotifyPersistService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class NotifyQueue implements Serializable {

    private static final long serialVersionUID = -1874590591434555963L;

    protected static final Logger logger = LoggerFactory.getLogger(NotifyQueue.class);

    private NotifyParam notifyParam;
    private NotifyPersistService notifyPersistService;

    public NotifyQueue() {}

    public NotifyQueue(NotifyParam notifyParam, NotifyPersistService notifyPersistService) {
        this.notifyParam = notifyParam;
        this.notifyPersistService = notifyPersistService;
    }

    /**
     * 将传过来的对象进行通知次数判断，之后决定是否放在任务队列中
     * 
     * @param notifyRecord
     */
    public void addElementToList(NotifyRecord notifyRecord) {
        if (notifyRecord == null) {
            return;
        }
        Integer notifyTimes = notifyRecord.getNotifyTimes(); // 通知次数
        Integer maxNotifyTime = 0;
        try {
            maxNotifyTime = notifyParam.getMaxNotifyTime();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (notifyRecord.getVersion().intValue() == 0) {// 刚刚接收到的数据
            notifyRecord.setLastNotifyTime(new Date());
        }
        long time = notifyRecord.getLastNotifyTime().getTime();
        Map<Integer, Integer> timeMap = notifyParam.getNotifyParams();
        if (notifyTimes < maxNotifyTime) {
            Integer nextKey = notifyTimes + 1;
            Integer next = timeMap.get(nextKey);
            if (next != null) {
                time += 1000 * 60 * next + 1;
                notifyRecord.setLastNotifyTime(new Date(time));
                ReciveNotifyMsgScheduler.tasks
                        .put(new NotifyTask(notifyRecord, this, notifyParam, notifyPersistService));
                logger.debug("第{}次往对列放的数据为:{}", notifyTimes, notifyRecord.toString());
            }
        } else {
            try {
                // 持久化到数据库中
                notifyPersistService.updateNotifyRord(notifyRecord, NotifyStatusEnum.FAILED.getValue());
                logger.info("Update NotifyRecord failed,merchantNo:" + notifyRecord.getMerchantNo()
                        + ",merchantOrderNo:"
                        + notifyRecord.getMerchantOrderNo());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
