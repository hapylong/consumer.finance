/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月9日 下午2:28:38
 * @version V1.0
 */
package com.iqb.consumer.service.inst;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentPlan;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface IInstPlanService {

    /**
     * 通过计划ID查询计划
     * 
     * @param planId
     * @return
     */
    InstallmentPlan getByPlanId(long planId);

    /**
     * 
     * Description: 根据id删除
     * 
     * @param
     * @return long
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:23:12
     */
    public long deleteById(JSONObject objs);

    /**
     * 
     * Description: update
     * 
     * @param
     * @return long
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:23:21
     */
    public long updateById(JSONObject objs);

    /**
     * 
     * Description: 插入
     * 
     * @param
     * @return long
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:23:37
     */
    public long insert(JSONObject objs);
}
