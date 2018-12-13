/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月15日 上午11:47:58
 * @version V1.0
 */
package com.iqb.consumer.common.journal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志插入
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface InsertJournal {

}
