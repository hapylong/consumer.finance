/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 下午2:02:54
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.dao.product.InterestDao;
import com.iqb.consumer.data.layer.mysql.domain.product.Interest;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
public class InterestDaoTest extends AbstractRepositoryTest {

    @Resource
    private InterestDao interestDao;

    @Test
    public void getInterestById() {
        String id = "1";
        Interest interest = interestDao.getInterestById(id);
        System.out.println(interest.getRemark());
    }
}
