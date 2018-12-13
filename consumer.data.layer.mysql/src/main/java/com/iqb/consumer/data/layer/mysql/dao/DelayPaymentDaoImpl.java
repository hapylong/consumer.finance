/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月25日 下午5:12:21
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.overdue.DelayPayment;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class DelayPaymentDaoImpl extends BaseDaoImpl<DelayPayment> implements DelayPaymentDao {

    @Override
    public long insert(DelayPayment delayPayment) {
        return super.insert(delayPayment);
    }

    @Override
    public DelayPayment getDelayPayment(String billId, Date delaydate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billId", billId);
        params.put("delayDate", delaydate);
        return (DelayPayment) super.selectOne(getStatement("getDelayPayment"), params);
    }

}
