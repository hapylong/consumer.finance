/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月22日 下午5:10:09
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import java.util.Map;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface BizSchedulerService {

    // 单日违约金计算
    Map<String, Object> calculeDelay(String endDate);

    // 批量违约金计算
    Map<String, Object> batchCalDelay(String endDate);
}
