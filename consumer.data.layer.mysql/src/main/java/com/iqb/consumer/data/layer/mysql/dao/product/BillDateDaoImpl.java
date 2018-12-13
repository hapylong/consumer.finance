package com.iqb.consumer.data.layer.mysql.dao.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.product.RepayDate;
import com.iqb.consumer.finance.cache.kv.KVCache;

/**
 * 
 * Description: 账单日相关dao实现
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月7日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Repository
public class BillDateDaoImpl extends BaseDaoImpl<RepayDate> implements BillDateDao {

    @Resource
    private KVCache<RepayDate> repayDateCache;

    @Override
    public RepayDate getById(long id) {
        String key = "RepayDate." + Long.toString(id);
        RepayDate repayDate = repayDateCache.get(key, RepayDate.class);
        if (repayDate == null) {
            repayDate = super.getById(id);
            repayDateCache.put(key, repayDate);
        }
        return repayDate;

    }

    @Override
    public long deleteById(long id) {
        String key = "RepayDate." + Long.toString(id);
        repayDateCache.remove(key);
        return super.deleteById(id);
    }

    @Override
    public long update(RepayDate repayDate) {
        String key = "RepayDate." + Long.toString(repayDate.getId());
        repayDateCache.remove(key);
        return super.update(repayDate);
    }

    @Override
    public long insert(RepayDate repayDate) {
        return super.insert(repayDate);
    }
}
