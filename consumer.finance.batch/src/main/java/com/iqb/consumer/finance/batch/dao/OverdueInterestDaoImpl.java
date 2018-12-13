/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:48:28
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.OverdueInterest;
import com.iqb.consumer.finance.batch.bean.OverdueRule;
import com.iqb.consumer.finance.batch.cache.KVCache;
import com.iqb.consumer.finance.batch.dao.basic.BaseDaoImpl;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
@Repository
public class OverdueInterestDaoImpl extends BaseDaoImpl<OverdueInterest> implements OverdueInterestDao {

    @Resource
    private KVCache<OverdueInterest> overdueInterestCache;

    @Override
    public OverdueInterest getOverdueInterestById(String id) {
        String key = "OverdueInterest." + id;
        OverdueInterest overdueInterest = overdueInterestCache.get(key, OverdueInterest.class);
        if (overdueInterest == null) {
            overdueInterest = super.getSqlSession().selectOne(getStatement("getOverdueInterestById"), id);
            overdueInterestCache.put(key, overdueInterest);
        }
        return overdueInterest;
    }

    @Override
    public long deleteById(long id) {
        String key = "OverdueInterest." + Long.toString(id);
        overdueInterestCache.remove(key);
        return super.deleteById(id);
    }

    @Override
    public long update(OverdueInterest overdueInterest) {
        String key = "OverdueInterest." + Long.toString(overdueInterest.getId());
        overdueInterestCache.remove(key);
        return super.update(overdueInterest);
    }

    @Override
    public long insert(OverdueInterest overdueInterest) {
        return super.insert(overdueInterest);
    }

    @Override
    public List<OverdueRule> getOverdueRuleSById(String id) {
        // 查询所有逾期计划
        String key = "OverdueRuleS." + id;
        String text = overdueInterestCache.get(key);
        if (text == null) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("poverdueId", id);
            List<OverdueRule> list = super.getSqlSession().selectList("getOverdueRuleSById", paramMap);
            overdueInterestCache.put(key, JSONObject.toJSONString(list));
            return list;
        } else {
            return JSONObject.parseArray(text, OverdueRule.class);
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年10月30日
     */
    @Override
    public int getTakePayment(String orderId) {
        Object count = super.getSqlSession().selectOne(getStatement("getTakePayment"), orderId);
        return Integer.parseInt(count == null ? "0" : count.toString());
    }

}
