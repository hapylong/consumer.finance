/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 下午3:28:02
 * @version V1.0
 */
package com.iqb.consumer.service.sys;

import com.iqb.consumer.data.layer.mysql.bean.sys.SysSmsConfig;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface SysSmsConfigService {

    SysSmsConfig getSmsChannelByOpenId(String openId);
}
