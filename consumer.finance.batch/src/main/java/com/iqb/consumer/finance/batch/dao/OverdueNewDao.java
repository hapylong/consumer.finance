package com.iqb.consumer.finance.batch.dao;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.finance.batch.bean.OverdueNewBean;
import com.iqb.consumer.finance.batch.dao.basic.BaseDao;

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
 * 2017年12月5日下午5:26:03 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface OverdueNewDao extends BaseDao<OverdueNewBean> {

    /**
     * 
     * Description:获取逾期的月供信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月5日
     */
    public List<OverdueNewBean> listOverdueMonth(Map<String, Object> paramMap);

    /**
     * 
     * Description:获取逾期的剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月5日
     */
    public List<OverdueNewBean> listOverduePincipal(Map<String, Object> paramMap);

    /**
     * 
     * Description:获取逾期的订单数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月5日
     */
    public List<OverdueNewBean> listOverdueNum(Map<String, Object> paramMap);

    /**
     * 查询MD5逾期剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverduePincipalForMD5(Map<String, Object> paramMap);

    /**
     * 查询MD5逾期月供
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueMonthForMD5(Map<String, Object> paramMap);

    /**
     * 查询MD5订单逾期订单个数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueNumForMD5(Map<String, Object> paramMap);

    /**
     * 查询MOB1逾期剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverduePincipalForMOB1(Map<String, Object> paramMap);

    /**
     * 查询MOB1逾期月供
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueMonthForMOB1(Map<String, Object> paramMap);

    /**
     * 查询Mob1订单逾期订单个数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueNumForMOB1(Map<String, Object> paramMap);

    /**
     * 查询MOB2逾期剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverduePincipalForMOB2(Map<String, Object> paramMap);

    /**
     * 查询MOB2逾期月供
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueMonthForMOB2(Map<String, Object> paramMap);

    /**
     * 查询Mob2订单逾期订单个数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueNumForMOB2(Map<String, Object> paramMap);

    /**
     * 查询MOB3逾期剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverduePincipalForMOB3(Map<String, Object> paramMap);

    /**
     * 查询MOB3逾期月供
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueMonthForMOB3(Map<String, Object> paramMap);

    /**
     * 查询Mob3订单逾期订单个数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueNumForMOB3(Map<String, Object> paramMap);

    /**
     * 查询MOB3逾期剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverduePincipalForMOB4(Map<String, Object> paramMap);

    /**
     * 查询MOB3逾期月供
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueMonthForMOB4(Map<String, Object> paramMap);

    /**
     * 查询Mob4订单逾期订单个数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listOverdueNumForMOB4(Map<String, Object> paramMap);

    /**
     * 查询逾期指标 mob1、mob2、mob3、mob资产存量 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月31日
     */
    public List<OverdueNewBean> listStockStatisticsAmt(Map<String, Object> paramMap);

    /**
     * 
     * Description:查询账单表中的所有商户
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月21日
     */
    public List<OverdueNewBean> listMerchantNo(Map<String, Object> paramMap);
}
