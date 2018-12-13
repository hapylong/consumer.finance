/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月16日 上午11:34:25
 * @version V1.0
 */

package com.iqb.consumer.web;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public abstract class AbstractServiceTest {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractServiceTest.class);
}
