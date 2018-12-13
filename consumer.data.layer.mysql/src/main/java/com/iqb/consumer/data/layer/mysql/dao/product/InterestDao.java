/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:48:15
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.product;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.domain.product.Interest;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
public interface InterestDao extends BaseDao<Interest> {

    /**
     * @description 通过id查询利息计算方式
     * @param id
     * @return Interest
     */
    Interest getInterestById(String id);

    /**
     * @description 插入利息计算信息
     * @param Interest
     * @return 0 1
     */
    long insertInterest(Interest interest);

}
