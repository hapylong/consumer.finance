/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月26日 下午6:22:43
 * @version V1.0
 */

package com.iqb.consumer.common.exception;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class ExceptionInterceptorLog implements ThrowsAdvice {

    protected static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptorLog.class);

    /**
     * 对未知异常的处理. <br>
     * Method method 执行的方法 Object[] args <br>
     * 方法参数 Object target <br>
     * 代理的目标对象 Throwable BizException 产生的异常 <br>
     */
    public void afterThrowing(Method method, Object[] args, Object target, BizException ex) {

        logger.info("==>ExceptionInterceptorlogger.BizException");
        logger.info("==>errCode:" + ex.getCode() + " errMsg:" + ex.getMsg());
        logger.info("==>" + ex.fillInStackTrace());
    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {

        logger.error("==>ExceptionInterceptorlogger.Exception");

        logger.error("==>Error class: " + target.getClass().getName());
        logger.error("==>Error method: " + method.getName());

        for (int i = 0; i < args.length; i++) {
            logger.error("==>args[" + i + "]: " + args[i]);
        }

        logger.error("==>Exception class: " + ex.getClass().getName());
        logger.error("==>" + ex.fillInStackTrace());
        ex.printStackTrace();
    }
}
