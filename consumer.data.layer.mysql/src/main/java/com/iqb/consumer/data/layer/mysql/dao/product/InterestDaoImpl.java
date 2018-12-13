/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:48:28
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.domain.product.Interest;
import com.iqb.consumer.finance.cache.kv.KVCache;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
@Repository
public class InterestDaoImpl extends BaseDaoImpl<Interest> implements InterestDao {

    @Resource
    private KVCache<Interest> interestCache;

    @Override
    public long insertInterest(Interest interest) {
        return super.insert(interest);
    }

    @Override
    public Interest getInterestById(String id) {
        String key = "Interest." + id;
        Interest interest = interestCache.get(key, Interest.class);
        if (interest == null) {
            interest = (Interest) super.selectOne(getStatement("getInterestById"), id);
            interestCache.put(key, interest);
        }
        return interest;
    }

    @Override
    public long deleteById(long id) {
        String key = "Interest." + Long.toString(id);
        interestCache.remove(key);
        return super.deleteById(id);
    }

    @Override
    public long update(Interest interest) {
        String key = "Interest." + Long.toString(interest.getId());
        interestCache.remove(key);
        return super.update(interest);
    }

}
