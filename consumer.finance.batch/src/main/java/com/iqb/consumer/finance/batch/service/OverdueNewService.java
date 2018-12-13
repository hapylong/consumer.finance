package com.iqb.consumer.finance.batch.service;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.finance.batch.bean.OverdueNewBean;

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
 * 2017年12月5日下午5:26:47 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface OverdueNewService {
    /**
     * 
     * Description:获取逾期的账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月5日
     */
    public List<OverdueNewBean> listOverdueMonth(Map<String, Object> paramMap);

    /**
     * 查询逾期指标 md5、mob1、mob2、mob3、mob资产存量
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月31日
     */
    public List<OverdueNewBean> listStockStatisticsAmt(Map<String, Object> paramMap);

    /**
     * 
     * Description:合并MD5 MOB1-MOB4数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月19日
     */
    public long mergeList(Map<String, Object> paramMap);
}
