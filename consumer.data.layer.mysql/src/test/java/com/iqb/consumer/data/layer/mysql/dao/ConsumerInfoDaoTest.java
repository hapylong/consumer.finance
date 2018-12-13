/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月25日 下午4:35:58
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.domain.ConsumerInfo;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class ConsumerInfoDaoTest extends AbstractRepositoryTest {

    @Resource
    private ConsumerInfoDao consumerInfoDao;

    @Test
    public void testAddConsumer() throws InterruptedException {
        ConsumerInfo entity = new ConsumerInfo();
        entity.setRegId("123123");
        consumerInfoDao.insert(entity);
        System.out.println("----------" + consumerInfoDao);
        Thread.sleep(5000);
    }
}
