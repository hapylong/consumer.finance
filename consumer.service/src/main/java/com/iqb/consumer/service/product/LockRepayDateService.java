package com.iqb.consumer.service.product;

import com.alibaba.fastjson.JSONObject;

public interface LockRepayDateService {

    /**
     * 返回锁定日期(yyyyMMdd)
     * 
     * @param date (yyyyMMdd)
     * @param id
     * @return
     */
    String getLockDays(String date, long id);

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
