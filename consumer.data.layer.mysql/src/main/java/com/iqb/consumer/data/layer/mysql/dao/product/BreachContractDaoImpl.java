package com.iqb.consumer.data.layer.mysql.dao.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.product.BreachContract;
import com.iqb.consumer.finance.cache.kv.KVCache;

/**
 * 
 * Description: 违约金比例实现类
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Repository
public class BreachContractDaoImpl extends BaseDaoImpl<BreachContract> implements BreachContractDao {

    @Resource
    private KVCache<BreachContract> breachContractCache;

    @Override
    public BreachContract getById(long id) {
        String key = "BreachContract." + Long.toString(id);
        BreachContract breachContract = breachContractCache.get(key, BreachContract.class);
        if (breachContract == null) {
            breachContract = super.getById(id);
            breachContractCache.put(key, breachContract);
        }
        return breachContract;
    }

    @Override
    public long deleteById(long id) {
        String key = "BreachContract." + Long.toString(id);
        breachContractCache.remove(key);
        return super.deleteById(id);
    }

    @Override
    public long update(BreachContract breachContract) {
        String key = "BreachContract." + Long.toString(breachContract.getId());
        breachContractCache.remove(key);
        return super.update(breachContract);
    }

    @Override
    public long insert(BreachContract breachContract) {
        return super.insert(breachContract);
    }

}
