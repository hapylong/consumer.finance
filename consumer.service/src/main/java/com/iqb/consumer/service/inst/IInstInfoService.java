/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月9日 上午11:30:53
 * @version V1.0
 */
package com.iqb.consumer.service.inst;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.service.consumer.dto.CalculateAmt;
import com.iqb.consumer.service.consumer.dto.fastBill.FastBillBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface IInstInfoService {

    /**
     * @description 保存分期信息
     * @param installmentInfo
     * @return 返回主键
     */
    long insertInstalInfo(InstallmentInfo installmentInfo);

    /**
     * 保存分期信息,仅仅针对力硕,做特殊处理
     * 
     * @param installmentInfo
     * @return
     */
    long saveInstalInfo(InstallmentInfo installmentInfo);

    /**
     * @description 保存房贷分期信息
     * @param installmentInfo
     * @return
     */
    long insertMortgageInstInfo(InstallmentInfo installmentInfo);

    /**
     * @description 分期重置
     * @param installmentInfo
     */
    void resetInstInfo(InstallmentInfo installmentInfo);

    /**
     * 计算账单信息
     * 
     * @param calculateAmt
     * @return
     */
    Map<String, BigDecimal> calculateAmt(CalculateAmt calculateAmt);

    /**
     * 查询行业下订单ID信息
     * 
     * @param orderId
     * @param openId
     * @return
     */
    List<InstallmentInfo> getInstInfoByParams(Map<String, Object> params);

    /**
     * 是否重复分期(同一笔订单，多次分期实际金额之和不能大于总分期金额)
     * 
     * @param params
     * @return
     */
    InstallmentInfo repeatInstInfo(Map<String, Object> params, InstallmentInfo installmentInfo);

    /**
     * 根据分期表生成分摊详情
     * 
     * @param installmentInfo
     * @return
     */
    Map<String, Object> genInstDetail(final InstallmentInfo installmentInfo);

    /**
     * 力硕分期表生成分摊详情
     * 
     * @param installmentInfo
     * @return
     */
    Map<String, Object> genLSInstDetail(final InstallmentInfo installmentInfo);

    /**
     * 根据分期表生成房贷分摊详情
     * 
     * @param installmentInfo
     * @return
     */
    Map<String, Object> genMortgageInstDetail(final InstallmentInfo installmentInfo);

    Map<String, Object> fastGenRepayBill(FastBillBean fbb);

    /**
     * 
     * Description: 根据regId openId orderId修改订单信息表
     * 
     * @param objs
     * @param request
     * @return
     */
    public long updateInstallmentInfoByCondition(InstallmentInfo installmentInfo);
}
