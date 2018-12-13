/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月18日 下午1:43:29
 * @version V1.0
 */

package com.iqb.consumer.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Aspect
public class PageValidationAdvice {
    public final static int MAX_PER_PAGE = 100;

    @Around("@annotation(com.iqb.consumer.finance.common.aop.PageValidation)")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        Object[] objs = pjp.getArgs();
        int page = (Integer) objs[0];
        int pageSize = (Integer) objs[1];

        if (pageSize < 1 || pageSize > MAX_PER_PAGE) {
            pageSize = MAX_PER_PAGE;
        }
        if (page < 1) {
            page = 1;
        }

        objs[0] = page;
        objs[1] = pageSize;

        return pjp.proceed(objs);
    }
}
