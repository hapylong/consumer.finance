/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月1日 上午11:52:35
 * @version V1.0
 */

package com.iqb.consumer.finance.notify.receive.core;

import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.enums.NotifyStatusEnum;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.utils.httpclient.SimpleHttpParam;
import com.iqb.consumer.common.utils.httpclient.SimpleHttpResult;
import com.iqb.consumer.common.utils.httpclient.SimpleHttpUtils;
import com.iqb.consumer.data.layer.mysql.domain.NotifyRecord;
import com.iqb.consumer.finance.notify.receive.domain.NotifyParam;
import com.iqb.consumer.service.notify.NotifyPersistService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class NotifyTask implements Runnable, Delayed {

    protected static final Logger logger = LoggerFactory.getLogger(NotifyTask.class);

    private long executeTime;
    private NotifyRecord notifyRecord;
    private NotifyQueue notifyQueue;
    private NotifyParam notifyParam;
    private NotifyPersistService notifyPersistService;

    public NotifyTask() {}

    public NotifyTask(NotifyRecord notifyRecord, NotifyQueue notifyQueue, NotifyParam notifyParam,
            NotifyPersistService notifyPersistService) {
        super();
        this.notifyRecord = notifyRecord;
        this.notifyQueue = notifyQueue;
        this.notifyParam = notifyParam;
        this.notifyPersistService = notifyPersistService;
        this.executeTime = getExecuteTime(notifyRecord);
    }

    private long getExecuteTime(NotifyRecord record) {
        long lastTime = record.getLastNotifyTime().getTime();
        Integer nextNotifyTime = notifyParam.getNotifyParams().get(record.getNotifyTimes());
        return (nextNotifyTime == null ? 0 : nextNotifyTime * 1000) + lastTime;
    }

    public int compareTo(Delayed o) {
        NotifyTask task = (NotifyTask) o;
        return executeTime > task.executeTime ? 1 : (executeTime < task.executeTime ? -1 : 0);
    }

    public long getDelay(TimeUnit unit) {
        return unit.convert(executeTime - System.currentTimeMillis(), unit.SECONDS);
    }

    public void run() {
        // 得到当前通知对象的通知次数
        Integer notifyTimes = notifyRecord.getNotifyTimes();
        // 去通知
        try {
            logger.info("Notify Type " + notifyRecord.getNotifyType());
            /** 采用 httpClient */
            SimpleHttpParam param = new SimpleHttpParam(notifyRecord.getUrl());
            SimpleHttpResult result = SimpleHttpUtils.httpRequest(param);
            logger.info("调用接口返回的结果{},状态{}--------", JSONObject.toJSONString(result), result.getStatusCode());
            notifyRecord.setNotifyTimes(notifyTimes + 1);
            String successValue = notifyParam.getSuccessValue();
            String responseMsg = "";
            Integer responseStatus = result.getStatusCode();
            // 得到返回状态，如果是200，也就是通知成功
            if (result != null
                    && (responseStatus == 200 || responseStatus == 201 || responseStatus == 202
                            || responseStatus == 203
                            || responseStatus == 204 || responseStatus == 205 || responseStatus == 206)) {
                responseMsg = result.getContent().trim();
                responseMsg = responseMsg.length() >= 600 ? responseMsg.substring(0, 600) : responseMsg;
                logger.info("订单号： " + notifyRecord.getMerchantOrderNo() + " HTTP_STATUS：" + responseStatus + "请求返回信息："
                        + responseMsg);
                Map<String, Object> responseMap = JSONObject.parseObject(responseMsg);
                // 通知成功
                if (successValue != null && successValue.equals(responseMap.get("retCode"))) {
                    notifyPersistService
                            .updateNotifyRord(notifyRecord,
                                    NotifyStatusEnum.SUCCESS.getValue());
                } else {
                    notifyQueue.addElementToList(notifyRecord);
                    notifyPersistService.updateNotifyRord(notifyRecord,
                            NotifyStatusEnum.HTTP_REQUEST_SUCCESS.getValue());

                }
                logger.info("Update NotifyRecord:" + JSONObject.toJSONString(notifyRecord));
            } else {
                logger.info("----else-------");
                notifyQueue.addElementToList(notifyRecord);
                // 再次放到通知列表中，由添加程序判断是否已经通知完毕或者通知失败
                notifyPersistService.updateNotifyRord(notifyRecord,
                        NotifyStatusEnum.HTTP_REQUEST_FALIED.getValue());
            }

            // 写通知日志表
            notifyPersistService.saveNotifyRecordLogs(notifyRecord.getId(), notifyRecord.getMerchantNo(),
                    notifyRecord.getMerchantOrderNo(),
                    notifyRecord.getUrl(), responseMsg, responseStatus);
            logger.info("Insert NotifyRecordLog, merchantNo:" + notifyRecord.getMerchantNo() + ",merchantOrderNo:"
                    + notifyRecord.getMerchantOrderNo());
        } catch (BizException e) {// 业务异常
            logger.error("NotifyTask", e);
        } catch (Exception e) {
            logger.error("NotifyTask", e);
            notifyQueue.addElementToList(notifyRecord);
            notifyPersistService.updateNotifyRord(notifyRecord,
                    NotifyStatusEnum.FAILED.getValue());
            notifyPersistService.saveNotifyRecordLogs(notifyRecord.getId(), notifyRecord.getMerchantNo(),
                    notifyRecord.getMerchantOrderNo(),
                    notifyRecord.getUrl(), "", NotifyStatusEnum.FAILED.getValue());
        }

    }

}
