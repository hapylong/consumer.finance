/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月19日 下午6:05:00
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.request.RepayAuthenticateRequestMessage;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfoNormalRepay;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.ShouldDebtDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.StockStatisticsBean;
import com.iqb.consumer.data.layer.mysql.bean.inst.ToRiskBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.request.FinishBillRequestMessage;
import com.iqb.consumer.data.layer.mysql.bean.request.OverDueBillPojo;
import com.iqb.consumer.data.layer.mysql.bean.request.QueryCondition;
import com.iqb.consumer.service.account.dto.InstallmentBillInfoDto;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface BillBizService {

    // 查询提前还款应还金额
    Map<String, Object> selectCost4Advance(Map<String, Object> params);

    // 查询账单分页(单查已还或者单查未还的分页)
    PageBean selectBillsByPage(Map<String, Object> params);

    /**
     * 分页查询账单(供贷后使用,查询参数包含：手机号,订单号,商户号,时间,状态,行业号)
     * 
     * @param params
     * @return
     */
    PageBean queryBillByParams(Map<String, Object> params);

    // 查询当前账单(显示三期)
    Object selectCurrBills(String regId, String openId);

    // 查询一期未还款和全部逾期账单
    Object selectSpeCurrBills(String regId, String openId);

    // 房贷生成账单
    public Map<String, Object> createMortgageBill(InstallmentInfo installmentInfo) throws Exception;

    // 生成账单
    Map<String, Object> createBill(InstallmentInfo installmentInfo) throws Exception;

    // 力硕生成账单
    Map<String, Object> createLSBill(InstallmentInfo installmentInfo) throws Exception;

    /**
     * @description 账单还款查询
     * @param conUUID
     * @return
     */
    List<InstallmentBillInfo> selectBills(String conUUID);

    Map<String, Object> payment(List<InstallmentBillInfoDto> list);

    /**
     * 
     * Description: 终止分期
     * 
     * @param
     * @return void
     * @throws Exception
     * @throws @Author wangxinbang Create Date: 2016年11月16日 上午10:21:54
     */
    public void stopPeriodization(JSONObject params) throws BizException;

    /**
     * 
     * Description: 重置分期
     * 
     * @param
     * @return void
     * @throws @Author wangxinbang Create Date: 2016年11月16日 上午11:15:02
     */
    public void resetPeriodization(JSONObject params);

    /**
     * 通过订单号和期数号查询应还总金额
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
     * @description 查询订单下所有账单信息 合并金额
     * @param orderId
     * @return
     */
    List<InstallmentBillInfo> getAllInstallmentBillInfoListForOrderId(String orderId);

    /**
     * @description 账单还款(正常还款)
     * @param List<PaymentDto>
     * @return
     */
    Map<String, Object> paymentNormal(List<InstallmentBillInfo> installmentBillInfoList);

    /**
     * @description 账单还款(提前还款)
     * @param InstallmentBillInfo
     * @return
     */
    Map<String, Object> paymentAdvance(InstallmentBillInfo installmentBillInfo);

    // /**
    // * 还款
    // *
    // * @param jsonStr
    // * @return
    // */
    // Map<String, Object> payment(String jsonStr);

    Map<String, Object> normalPrepayment(Map<String, List<InstallmentBillInfo>> map, String tradeNo, String repayType,
            String bankCardNo, String bankName);

    Map<String, Object> allPrepayment(String orderId, String tradeNo, Date repayDate, String merchantNo,
            String repayType, String bankCardNo, String bankName, BigDecimal sumAmt);

    Map<String, Object> partPrepayment();

    Long checkIntertemporal(String orderId, String repayNo);

    /**
     * 提前清算(查询提前清算相关信息)
     * 
     * @param params
     * @return
     */
    Map<String, Object> balanceAdvance(Map<String, Object> params);

    /**
     * 获得当前订单最后还款期数
     * 
     * @param orderId
     * @return
     */
    Map<String, Object> getRepayNo(String orderId);

    /**
     * 获取订单已还金额
     * 
     * @param orderId
     * @return
     */
    BigDecimal getRealRepayAmtByOrderId(String orderId);

    /**
     * 获取给风控的用户账单信息
     * 
     * @param params
     * @return
     */
    public List<ToRiskBillInfo> getToRiskBillInfo(Map<String, Object> params);

    /**
     * 财务应付明细查询接口(分页)
     * 
     * @param params
     * @return
     */
    public PageBean selectShouldDebtDetail(Map<String, Object> params);

    /**
     * 统计明细查询到的金额
     * 
     * @param params
     * @return
     */
    public BigDecimal getTotalRepayAmt(Map<String, Object> params);

    /**
     * 财务应付明细查询接口(不分页)
     * 
     * @param params
     * @return
     */
    public List<ShouldDebtDetail> selectShouldDebtDetail2(Map<String, Object> params);

    /**
     * 存量统计查询(分页)
     * 
     * @param params
     * @return
     */
    public PageBean listStockStatisticsPage(Map<String, Object> params);

    /**
     * 存量统计查询(不分页)
     * 
     * @param paramMap
     * @return
     */
    public List<StockStatisticsBean> listStockStatistics(Map<String, Object> paramMap);

    /**
     * 
     * Description: 罚息减免
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年3月14日 上午10:01:04
     */
    public Map<String, Object> deratePenalty(JSONObject params);

    /**
     * 抵押车业务查询-账单查询(带分页)
     * 
     * @param params
     * @return
     */
    PageBean queryBillByParamsForMortgage(Map<String, Object> params);

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

    int repayAuthenticate(RepayAuthenticateRequestMessage rarm);

    /**
     * 以租代购-转租
     * 
     * @param params
     */
    public void getSubletInfo(Map<String, Object> params);

    /**
     * 根据orderId集合获取逾期账单
     * 
     * @param params
     * @return 是否逾期标识
     * @Author haojinlong Create Date: 2017年5月27日
     */
    public String getOverdueInstallmentBillInfo(Map<String, Object> params);

    /**
     * 根据订单号获取全部未失效账单-中阁使用
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    PageBean listInstallmentBillInfoPage(Map<String, Object> params);

    /**
     * 
     * Description:根据orderId repayno获取账单应还总金额
     * 
     * @author haojinlong
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

    /**
     * 
     * Description:部分还款
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> partPrepayment(Map<String, List<InstallmentBillInfo>> map, String tradeNo,
            String repayType, String bankCardNo, String bankName);

    OverDueBillPojo overdueBillQuery(QueryCondition qc);

    /**
     * 
     * Description:查询每日到期账单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    PageBean selectEveryDayExpireBill(Map<String, Object> params);

    // api标准接口使用-查询当前账单(显示三期)
    Object selectCurrBills(String regId, String openId, String orderId);

    /**
     * 
     * Description:根据订单号获取房贷分期详细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月14日
     */
    public List<Map<String, Object>> getMorgateOrderInfo(Map<String, Object> params);

    BigDecimal getAddAmt(Integer hasRepayNo, String orderId);

    Map<String, Object> getPrepaymentResponsePojo(String orderId, String interestRate);

    Map<String, Integer> finishBill(FinishBillRequestMessage fbrm);

    /**
     * 
     * Description:根据订单获取订单账单状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月8日
     */
    public List<Map<String, Object>> getBillStatusByOrderIds(Map<String, Object> params);

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
    List<InstallmentBillInfo> queryBillLastDateThree(Map<String, Object> params);

    List<InstallmentBillInfo> queryBillLastDateGreaterThanFive(Map<String, Object> params);

    /**
     * 
     * 获取距当前系统时间三天的账单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    public PageBean selectLatelyThreeDaysBill(Map<String, Object> params);

    /**
     * 
     * Description:根据订单号 期数查询账单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月2日
     */
    public InstallmentBillInfo getInstallmentBillInfoByOrderId(JSONObject objs);

    List<InstallmentBillInfo> selectEveryDayExpireBill2(Map<String, Object> params);

    /**
     * 
     * Description:根据订单号修改账单信息表客户接收短信号码
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月24日
     */
    public int batchUpdateBillSmsMobile(JSONObject objs);

    /**
     * 
     * Description:根据订单列表获取每个订单最大已还期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    public List<Map<String, String>> getMaxRepayNosByOrderId(JSONObject objs);

    /**
     * 
     * Description:按订单分组汇总账单信息
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月19日
     */
    public List<Map<String, Object>> collectBillDataByOrder(JSONObject objs);

    /**
     * 
     * Description:根据订单号查询首次还款时间
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    public InstallmentBillInfo getFirstLastRepayDateByOrderId(JSONObject objs);

    /**
     * 根据订单号查询 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月17日
     */
    public Map<String, Object> getAllocationInfoOrderId(JSONObject objs);
}
