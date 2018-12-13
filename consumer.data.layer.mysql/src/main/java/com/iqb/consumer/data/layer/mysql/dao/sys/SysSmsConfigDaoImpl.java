/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 下午2:48:32
 * @version V1.0
 */
package com.iqb.consumer.data.layer.mysql.dao.sys;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.sys.SysSmsConfig;
import com.iqb.consumer.finance.cache.kv.KVCache;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class SysSmsConfigDaoImpl extends BaseDaoImpl<SysSmsConfig> implements SysSmsConfigDao {

    @Resource
    private KVCache<SysSmsConfig> smsConfigCache;

    @Override
    public SysSmsConfig getSmsChannelByOpenId(String openId) {
        String key = "smsChannel." + openId;
        SysSmsConfig sysSmsConfig = smsConfigCache.get(key, SysSmsConfig.class);
        if (sysSmsConfig == null) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("openId", openId);
            sysSmsConfig = (SysSmsConfig) super.getBy(paramMap, "getSmsChannelByOpenId");
            smsConfigCache.putForever(key, sysSmsConfig);
        }
        return sysSmsConfig;
    }

}
