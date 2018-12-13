package com.iqb.consumer.finance.batch.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年10月19日下午3:15:12 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface ConcurrentBizSchedulerOverdueService {
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
}
