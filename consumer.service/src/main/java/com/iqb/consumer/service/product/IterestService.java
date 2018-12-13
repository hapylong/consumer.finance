/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:29:42
 * @version V1.0
 */

package com.iqb.consumer.service.product;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
public interface IterestService {

    /**
     * @description 计算利息
     * @param BigDecimal amt 金额,String id 利息计算id, Date date 日期
     * @return BigDecimal result 利息
     */
    BigDecimal calculateInterest(BigDecimal amt, String id, Date date);

    /**
     * @description 计算天利息
     * @param BigDecimal amt 金额,String id 利息计算id, Date date 日期
     * @return BigDecimal result 利息
     */
    BigDecimal calculateInterestForDay(BigDecimal amt, String id, Date date);

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
