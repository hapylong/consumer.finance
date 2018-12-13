package com.iqb.consumer.service.product;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Description: 账单日相关服务
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月7日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public interface IBillDateService {

    /**
     * 
     * Description: 获取账单日
     * 
     * @param 分期日期
     * @param 分期账单id
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午5:55:06
     */
    public String getBillDate(String date, long bid);

    /**
     * 
     * Description: 获取账单日
     * 
     * @param 上期账单日期
     * @param 分期日期
     * @param 分期账单id
     * @param 日期类型
     * @param 动态账单日天数
     * @param 账单周期
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午5:56:26
     */
    public String getBillDate(String curBillDate, String date, long bid, String format, String days, Integer cycle);

    /**
     * 
     * Description: 获取账单日
     * 
     * @param 首次分期日期
     * @param 首次分期账单日id
     * @param 第二次分期日期
     * @param 第二次分期账单日id
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 下午2:24:43
     */
    public String getBillDate(String date, long bid, String sDate, long Sbid);

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
