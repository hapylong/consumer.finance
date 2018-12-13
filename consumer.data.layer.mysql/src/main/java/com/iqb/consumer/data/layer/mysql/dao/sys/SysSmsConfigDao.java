/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 下午2:47:16
 * @version V1.0
 */
package com.iqb.consumer.data.layer.mysql.dao.sys;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.bean.sys.SysSmsConfig;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface SysSmsConfigDao extends BaseDao<SysSmsConfig> {
    /**
     * 通过行业号查询短信渠道
     * 
     * @param openId
     * @return
     */
    SysSmsConfig getSmsChannelByOpenId(String openId);
}
