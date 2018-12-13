/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 上午10:44:50
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.InstallmentBillInfo;
import com.iqb.consumer.finance.batch.bean.ShouldDebtDetail;
import com.iqb.consumer.finance.batch.bean.StockStatisticsBean;
import com.iqb.consumer.finance.batch.bean.ToRiskBillInfo;
import com.iqb.consumer.finance.batch.dao.basic.BaseDaoImpl;
import com.iqb.consumer.finance.batch.page.PageBean;
import com.iqb.consumer.finance.batch.page.PageParam;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class InstallmentBillInfoDaoImpl extends BaseDaoImpl<InstallmentBillInfo> implements InstallmentBillInfoDao {

    @Override
    public InstallmentBillInfo getLastInstallmentBillInfo(String orderID, long installID, int repayNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderID);
        params.put("installId", installID);
        params.put("repayNo", repayNo);
        return (InstallmentBillInfo) super.getBy(params, "getLastInstallmentBillInfo");
    }

    public Map<String, List<InstallmentBillInfo>> listCurrBill(String regId, String openId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("regId", regId);
        params.put("status", 1);
        params.put("openId", openId);
        List<InstallmentBillInfo> allList = super.getSqlSession().selectList("listCurrBill", params);// 所有未还账单
        // 定义一个map集合用于分组
        Map<String, List<InstallmentBillInfo>> mapList = new HashMap<String, List<InstallmentBillInfo>>();
        // 返回的处理好的集合对象
        Iterator<InstallmentBillInfo> it = allList.iterator();
        while (it.hasNext()) {
            InstallmentBillInfo billInfo = it.next();
            if (mapList.containsKey(billInfo.getOrderId())) {// 如果在这个map中包含有相同的键,这创建一个集合将其存起来
                List<InstallmentBillInfo> thisList = mapList.get(billInfo.getOrderId());
                if (thisList.size() < 3) {
                    thisList.add(billInfo);
                }
            } else {// 如果没有包含相同的键，在创建一个集合保存数据
                List<InstallmentBillInfo> newbills = new ArrayList<InstallmentBillInfo>();
                if (newbills.size() < 3) {// 只存三条
                    newbills.add(billInfo);
                }
                mapList.put(billInfo.getOrderId(), newbills);
            }
        }

        logger.debug("mapList:" + mapList);

        return mapList;
    }

    @Override
    public PageBean listOverdueInterestPage(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "listOverdueInterestPage");
    }

    @Override
    public PageBean listOverdueInterestPageRepayNoDesc(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "listOverdueInterestPage");
    }

    @Override
    public long update(InstallmentBillInfo installmentBillInfo) {
        return super.update(installmentBillInfo);
    }

    @Override
    public long stopInstallmentBillInfo(InstallmentBillInfo installmentBillInfo) {
        return super.getSqlSession().update(getStatement("stopInstallmentBillInfo"), installmentBillInfo);
    }

    @Override
    public int getRepayNo(String orderId) {
        Object count = super.getSqlSession().selectOne(getStatement("getRepayNo"), orderId);
        return Integer.parseInt(count == null ? "0" : count.toString());
    }

    @Override
    public int getRepayNoByOrderId(String orderId, String today, String nextDay) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        params.put("today", today);
        params.put("nextDay", nextDay);
        Object count = super.getSqlSession().selectOne(getStatement("getRepayNoByOrderId"), params);
        return Integer.parseInt(count == null ? "0" : count.toString());
    }

    @Override
    public BigDecimal getSAmtByOrderIdAndRepayNo(Map<String, Object> params) {
        return (BigDecimal) super.getBy(params, "getSAmtByOrderIdAndRepayNo");
    }

    @Override
    public InstallmentBillInfo getInstBillByOrderIdAndRepayNo(Map<String, Object> params) {
        return (InstallmentBillInfo) super.getBy(params, "getInstBillByOrderIdAndRepayNo");
    }

    @Override
    public List<InstallmentBillInfo> getAllInstallmentBillInfoListForOrderId(String orderId) {
        return super.getSqlSession().selectList(getStatement("getAllInstallmentBillInfoListForOrderId"), orderId);
    }

    @Override
    public long updateForNormalRepayment(List<InstallmentBillInfo> installmentBillInfoList) {
        long result = 0;
        for (InstallmentBillInfo installmentBillInfo : installmentBillInfoList) {
            result += super.getSessionTemplate().update(getStatement("updateForNormalRepayment"), installmentBillInfo);
        }
        return result;
    }

    @Override
    public long updateForAdvanceRepayment(InstallmentBillInfo installmentBillInfo) {
        return super.getSqlSession().update(getStatement("updateForAdvanceRepayment"), installmentBillInfo);
    }

    @Override
    public long updateForAdvanceRepaymentById(InstallmentBillInfo installmentBillInfo) {
        return super.getSqlSession().update(getStatement("updateForAdvanceRepaymentById"), installmentBillInfo);
    }

    @Override
    public long checkIntertemporal(String orderId, String repayNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        params.put("repayNo", repayNo);
        Object count = super.getSqlSession().selectOne(getStatement("checkIntertemporal"), params);
        return Long.parseLong(count == null ? "0" : count.toString());
    }

    @Override
    public List<InstallmentBillInfo> queryBillByParams(Map<String, Object> params) {

        return null;
    }

    @Override
    public double getRealRepayAmtByOrderId(String orderId) {
        Object selectOne = super.getSqlSession().selectOne(getStatement("getRealRepayAmtByOrderId"), orderId);
        return (double) (selectOne == null ? 0d : selectOne);
    }

    @Override
    public List<InstallmentBillInfo> getRepayRemindBillList(String wechatNo, String merchantNo, Date repayDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("wechatNo", wechatNo);
        params.put("merchantNo", merchantNo);
        params.put("repayDate", repayDate);
        return super.getSqlSession().selectList(getStatement("getRepayRemindBillList"), params);
    }

    @Override
    public List<InstallmentBillInfo> getOverdueBillListByDay(Integer overdueDay) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("overdueDay", overdueDay);
        return super.getSqlSession().selectList(
                getStatement("getOverdueBillListByDay"), params);
    }

    @Override
    public List<ToRiskBillInfo> getToRiskBillInfo(Map<String, Object> params) {
        return super.getSqlSession().selectList(getStatement("getToRiskBillInfo"), params);
    }

    @Override
    public PageBean selectShouldDebtDetail(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "selectShouldDebtDetail");
    }

    @Override
    public BigDecimal getTotalRepayAmt(Map<String, Object> paramMap) {
        BigDecimal result = (BigDecimal) super.getSqlSession().selectOne(getStatement("getTotalRepayAmt"), paramMap);
        return result;
    }

    @Override
    public List<ShouldDebtDetail> selectShouldDebtDetail(Map<String, Object> paramMap) {
        return super.getSqlSession().selectList(getStatement("selectShouldDebtDetail"), paramMap);
    }

    @Override
    public List<StockStatisticsBean> listStockStatistics(Map<String, Object> paramMap) {
        return super.getSqlSession().selectList(getStatement("listStockStatistics"), paramMap);
    }

    @Override
    public PageBean listStockStatisticsPage(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "listStockStatistics");
    }

    @Override
    public long deratePenalty(JSONObject params) {
        return super.getSqlSession().update(getStatement("deratePenalty"), params);
    }

    @Override
    public InstallmentBillInfo getRemainPriAndInterest(String orderID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderID);
        return (InstallmentBillInfo) super.getBy(params, "getRemainPriAndInterest");
    }
}
