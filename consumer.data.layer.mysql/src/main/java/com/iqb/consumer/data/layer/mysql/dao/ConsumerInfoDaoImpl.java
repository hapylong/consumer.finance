/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月7日 下午5:50:03
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.domain.ConsumerInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class ConsumerInfoDaoImpl extends BaseDaoImpl<ConsumerInfo> implements ConsumerInfoDao {

    @Override
    public long insert(ConsumerInfo t) {
        return super.insert(t);
    }

}
