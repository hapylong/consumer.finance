/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 上午10:41:15
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentPlan;
import com.iqb.consumer.finance.cache.kv.KVCache;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class InstallmentPlanDaoImpl extends BaseDaoImpl<InstallmentPlan> implements InstallmentPlanDao {

    @Resource
    private KVCache<InstallmentPlan> instPlanCache;

    @Override
    public long insert(InstallmentPlan t) {
        return super.insert(t);
    }

    @Override
    public InstallmentPlan getByPlanId(long planId) {
        String key = "instPlan." + planId;
        InstallmentPlan installmentPlan = instPlanCache.get(key, InstallmentPlan.class);
        if (installmentPlan == null) {
            installmentPlan = super.getById(planId);
            instPlanCache.put(key, installmentPlan);
        }
        return installmentPlan;
    }

    @Override
    public long deleteById(long id) {
        String key = "instPlan." + Long.toString(id);
        instPlanCache.remove(key);
        return super.deleteById(id);
    }

    @Override
    public long update(InstallmentPlan installmentPlan) {
        String key = "instPlan." + Long.toString(installmentPlan.getId());
        instPlanCache.remove(key);
        return super.update(installmentPlan);
    }

}
