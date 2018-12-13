/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月31日 下午1:35:50
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iqb.consumer.data.layer.mysql.dao.ConsumerInfoDao;
import com.iqb.consumer.data.layer.mysql.domain.ConsumerInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class TestImpl implements ITest {

    @Resource
    private ConsumerInfoDao consumerInfoDao;

    @Override
    public int addConsumer(ConsumerInfo consumer) {

        return 0;
    }

}
