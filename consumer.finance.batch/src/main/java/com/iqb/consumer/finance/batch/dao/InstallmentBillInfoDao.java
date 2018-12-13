/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 上午10:44:37
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.InstallmentBillInfo;
import com.iqb.consumer.finance.batch.bean.ShouldDebtDetail;
import com.iqb.consumer.finance.batch.bean.StockStatisticsBean;
import com.iqb.consumer.finance.batch.bean.ToRiskBillInfo;
import com.iqb.consumer.finance.batch.dao.basic.BaseDao;
import com.iqb.consumer.finance.batch.page.PageBean;
import com.iqb.consumer.finance.batch.page.PageParam;

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
}
