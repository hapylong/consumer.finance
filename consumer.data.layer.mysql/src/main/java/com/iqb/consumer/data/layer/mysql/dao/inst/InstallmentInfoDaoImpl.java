/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:45:37
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.finance.cache.kv.KVCache;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class InstallmentInfoDaoImpl extends BaseDaoImpl<InstallmentInfo> implements InstallmentInfoDao {

    @Resource
    private KVCache<InstallmentInfo> instPlanCache;

    @Override
    public long insertInstalInfo(InstallmentInfo installmentInfo) {
        long instId = super.insert(installmentInfo);
        String key = "instInfo." + instId;
        instPlanCache.put(key, installmentInfo);
        return instId;
    }

    @Override
    public List<InstallmentInfo> getInstInfoByParams(Map<String, Object> params) {
        return super.listBy(params);
    }

    @Override
    public InstallmentInfo getInstInfoByOrderId(String orderId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId);
        return (InstallmentInfo) super.getBy(paramMap, "getInstInfoByOrderId");
    }

    @Override
    public long stopInstallmentInfo(InstallmentInfo installmentInfo) {
        return super.getSqlSession().update(getStatement("stopInstallmentInfo"), installmentInfo);
    }

    @Override
    public InstallmentInfo getLimitInstInfoByOrderId(String orderId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId);
        return (InstallmentInfo) super.getBy(paramMap, "getLimitInstInfoByOrderId");
    }

    /**
     * 转租--根据订单号修改Regid baseId
     * 
     * @param orderId
     * @return
     */
    @Override
    public long updateInstallmentInfoByOrderId(InstallmentInfo installmentInfo) {
        return super.getSqlSession().update(getStatement("updateInstallmentInfoByOrderId"), installmentInfo);
    }

    /**
     * 根据regId openId orderId修改订单信息表
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月5日
     */
    @Override
    public long updateInstallmentInfoByCondition(InstallmentInfo installmentInfo) {
        return super.getSqlSession().update(getStatement("updateInstallmentInfoByCondition"), installmentInfo);
    }

}
