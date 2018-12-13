/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月18日 下午1:53:36
 * @version V1.0
 */

package com.iqb.consumer.common.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
/**
 * For Dao method, which need to support pagination. All methods annotated this annotation should pass
 * page, pageSize by order as the frist two arguments. Then page and pageSize should be used as following.
 *      if (pageSize < 1 || pageSize > CommonConstants.MAX_PER_PAGE) {
 *           pageSize = CommonConstants.MAX_PER_PAGE;
 *       }
 *       if (page < 1) {
 *           page = 1;
 *       }
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public @interface PageValidation {

}
