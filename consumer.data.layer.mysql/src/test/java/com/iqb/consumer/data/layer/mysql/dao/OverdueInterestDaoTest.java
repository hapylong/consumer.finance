/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 下午2:02:54
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.dao.product.OverdueInterestDao;
import com.iqb.consumer.data.layer.mysql.domain.product.OverdueInterest;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
public class OverdueInterestDaoTest extends AbstractRepositoryTest {

    @Resource
    private OverdueInterestDao overdueInterestDao;

    @Test
    public void getOverdueInterestById() {
        String id = "0";
        OverdueInterest overdueInterest = overdueInterestDao.getOverdueInterestById(id);
        System.out.println(overdueInterest.getRemark());
    }
}
