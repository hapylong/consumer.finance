package com.iqb.consumer.finance.batch.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.OverdueNewBean;
import com.iqb.consumer.finance.batch.dao.OverdueNewDao;

/**
 * Description:逾期报表第二版
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年12月5日下午5:27:02 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class OverdueNewServiceImpl implements OverdueNewService {
    protected static final Logger logger = LoggerFactory.getLogger(InstBillInfoServiceImpl.class);
    @Resource
    private OverdueNewDao overdueNewDao;

    /**
     * 获取资产逾期数据
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月5日
     */
    @Override
    public List<OverdueNewBean> listOverdueMonth(Map<String, Object> paramMap) {
        return overdueNewDao.listOverdueMonth(paramMap);
    }

    /**
     * 查询逾期指标 md5、mob1、mob2、mob3、mob资产存量
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月5日
     */
    @Override
    public List<OverdueNewBean> listStockStatisticsAmt(Map<String, Object> paramMap) {
        return overdueNewDao.listStockStatisticsAmt(paramMap);
    }

    /**
     * 
     * Description:合并MD5 MOB1-MOB4数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月19日
     */
    public long mergeList(Map<String, Object> paramMap) {
        logger.info("----------开始查询逾期指标数据----------", JSONObject.toJSON(paramMap));
        long result = 0;

        try {
            /**
             * 1.获取账单表中所有商户 2.根据商户依次获取门店逾期剩余本金、门店逾期月供、门店逾期订单个数
             * 3.根据商户依次获取门店MD5逾期剩余本金、MD5门店逾期月供、MD5门店逾期订单个数
             * 4.根据商户依次获取门店MOB1逾期剩余本金、MOB1门店逾期月供、MOB1门店逾期订单个数
             * 5.根据商户依次获取门店MOB2逾期剩余本金、MOB2门店逾期月供、MOB2门店逾期订单个数
             * 6.根据商户依次获取门店MOB3逾期剩余本金、MOB3门店逾期月供、MOB3门店逾期订单个数
             * 7.根据商户依次获取门店MOB4逾期剩余本金、MOB4门店逾期月供、MOB4门店逾期订单个数
             */
            List<OverdueNewBean> listMerchant = overdueNewDao.listMerchantNo(paramMap);

            /**
             * 分批次将MD5 MOB1-MOB4数据更新到逾期报表中
             */
            if (!CollectionUtils.isEmpty(listMerchant)) {
                result += overdueNewDao.insert(listMerchant);
            }
            if (!CollectionUtils.isEmpty(listMerchant)) {
                for (OverdueNewBean bean : listMerchant) {
                    paramMap.put("merchantNo", bean.getMerchantNo());
                    /**
                     * 更新逾期月供
                     */
                    List<OverdueNewBean> listMonth = overdueNewDao.listOverdueMonth(paramMap);
                    if (!CollectionUtils.isEmpty(listMonth)) {
                        bean.setOverdueMonth(listMonth.get(0).getOverdueMonth());
                    }
                    /**
                     * 更新逾期剩余本金
                     */
                    List<OverdueNewBean> listPincipal = overdueNewDao.listOverduePincipal(paramMap);
                    if (!CollectionUtils.isEmpty(listPincipal)) {
                        bean.setOverduePincipal(listPincipal.get(0).getOverduePincipal());
                    }
                    /**
                     * 更新门店逾期订单数
                     */
                    List<OverdueNewBean> listNum = overdueNewDao.listOverdueNum(paramMap);
                    if (!CollectionUtils.isEmpty(listNum)) {
                        bean.setOverdueNum(listNum.get(0).getOverdueNum());
                    }

                    /**
                     * 更新MD5门店逾期剩余本金
                     */
                    List<OverdueNewBean> md5PincipalList = overdueNewDao.listOverduePincipalForMD5(paramMap);
                    if (!CollectionUtils.isEmpty(md5PincipalList)) {
                        bean.setOverduePincipalMD5(md5PincipalList.get(0).getOverduePincipalMD5());
                    }
                    /**
                     * 更新MD5门店逾期月供
                     */
                    List<OverdueNewBean> md5MonthList = overdueNewDao.listOverdueMonthForMD5(paramMap);
                    if (!CollectionUtils.isEmpty(md5PincipalList)) {
                        bean.setOverdueMonthMD5(md5MonthList.get(0).getOverdueMonthMD5());
                    }
                    /**
                     * 更新MD5门店逾期订单个数
                     */
                    List<OverdueNewBean> md5NumList = overdueNewDao.listOverdueNumForMD5(paramMap);
                    if (!CollectionUtils.isEmpty(md5NumList)) {
                        bean.setOverdueNumMD5(md5NumList.get(0).getOverdueNumMD5());
                    }

                    /**
                     * 更新MOB1门店逾期剩余本金
                     */
                    List<OverdueNewBean> mob1PincipalList = overdueNewDao.listOverduePincipalForMOB1(paramMap);
                    if (!CollectionUtils.isEmpty(mob1PincipalList)) {
                        bean.setOverduePincipalMB1(mob1PincipalList.get(0).getOverduePincipalMB1());
                    }
                    /**
                     * 更新MOB1门店逾期月供
                     */
                    List<OverdueNewBean> mob1MonthList = overdueNewDao.listOverdueMonthForMOB1(paramMap);
                    if (!CollectionUtils.isEmpty(md5PincipalList)) {
                        bean.setOverdueMonthMB1(mob1MonthList.get(0).getOverdueMonthMB1());
                    }
                    /**
                     * 更新MOB1门店逾期订单个数
                     */
                    List<OverdueNewBean> mob1NumList = overdueNewDao.listOverdueNumForMOB1(paramMap);
                    if (!CollectionUtils.isEmpty(md5NumList)) {
                        bean.setOverdueNumMB1(mob1NumList.get(0).getOverdueNumMB1());
                    }

                    /**
                     * 更新MOB2门店逾期剩余本金
                     */
                    List<OverdueNewBean> mob2PincipalList = overdueNewDao.listOverduePincipalForMOB2(paramMap);
                    if (!CollectionUtils.isEmpty(mob2PincipalList)) {
                        bean.setOverduePincipalMB2(mob2PincipalList.get(0).getOverduePincipalMB2());
                    }
                    /**
                     * 更新MOB2门店逾期月供
                     */
                    List<OverdueNewBean> mob2MonthList = overdueNewDao.listOverdueMonthForMOB2(paramMap);
                    if (!CollectionUtils.isEmpty(mob2MonthList)) {
                        bean.setOverdueMonthMB2(mob2MonthList.get(0).getOverdueMonthMB2());
                    }
                    /**
                     * 更新MOB2门店逾期订单个数
                     */
                    List<OverdueNewBean> mob2NumList = overdueNewDao.listOverdueNumForMOB2(paramMap);
                    if (!CollectionUtils.isEmpty(mob2NumList)) {
                        bean.setOverdueNumMB2(mob2NumList.get(0).getOverdueNumMB2());
                    }

                    /**
                     * 更新MOB3门店逾期剩余本金
                     */
                    List<OverdueNewBean> mob3PincipalList = overdueNewDao.listOverduePincipalForMOB3(paramMap);
                    if (!CollectionUtils.isEmpty(mob3PincipalList)) {
                        bean.setOverduePincipalMB3(mob3PincipalList.get(0).getOverduePincipalMB3());
                    }
                    /**
                     * 更新MOB3门店逾期月供
                     */
                    List<OverdueNewBean> mob3MonthList = overdueNewDao.listOverdueMonthForMOB3(paramMap);
                    if (!CollectionUtils.isEmpty(mob3MonthList)) {
                        bean.setOverdueMonthMB3(mob3MonthList.get(0).getOverdueMonthMB3());
                    }
                    /**
                     * 更新MOB3门店逾期订单个数
                     */
                    List<OverdueNewBean> mob3NumList = overdueNewDao.listOverdueNumForMOB3(paramMap);
                    if (!CollectionUtils.isEmpty(mob3NumList)) {
                        bean.setOverdueNumMB3(mob3NumList.get(0).getOverdueNumMB3());
                    }

                    /**
                     * 更新MOB4门店逾期剩余本金
                     */
                    List<OverdueNewBean> mob4PincipalList = overdueNewDao.listOverduePincipalForMOB4(paramMap);
                    if (!CollectionUtils.isEmpty(mob4PincipalList)) {
                        bean.setOverduePincipalMB4(mob4PincipalList.get(0).getOverduePincipalMB4());
                    }
                    /**
                     * 更新MOB4门店逾期月供
                     */
                    List<OverdueNewBean> mob4MonthList = overdueNewDao.listOverdueMonthForMOB4(paramMap);
                    if (!CollectionUtils.isEmpty(mob4MonthList)) {
                        bean.setOverdueMonthMB4(mob4MonthList.get(0).getOverdueMonthMB4());
                    }
                    /**
                     * 更新MOB4门店逾期订单个数
                     */
                    List<OverdueNewBean> mob4NumList = overdueNewDao.listOverdueNumForMOB4(paramMap);
                    if (!CollectionUtils.isEmpty(mob4NumList)) {
                        bean.setOverdueNumMB4(mob4NumList.get(0).getOverdueNumMB4());
                    }
                    /** 资产存量 **/
                    List<OverdueNewBean> staticList = overdueNewDao.listStockStatisticsAmt(paramMap);
                    if (!CollectionUtils.isEmpty(staticList)) {
                        bean.setStockAmt(staticList.get(0).getStockAmt());
                        bean.setStockAmtMD5(staticList.get(0).getStockAmtMD5());
                        bean.setStockAmtMB1(staticList.get(0).getStockAmtMB1());
                        bean.setStockAmtMB2(staticList.get(0).getStockAmtMB2());
                        bean.setStockAmtMB3(staticList.get(0).getStockAmtMB3());
                        bean.setStockAmtMB4(staticList.get(0).getStockAmtMB4());
                        bean.setOverdueStockNum(staticList.get(0).getOverdueStockNum());
                    } else {
                        bean.setOverdueStockNum("0");
                    }
                    overdueNewDao.update(bean);
                }
            }
        } catch (Exception e) {
            logger.error("每日资产逾期第二版数据保存报错---{}", e);
        }
        return result;
    }
}
