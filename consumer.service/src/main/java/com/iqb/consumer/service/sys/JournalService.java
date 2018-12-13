/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 上午10:09:05
 * @version V1.0
 */
package com.iqb.consumer.service.sys;

import com.iqb.consumer.data.layer.mysql.bean.sys.SysOperateInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface JournalService {

    long insert(SysOperateInfo sysOperateInfo);
}
