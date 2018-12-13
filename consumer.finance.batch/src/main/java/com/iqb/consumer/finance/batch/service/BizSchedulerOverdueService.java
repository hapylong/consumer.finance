package com.iqb.consumer.finance.batch.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Description: 逾期调度服务接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月11日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public interface BizSchedulerOverdueService {

    /**
     * 
     * Description: 逾期费用计算
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月11日 下午6:11:15
     */
    public void calOverdueInterest(String orderId, String instBillId, String startDate, String endDate);

    /**
     * 
     * Description: 逾期费用计算
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月11日 下午6:11:15
     */
    public void calOverdueInterest(JSONObject objs);

    /**
     * 
     * Description: 逾期费用计算非复利测试
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月16日 下午3:41:55
     */
    public void calOverdueInterest4Test(JSONObject objs);

}
