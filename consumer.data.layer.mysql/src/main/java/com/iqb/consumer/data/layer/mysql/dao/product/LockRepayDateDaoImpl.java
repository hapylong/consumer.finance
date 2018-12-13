package com.iqb.consumer.data.layer.mysql.dao.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.product.LockRepayDate;
import com.iqb.consumer.finance.cache.kv.KVCache;

@Repository
public class LockRepayDateDaoImpl extends BaseDaoImpl<LockRepayDate> implements LockRepayDateDao {

    @Resource
    private KVCache<LockRepayDate> lockRepayDateCache;

    @Override
    public LockRepayDate getById(long id) {
        String key = "LockRepayDate." + Long.toString(id);
        LockRepayDate lockRepayDate = lockRepayDateCache.get(key, LockRepayDate.class);
        if (lockRepayDate == null) {
            lockRepayDate = super.getById(id);
            lockRepayDateCache.put(key, lockRepayDate);
        }
        return lockRepayDate;
    }

    @Override
    public long deleteById(long id) {
        String key = "LockRepayDate." + Long.toString(id);
        lockRepayDateCache.remove(key);
        return super.deleteById(id);
    }

    @Override
    public long update(LockRepayDate lockRepayDate) {
        String key = "LockRepayDate." + Long.toString(lockRepayDate.getId());
        lockRepayDateCache.remove(key);
        return super.update(lockRepayDate);
    }

    @Override
    public long insert(LockRepayDate lockRepayDate) {
        return super.insert(lockRepayDate);
    }

}
