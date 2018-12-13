package com.iqb.consumer.finance.batch.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
 * 2018年10月24日下午2:29:53 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class SpringBeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年10月24日
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.applicationContext = applicationContext;
    }

    public static Object getBeanByName(String beanName) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }
}
