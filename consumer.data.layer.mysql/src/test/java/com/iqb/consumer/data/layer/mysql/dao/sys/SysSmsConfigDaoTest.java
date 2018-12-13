/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 下午3:06:17
 * @version V1.0
 */
package com.iqb.consumer.data.layer.mysql.dao.sys;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.bean.sys.SysSmsConfig;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class SysSmsConfigDaoTest extends AbstractRepositoryTest {

    @Resource
    private SysSmsConfigDao sysSmsConfigDao;

    @Test
    public void testQueryChannel() {
        SysSmsConfig sc = sysSmsConfigDao.getSmsChannelByOpenId("10102");
        System.out.println(sc.toString());
    }
}
