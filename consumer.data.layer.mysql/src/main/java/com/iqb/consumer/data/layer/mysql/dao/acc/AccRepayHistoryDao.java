/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月17日 下午6:37:24
 * @version V1.0
 */
package com.iqb.consumer.data.layer.mysql.dao.acc;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccRepayHistory;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface AccRepayHistoryDao extends BaseDao<AccRepayHistory> {

    /**
     * 查询订单已还金额
     * 
     * @param orderId
     * @return
     */
    double getRepayAmtByOrderId(String orderId);

}
