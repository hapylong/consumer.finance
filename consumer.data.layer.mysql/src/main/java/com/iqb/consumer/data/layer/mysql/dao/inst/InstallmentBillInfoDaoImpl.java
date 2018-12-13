/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 上午10:44:50
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfoNormalRepay;
import com.iqb.consumer.data.layer.mysql.bean.inst.ShouldDebtDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.StockStatisticsBean;
import com.iqb.consumer.data.layer.mysql.bean.inst.ToRiskBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.pojo.PrepaymentAnalysisPojo;
import com.iqb.consumer.data.layer.mysql.bean.request.OverDueBillPojo;
import com.iqb.consumer.data.layer.mysql.bean.request.QueryCondition;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class InstallmentBillInfoDaoImpl extends BaseDaoImpl<InstallmentBillInfo> implements InstallmentBillInfoDao {

    @Override
    public InstallmentBillInfo getLastInstallmentBillInfo(String orderID, long installID, int repayNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderID);
        params.put("installId", installID);
        params.put("repayNo", repayNo);
        return (InstallmentBillInfo) super.getBy(params, "getLastInstallmentBillInfo");
    }

    @Override
    public Map<String, List<InstallmentBillInfo>> listCurrBill(String regId, String openId) {
        Map<String, Object> params = new HashMap<>();
        params.put("regId", regId);
        params.put("status", 1);
        params.put("openId", openId);
        List<InstallmentBillInfo> allList = super.selectList("listCurrBill", params);// 所有未还账单
        // 定义一个map集合用于分组
        Map<String, List<InstallmentBillInfo>> mapList = new HashMap<>();
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
                List<InstallmentBillInfo> newbills = new ArrayList<>();
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
    public Map<String, List<InstallmentBillInfo>> listSpeCurrBill(String regId, String openId) {
        Map<String, Object> params = new HashMap<>();
        params.put("regId", regId);
        params.put("status", 1);
        params.put("openId", openId);
        List<InstallmentBillInfo> allList = super.selectList("listCurrBill", params);// 所有未还账单
        // 定义一个map集合用于分组
        Map<String, List<InstallmentBillInfo>> mapList = new HashMap<>();
        // 返回的处理好的集合对象
        Iterator<InstallmentBillInfo> it = allList.iterator();
        while (it.hasNext()) {
            InstallmentBillInfo billInfo = it.next();
            // FINANCE-2478 轮动微信账号：我要付款页面，只显示最近一期待还款记录，若有逾期，则显示第一期逾期记录 只显示一期
            if (!mapList.containsKey(billInfo.getOrderId())) {
                List<InstallmentBillInfo> newbills = new ArrayList<>();
                newbills.add(billInfo);
                mapList.put(billInfo.getOrderId(), newbills);
            }
        }

        logger.debug("mapList:" + mapList);

        return mapList;
    }

    public static void main(String[] args) {
        Map<String, Boolean> flag = new HashMap<String, Boolean>();
        System.out.println(flag.get("11"));
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
        Object count = super.selectOne(getStatement("getRepayNo"), orderId);
        return Integer.parseInt(count == null ? "0" : count.toString());
    }

    @Override
    public int getRepayNoByOrderId(String orderId, String today, String nextDay) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("today", today);
        params.put("nextDay", nextDay);
        Object count = super.selectOne(getStatement("getRepayNoByOrderId"), params);
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
        return super.selectList(getStatement("getAllInstallmentBillInfoListForOrderId"), orderId);
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
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("repayNo", repayNo);
        Object count = super.selectOne(getStatement("checkIntertemporal"), params);
        return Long.parseLong(count == null ? "0" : count.toString());
    }

    @Override
    public List<InstallmentBillInfo> queryBillByParams(Map<String, Object> params) {

        return null;
    }

    @Override
    public double getRealRepayAmtByOrderId(String orderId) {
        Object selectOne = super.selectOne(getStatement("getRealRepayAmtByOrderId"), orderId);
        return (double) (selectOne == null ? 0d : selectOne);
    }

    @Override
    public List<InstallmentBillInfo> getRepayRemindBillList(String wechatNo, String merchantNo, Date repayDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("wechatNo", wechatNo);
        params.put("merchantNo", merchantNo);
        params.put("repayDate", repayDate);
        return super.selectList(getStatement("getRepayRemindBillList"), params);
    }

    @Override
    public List<InstallmentBillInfo> getOverdueBillListByDay(Integer overdueDay) {
        Map<String, Object> params = new HashMap<>();
        params.put("overdueDay", overdueDay);
        return super.selectList(
                getStatement("getOverdueBillListByDay"), params);
    }

    @Override
    public List<ToRiskBillInfo> getToRiskBillInfo(Map<String, Object> params) {
        return super.selectList(getStatement("getToRiskBillInfo"), params);
    }

    @Override
    public PageBean selectShouldDebtDetail(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "selectShouldDebtDetail");
    }

    @Override
    public BigDecimal getTotalRepayAmt(Map<String, Object> paramMap) {
        BigDecimal result = (BigDecimal) super.selectOne(getStatement("getTotalRepayAmt"), paramMap);
        return result;
    }

    @Override
    public List<ShouldDebtDetail> selectShouldDebtDetail(Map<String, Object> paramMap) {
        return super.selectList(getStatement("selectShouldDebtDetail"), paramMap);
    }

    @Override
    public List<StockStatisticsBean> listStockStatistics(Map<String, Object> paramMap) {
        return super.selectList(getStatement("listStockStatistics"), paramMap);
    }

    @Override
    public PageBean listStockStatisticsPage(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "listStockStatistics");
    }

    @Override
    public long deratePenalty(JSONObject params) {
        return super.getSqlSession().update(getStatement("deratePenalty"), params);
    }

    /**
     * 抵押车业务查询-账单查询
     * 
     * @param params
     * @return List
     */
    @Override
    public List<ToRiskBillInfo> queryBillByParamsForMortgage(Map<String, Object> params) {
        return super.selectList(getStatement("queryBillByParamsForMortgage"), params);
    }

    @Override
    public InstallmentBillInfo getFactors(Map<String, Object> params) {
        return (InstallmentBillInfo) super.getBy(params, "getFactors");
    }

    @Override
    public InstallmentBillInfo getPayAmt(Map<String, Object> params) {
        return (InstallmentBillInfo) super.getBy(params, "getPayAmt");
    }

    /**
     * 转租
     * 
     * @param installmentBillInfo
     * @return
     */
    @Override
    public long updateForRepaymentByOrderId(InstallmentBillInfo installmentBillInfo) {
        return super.getSqlSession().update(getStatement("updateForRepaymentByOrderId"), installmentBillInfo);
    }

    @Override
    public List<InstallmentBillInfo> getAllInstallmentBillInfoByOid(String orderId) {
        return super.selectList(getStatement("getAllInstallmentBillInfoByOid"), orderId);
    }

    /**
     * 转租-根据id更新订单明细中的REPAYNO
     */
    @Override
    public long updateForRepaymentById(InstallmentBillInfo installmentBillInfo) {
        return super.getSqlSession().update(getStatement("updateForRepaymentById"), installmentBillInfo);
    }

    /**
     * 根据orderId集合获取逾期账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年5月27日
     */
    @Override
    public List<InstallmentBillInfo> getOverdueInstallmentBillInfo(Map<String, Object> params) {
        return super.selectList(getStatement("getOverdueInstallmentBillInfo"), params);
    }

    /**
     * 根据订单号获取全部未失效账单-中阁使用
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年5月31日
     */
    @Override
    public PageBean listInstallmentBillInfoPage(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "listInstallmentBillInfoPage");
    }

    /**
     * 根据orderId repayno获取账单应还总金额
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月12日
     */
    @Override
    public InstallmentBillInfo getCurRepayAmtByRepayno(Map<String, Object> params) {
        return (InstallmentBillInfo) super.getBy(params, "getCurRepayAmtByRepayno");
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月14日
     */
    @Override
    public List<InstallmentBillInfoNormalRepay> selectBillsByCondition(Map<String, Object> params) {
        return super.selectList(getStatement("selectBillsByCondition"), params);
    }

    @Override
    public OverDueBillPojo overdueBillQuery(QueryCondition qc) {
        return super.getSqlSession().selectOne(getStatement("overdueBillQuery"), qc);
    }

    @Override
    public InstallmentBillInfo getRemainPriAndInterest(String orderID) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderID);
        return (InstallmentBillInfo) super.getBy(params, "getRemainPriAndInterest");
    }

    /**
     * 存量统计查询--查询应还总额 已还本金 未还本金(分页)
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月22日
     */
    @Override
    public PageBean listStockStatisticsAmtPage(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "listStockStatisticsAmtPage");
    }

    /**
     * 存量统计查询--总期数 已还期数查询 未还期数(分页)
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月22日
     */
    @Override
    public PageBean listStockStatisticsNosPage(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "listStockStatisticsNosPage");
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月22日
     */
    @Override
    public PageBean listStockStatisticsPageNew(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "listStockStatisticsPageNew");
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月23日
     */
    @Override
    public PageBean selectEveryDayExpireBill(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "selectEveryDayExpireBill");
    }

    /**
     * 部分还款
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月27日
     */
    @Override
    public long updateForPartRepayment(List<InstallmentBillInfo> installmentBillInfoList) {
        long result = 0;
        for (InstallmentBillInfo installmentBillInfo : installmentBillInfoList) {
            result += super.getSessionTemplate().update(getStatement("updateForPartRepayment"), installmentBillInfo);
        }
        return result;
    }

    /**
     * 存量统计查询--查询应还总额 已还本金 未还本金
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月29日
     */
    @Override
    public List<StockStatisticsBean> listStockStatisticsAmt(Map<String, Object> paramMap) {
        return super.selectList(getStatement("listStockStatisticsAmt"), paramMap);
    }

    /**
     * 存量统计查询--总期数 已还期数查询 未还期数
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月29日
     */
    @Override
    public List<StockStatisticsBean> listStockStatisticsNos(Map<String, Object> paramMap) {
        return super.selectList(getStatement("listStockStatisticsNos"), paramMap);
    }

    /**
     * api标准接口使用-查询最近三期账单 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    @Override
    public Map<String, List<InstallmentBillInfo>> listCurrBill(String regId, String openId, String orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("regId", regId);
        params.put("status", 1);
        params.put("openId", openId);
        params.put("orderId", orderId);
        List<InstallmentBillInfo> allList = super.selectList("listCurrBill", params);// 所有未还账单
        // 定义一个map集合用于分组
        Map<String, List<InstallmentBillInfo>> mapList = new HashMap<>();
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
                List<InstallmentBillInfo> newbills = new ArrayList<>();
                if (newbills.size() < 3) {// 只存三条
                    newbills.add(billInfo);
                }
                mapList.put(billInfo.getOrderId(), newbills);
            }
        }

        logger.debug("mapList:" + mapList);

        return mapList;
    }

    /**
     * 修改上收月供剩余本金
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月11日
     */
    @Override
    public long updatePrincipalForBillInfo(List<InstallmentBillInfo> installmentBillInfoList) {
        long result = 0;
        for (InstallmentBillInfo installmentBillInfo : installmentBillInfoList) {
            result +=
                    super.getSessionTemplate().update(getStatement("updatePrincipalForBillInfo"), installmentBillInfo);
        }
        return result;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月14日
     */
    @Override
    public List<InstallmentBillInfo> getSubOrderNoByOrderNo(Map<String, Object> paramMap) {
        return super.selectList(getStatement("getSubOrderNoByOrderNo"), paramMap);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月14日
     */
    @Override
    public List<InstallmentBillInfo> getMorgateOrderInfoByOrderId(Map<String, Object> paramMap) {
        return super.selectList(getStatement("getMorgateOrderInfoByOrderId"), paramMap);
    }

    @Override
    public BigDecimal getAddAmt(Integer hasRepayNo, String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("hasRepayNo", hasRepayNo);
        map.put("orderId", orderId);
        return (BigDecimal) super.selectOne(getStatement("getAddAmt"), map);
    }

    @Override
    public PrepaymentAnalysisPojo getLastRepayInfoByOId(String orderId) {
        return (PrepaymentAnalysisPojo) super.selectOne(getStatement("getLastRepayInfoByOId"), orderId);
    }

    @Override
    public BigDecimal sumYhbj(Integer repayNo, String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("repayNo", repayNo);
        map.put("orderId", orderId);
        return (BigDecimal) super.selectOne(getStatement("sumYhbj"), map);
    }

    @Override
    public InstallmentBillInfo getCurrentBill(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        /** 数据库存的最迟还款日 lastrepaydate 一般都是 2017-07-28 而不是 2017-07-28 23:59:59 结合sql 按昨天日期算 **/
        map.put("yesterday", DateUtil.getYesterdayDate());
        return (InstallmentBillInfo) super.selectOne(getStatement("getCurrentBill"), map);
    }

    @Override
    public InstallmentBillInfo getLastBill(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return (InstallmentBillInfo) super.selectOne(getStatement("getLastBill"), map);
    }

    @Override
    public Map<String, Integer> finishBill(InstallmentBillInfo ibi) {
        Map<String, Integer> result = new HashMap<>();
        result.put("one", super.getSqlSession().update(getStatement("finishBillStepOne"), ibi));
        result.put("two", super.getSqlSession().update(getStatement("finishBillStepTwo"), ibi));
        result.put("three", super.getSqlSession().update(getStatement("finishBillStepThree"), ibi));
        return result;
    }

    @Override
    public BigDecimal sumYhlx(Integer repayNo, String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("repayNo", repayNo);
        map.put("orderId", orderId);
        return (BigDecimal) super.selectOne(getStatement("sumYhlx"), map);
    }

    @Override
    public boolean isTakeMonth(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return (Integer) super.selectOne(getStatement("isTakeMonth"), map) == 1;
    }

    @Override
    public BigDecimal getSjyhjeByOid(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return (BigDecimal) super.selectOne(getStatement("sjyhjeByOid"), map);
    }

    /**
     * 根据订单号获取总罚息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月25日
     */
    @Override
    public BigDecimal getTotalOverDueInterest(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return (BigDecimal) super.selectOne(getStatement("getTotalOverDueInterest"), map);
    }

    /**
     * 根据订单号获取逾期期数
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月25日
     */
    @Override
    public int getOverdueItems(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return (int) super.selectOne(getStatement("getOverdueItems"), map);
    }

    /**
     * 自动代扣-每日到期账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年2月6日
     */
    @Override
    public PageBean selectEveryDayExpireBillForAuto(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "selectEveryDayExpireBillForAuto");
    }

    /**
     * 根据订单号查询当期账单信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月8日
     */
    @Override
    public InstallmentBillInfo getAllInstallmentBillInfoByOrderId(Map<String, Object> paramMap) {
        return (InstallmentBillInfo) super.selectOne(getStatement("getAllInstallmentBillInfoByOrderId"), paramMap);
    }

    /**
     * 根据订单号查询当期以前的账单信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月8日
     */
    @Override
    public List<InstallmentBillInfo> getAllInstallmentBillInfoByOrderIdList(Map<String, Object> paramMap) {
        return super.selectList(getStatement("getAllInstallmentBillInfoByOrderIdList"), paramMap);
    }

    public List<InstallmentBillInfo> queryBillLastDateThree(Map<String, Object> paramMap) {
        return super.selectList(getStatement("queryBillLastDateThree"), paramMap);
    }

    public InstallmentBillInfo queryBillInfoByOrderIdPage(Map<String, Object> map) {
        return (InstallmentBillInfo) super.selectOne(getStatement("queryBillInfoByOrderIdPage"), map);
    }

    public List<InstallmentBillInfo> queryBillLastDateGreaterThanFive(Map<String, Object> params) {
        return super.selectList(getStatement("queryBillLastDateGreaterThanFive"), params);
    }

    /**
     * 获取距当前系统时间三天后的账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年4月27日
     */
    @Override
    public PageBean selectLatelyThreeDaysBill(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam, paramMap, "selectLatelyThreeDaysBill");
    }

    /**
     * 
     * Description:根据订单号 期数查询账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月2日
     */
    public InstallmentBillInfo getInstallmentBillInfoByOrderId(Map<String, Object> paramMap) {
        return (InstallmentBillInfo) super.selectOne(getStatement("getInstallmentBillInfoByOrderId"), paramMap);
    }

    /**
     * 
     * Description:批量修改接收短信手机号码
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月14日
     */
    public int updateInstallmentBillInfoByOrderId(InstallmentBillInfo installmentBillInfo) {
        return super.getSqlSession().update(getStatement("updateInstallmentBillInfoByOrderId"), installmentBillInfo);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月26日
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getRemainPrincipal(String orderId) {
        return (Map<String, Object>) super.selectOne(getStatement("getRemainPrincipal"), orderId);
    }

    /**
     * 
     * Description:按订单分组汇总账单信息
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月19日
     */
    @Override
    public List<Map<String, Object>> collectBillDataByOrder(Map<String, Object> paramMap) {
        return super.selectList(getStatement("collectBillDataByOrder"), paramMap);
    }

    /**
     * 根据订单号查询首次还款时间
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年8月16日
     */
    @Override
    public InstallmentBillInfo getFirstLastRepayDateByOrderId(String orderId) {
        return (InstallmentBillInfo) super.selectOne(getStatement("getFirstLastRepayDateByOrderId"), orderId);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年9月17日
     */
    @Override
    public InstallmentBillInfo getLatelyInstallmentBillInfoDesc(Map<String, Object> paramMap) {
        return (InstallmentBillInfo) super.selectOne(getStatement("getLatelyInstallmentBillInfoDesc"), paramMap);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年9月17日
     */
    @Override
    public InstallmentBillInfo getLatelyInstallmentBillInfoAsc(Map<String, Object> paramMap) {
        return (InstallmentBillInfo) super.selectOne(getStatement("getLatelyInstallmentBillInfoAsc"), paramMap);
    }

    /**
     * 根据订单号获取账单总期数 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月18日
     */
    public Integer getBillNums(String orderId) {
        return (Integer) super.selectOne(getStatement("getBillNums"), orderId);
    }
}
