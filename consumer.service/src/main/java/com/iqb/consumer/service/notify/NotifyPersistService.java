/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月1日 上午11:39:58
 * @version V1.0
 */

package com.iqb.consumer.service.notify;

import java.util.Map;

import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.domain.NotifyRecord;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface NotifyPersistService {

    // 创建商户通知记录
    long saveNotifyRecord(NotifyRecord notifyRecord);

    /**
     * 更新商户通知记录
     * 
     * @param id
     * @param notifyTimes 通知次数
     * @param status 通知状态
     */
    void updateNotifyRord(NotifyRecord notifyRecord, int status);

    /**
     * 创建商户通知日志记录
     * 
     * @param notifyId
     * @param merchantNo
     * @param merchantOrderNo
     * @param request
     * @param response
     * @param httpStatus
     * @return
     */
    long saveNotifyRecordLogs(long notifyId, String merchantNo, String merchantOrderNo, String request,
            String response, int httpStatus);

    /**
     * 分页查询落地消息
     * 
     * @param pageParam
     * @param paramMap
     * @return
     */
    PageBean queryNotifyRecordListPage(PageParam pageParam, Map<String, Object> paramMap);

}
