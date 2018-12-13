package com.iqb.consumer.data.layer.mysql.dao.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.product.RefundCost;
import com.iqb.consumer.finance.cache.kv.KVCache;

/**
 * 
 * Description: 退费dao服务实现类
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月10日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Repository
public class RefundCostDaoImpl extends BaseDaoImpl<RefundCost> implements RefundCostDao {

    @Resource
    private KVCache<RefundCost> refundCostCache;

    @Override
    public RefundCost getById(long id) {
        String key = "RefundCost." + Long.toString(id);
        RefundCost refundCost = refundCostCache.get(key, RefundCost.class);
        if (refundCost == null) {
            refundCost = super.getById(id);
            refundCostCache.put(key, refundCost);
        }
        return refundCost;
    }

    @Override
    public long deleteById(long id) {
        String key = "RefundCost." + Long.toString(id);
        refundCostCache.remove(key);
        return super.deleteById(id);
    }

    @Override
    public long update(RefundCost refundCost) {
        String key = "RefundCost." + Long.toString(refundCost.getId());
        refundCostCache.remove(key);
        return super.update(refundCost);
    }

    @Override
    public long insert(RefundCost refundCost) {
        return super.insert(refundCost);
    }

}
