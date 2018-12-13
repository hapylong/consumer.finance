/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月1日 上午11:44:58
 * @version V1.0
 */

package com.iqb.consumer.service.notify;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.dao.NotifyRecordDao;
import com.iqb.consumer.data.layer.mysql.dao.NotifyRecordLogDao;
import com.iqb.consumer.data.layer.mysql.domain.NotifyRecord;
import com.iqb.consumer.data.layer.mysql.domain.NotifyRecordLog;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class NotifyPersistServiceImpl implements NotifyPersistService {

    @Resource
    private NotifyRecordDao notifyRecordDao;
    @Resource
    private NotifyRecordLogDao notifyRecordLogDao;

    @Override
    public long saveNotifyRecord(NotifyRecord notifyRecord) {
        return notifyRecordDao.insert(notifyRecord);
    }

    @Override
    public void updateNotifyRord(NotifyRecord notifyRecord, int status) {
        NotifyRecord record = notifyRecordDao.getById(notifyRecord.getId());
        record.setLastNotifyTime(notifyRecord.getLastNotifyTime());
        record.setNotifyTimes(notifyRecord.getNotifyTimes());
        record.setStatus(status);
        notifyRecordDao.update(record);
    }

    @Override
    public long saveNotifyRecordLogs(long notifyId, String merchantNo, String merchantOrderNo, String request,
            String response, int httpStatus) {
        NotifyRecordLog notifyRecordLog = new NotifyRecordLog();
        notifyRecordLog.setHttpStatus(httpStatus);
        notifyRecordLog.setMerchantNo(merchantNo);
        notifyRecordLog.setMerchantOrderNo(merchantOrderNo);
        notifyRecordLog.setNotifyID(notifyId);
        notifyRecordLog.setRequest(request);
        notifyRecordLog.setResponse(response);
        return notifyRecordLogDao.insert(notifyRecordLog);
    }

    @Override
    public PageBean queryNotifyRecordListPage(PageParam pageParam, Map<String, Object> paramMap) {
        return notifyRecordDao.listPage(pageParam, paramMap);
    }

}
