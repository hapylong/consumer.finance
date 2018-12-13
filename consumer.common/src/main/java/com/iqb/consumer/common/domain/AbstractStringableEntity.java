/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月15日 下午6:37:14
 * @version V1.0
 */

package com.iqb.consumer.common.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public abstract class AbstractStringableEntity {

    @Override
    public String toString() {
        // just a bit of lazy here
        return ToStringBuilder.reflectionToString(this);
    }
}
