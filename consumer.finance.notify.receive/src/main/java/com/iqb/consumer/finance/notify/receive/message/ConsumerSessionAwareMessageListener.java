/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月1日 上午11:25:33
 * @version V1.0
 */

package com.iqb.consumer.finance.notify.receive.message;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.enums.NotifyStatusEnum;
import com.iqb.consumer.data.layer.mysql.domain.NotifyRecord;
import com.iqb.consumer.finance.notify.receive.core.NotifyQueue;
import com.iqb.consumer.finance.notify.receive.domain.NotifyParam;
import com.iqb.consumer.service.notify.NotifyPersistService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class ConsumerSessionAwareMessageListener implements SessionAwareMessageListener<Message> {

    protected static final Logger logger = LoggerFactory.getLogger(ConsumerSessionAwareMessageListener.class);

    @Resource
    private JmsTemplate notifyJmsTemplate;
    @Resource
    private Destination sessionAwareQueue;
    @Resource
    private NotifyPersistService notifyPersistService;
    @Resource
    private NotifyParam notifyParam;

    @SuppressWarnings("static-access")
    @Override
    public synchronized void onMessage(Message message, Session session) throws JMSException {
        try {
            ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
            final String ms = msg.getText();
            logger.info("== receive message:" + ms);
            NotifyRecord notifyRecord = JSONObject.parseObject(ms, NotifyRecord.class);// 这里转换成相应的对象还有问题
            if (notifyRecord == null) {
                return;
            }
            // log.info("notifyParam:" + notifyParam);
            notifyRecord.setStatus(NotifyStatusEnum.CREATED.getValue());
            if (notifyRecord.getId() == null) {// 判断数据库中是否已有通知记录
                while (notifyPersistService == null) {
                    Thread.currentThread().sleep(1000); // 主动休眠，防止类notifyRecordFacade未加载完成，监听服务就开启监听出现空指针异常
                }
                try {
                    // 将获取到的通知先保存到数据库中
                    long notifyId = 0;
                    try {
                        notifyId = notifyPersistService.saveNotifyRecord(notifyRecord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    notifyRecord.setId(notifyId); // 插入后，立即返回ID
                    NotifyQueue notifyQueue = new NotifyQueue(notifyParam, notifyPersistService);
                    // 添加到通知队列
                    notifyQueue.addElementToList(notifyRecord);
                } catch (Exception e) {
                    logger.error("BizException :", e);
                    notifyJmsTemplate.send(sessionAwareQueue, new MessageCreator() {
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(ms);
                        }
                    });

                    logger.error("RpcException :", e);
                }
            }
        } catch (Exception e) {
            logger.error("receive msg could not format NotifyRecord", e);
        }
    }

}
