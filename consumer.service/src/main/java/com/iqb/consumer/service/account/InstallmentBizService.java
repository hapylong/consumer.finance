/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月14日 下午1:46:29
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import java.util.Map;

import com.iqb.consumer.service.account.dto.OrderInfoDto;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface InstallmentBizService {

    Map<String, Object> paymentByInstall(OrderInfoDto orderInfoDto, final String billUrl, final String smsUrl);
}
