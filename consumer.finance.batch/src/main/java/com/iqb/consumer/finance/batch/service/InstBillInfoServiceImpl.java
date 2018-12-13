package com.iqb.consumer.finance.batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.InstBillInfoBean;
import com.iqb.consumer.finance.batch.dao.InstBillInfoDao;

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
 * 2017年10月18日下午2:29:13 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class InstBillInfoServiceImpl implements InstBillInfoService {
    protected static final Logger logger = LoggerFactory.getLogger(InstBillInfoServiceImpl.class);
    @Resource
    private InstBillInfoDao instBillInfoDao;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月18日
     */
    @Override
    public List<InstBillInfoBean> listOverdueForMD5(Map<String, Object> paramMap) {
        return instBillInfoDao.listOverdueForMD5(paramMap);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月18日
     */
    @Override
    public List<InstBillInfoBean> listOverdueForMOB1(Map<String, Object> paramMap) {
        return instBillInfoDao.listOverdueForMOB1(paramMap);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月18日
     */
    @Override
    public List<InstBillInfoBean> listOverdueForMOB2(Map<String, Object> paramMap) {
        return instBillInfoDao.listOverdueForMOB2(paramMap);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月18日
     */
    @Override
    public List<InstBillInfoBean> listOverdueForMOB3(Map<String, Object> paramMap) {
        return instBillInfoDao.listOverdueForMOB3(paramMap);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月18日
     */
    @Override
    public List<InstBillInfoBean> listOverdueForMOB4(Map<String, Object> paramMap) {
        return instBillInfoDao.listOverdueForMOB4(paramMap);
    }

    /**
     * 合并MD5 MOB1-MOB4数据
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年10月19日
     */
    @Override
    public long mergeList(Map<String, Object> paramMap) {
        List<InstBillInfoBean> resultList = new ArrayList<>();

        logger.debug("----------开始查询逾期指标数据----------", JSONObject.toJSON(paramMap));

        List<InstBillInfoBean> md5List = instBillInfoDao.listOverdueForMD5(paramMap);
        List<InstBillInfoBean> mob1List = instBillInfoDao.listOverdueForMOB1(paramMap);
        List<InstBillInfoBean> mob2List = instBillInfoDao.listOverdueForMOB2(paramMap);
        List<InstBillInfoBean> mob3List = instBillInfoDao.listOverdueForMOB3(paramMap);
        List<InstBillInfoBean> mob4List = instBillInfoDao.listOverdueForMOB4(paramMap);

        long result = 0;
        if (!CollectionUtils.isEmpty(md5List)) {
            for (InstBillInfoBean bean : md5List) {
                paramMap.put("merchantNo", bean.getMerchantNo());
                List<InstBillInfoBean> instBillInfoBeanList = instBillInfoDao.listAllMerchantnoByParams(paramMap);
                if (!CollectionUtils.isEmpty(instBillInfoBeanList)) {
                    instBillInfoDao.update(bean);
                } else {
                    resultList.clear();
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    resultList.add(bean);
                    result += instBillInfoDao.insert(resultList);
                }
            }
        }

        if (!CollectionUtils.isEmpty(mob1List)) {
            for (InstBillInfoBean bean : mob1List) {
                paramMap.put("merchantNo", bean.getMerchantNo());
                List<InstBillInfoBean> instBillInfoBeanList = instBillInfoDao.listAllMerchantnoByParams(paramMap);
                if (!CollectionUtils.isEmpty(instBillInfoBeanList)) {
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    instBillInfoDao.update(bean);
                } else {
                    resultList.clear();
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    resultList.add(bean);
                    result += instBillInfoDao.insert(resultList);
                }
            }
        }

        if (!CollectionUtils.isEmpty(mob2List)) {
            for (InstBillInfoBean bean : mob2List) {
                paramMap.put("merchantNo", bean.getMerchantNo());
                List<InstBillInfoBean> instBillInfoBeanList = instBillInfoDao.listAllMerchantnoByParams(paramMap);
                if (!CollectionUtils.isEmpty(instBillInfoBeanList)) {
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    instBillInfoDao.update(bean);
                } else {
                    resultList.clear();
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    resultList.add(bean);
                    result += instBillInfoDao.insert(resultList);
                }
            }
        }

        if (!CollectionUtils.isEmpty(mob3List)) {
            for (InstBillInfoBean bean : mob3List) {
                paramMap.put("merchantNo", bean.getMerchantNo());
                List<InstBillInfoBean> instBillInfoBeanList = instBillInfoDao.listAllMerchantnoByParams(paramMap);
                if (!CollectionUtils.isEmpty(instBillInfoBeanList)) {
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    instBillInfoDao.update(bean);
                } else {
                    resultList.clear();
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    resultList.add(bean);
                    result += instBillInfoDao.insert(resultList);
                }
            }
        }

        if (!CollectionUtils.isEmpty(mob4List)) {
            for (InstBillInfoBean bean : mob4List) {
                paramMap.put("merchantNo", bean.getMerchantNo());
                List<InstBillInfoBean> instBillInfoBeanList = instBillInfoDao.listAllMerchantnoByParams(paramMap);
                if (!CollectionUtils.isEmpty(instBillInfoBeanList)) {
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    instBillInfoDao.update(bean);
                } else {
                    resultList.clear();
                    bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                    resultList.add(bean);
                    result += instBillInfoDao.insert(resultList);
                }
            }
        }
        /** 资产存量 **/
        paramMap.put("merchantNo", "");
        List<InstBillInfoBean> instBillInfoBeanList = instBillInfoDao.listAllMerchantnoByParams(paramMap);
        List<String> merchantList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(instBillInfoBeanList)) {
            for (InstBillInfoBean billBean : instBillInfoBeanList) {
                merchantList.add(billBean.getMerchantNo());
            }
        }

        paramMap.put("merchantNos", merchantList);
        List<InstBillInfoBean> staticList = instBillInfoDao.listStockStatisticsAmt(paramMap);
        if (!CollectionUtils.isEmpty(staticList)) {
            for (InstBillInfoBean bean : staticList) {
                bean.setSchedueDate(String.valueOf(paramMap.get("currentDate")));
                instBillInfoDao.update(bean);
            }
        }
        return result;
    }

}
