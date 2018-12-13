/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:48:15
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.dao;

import java.util.List;

import com.iqb.consumer.finance.batch.bean.OverdueInterest;
import com.iqb.consumer.finance.batch.bean.OverdueRule;
import com.iqb.consumer.finance.batch.dao.basic.BaseDao;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
public interface OverdueInterestDao extends BaseDao<OverdueInterest> {

    /**
     * @description 通过id查询逾期计算方式
     * @param id
     * @return OverdueInterest
     */
    OverdueInterest getOverdueInterestById(String id);

    /**
     * 通过逾期ID查询所有逾期规则明细
     * 
     * @param id
     * @return
     */
    List<OverdueRule> getOverdueRuleSById(String id);

    /**
     * 
     * Description:根据订单获取产品方案是否上收月供
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月30日
     */
    int getTakePayment(String orderId);
}
