/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月17日 下午6:43:23
 * @version V1.0
 */
package com.iqb.consumer.data.layer.mysql.dao.acc;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccRepayHistory;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class AccRepayHistoryDaoImpl extends BaseDaoImpl<AccRepayHistory> implements AccRepayHistoryDao {

    @Override
    public double getRepayAmtByOrderId(String orderId) {
        Object selectOne = super.selectOne(getStatement("getRepayAmtByOrderId"), orderId);
        return (double) (selectOne == null ? 0d : selectOne);
    }

}
