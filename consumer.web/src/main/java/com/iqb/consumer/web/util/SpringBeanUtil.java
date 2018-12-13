package com.iqb.consumer.web.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * Description: spring中获取bean
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月24日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.applicationContext = applicationContext;
    }

    @SuppressWarnings("static-access")
    public static <T> T getBean(Class<T> clazz) {
        int i = 0;
        while (applicationContext == null && i < 5) {
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                i++;
            }
        }
        return applicationContext.getBean(clazz);
    }

}
