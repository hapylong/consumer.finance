/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月25日 下午5:12:08
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import java.util.Date;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.bean.overdue.DelayPayment;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface DelayPaymentDao extends BaseDao<DelayPayment> {

    /**
     * 
     * Description: 获取逾期信息
     * 
     * @param
     * @return DelayPayment
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 下午6:05:33
     */
    public DelayPayment getDelayPayment(String billId, Date delaydate);

}
