/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月24日 下午8:25:06
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.repository;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public abstract class AbstractRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRepositoryTest.class);

    @Before
    public void setUp() {}

}
