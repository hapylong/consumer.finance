/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 上午10:40:57
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentPlan;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface InstallmentPlanDao extends BaseDao<InstallmentPlan> {

    /**
     * 通过ID查询计划
     * 
     * @param planId
     * @return
     */
    InstallmentPlan getByPlanId(long planId);
}
