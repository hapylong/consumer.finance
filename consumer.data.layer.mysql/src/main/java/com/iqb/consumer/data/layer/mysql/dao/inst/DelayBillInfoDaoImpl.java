/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月26日 下午5:50:05
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.domain.DelayBillInfo;
import com.iqb.consumer.finance.cache.kv.KVCache;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class DelayBillInfoDaoImpl extends BaseDaoImpl<DelayBillInfo> implements DelayBillInfoDao {

    @Resource
    private KVCache<DelayBillInfo> delayBillCache;

    @Override
    public List<DelayBillInfo> listBy(Map<String, Object> paramMap) {
        return super.listBy(paramMap);
    }

    @Override
    public DelayBillInfo getBy(Map<String, Object> paramMap) {
        String key = "DelayBill." + paramMap.get("conUUID");
        DelayBillInfo delayBillInfo = delayBillCache.get(key, DelayBillInfo.class);
        if (delayBillInfo == null) {
            delayBillInfo = super.getBy(paramMap);
            delayBillCache.put(key, delayBillInfo);
        }
        return delayBillInfo;
    }

}
