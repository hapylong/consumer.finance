/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月22日 下午6:06:56
 * @version V1.0
 */

package com.iqb.consumer.finance.activemq.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class ConsumerSessionAwareMessageListener implements SessionAwareMessageListener<Message> {

    @Override
    public void onMessage(Message arg0, Session arg1) throws JMSException {

    }

}
