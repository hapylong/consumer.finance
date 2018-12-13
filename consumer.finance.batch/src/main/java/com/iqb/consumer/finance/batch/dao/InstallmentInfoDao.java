/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:45:03
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.dao;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.finance.batch.bean.InstallmentInfo;
import com.iqb.consumer.finance.batch.dao.basic.BaseDao;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface InstallmentInfoDao extends BaseDao<InstallmentInfo> {
    /**
     * @description 保存分期信息
     * @param installmentInfo
     * @return 返回主键
     */
    long insertInstalInfo(InstallmentInfo installmentInfo);

    /**
     * 查询行业下订单ID信息
     * 
     * @param orderId
     * @param openId
     * @return
     */
    List<InstallmentInfo> getInstInfoByParams(Map<String, Object> params);

    /**
     * 通过订单ID查询分期信息
     * 
     * @param orderId
     * @return
     */
    InstallmentInfo getInstInfoByOrderId(String orderId);

    /**
     * 
     * Description: 终止分期信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年11月16日 上午10:50:10
     */
    public long stopInstallmentInfo(InstallmentInfo installmentInfo);

    /**
     * 
     * Description: 根据订单id获取订单信息
     * 
     * @param
     * @return InstallmentInfo
     * @throws
     * @Author wangxinbang Create Date: 2016年11月16日 上午11:47:50
     */
    public InstallmentInfo getLimitInstInfoByOrderId(String orderId);
}
