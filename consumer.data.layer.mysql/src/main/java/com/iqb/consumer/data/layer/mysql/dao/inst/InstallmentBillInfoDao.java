/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 上午10:44:37
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
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
public interface InstallmentBillInfoDao extends BaseDao<InstallmentBillInfo> {

    /**
     * 
     * Description: 获取上期订单信息
     * 
     * @param
     * @return InstallmentBillInfo
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 上午10:47:33
     */
    public InstallmentBillInfo getLastInstallmentBillInfo(String orderID, long installID, int repayNo);

    /**
     * 根据订单号查询剩余本息
     * 
     * @param orderID
     * @return
     */
    public InstallmentBillInfo getRemainPriAndInterest(String orderID);

    /**
     * 查询用户每个订单下的最多3个未还款账单
     * 
     * @param regId
     * @param openId
     * @return
     */
    Map<String, List<InstallmentBillInfo>> listCurrBill(String regId, String openId);

    /**
     * 查询每个订单下的未还款账单，未还款一期，逾期全部
     * 
     * @param regId
     * @param openId
     * @return
     */
    Map<String, List<InstallmentBillInfo>> listSpeCurrBill(String regId, String openId);

    /**
     * 
     * Description: 获取逾期账单列表
     * 
     * @param
     * @return PageBean
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 下午2:48:35
     */
    public PageBean listOverdueInterestPage(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 
     * Description: 获取逾期账单列表repayno倒序
     * 
     * @param
     * @return PageBean
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 下午2:48:35
     */
    public PageBean listOverdueInterestPageRepayNoDesc(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 
     * Description: 终止账单明细信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年11月16日 上午10:41:24
     */
    public long stopInstallmentBillInfo(InstallmentBillInfo installmentBillInfo);

    /**
     * 获得当前订单最后还款期数
     * 
     * @param orderId
     * @return
     */
    int getRepayNo(String orderId);

    /**
     * 获得还款时应该还第几期
     * 
     * @param orderId
     * @return
     */
    int getRepayNoByOrderId(String orderId, String today, String nextDay);

    /**
     * 通过订单号和序号获取对应总金额
     * 
     * @param params
     * @return
     */
    BigDecimal getSAmtByOrderIdAndRepayNo(Map<String, Object> params);

    /**
     * 通过订单号和序号查询单期账单应还款和已还款
     * 
     * @param params
     * @return
     */
    InstallmentBillInfo getInstBillByOrderIdAndRepayNo(Map<String, Object> params);

    /**
     * 
     * Description: 查询订单下所有账单信息 合并金额
     * 
     * @param
     * @return String
     * @throws @Author gxy Create Date: 2016年11月17日 上午10:41:24
     */
    List<InstallmentBillInfo> getAllInstallmentBillInfoListForOrderId(String orderId);

    /**
     * 
     * Description: 正常还款（支持多订单、多次放款）
     * 
     * @param
     * @return Integer
     * @throws @Author gxy Create Date: 2016年11月16日 上午10:41:24
     */
    public long updateForNormalRepayment(List<InstallmentBillInfo> installmentBillInfoList);

    /**
     * 
     * Description: 提前还款 只支持一个订单
     * 
     * @param
     * @return Integer
     * @throws @Author gxy Create Date: 2016年11月17日 上午10:41:24
     */
    public long updateForAdvanceRepayment(InstallmentBillInfo installmentBillInfo);

    /**
     * 
     * Description: 提前还款
     * 
     * @param
     * @return Integer
     * @throws @Author gxy Create Date: 2016年11月30日 下午17:16:24
     */
    public long updateForAdvanceRepaymentById(InstallmentBillInfo installmentBillInfo);

    /**
     * 
     * Description: 检查跨期还款操作
     * 
     * @param
     * @return Integer
     * @throws @Author gxy Create Date: 2016年11月30日 下午17:16:24
     */
    public long checkIntertemporal(String orderId, String repayNo);

    /**
     * 查询账单表信息
     * 
     * @param params
     * @return
     */
    List<InstallmentBillInfo> queryBillByParams(Map<String, Object> params);

    /**
     * 根据订单查询已还金额
     * 
     * @param params
     * @return
     */
    double getRealRepayAmtByOrderId(String orderId);

    /**
     * 
     * Description: 查询需要提醒还款账单信息
     * 
     * @param
     * @return List<InstallmentBillInfo>
     * @throws
     * @Author wangxinbang Create Date: 2017年1月11日 下午3:02:38
     */
    public List<InstallmentBillInfo> getRepayRemindBillList(String wechatNo, String merchantNo, Date repayDate);

    public List<InstallmentBillInfo> getOverdueBillListByDay(Integer overdueDay);

    /**
     * 获取给风控的用户账单信息
     * 
     * @param params
     * @return
     */
    public List<ToRiskBillInfo> getToRiskBillInfo(Map<String, Object> params);

    /**
     * 财务应收明细查询(分页)
     * 
     * @param params
     * @return
     */
    public PageBean selectShouldDebtDetail(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 统计明细查询到的金额
     * 
     * @param paramMap
     * @return
     */
    public BigDecimal getTotalRepayAmt(Map<String, Object> paramMap);

    /**
     * 财务应收明细查询(不分页)
     * 
     * @param paramMap
     * @return
     */
    public List<ShouldDebtDetail> selectShouldDebtDetail(Map<String, Object> paramMap);

    /**
     * 存量统计查询(不分页)
     * 
     * @param paramMap
     * @return
     */
    public List<StockStatisticsBean> listStockStatistics(Map<String, Object> paramMap);

    /**
     * 存量统计查询(分页)
     * 
     * @param paramMap
     * @return
     */
    public PageBean listStockStatisticsPage(PageParam pageParam, Map<String, Object> paramMap);

    public long deratePenalty(JSONObject params);

    /**
     * 抵押车业务查询-账单查询
     * 
     * @param params
     * @return List
     */
    public List<ToRiskBillInfo> queryBillByParamsForMortgage(Map<String, Object> params);

    /**
     * 查询账户系统-订单要素 违约金 最迟还款日期 当期期数
     * 
     * @param params
     * @return
     */
    public InstallmentBillInfo getFactors(Map<String, Object> params);

    /**
     * 查询账户系统-订单要素 已还金额
     * 
     * @param params
     * @return
     */
    public InstallmentBillInfo getPayAmt(Map<String, Object> params);

    /**
     * 转租-更新订单明细信息
     * 
     * @param installmentBillInfo
     * @return
     */
    public long updateForRepaymentByOrderId(InstallmentBillInfo installmentBillInfo);

    /**
     * 转租-根据id更新订单明细中的REPAYNO
     * 
     * @param installmentBillInfo
     * @return
     */
    public long updateForRepaymentById(InstallmentBillInfo installmentBillInfo);

    public List<InstallmentBillInfo> getAllInstallmentBillInfoByOid(String orderId);

    /**
     * 根据orderId集合获取逾期账单
     * 
     * @param params
     * @param request
     * @return
     */
    public List<InstallmentBillInfo> getOverdueInstallmentBillInfo(Map<String, Object> params);

    /**
     * 根据订单号获取全部未失效账单-中阁使用
     * 
     * 
     * @param objs
     * @param request
     * @return
     */
    public PageBean listInstallmentBillInfoPage(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 
     * Description:根据orderId repayno获取账单应还总金额
     * 
     * @param objs
     * @param request
     * @return
     */
    InstallmentBillInfo getCurRepayAmtByRepayno(Map<String, Object> params);

    /**
     * 
     * Description:根据订单号 手机号码 还款期数 查询账单信息
     * 
     * @param objs
     * @param request
     * @return
     */
    List<InstallmentBillInfoNormalRepay> selectBillsByCondition(Map<String, Object> params);

    public OverDueBillPojo overdueBillQuery(QueryCondition qc);

    /**
     * 存量统计查询--查询应还总额 已还本金 未还本金(分页)
     * 
     * @param paramMap
     * @return
     */
    public PageBean listStockStatisticsAmtPage(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 存量统计查询--总期数 已还期数查询 未还期数(分页)
     * 
     * @param paramMap
     * @return
     */
    public PageBean listStockStatisticsNosPage(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 
     * Description:存量统计查询
     * 
     * @param objs
     * @param request
     * @return
     */
    public PageBean listStockStatisticsPageNew(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 
     * Description:查询每日到期账单
     * 
     * @param objs
     * @param request
     * @return
     */

    public PageBean selectEveryDayExpireBill(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 
     * Description: 部分还款（支持多订单、多次放款）
     * 
     * @param
     * @return Integer
     * @throws
     * @Author haojinlong
     */
    public long updateForPartRepayment(List<InstallmentBillInfo> installmentBillInfoList);
    
    /**
     * 存量统计查询--查询应还总额 已还本金 未还本金
     * 
     * @param paramMap
     * @return
     */
    public List<StockStatisticsBean> listStockStatisticsAmt(Map<String, Object> paramMap);

    /**
     * 存量统计查询--总期数 已还期数查询 未还期数
     * 
     * @param paramMap
     * @return
     */
    public List<StockStatisticsBean> listStockStatisticsNos(Map<String, Object> paramMap);

    /**
     * 
     * Description:api标准接口使用-查询最近三期账单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public Map<String, List<InstallmentBillInfo>> listCurrBill(String regId, String openId, String orderId);
    /**
     * 
     * Description:修改上收月供剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月11日
     */
    public long updatePrincipalForBillInfo(List<InstallmentBillInfo> installmentBillInfoList);

    /**
     * 
     * Description:房贷-根据订单号获取子订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月14日
     */
    public List<InstallmentBillInfo> getSubOrderNoByOrderNo(Map<String, Object> paramMap);

    /**
     * 
     * Description:房贷-根据订单号获取账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月14日
     */
    public List<InstallmentBillInfo> getMorgateOrderInfoByOrderId(Map<String, Object> paramMap);

    public BigDecimal getAddAmt(Integer hasRepayNo, String orderId);

    public PrepaymentAnalysisPojo getLastRepayInfoByOId(String orderId);

    public BigDecimal sumYhbj(Integer repayNo, String orderId);

    public InstallmentBillInfo getCurrentBill(String orderId);

    public BigDecimal sumYhlx(Integer repayNo, String orderId);

    public boolean isTakeMonth(String orderId);

    public BigDecimal getSjyhjeByOid(String orderId);

    public InstallmentBillInfo getLastBill(String orderId);

    public Map<String, Integer> finishBill(InstallmentBillInfo ibi);

    /**
     * 
     * Description:根据订单号获取总罚息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月25日
     */
    public BigDecimal getTotalOverDueInterest(String orderId);

    /**
     * 
     * Description:根据订单号获取逾期期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月25日
     */
    public int getOverdueItems(String orderId);

    /**
     * 
     * Description:查询每日到期账单
     * 
     * @param objs
     * @param request
     * @return
     */

    public PageBean selectEveryDayExpireBillForAuto(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 
     * Description:根据订单号查询账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月8日
     */
    public InstallmentBillInfo getAllInstallmentBillInfoByOrderId(Map<String, Object> paramMap);

    /**
     * 
     * Description:根据订单号当前期数获取以前的账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月8日
     */
    public List<InstallmentBillInfo> getAllInstallmentBillInfoByOrderIdList(Map<String, Object> paramMap);

    /**
     * 
     * Description:查询距离最迟还款日小于等于3天的账单，账单状态为：待还款；
     * 
     * @author chengzhen
     * @param params
     * @param objs
     * @param request
     * @return 2018年4月23日 16:12:13
     */
    public List<InstallmentBillInfo> queryBillLastDateThree(Map<String, Object> params);

    public List<InstallmentBillInfo> queryBillLastDateGreaterThanFive(Map<String, Object> params);

    public InstallmentBillInfo queryBillInfoByOrderIdPage(Map<String, Object> map);

    /**
     * 
     * Description:获取距当前系统时间三天后的账单
     * 
     * @param objs
     * @param request
     * @return
     */

    public PageBean selectLatelyThreeDaysBill(PageParam pageParam, Map<String, Object> paramMap);

    /**
     * 
     * Description:根据订单号 期数查询账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月2日
     */
    public InstallmentBillInfo getInstallmentBillInfoByOrderId(Map<String, Object> paramMap);

    /**
     * 
     * Description:批量修改接收短信手机号码
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月14日
     */
    public int updateInstallmentBillInfoByOrderId(InstallmentBillInfo installmentBillInfo);

    /**
     * 
     * Description:按订单分组汇总账单信息
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月19日
     */
    public List<Map<String, Object>> collectBillDataByOrder(Map<String, Object> paramMap);

    /**
     * 
     * 根据订单号获取订单剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月26日
     */
    public Map<String, Object> getRemainPrincipal(String orderId);

    /**
     * 
     * Description:根据订单号查询首次还款时间
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    public InstallmentBillInfo getFirstLastRepayDateByOrderId(String orderId);

    /**
     * 
     * Description:根据订单号,放款时间查询小于最迟还款日最近的一期账单(倒叙)
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月17日
     */
    public InstallmentBillInfo getLatelyInstallmentBillInfoDesc(Map<String, Object> paramMap);

    /**
     * 
     * Description:根据订单号,放款时间查询大于最迟还款日最近的一期账单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月17日
     */
    public InstallmentBillInfo getLatelyInstallmentBillInfoAsc(Map<String, Object> paramMap);

    /**
     * 根据订单号获取账单总期数 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月18日
     */
    public Integer getBillNums(String orderId);
}
