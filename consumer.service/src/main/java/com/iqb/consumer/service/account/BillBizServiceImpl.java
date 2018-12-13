/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账单业务层
 * @date 2016年7月19日 下午6:05:40
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.constant.AccountConstant;
import com.iqb.consumer.common.constant.AccountConstant.DeratePenaltyConstant;
import com.iqb.consumer.common.constant.AccountConstant.RedisLockConstant;
import com.iqb.consumer.common.constant.FinanceUtil;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.exception.RequestException;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.common.request.RepayAuthenticateRequestMessage;
import com.iqb.consumer.common.utils.BeanUtil;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.common.utils.httpclient.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.EncryptUtils;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccRepayHistory;
import com.iqb.consumer.data.layer.mysql.bean.acc.OpenInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfoNormalRepay;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentPlan;
import com.iqb.consumer.data.layer.mysql.bean.inst.QueryBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.ShouldDebtDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.StockStatisticsBean;
import com.iqb.consumer.data.layer.mysql.bean.inst.ToRiskBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.pojo.PrepaymentAnalysisPojo;
import com.iqb.consumer.data.layer.mysql.bean.pojo.PrepaymentResponsePojo;
import com.iqb.consumer.data.layer.mysql.bean.product.RefundCost;
import com.iqb.consumer.data.layer.mysql.bean.request.FinishBillRequestMessage;
import com.iqb.consumer.data.layer.mysql.bean.request.OverDueBillPojo;
import com.iqb.consumer.data.layer.mysql.bean.request.QueryCondition;
import com.iqb.consumer.data.layer.mysql.bean.sys.SysOperateInfo;
import com.iqb.consumer.data.layer.mysql.dao.acc.AccRepayHistoryDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.DelayBillInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentBillInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentDetailDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentPlanDao;
import com.iqb.consumer.data.layer.mysql.dao.product.RefundCostDao;
import com.iqb.consumer.finance.cache.kv.KVCache;
import com.iqb.consumer.service.account.dto.AdvancedPayBillsDto;
import com.iqb.consumer.service.account.dto.InstallmentBillInfoDto;
import com.iqb.consumer.service.inst.IInstInfoService;
import com.iqb.consumer.service.product.IBillDateService;
import com.iqb.consumer.service.product.IBreachContractService;
import com.iqb.consumer.service.product.IterestService;
import com.iqb.consumer.service.product.LockRepayDateService;
import com.iqb.consumer.service.product.OverdueIterestService;
import com.iqb.consumer.service.sys.JournalService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class BillBizServiceImpl implements BillBizService {

    protected static final Logger logger = LoggerFactory.getLogger(BillBizServiceImpl.class);

    public static final String SQL_QUERYBILLBYPARAMS = "queryBillByParams";

    public static final String SQL_QUERYBILLBYPARAMSFORMORTGAGE = "queryBillByParamsForMortgage";
    @Resource
    private InstallmentDetailDao installmentDetailDao;
    @Resource
    private InstallmentBillInfoDao installmentBillInfoDao;
    @Resource
    private InstallmentPlanDao installmentPlanDao;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private DelayBillInfoDao delayBillInfoDao;
    @Resource
    private IBillDateService billDateService;
    @Resource
    private IterestService iterestService;
    @Resource
    private OverdueIterestService overdueIterestService;
    @Resource
    private InstallmentInfoDao installmentInfoDao;
    @Resource
    private LockRepayDateService lockRepayDateService;
    @Resource
    private IInstInfoService instInfoServiceImpl;
    @Resource
    private KVCache<QueryBillInfo> queryBillInfoCache;
    @Resource
    private RefundCostDao refundCostDao;
    @Resource
    private AccRepayHistoryService accRepayHistoryService;
    @Resource
    private IBreachContractService breachContractService;
    @Resource
    private JournalService journalService;
    @Resource
    private AcctInfoService acctInfoService;
    @Resource
    private AccRepayHistoryDao accRepayHistoryDao;
    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private ConsumerConfig consumerConfig;

    private final int SUCCESS = 0; // 请求信息错误
    private final int INVILAD_REQUEST = -1; // 请求信息错误
    private final int INVILAD_SQL_DATA = -2; // 订单不存在
    private final int INVILAD_OVERDUE_ORDER = -3; // 有逾期订单
    private final int INVILAD_THREE_DAY_ORDER = -4; // 3天内需要还款的订单
    private final int INVILAD_NO_ORDERS_NEED_REPAY = -5; // 没有未还款订单
    private final int UNKNOWN_ERROR = -6; // 未知异常

    @Override
    public PageBean selectBillsByPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        return installmentBillInfoDao.listPage(pageParam, params);
    }

    @Override
    public Object selectCurrBills(String regId, String openId) {
        // 查询redis
        String key = "SelectCurrBills." + regId + "." + openId;
        String redisResult = queryBillInfoCache.get(key);
        if (redisResult != null && !"".equals(redisResult)) {
            List<QueryBillInfo> list = JSON.parseArray(redisResult, QueryBillInfo.class);
            return list;
        }
        // 查询数据库
        Map<String, List<InstallmentBillInfo>> billListMap = installmentBillInfoDao.listCurrBill(regId, openId);
        if (billListMap == null || billListMap.size() == 0) {
            logger.info("查询数据库listCurrBill未查询到数据。");
            return null;
        }
        List<QueryBillInfo> result = new LinkedList<>();
        Iterator<Entry<String, List<InstallmentBillInfo>>> it = billListMap.entrySet().iterator();
        while (it.hasNext()) {// 遍历结果集
            Map.Entry<String, List<InstallmentBillInfo>> entry = it
                    .next();
            QueryBillInfo queryBillInfo = new QueryBillInfo();
            List<InstallmentBillInfo> list = entry.getValue();
            list = handleInstallmentBillInfoList(list);
            queryBillInfo.setOrderId(entry.getKey());
            queryBillInfo.setBillList(list);
            result.add(queryBillInfo);
        }

        // 结果存入redis
        // queryBillInfoCache.put(key, JSON.toJSONString(result), 60 * 30);

        return result;
    }

    @Override
    public Object selectSpeCurrBills(String regId, String openId) {
        // 查询redis
        String key = "SelectCurrBills." + regId + "." + openId;
        String redisResult = queryBillInfoCache.get(key);
        if (redisResult != null && !"".equals(redisResult)) {
            List<QueryBillInfo> list = JSON.parseArray(redisResult, QueryBillInfo.class);
            return list;
        }
        // 查询数据库
        Map<String, List<InstallmentBillInfo>> billListMap = installmentBillInfoDao.listSpeCurrBill(regId, openId);
        if (billListMap == null || billListMap.size() == 0) {
            logger.info("查询数据库listCurrBill未查询到数据。");
            return null;
        }
        List<QueryBillInfo> result = new LinkedList<>();
        Iterator<Entry<String, List<InstallmentBillInfo>>> it = billListMap.entrySet().iterator();
        while (it.hasNext()) {// 遍历结果集
            Map.Entry<String, List<InstallmentBillInfo>> entry = it
                    .next();
            QueryBillInfo queryBillInfo = new QueryBillInfo();
            List<InstallmentBillInfo> list = entry.getValue();
            list = handleInstallmentBillInfoList(list);
            queryBillInfo.setOrderId(entry.getKey());
            queryBillInfo.setBillList(list);
            result.add(queryBillInfo);
        }

        // 结果存入redis
        // queryBillInfoCache.put(key, JSON.toJSONString(result), 60 * 30);

        return result;
    }

    /**
     * api标准接口使用-查询最近三期账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月29日
     */
    @Override
    public Object selectCurrBills(String regId, String openId, String orderId) {
        // 查询redis
        String key = "SelectCurrBills." + regId + "." + openId;
        String redisResult = queryBillInfoCache.get(key);
        if (redisResult != null && !"".equals(redisResult)) {
            List<QueryBillInfo> list = JSON.parseArray(redisResult, QueryBillInfo.class);
            return list;
        }
        // 查询数据库
        Map<String, List<InstallmentBillInfo>> billListMap =
                installmentBillInfoDao.listCurrBill(regId, openId, orderId);
        if (billListMap == null || billListMap.size() == 0) {
            logger.info("查询数据库listCurrBill未查询到数据。");
            return null;
        }
        List<QueryBillInfo> result = new LinkedList<>();
        Iterator<Entry<String, List<InstallmentBillInfo>>> it = billListMap.entrySet().iterator();
        while (it.hasNext()) {// 遍历结果集
            Map.Entry<String, List<InstallmentBillInfo>> entry = it
                    .next();
            QueryBillInfo queryBillInfo = new QueryBillInfo();
            List<InstallmentBillInfo> list = entry.getValue();
            list = handleInstallmentBillInfoList(list);
            queryBillInfo.setOrderId(entry.getKey());
            queryBillInfo.setBillList(list);
            result.add(queryBillInfo);
        }
        return result;
    }

    // 处理金额，缩小100倍
    private List<InstallmentBillInfo> handleInstallmentBillInfoList(List<InstallmentBillInfo> list) {
        BigDecimal h = new BigDecimal("100");
        for (int i = 0; i < list.size(); i++) {
            InstallmentBillInfo installmentBillInfo = list.get(i);
            installmentBillInfo.setCurRealRepayamt(BigDecimalUtil.div(installmentBillInfo.getCurRealRepayamt(), h));
            installmentBillInfo.setCurRepayAmt(BigDecimalUtil.div(installmentBillInfo.getCurRepayAmt(), h));
            installmentBillInfo.setCurRepayInterest(BigDecimalUtil.div(installmentBillInfo.getCurRepayInterest(), h));
            installmentBillInfo.setCurRepayOverdueInterest(
                    BigDecimalUtil.div(installmentBillInfo.getCurRepayOverdueInterest(), h));
            installmentBillInfo.setCurRepayPrincipal(BigDecimalUtil.div(installmentBillInfo.getCurRepayPrincipal(), h));
            installmentBillInfo.setFixedOverdueAmt(BigDecimalUtil.div(installmentBillInfo.getFixedOverdueAmt(), h));
            installmentBillInfo.setInstallAmt(BigDecimalUtil.div(installmentBillInfo.getInstallAmt(), h));
            installmentBillInfo.setInstallSumAmt(BigDecimalUtil.div(installmentBillInfo.getInstallSumAmt(), h));
            installmentBillInfo.setPreInterest(BigDecimalUtil.div(installmentBillInfo.getPreInterest(), h));
            installmentBillInfo
                    .setPreOverdueInterest(BigDecimalUtil.div(installmentBillInfo.getPreOverdueInterest(), h));
            installmentBillInfo.setPrincipal(BigDecimalUtil.div(installmentBillInfo.getPrincipal(), h));
            installmentBillInfo.setRealPayamt(BigDecimalUtil.div(installmentBillInfo.getRealPayamt(), h));
            installmentBillInfo.setRemainPrincipal(BigDecimalUtil.div(installmentBillInfo.getRemainPrincipal(), h));
            installmentBillInfo
                    .setCutFixedOverdueAmt(BigDecimalUtil.div(installmentBillInfo.getCutFixedOverdueAmt(), h));
            installmentBillInfo.setCutInterest(BigDecimalUtil.div(installmentBillInfo.getCutInterest(), h));
            installmentBillInfo
                    .setCutOverdueInterest(BigDecimalUtil.div(installmentBillInfo.getCutOverdueInterest(), h));
        }
        return list;
    }

    /**
     * 房贷生成账单
     * 
     * @param installmentInfo
     * @return
     */
    @Override
    public Map<String, Object> createMortgageBill(InstallmentInfo installmentInfo) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1.查询分期明细
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("installInfoId", installmentInfo.getId());
            paramMap.put("orderId", installmentInfo.getOrderId());
            List<InstallmentDetail> details = installmentDetailDao.listBy(paramMap);

            // 2.分期计划
            InstallmentPlan installmentPlan = installmentPlanDao.getById(installmentInfo.getPlanId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            // // 获取第一期最早还款日，方便计算遍历中的账单日期
            // String earlyPayDate = getEarlyPayDate(billDate, installmentPlan);
            // Date earliestPayDate = null;
            // if (earlyPayDate != null) {
            // Calendar earliestPayDateCal = DateUtil.parseCalendar(earlyPayDate,
            // DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            // earlyPayDate = DateUtil.format(earliestPayDateCal,
            // DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            // earliestPayDate = sdf.parse(earlyPayDate);
            // }

            List<InstallmentBillInfo> bills = new ArrayList<>(details.size());
            // 初始化剩余本息 by yeoman 20170620
            BigDecimal remainPriandInterest =
                    BigDecimalUtil.add(installmentInfo.getInstallAmt(), installmentInfo.getInterestAmt());// 总本金+总利息
            // String billDate = getFirstBillDate(details, installmentPlan, installmentInfo, sdf);
            String curBillDate = null;
            for (int i = 0; i < details.size(); i++) {
                // 先息后本上收月供 ,首期为已还款且还款金额从json获取，最后一期为本金
                // 非上收月供.首期为json获取,最后一期为本+息
                InstallmentDetail installmentDetail = details.get(i);
                // 计算还款日期
                // 获取最后还款日和最早还款日
                // Calendar billDateCal = DateUtil.parseCalendar(billDate,
                // DateUtil.SHORT_DATE_FORMAT_NO_DASH);
                // String billDate2 = DateUtil.format(billDateCal,
                // DateUtil.SHORT_DATE_FORMAT_NO_DASH);
                // 修改账单日计算规则,入参:分期时间,期数,账单日生成计划ID
                String installDate = sdf.format(details.get(0).getInstallDate());// 分期时间
                String dynamicDays = details.get(0).getCycle() + ""; // 动态账单日天数
                if (installmentPlan.getRepayMethod() == 3) {
                    curBillDate =
                            billDateService.getBillDate(curBillDate, installDate,
                                    installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH, dynamicDays,
                                    installmentDetail.getInstallTerms());
                } else {
                    // 上收月供的情况需要判断上收期数然后开始计算最后还款日
                    if (installmentInfo.getTakeMonth() == 1) {
                        if (i + 1 <= installmentInfo.getTakePayment()) {
                            curBillDate = installDate;
                        } else {
                            curBillDate =
                                    billDateService.getBillDate(curBillDate, installDate,
                                            installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH,
                                            dynamicDays, i + 1 - installmentInfo.getTakePayment());
                        }
                    } else {
                        curBillDate =
                                billDateService.getBillDate(curBillDate, installDate,
                                        installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH,
                                        dynamicDays, i + 1);
                    }
                }
                Date lastRepayDate = sdf.parse(curBillDate);
                // 获取滞纳金计算时间
                Date delayBeginDate = DateUtil.addDayFromDate(lastRepayDate, installmentPlan.getGraceDays());
                // 获取利息
                Map<Integer, InstDetail> instDetail = getInstDetail(installmentInfo);
                BigDecimal monthInterest = BigDecimal.ZERO;
                if (instDetail.size() >= i + 1) {
                    // 一次性还本付息利息为所有详情之和
                    if (installmentPlan.getRepayMethod() == 3) { // 一次性还本付息
                        monthInterest = getAllInterest(instDetail);
                    } else {
                        // 上收月供的最后一期利息为0,仅有本金
                        monthInterest = instDetail.get(i + 1).getInterest();
                    }
                }
                // 本期应还总金额=(每期本金 + 月利息 + 滞纳金 + 其他费用) by yeoman20170502
                BigDecimal curRepayAmt =
                        BigDecimalUtil.add(installmentDetail.getPrincipal(), monthInterest,
                                BigDecimal.ZERO, installmentDetail.getOtherAmt() == null
                                        ? BigDecimal.ZERO
                                        : installmentDetail.getOtherAmt());
                // 产品要求最后一期本金不能包含其他费用例如：停车费 (产品确认人：罗春燕)
                BigDecimal otherAmt = installmentDetail.getOtherAmt();
                if (i == details.size() - 1) {
                    curRepayAmt = BigDecimalUtil.add(installmentDetail.getPrincipal(), monthInterest, BigDecimal.ZERO);
                    otherAmt = BigDecimal.ZERO;
                }
                // 每天利息
                BigDecimal preInterest = BigDecimal.ZERO;
                // 每期实际分摊金额
                BigDecimal realPayamt = BigDecimalUtil.add(installmentDetail.getPrincipal(), monthInterest);
                // 滞纳金and违约金
                Map<String, BigDecimal> preOverdueMap =
                        overdueIterestService.calculateOverdueInterestWithFixed(
                                installmentDetail.getRemainPrincipal(), installmentDetail.getInstallAmt(), realPayamt,
                                installmentDetail.getContractAmt(), remainPriandInterest, 1,
                                installmentPlan.getOvedueId() + "");
                BigDecimal poi = preOverdueMap.get("Amt");
                BigDecimal fixedOverdueAmt = preOverdueMap.get("Fixed");

                // 获取账单状态
                int status = getStatus(i, installmentInfo, installmentDetail);
                // 本期实际还款金额
                BigDecimal curRealRepayamt = getCurRealRepayamt(i, installmentInfo, installmentDetail, curRepayAmt); // 需要改造

                InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo();
                installmentBillInfo.setCurRealRepayamt(curRealRepayamt);// 本期实际还款金额
                installmentBillInfo.setCurRepayAmt(curRepayAmt);// 本期应还总金额
                installmentBillInfo.setCurRepayInterest(monthInterest);// 本期应还利息
                installmentBillInfo.setCurRepayOverdueInterest(BigDecimal.ZERO);// 本期应还逾期金额
                installmentBillInfo.setCurRepayPrincipal(installmentDetail.getPrincipal());// 本期应还本金
                installmentBillInfo.setDelayBeginDate(delayBeginDate);// 滞纳金计算时间
                installmentBillInfo.setInstallAmt(installmentDetail.getInstallAmt());// 分期金额
                installmentBillInfo.setInstallDetailId(installmentDetail.getId());// 分期明细ID
                installmentBillInfo.setInstallInfoId(installmentInfo.getId());// 分期总账号ID
                installmentBillInfo.setLastRepayDate(lastRepayDate);// 最后还款日
                installmentBillInfo.setOpenId(installmentInfo.getOpenId());// 账户号
                installmentBillInfo.setOverdueDays(0);// 逾期天数
                installmentBillInfo.setPartPayment(installmentPlan.getPartpayMent());// 部分还款
                installmentBillInfo.setPreInterest(preInterest);// 每天的利息
                installmentBillInfo.setPreOverdueInterest(poi);// 每日滞纳金
                installmentBillInfo.setPrePayment(installmentPlan.getPrepayMent());// 提前还款
                installmentBillInfo.setPrincipal(installmentDetail.getPrincipal());// 本金
                installmentBillInfo.setRegId(installmentInfo.getRegId());// 注册号
                installmentBillInfo.setRemainPrincipal(installmentDetail.getRemainPrincipal());// 剩余本金
                installmentBillInfo.setRemainPriandInterest(remainPriandInterest);// 剩余本息
                // 下期账单对应的剩余本息 = 剩余本息-本期使用的本金-本期利息
                remainPriandInterest =
                        BigDecimalUtil.sub(remainPriandInterest,
                                BigDecimalUtil.add(installmentDetail.getPrincipal(), monthInterest));
                installmentBillInfo.setRepayNo(i + 1);// 还款序号
                installmentBillInfo.setInstOrder(installmentDetail.getInstOrder());// 放款顺序
                installmentBillInfo.setOrderId(installmentDetail.getOrderId());// 订单id
                installmentBillInfo.setSubOrderId(installmentDetail.getSubOrderId());// 订单id
                installmentBillInfo.setOrderDate(installmentDetail.getOrderDate());// 订单时间
                installmentBillInfo.setCurRepayDate(null);// 还款时间
                installmentBillInfo.setRealPayamt(realPayamt);// 实际分摊金额
                installmentBillInfo.setOtherAmt(otherAmt);// 其他综合费用
                installmentBillInfo.setEarliestPayDate(new Date());// 最早还款日
                installmentBillInfo.setPlanId(installmentPlan.getId());// 计划id
                installmentBillInfo.setInstallSumAmt(installmentDetail.getInstallSumAmt());// 合同金额
                installmentBillInfo.setMerchantNo(installmentDetail.getMerchantNo());// 商户号
                installmentBillInfo.setFixedOverdueAmt(BigDecimal.ZERO);// 逾期固定收取金额 生成账单对应的首期违约金位0
                installmentBillInfo.setStatus(status);// 账单状态

                bills.add(installmentBillInfo);
            }
            installmentBillInfoDao.insert(bills);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "账单生成成功");
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "账单生成失败");
            logger.error("创建账单出现异常：{}", e);
        }
        return result;
    }

    private BigDecimal getAllInterest(Map<Integer, InstDetail> instDetail) {
        BigDecimal allInterest = BigDecimal.ZERO;
        for (int i = 1; i <= instDetail.size(); i++) {
            allInterest = BigDecimalUtil.add(allInterest, instDetail.get(i).getInterest());
        }
        return allInterest;
    }

    /**
     * 房贷分期明细生成Map
     * 
     * @param installmentInfo
     * @return
     */
    private Map<Integer, InstDetail> getInstDetail(InstallmentInfo installmentInfo) {
        List<InstDetail> arrayInsts = JSONObject.parseArray(installmentInfo.getInstDetails(), InstDetail.class);
        // 房贷分期细则转Map key=期数repayNo
        Map<Integer, InstDetail> result = new HashMap<>();
        for (InstDetail instDetail : arrayInsts) {
            result.put(instDetail.getRepayNo(), instDetail);
        }
        return result;
    }

    @Override
    public Map<String, Object> createBill(InstallmentInfo installmentInfo) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1.查询分期明细
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("installInfoId", installmentInfo.getId());
            paramMap.put("orderId", installmentInfo.getOrderId());
            List<InstallmentDetail> details = installmentDetailDao.listBy(paramMap);

            // 2.分期计划
            InstallmentPlan installmentPlan = installmentPlanDao.getById(installmentInfo.getPlanId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // // 获取第一期账单，方便计算遍历中的账单日期
            // String billDate = getFirstBillDate(details, installmentPlan, installmentInfo, sdf);
            // // 获取第一期最早还款日，方便计算遍历中的账单日期
            // String earlyPayDate = getEarlyPayDate(billDate, installmentPlan);
            // Date earliestPayDate = null;
            // if (earlyPayDate != null) {
            // Calendar earliestPayDateCal = DateUtil.parseCalendar(earlyPayDate,
            // DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            // earlyPayDate = DateUtil.format(earliestPayDateCal,
            // DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            // earliestPayDate = sdf.parse(earlyPayDate);
            // }

            List<InstallmentBillInfo> bills = new ArrayList<>(details.size());
            // 初始化剩余本息 by yeoman 20170620
            BigDecimal remainPriandInterest =
                    BigDecimalUtil.add(installmentInfo.getInstallAmt(), installmentInfo.getInterestAmt());// 总本金+总利息
            String curBillDate = null;
            for (int i = 0; i < details.size(); i++) {
                InstallmentDetail installmentDetail = details.get(i);
                // 修改账单日计算规则,入参:分期时间,期数,账单日生成计划ID
                String installDate = sdf.format(details.get(0).getInstallDate());// 分期时间
                String dynamicDays = details.get(0).getCycle() + ""; // 动态账单日天数
                if (installmentPlan.getRepayMethod() == 3) {
                    curBillDate =
                            billDateService.getBillDate(curBillDate, installDate,
                                    installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH, dynamicDays,
                                    installmentDetail.getInstallTerms());
                } else {
                    // 上收月供的情况需要判断上收期数然后开始计算最后还款日
                    if (installmentInfo.getTakeMonth() == 1) {
                        if (i + 1 <= installmentInfo.getTakePayment()) {
                            curBillDate = installDate;
                        } else {
                            curBillDate =
                                    billDateService.getBillDate(curBillDate, installDate,
                                            installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH,
                                            dynamicDays, i + 1 - installmentInfo.getTakePayment());
                        }
                    } else {
                        curBillDate =
                                billDateService.getBillDate(curBillDate, installDate,
                                        installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH,
                                        dynamicDays, i + 1);
                    }
                }
                Date lastRepayDate = sdf.parse(curBillDate);

                // 获取滞纳金计算时间
                Date delayBeginDate = DateUtil.addDayFromDate(lastRepayDate, installmentPlan.getGraceDays());
                // 获取月利息
                BigDecimal monthInterest = null;
                // 一次性还本付息的利息 = 月利息*期数
                if (installmentPlan.getRepayMethod() == 3) {
                    monthInterest = getMonthInterest(installmentInfo, installmentDetail, installmentPlan,
                            lastRepayDate);
                    monthInterest =
                            BigDecimalUtil.mul(monthInterest, new BigDecimal(installmentDetail.getInstallTerms()));
                    // 后续如果存在日利息，可能对日利息有影响,原因现在的利息包含了所有期数的,但是日利息算法为日利息 = 总利息/月天数。
                } else {
                    monthInterest = getMonthInterest(installmentInfo, installmentDetail, installmentPlan,
                            lastRepayDate);
                }
                // 本期应还总金额=(每期本金 + 月利息 + 滞纳金 + 其他费用) by yeoman20170502
                BigDecimal curRepayAmt = BigDecimalUtil.add(installmentDetail.getPrincipal(), monthInterest,
                        BigDecimal.ZERO, installmentDetail.getOtherAmt());
                // 每天利息
                BigDecimal preInterest = getPreInterest(installmentInfo, installmentDetail, installmentPlan,
                        lastRepayDate, monthInterest);
                // 每期实际分摊金额
                BigDecimal realPayamt = getRealPayamt(installmentInfo, installmentDetail, monthInterest);
                // 滞纳金and违约金
                Map<String, BigDecimal> preOverdueMap =
                        overdueIterestService.calculateOverdueInterestWithFixed(
                                installmentDetail.getRemainPrincipal(), installmentDetail.getInstallAmt(), realPayamt,
                                installmentDetail.getContractAmt(), remainPriandInterest, 1,
                                installmentPlan.getOvedueId() + "");
                BigDecimal poi = preOverdueMap.get("Amt");
                BigDecimal fixedOverdueAmt = preOverdueMap.get("Fixed");

                // 获取账单状态
                int status = getStatus(i, installmentInfo, installmentDetail);
                // 本期实际还款金额
                BigDecimal curRealRepayamt = getCurRealRepayamt(i, installmentInfo, installmentDetail, curRepayAmt);

                InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo();
                installmentBillInfo.setCurRealRepayamt(curRealRepayamt);// 本期实际还款金额
                installmentBillInfo.setCurRepayAmt(curRepayAmt);// 本期应还总金额
                installmentBillInfo.setCurRepayInterest(monthInterest);// 本期应还利息
                installmentBillInfo.setCurRepayOverdueInterest(BigDecimal.ZERO);// 本期应还逾期金额
                installmentBillInfo.setCurRepayPrincipal(installmentDetail.getPrincipal());// 本期应还本金
                installmentBillInfo.setDelayBeginDate(delayBeginDate);// 滞纳金计算时间
                installmentBillInfo.setInstallAmt(installmentDetail.getInstallAmt());// 分期金额
                installmentBillInfo.setInstallDetailId(installmentDetail.getId());// 分期明细ID
                installmentBillInfo.setInstallInfoId(installmentInfo.getId());// 分期总账号ID
                installmentBillInfo.setLastRepayDate(lastRepayDate);// 最后还款日
                installmentBillInfo.setOpenId(installmentInfo.getOpenId());// 账户号
                installmentBillInfo.setOverdueDays(0);// 逾期天数
                installmentBillInfo.setPartPayment(installmentPlan.getPartpayMent());// 部分还款
                installmentBillInfo.setPreInterest(preInterest);// 每天的利息
                installmentBillInfo.setPreOverdueInterest(poi);// 每日滞纳金
                installmentBillInfo.setPrePayment(installmentPlan.getPrepayMent());// 提前还款
                installmentBillInfo.setPrincipal(installmentDetail.getPrincipal());// 本金
                installmentBillInfo.setRegId(installmentInfo.getRegId());// 注册号
                installmentBillInfo.setRemainPrincipal(installmentDetail.getRemainPrincipal());// 剩余本金
                installmentBillInfo.setRemainPriandInterest(remainPriandInterest);// 剩余本息
                // 下期账单对应的剩余本息 = 剩余本息-本期使用的本金-本期利息
                remainPriandInterest =
                        BigDecimalUtil.sub(remainPriandInterest,
                                BigDecimalUtil.add(installmentDetail.getPrincipal(), monthInterest));
                installmentBillInfo.setRepayNo(i + 1);// 还款序号
                installmentBillInfo.setInstOrder(installmentDetail.getInstOrder());// 放款顺序
                installmentBillInfo.setOrderId(installmentDetail.getOrderId());// 订单id
                installmentBillInfo.setSubOrderId(installmentDetail.getSubOrderId());// 子订单id
                installmentBillInfo.setOrderDate(installmentDetail.getOrderDate());// 订单时间
                installmentBillInfo.setCurRepayDate(null);// 还款时间
                installmentBillInfo.setRealPayamt(realPayamt);// 实际分摊金额
                installmentBillInfo.setOtherAmt(installmentDetail.getOtherAmt());// 其他综合费用
                installmentBillInfo.setEarliestPayDate(lastRepayDate);// 最早还款日---》
                installmentBillInfo.setPlanId(installmentPlan.getId());// 计划id
                installmentBillInfo.setInstallSumAmt(installmentDetail.getInstallSumAmt());// 合同金额
                installmentBillInfo.setMerchantNo(installmentDetail.getMerchantNo());// 商户号
                installmentBillInfo.setFixedOverdueAmt(BigDecimal.ZERO);// 逾期固定收取金额，生成账单对应的违约金为0,逾期后会计算
                installmentBillInfo.setStatus(status);// 账单状态

                bills.add(installmentBillInfo);
            }
            installmentBillInfoDao.insert(bills);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "账单生成成功");
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "账单生成失败");
            logger.error("创建账单出现异常：{}", e);
        }
        return result;
    }

    public static void main(String[] args) {
        Calendar billDateCal = DateUtil.parseCalendar("20171130", DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        billDateCal.add(Calendar.MONTH, 3);
        String billDate2 = DateUtil.format(billDateCal, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        System.out.println(billDate2);
    }

    // 获取实际已还金额
    private BigDecimal getCurRealRepayamt(int i, InstallmentInfo installmentInfo, InstallmentDetail installmentDetail,
            BigDecimal curRepayamt) {
        BigDecimal curRealRepayamt = BigDecimal.ZERO;
        if (installmentDetail.getTakeMonth() == 1 && installmentDetail.getInstOrder() == 1) {// 上收利息的情况且第一期
            if (i < installmentInfo.getTakePayment()) {
                curRealRepayamt = curRepayamt;
            }
        }
        return curRealRepayamt;
    }

    // 获取账单状态
    private int getStatus(int i, InstallmentInfo installmentInfo, InstallmentDetail installmentDetail) {
        int status = 1;
        if (installmentDetail.getTakeMonth() == 1 && installmentDetail.getInstOrder() == 1) {// 上收利息的情况且第一期
            if (i < installmentInfo.getTakePayment()) {
                status = 3;
            }
        }
        return status;
    }

    // 获取月供
    private BigDecimal getRealPayamt(InstallmentInfo installmentInfo, InstallmentDetail installmentDetail,
            BigDecimal monthInterest) {
        BigDecimal realPayamt = BigDecimal.ZERO;
        if (installmentDetail.getTakeMonth() == 1 && installmentDetail.getInstOrder() == 1) {// 上收利息的情况且第一期
            // 月供 = 上收月供金额 / 上收月供数
            realPayamt = BigDecimalUtil.div(installmentDetail.getTakePaymentAmt(),
                    new BigDecimal(installmentDetail.getTakePayment()));
        } else {
            realPayamt =
                    BigDecimalUtil
                            .add(installmentDetail.getPrincipal(), monthInterest, installmentDetail.getOtherAmt());
        }
        return realPayamt;
    }

    // 获取每日利息
    private BigDecimal getPreInterest(InstallmentInfo installmentInfo, InstallmentDetail installmentDetail,
            InstallmentPlan installmentPlan, Date lastRepayDate, BigDecimal monthInterest) {
        BigDecimal preInterest = BigDecimal.ZERO;
        if (installmentDetail.getTakeInterest() == 1) {// 上收利息的情况
            int days = DateUtil.getDaysByDate(lastRepayDate);// 当期天数
            preInterest = BigDecimalUtil.div(monthInterest, new BigDecimal(days));
        } else {
            preInterest = iterestService.calculateInterestForDay(installmentDetail.getInstallAmt(),
                    installmentPlan.getInterestId() + "", lastRepayDate);
        }
        return preInterest;
    }

    // 获取月利息
    private BigDecimal getMonthInterest(InstallmentInfo installmentInfo, InstallmentDetail installmentDetail,
            InstallmentPlan installmentPlan, Date lastRepayDate) {
        BigDecimal monthInterest = BigDecimal.ZERO;
        if (installmentDetail.getTakeInterest() == 1) {// 上收利息的情况
            if (installmentInfo.getTakeMonth() == 1) {// 上收月供的区别估计
                // 月利息 = 剩余利息 / (期数 - 上收期数)
                monthInterest = BigDecimalUtil.div(installmentDetail.getInterestAmt(),
                        new BigDecimal(installmentInfo.getInstallTerms() - installmentInfo.getTakePayment()));
                return monthInterest;
            }
            // 月利息 = 剩余利息 / 期数
            monthInterest = BigDecimalUtil.div(installmentDetail.getInterestAmt(),
                    new BigDecimal(installmentInfo.getInstallTerms()));
        } else {
            monthInterest = iterestService.calculateInterest(installmentDetail.getInstallAmt(),
                    installmentPlan.getInterestId() + "", lastRepayDate);
        }
        return monthInterest;
    }

    // 获取第一期最早还款日
    private String getEarlyPayDate(String billDate, InstallmentPlan installmentPlan) {
        String earlyPayDate = null;
        if (installmentPlan.getLockRepayDate() == 2) {
            earlyPayDate = lockRepayDateService.getLockDays(billDate, installmentPlan.getLockRepayDateId());
        }
        return earlyPayDate;
    }

    // 获取第一期账单日期
    /*
     * private String getFirstBillDate(List<InstallmentDetail> details, InstallmentPlan
     * installmentPlan, InstallmentInfo installmentInfo, SimpleDateFormat sdf) { // 是否是第一次放款 boolean
     * isFirst = true;// details.get(0).getInstOrder() == 1; // 获取第一期账单 String billDate = null; if
     * (isFirst) { // 第一次放款 if (installmentInfo.getTakeMonth() == 1) {// 上收月供的情况 // 第一期账单日期 = 订单分期日期
     * billDate = sdf.format(details.get(0).getInstallDate()); return billDate; } billDate =
     * billDateService.getBillDate(sdf.format(details.get(0).getInstallDate()),
     * installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH, details.get(0)
     * .getCycle() + "");// 获取账单日期 } else { // 房贷第N次放款首期时间还没有定规则,临时放弃该规则 InstallmentInfo info =
     * installmentInfoDao.getInstInfoByOrderId(installmentInfo.getOrderId());// 首期账单信息 //
     * InstallmentPlan plan = // installmentPlanDao.getById(info.getPlanId());// 首期账单计划 billDate =
     * billDateService.getBillDate(sdf.format(info.getCreateTime()),
     * installmentPlan.getRepayDateId(), sdf.format(installmentInfo.getCreateTime()),
     * installmentPlan.getRepayDateId()); } return billDate; }
     */

    @Override
    public List<InstallmentBillInfo> selectBills(String conUUID) {
        List<InstallmentBillInfo> list = null;
        // try {
        // list = installmentBillInfoDao.getByConUUID(conUUID,
        // StatusEnum.BILL_USING.getValue());
        // } catch (Exception e) {
        // logger.error("用户:{}查询账单发生异常", conUUID, e);
        // }
        return list;
    }

    @Transactional
    @Override
    public Map<String, Object> payment(List<InstallmentBillInfoDto> list) {
        Map<String, Object> result = new HashMap<>();
        // // 还款锁用户
        // try {
        // BigDecimal sumRepayAmt = new BigDecimal(0);
        // String conUUID = list.get(0).getConUUID();
        // for (InstallmentBillInfoDto bill : list) {
        // // 根据ID,用户ConUUID,订单号,分期号,账单号进行还款
        // // 查询订单,
        // InstallmentBillInfo installmentBillInfo =
        // installmentBillInfoDao.getById(bill.getId());
        // // 修改订单
        // BigDecimal sumBillAmt =
        // BigDecimalUtil.sub(installmentBillInfo.getSumBillAmt(),
        // bill.getCurrentRepayAmt());
        // if (sumBillAmt.compareTo(new BigDecimal("0.5")) < 0) {
        // sumBillAmt = BigDecimal.ZERO;
        // installmentBillInfo.setStatus(StatusEnum.BILL_COMPLETE.getValue());
        // }
        // installmentBillInfo.setSumBillAmt(sumBillAmt);
        // installmentBillInfoDao.update(installmentBillInfo);
        // sumRepayAmt = BigDecimalUtil.sub(sumRepayAmt,
        // bill.getCurrentRepayAmt());
        // }
        // // 判断用户所有订单是否还款完毕,如果完毕,需要将用户状态修改为可用,可用额度继续增加
        // AccountInfo accountInfo = accountInfoService.getByConUUID(conUUID);
        // List<InstallmentBillInfo> repayBills = selectBills(conUUID);
        // if (repayBills == null || repayBills.size() == 0) {
        // accountInfo.setAccStatus(StatusEnum.ACC_NORMAL.getValue());
        // // 还清欠款后,删除冷冻状态(逻辑删除)
        // DelayBillInfo delayBillInfo = new DelayBillInfo();
        // delayBillInfo.setConUUID(conUUID);
        // delayBillInfo.setStatus(StatusEnum.DELAY_REPAY.getValue());
        // delayBillInfoDao.update(delayBillInfo);
        // }
        // accountInfo.setAvailLimit(BigDecimalUtil.add(accountInfo.getAvailLimit(),
        // sumRepayAmt));
        // accountInfoService.updateAccount(accountInfo);
        // result.put("retCode", AccountConstant.SUCCESS);
        // result.put("msg", "还款成功");
        // } catch (Exception e) {
        // logger.error("还款失败", e);
        // result.put("retCode", AccountConstant.ERROR);
        // result.put("msg", "还款失败");
        // throw new BizException("还款异常");
        // } finally {
        // // 释放用户
        // }
        return result;
    }

    @Override
    public void stopPeriodization(JSONObject params) throws BizException {
        /** 格式校验 **/
        if (CollectionUtils.isEmpty(params)) {
            throw BizException.BILL_STOP_PERIODIZATION_PARAM_ERROR;
        }
        logger.info("终止分期传入参数：" + JSONObject.toJSONString(params));
        String regId = params.getString("regId");
        String openId = params.getString("openId");
        String orderId = params.getString("orderId");
        if (StringUtil.isEmpty(regId) || StringUtil.isEmpty(openId) || StringUtil.isEmpty(orderId)) {
            throw BizException.BILL_STOP_PERIODIZATION_PARAM_ERROR;
        }

        /** 数据库操作 **/
        this.stopPeriodizationDB(regId, openId, orderId);
    }

    /**
     * 
     * Description: 停止数据库操作
     * 
     * @param
     * @return void
     * @throws @Author wangxinbang Create Date: 2016年11月16日 上午10:36:43
     */
    @Transactional
    public void stopPeriodizationDB(String regId, String openId, String orderId) {
        Map<String, Object> param = new HashMap<>();
        param.put("regId", regId);
        param.put("openId", openId);
        param.put("orderId", orderId);

        InstallmentInfo installmentInfo = this.installmentInfoDao.getLimitInstInfoByOrderId(orderId);
        if (installmentInfo == null) {
            throw BizException.BILL_STOP_PERIODIZATION_BILL_NULL_ERROR;
        }

        this.installmentBillInfoDao.stopInstallmentBillInfo(BeanUtil.mapToBean(param, InstallmentBillInfo.class));
        this.installmentDetailDao.stopInstallmentDetail(BeanUtil.mapToBean(param, InstallmentDetail.class));
        this.installmentInfoDao.stopInstallmentInfo(BeanUtil.mapToBean(param, InstallmentInfo.class));
    }

    @Override
    public void resetPeriodization(JSONObject params) {
        /** 格式校验 **/
        if (CollectionUtils.isEmpty(params)) {
            throw BizException.BILL_STOP_PERIODIZATION_PARAM_ERROR;
        }
        logger.info("重置分期传入参数：" + JSONObject.toJSONString(params));
        String regId = params.getString("regId");
        String openId = params.getString("openId");
        String orderId = params.getString("orderId");
        if (StringUtil.isEmpty(regId) || StringUtil.isEmpty(openId) || StringUtil.isEmpty(orderId)) {
            throw BizException.BILL_STOP_PERIODIZATION_PARAM_ERROR;
        }

        InstallmentInfo installmentInfo = this.installmentInfoDao.getLimitInstInfoByOrderId(orderId);
        if (installmentInfo == null) {
            throw BizException.BILL_STOP_PERIODIZATION_BILL_NULL_ERROR;
        }

        /** 数据库操作 **/
        this.resetPeriodizationDB(regId, openId, orderId);
        this.instInfoServiceImpl.resetInstInfo(installmentInfo);
    }

    /**
     * 
     * Description: 重置分期数据库操作
     * 
     * @param
     * @return void
     * @throws @Author wangxinbang Create Date: 2016年11月16日 上午11:16:10
     */
    @Transactional
    private void resetPeriodizationDB(String regId, String openId, String orderId) {
        Map<String, Object> param = new HashMap<>();
        param.put("regId", regId);
        param.put("openId", openId);
        param.put("orderId", orderId);

        this.installmentBillInfoDao.stopInstallmentBillInfo(BeanUtil.mapToBean(param, InstallmentBillInfo.class));
        this.installmentDetailDao.stopInstallmentDetail(BeanUtil.mapToBean(param, InstallmentDetail.class));
    }

    @Override
    public Map<String, Object> selectCost4Advance(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String orderId = (String) params.get("orderId");
        String regId = (String) params.get("regId");
        BigDecimal breachContract = BigDecimal.ZERO;// 实际应付违约金金额
        BigDecimal lastRepayAmt = BigDecimal.ZERO;// 应付金额(用于调用违约金计算服务)
        BigDecimal repayAmt = BigDecimal.ZERO;// 提前还款金额
        BigDecimal remainPrincipal = BigDecimal.ZERO;// 剩余本金
        // 获取未还款账单（可能是多批放款）
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);
        paramMap.put("regId", regId);
        paramMap.put("status", 5);
        List<InstallmentBillInfo> payBills = installmentBillInfoDao.listBy(paramMap);
        if (payBills == null || payBills.size() == 0) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "未查询到需还款账单");
            logger.info("提前还款->订单:{},未查询到需还款账单", orderId);
            return result;
        }
        // 获取分期计划
        InstallmentPlan installmentPlan = installmentPlanDao.getById(payBills.get(0).getPlanId());
        // 校验账单
        result = validatePayBills(installmentPlan, payBills, orderId);
        if (AccountConstant.ERROR.equals(result.get("retCode"))) {
            return result;
        }
        // 获取免息处理的期数
        int repayNo = getRepayNo(orderId, payBills);
        boolean refundFreeFlag = installmentPlan.getRefundInterest() == 1;// 是否退息
        // boolean refundCostFlag = installmentPlan.getRefundCost() == 1;// 是否退费
        // RefundCost refundCost =
        // refundCostDao.getById(installmentPlan.getRefundCostId());// 退费计划
        RefundCost refundFree = refundCostDao.getById(installmentPlan.getFreeId());// 退息计划
        for (int i = 0; i < payBills.size(); i++) {
            InstallmentBillInfo installmentBillInfo = payBills.get(i);
            if (remainPrincipal == BigDecimal.ZERO) {
                remainPrincipal = installmentBillInfo.getRemainPrincipal();// 获取剩余本金，用于计算违约金
            }
            if (installmentBillInfo.getRepayNo() == repayNo) {
                // 应付金额(用于调用违约金计算服务) = 当期本金 + 利息
                lastRepayAmt = BigDecimalUtil.add(lastRepayAmt, installmentBillInfo.getCurRepayPrincipal(),
                        installmentBillInfo.getCurRepayInterest());
            }
            if (installmentBillInfo.getRepayNo() == repayNo && refundFreeFlag) {// 实际本期应还利息改变
                installmentBillInfo.setCurRepayInterest(this.calculateRealInterest(refundFree, installmentBillInfo));
                // 本期应还 = 本期应还本金 + 本期实际利息
                installmentBillInfo.setCurRepayAmt(BigDecimalUtil.add(installmentBillInfo.getCurRepayPrincipal(),
                        installmentBillInfo.getCurRepayInterest()));
            } else if (installmentBillInfo.getRepayNo() > repayNo) {
                // 实际本期应还即本期应还本金
                installmentBillInfo.setCurRepayAmt(installmentBillInfo.getCurRepayPrincipal());
            }

            // 3，计算实际应还费用 = 每期应还金额之和
            repayAmt = BigDecimalUtil.add(repayAmt, installmentBillInfo.getCurRepayAmt());
        }

        // 获取违约金
        if (installmentPlan.getBreachContract() == 1) {// 收取违约金
            breachContract =
                    BigDecimalUtil.add(
                            breachContract,
                            breachContractService.getBreachContract(remainPrincipal,
                                    payBills.get(0).getInstallSumAmt(), repayAmt, lastRepayAmt,
                                    installmentPlan.getBreachContractId()));
        }

        // 提前还款账单放入redis中30分钟
        this.saveAdvancedPayBillsInfo(orderId, payBills, breachContract);

        // 实际应还总费用
        repayAmt = BigDecimalUtil.div(BigDecimalUtil.add(repayAmt, breachContract), new BigDecimal("100"));
        logger.info("提前还款->订单:{},违约金金额:{},总额(/100):{}", orderId, breachContract, repayAmt);
        result.put("retCode", AccountConstant.SUCCESS);
        result.put("retMsg", "提前还款查询金额成功");
        result.put("repayAmt", repayAmt + "");
        result.put("orderId", orderId);
        return result;
    }

    private Map<String, Object> validatePayBills(InstallmentPlan installmentPlan, List<InstallmentBillInfo> payBills,
            String orderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", AccountConstant.SUCCESS);
        // 判断是否允许提前还款
        if (installmentPlan.getPrepayMent() != 1) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "该订单不允许提前还款");
            logger.info("提前还款->订单:{},该订单不允许提前还款", orderId);
            return result;
        }
        // 判断是否到达最早还款日
        if (!isGetRepayTime(payBills)) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "未到最早还款日期");
            logger.info("提前还款->订单:{},未到最早还款日期", orderId);
            return result;
        }
        // 判断是否超过最后一期还款日
        if (isOverLastRepayDay(payBills)) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "已超过该订单提前还款时间，不允许提前还款");
            logger.info("提前还款->订单:{},已超过该订单提前还款时间，不允许提前还款", orderId);
            return result;
        }
        return result;
    }

    // 获取提前还款信息
    private String getAdvancedPayBillsInfo(Map<String, Object> params) {
        String orderId = (String) params.get("orderId");
        String regId = (String) params.get("regId");
        BigDecimal breachContract = BigDecimal.ZERO;// 实际应付违约金金额
        BigDecimal lastRepayAmt = BigDecimal.ZERO;// 最后一期应付金额(用于调用违约金计算服务)
        BigDecimal repayAmt = BigDecimal.ZERO;// 提前还款金额
        BigDecimal remainPrincipal = BigDecimal.ZERO;// 剩余本金
        // 获取未还款账单（可能是多批放款）
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);
        paramMap.put("regId", regId);
        paramMap.put("status", 5);
        List<InstallmentBillInfo> payBills = installmentBillInfoDao.listBy(paramMap);
        // 获取分期计划
        InstallmentPlan installmentPlan = installmentPlanDao.getById(payBills.get(0).getPlanId());
        // 获取免息处理的期数
        int repayNo = getRepayNo(orderId, payBills);
        boolean refundFreeFlag = installmentPlan.getRefundInterest() == 1;// 是否退息
        // boolean refundCostFlag = installmentPlan.getRefundCost() == 1;// 是否退费
        // RefundCost refundCost =
        // refundCostDao.getById(installmentPlan.getRefundCostId());// 退费计划
        RefundCost refundFree = refundCostDao.getById(installmentPlan.getFreeId());// 退息计划
        for (int i = 0; i < payBills.size(); i++) {
            InstallmentBillInfo installmentBillInfo = payBills.get(i);
            if (remainPrincipal == BigDecimal.ZERO) {
                remainPrincipal = installmentBillInfo.getRemainPrincipal();// 获取剩余本金，用于计算违约金
            }
            if (installmentBillInfo.getRepayNo() == repayNo) {
                // 应付金额(用于调用违约金计算服务) = 当期本金 + 利息
                lastRepayAmt = BigDecimalUtil.add(lastRepayAmt, installmentBillInfo.getCurRepayPrincipal(),
                        installmentBillInfo.getCurRepayInterest());
            }
            if (installmentBillInfo.getRepayNo() == repayNo && refundFreeFlag) {// 实际本期应还利息改变
                installmentBillInfo.setCurRepayInterest(this.calculateRealInterest(refundFree, installmentBillInfo));
                // 本期应还 = 本期应还本金 + 本期实际利息
                installmentBillInfo.setCurRepayAmt(BigDecimalUtil.add(installmentBillInfo.getCurRepayPrincipal(),
                        installmentBillInfo.getCurRepayInterest()));
            } else if (installmentBillInfo.getRepayNo() > repayNo) {
                // 实际本期应还即本期应还本金
                installmentBillInfo.setCurRealRepayamt(installmentBillInfo.getCurRepayPrincipal());
            }

            // 3，计算实际应还费用 = 每期应还金额之和
            repayAmt = BigDecimalUtil.add(repayAmt, installmentBillInfo.getCurRepayAmt());
        }

        // 获取违约金
        if (installmentPlan.getBreachContract() == 1) {// 收取违约金
            breachContract =
                    BigDecimalUtil.add(
                            breachContract,
                            breachContractService.getBreachContract(remainPrincipal,
                                    payBills.get(0).getInstallSumAmt(), repayAmt, lastRepayAmt,
                                    installmentPlan.getBreachContractId()));
        }

        String advancedPayBillsInfo = saveAdvancedPayBillsInfo(orderId, payBills, breachContract);
        return advancedPayBillsInfo;
    }

    // 提前还款信息存入redis中
    private String saveAdvancedPayBillsInfo(String orderId, List<InstallmentBillInfo> payBills,
            BigDecimal breachContract) {
        AdvancedPayBillsDto advancedPayBillsDto = new AdvancedPayBillsDto();
        advancedPayBillsDto.setPayBills(payBills);
        advancedPayBillsDto.setBreachContract(breachContract);
        String key = "AdvancedPayBills." + orderId;
        String value = JSON.toJSONString(advancedPayBillsDto);
        queryBillInfoCache.put(key, value, 60 * 30);
        return value;
    }

    // 判断是否到达最早还款时间
    private boolean isGetRepayTime(List<InstallmentBillInfo> payBills) {
        if (payBills.get(0).getEarliestPayDate() == null) {
            return true;
        }
        Date date = getToday();
        return date.compareTo(payBills.get(0).getEarliestPayDate()) > 0;
    }

    // 判断是否超过最后一期还款日
    private boolean isOverLastRepayDay(List<InstallmentBillInfo> payBills) {
        Date date = getToday();
        return date.compareTo(payBills.get(payBills.size() - 1).getDelayBeginDate()) > 0;
    }

    // 获取今天日期
    private Date getToday() {
        String today = queryBillInfoCache.get(RedisLockConstant.BIZ_LOCKED_DAY_LOCK_KEY);
        if (today == null || "".equals(today)) {
            today = DateUtil.getDateString(Calendar.getInstance(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        return DateUtil.parseDate(today, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
    }

    // 获取免息处理的期数
    private int getRepayNo(String orderId, List<InstallmentBillInfo> payBills) {
        int repayNo = 0;
        try {
            String today = queryBillInfoCache.get(RedisLockConstant.BIZ_LOCKED_DAY_LOCK_KEY);
            if (today == null || "".equals(today)) {
                today = DateUtil.getDateString(Calendar.getInstance(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            }
            Calendar cal = DateUtil.parseCalendar(today, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            cal.add(Calendar.MONTH, 1);
            String nextDay = DateUtil.getDateString(cal, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            repayNo = installmentBillInfoDao.getRepayNoByOrderId(orderId, today, nextDay);// 获取免息处理的期数
            if (repayNo == 0) {// 则为最后一期做免息处理
                repayNo = payBills.get(payBills.size() - 1).getRepayNo();
            }
        } catch (Exception e) {
            logger.error("获取还款期数异常", e);
            return 0;
        }
        logger.debug("获取免息处理的期数:{}", repayNo);
        return repayNo;
    }

    // 判断是否逾期
    @SuppressWarnings("unused")
    private boolean judgeOverdue(Date delayDate) {
        // 判断是否逾期，逾期月份
        Calendar delayBeginDate = DateUtil.fromDate(delayDate);
        Calendar today = DateUtil.fromDate(getToday());
        return today.after(delayBeginDate);
    }

    // 计算提前还款的实际利息或实际费用
    private BigDecimal calculateRealInterest(RefundCost refundCost, InstallmentBillInfo installmentBillInfo) {
        Calendar today = DateUtil.fromDate(getToday());
        Date delayBeginDate = installmentBillInfo.getDelayBeginDate();// 获取最迟还款时间
        Calendar delayDate = DateUtil.fromDate(delayBeginDate);
        delayDate.add(Calendar.MONTH, -1);

        BigDecimal result = BigDecimal.ZERO;
        int days = 0;// 计算利息天数
        String type = refundCost.getType();
        if ("1".equals(type)) {// 退费

        } else if ("2".equals(type)) {// 退息
            switch (refundCost.getRefundMethod()) {
                case "1":// 按照实际占用天数退费
                    days = DateUtil.daysBetween(delayDate, today);
                    result = BigDecimalUtil.mul(installmentBillInfo.getPreInterest(), new BigDecimal(days));
                    break;
                case "2":// 退息，实际占用天数资金退息
                    result = installmentBillInfo.getCurRepayInterest();
                    break;
                case "3":// 全部还款不退息
                    break;
            }
        }

        return result;
    }

    @Override
    public BigDecimal getSAmtByOrderIdAndRepayNo(Map<String, Object> params) {
        return installmentBillInfoDao.getSAmtByOrderIdAndRepayNo(params);
    }

    @Override
    public InstallmentBillInfo getInstBillByOrderIdAndRepayNo(Map<String, Object> params) {
        return installmentBillInfoDao.getInstBillByOrderIdAndRepayNo(params);
    }

    @Transactional
    @Override
    /* 正常还款模式 */
    public Map<String, Object> paymentNormal(List<InstallmentBillInfo> installmentBillInfoList) {
        Map<String, Object> result = new HashMap<>();

        long num = installmentBillInfoDao.updateForNormalRepayment(installmentBillInfoList);
        if (num > 0) {
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("msg", "还款成功");
        } else {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "没有这个订单 序号");
        }

        return result;
    }

    @Transactional
    @Override
    /* 提前还款模式 */
    public Map<String, Object> paymentAdvance(InstallmentBillInfo installmentBillInfo) {
        Map<String, Object> result = new HashMap<>();

        long num = installmentBillInfoDao.updateForAdvanceRepayment(installmentBillInfo);
        if (num > 0) {
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("msg", "还款成功");
        } else {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "没有这个订单号");
        }

        return result;
    }

    @Override
    public List<InstallmentBillInfo> getAllInstallmentBillInfoListForOrderId(String orderId) {
        return installmentBillInfoDao.getAllInstallmentBillInfoListForOrderId(orderId);
    }

    // @Override
    // public Map<String, Object> payment(String jsonStr) {
    // Map<String, Object> result = new HashMap<String, Object>();
    // List<PaymentDto> list = JSONObject.parseArray(jsonStr, PaymentDto.class);
    // // JSONObject payJson = JSONObject.parseObject(jsonStr);// 解析信息
    // // List<PaymentDto> list =
    // JSONObject.parseArray(JSONObject.toJSONString(payJson),
    // // PaymentDto.class);
    //
    // /* 日志信息列表 */
    // List<AccRepayHistory> AccRepayHistoryList = new ArrayList<>();
    //
    // /* 还款头信息 */
    // long orderId = 0;// 订单号
    // long openId = 0;// 开户号
    // String repayModel = null;// 还款模式
    // String regId = null;// 手机号
    // String merchantNo = null;// 商户号
    // String tradeNo = null;// 流水号
    // Date repayDate = null;// 还款时间
    // BigDecimal sumAmt = BigDecimal.ZERO;// 总还款金额
    //
    // logger.info("账单还款开始处理");
    // /* 循环处理每条订单 */
    // for (PaymentDto paymentDto : list) {
    //
    // /* 获取还款列表头信息数据 */
    // orderId = paymentDto.getOrderId();
    // openId = paymentDto.getOpenId();
    // repayModel = paymentDto.getRepayModel();
    // regId = paymentDto.getRegId();
    // merchantNo = paymentDto.getMerchantNo();
    // tradeNo = paymentDto.getTradeNo();
    // repayDate = paymentDto.getRepayDate();
    // sumAmt = paymentDto.getSumAmt();
    //
    // if (StringUtils.isEmpty(repayModel)) {// 还款方式为空
    // logger.error("还款方式为空!");
    // result.put("retCode", AccountConstant.ERROR);
    // result.put("msg", "没有还款方式信息");
    // } else if (REPAY_MODEL_NORMAL.equals(repayModel)) {// 正常还款
    // logger.debug("正常还款模式");
    // /* 装配还款信息列表 */
    // List<InstallmentBillInfo> installmentBillInfoList = new ArrayList<>();
    //
    // /* 循环获取还款信息 */
    // for (RepayList repayList : paymentDto.getRepayList()) {
    // InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo();
    // installmentBillInfo.setOrderId(Long.toString(orderId));// 订单号
    // installmentBillInfo.setRepayNo(repayList.getRepayNo());// 序号
    // installmentBillInfo.setCurRealRepayamt(repayList.getAmt());// 金额
    // installmentBillInfo.setCurRepayDate(repayDate);// 还款时间
    // installmentBillInfo.setStatus(3);// 账单状态 1，未还款，2,部分还款 3，全部还款，4，失效账单
    //
    // /* 装填入list,等待更新 */
    // installmentBillInfoList.add(installmentBillInfo);
    //
    // /* 日志部分 */
    // AccRepayHistory accRepayHistory = new AccRepayHistory();
    // accRepayHistory.setOrderId(Long.toString(orderId));// 订单号
    // accRepayHistory.setRepayNo(Integer.toString(repayList.getRepayNo()));//
    // 序号
    // accRepayHistory.setOpenId(Long.toString(openId));// 开户号
    // accRepayHistory.setRepayAmt(repayList.getAmt().toString());// 还款金额
    // accRepayHistory.setChargeAmount(repayList.getAmt().toString());// 收取金额
    // accRepayHistory.setTradeNo(tradeNo);// 交易号
    // accRepayHistory.setMerchantNo(merchantNo);// 商户号
    // accRepayHistory.setRegId(regId);// 手机号
    //
    // /* 装填入list,等待更新 */
    // AccRepayHistoryList.add(accRepayHistory);
    //
    // /* 金额校验 */
    // sumAmt = BigDecimalUtil.sub(sumAmt,
    // installmentBillInfo.getCurRealRepayamt());
    // }
    // if (sumAmt.compareTo(BigDecimal.ZERO) != 0) {
    // logger.error("总金额不等于列表金额");
    // result.put("retCode", AccountConstant.ERROR);
    // result.put("msg", "金额异常");
    // break;
    // } else {
    // try {
    // logger.debug("正常还款开始更新账单信息");
    // result = this.paymentNormal(installmentBillInfoList);
    // logger.debug("正常还款结束更新账单信息");
    // /* 检查更新情况 */
    // if (AccountConstant.SUCCESS.equals(result.get("retCode"))) {
    // logger.debug("正常还款开始写入日志表");
    // accRepayHistoryService.batchInsertAccRepayHistory(AccRepayHistoryList);
    // logger.debug("正常还款结束写入日志表");
    // } else
    // break;
    //
    // } catch (BizException e) {
    // logger.error("还款异常", e);
    // result.put("retCode", AccountConstant.ERROR);
    // result.put("msg", "还款异常");
    // }
    // }
    // } else if (REPAY_MODEL_ALL.equals(repayModel)) {// 提前还款
    // logger.debug("提前还款模式");
    //
    // /* 装配还款信息 */
    // InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo(); //
    // 提前还款只支持一个订单
    //
    // installmentBillInfo.setOrderId(Long.toString(orderId));// 订单号
    // installmentBillInfo.setCurRepayDate(repayDate);// 还款时间
    // installmentBillInfo.setStatus(3);// 账单状态 1，未还款，2,部分还款 3，全部还款，4，失效账单
    //
    // /* 提前准备日志信息 */
    // List<InstallmentBillInfo> installmentBillInfoList = new ArrayList<>();
    // try {
    // installmentBillInfoList =
    // this.getAllInstallmentBillInfoListForOrderId(Long.toString(orderId));
    // } catch (BizException e) {
    // logger.error("还款异常", e);
    // result.put("retCode", AccountConstant.ERROR);
    // result.put("msg", "获取账单信息异常");
    // }
    // for (InstallmentBillInfo installmentBillInfoTmp :
    // installmentBillInfoList) {
    // /* 日志部分 */
    // AccRepayHistory accRepayHistory = new AccRepayHistory();
    // accRepayHistory.setOrderId(Long.toString(orderId));// 订单号
    // accRepayHistory.setRepayNo(Integer.toString(installmentBillInfoTmp.getRepayNo()));//
    // 序号
    // accRepayHistory.setOpenId(Long.toString(openId));// 开户号
    // accRepayHistory.setRepayAmt(installmentBillInfoTmp.getCurRepayAmt().toString());//
    // 还款金额
    // accRepayHistory.setChargeAmount(installmentBillInfoTmp.getCurRepayAmt().toString());//
    // 收取金额
    // accRepayHistory.setTradeNo(tradeNo);// 交易号
    // accRepayHistory.setMerchantNo(merchantNo);// 商户号
    // accRepayHistory.setRegId(regId);// 手机号
    //
    // /* 装填入list,等待更新 */
    // AccRepayHistoryList.add(accRepayHistory);
    // }
    //
    // try {
    // logger.debug("提前还款结束更新账单信息");
    // result = this.paymentAdvance(installmentBillInfo);
    // logger.debug("提前还款结束更新账单信息");
    // /* 检查更新情况 */
    // if (AccountConstant.SUCCESS.equals(result.get("retCode"))) {
    // logger.debug("提前还款开始写入日志表");
    // accRepayHistoryService.batchInsertAccRepayHistory(AccRepayHistoryList);
    // logger.debug("提前还款结束写入日志表");
    // } else
    // break;
    //
    // } catch (BizException e) {
    // logger.error("还款异常", e);
    // result.put("retCode", AccountConstant.ERROR);
    // result.put("msg", "还款异常");
    // }
    //
    // } else if (REPAY_MODEL_PART.equals(repayModel)) {// 部分还款
    // logger.debug("部分还款模式");
    // result.put("retCode", AccountConstant.ERROR);
    // result.put("msg", "暂不支持部分还款");
    // }
    //
    // }
    // logger.info("账单还款处理结束");
    //
    // return result;
    // }

    @Transactional
    @Override
    public Map<String, Object> normalPrepayment(Map<String, List<InstallmentBillInfo>> map, String tradeNo,
            String repayType, String bankCardNo, String bankName) {
        logger.debug("正常还款模式开始......");
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", AccountConstant.SUCCESS);
        for (Entry<String, List<InstallmentBillInfo>> e : map.entrySet()) {
            /* 循环获取还款信息 */
            List<AccRepayHistory> accRepayHistoryList = new ArrayList<>();
            for (InstallmentBillInfo installmentBillInfo : e.getValue()) {

                // 金额放大100倍
                installmentBillInfo.setCurRealRepayamt(
                        BigDecimalUtil.mul(installmentBillInfo.getCurRealRepayamt(), new BigDecimal("100")));

                // 支付流水
                AccRepayHistory accRepayHistory = new AccRepayHistory();
                accRepayHistory.setOrderId(installmentBillInfo.getOrderId());// 订单号
                accRepayHistory.setRepayNo(installmentBillInfo.getRepayNo());// 序号
                accRepayHistory.setOpenId(installmentBillInfo.getOpenId());// 开户号
                accRepayHistory.setRepayAmt(installmentBillInfo.getCurRealRepayamt());// 还款金额
                accRepayHistory.setChargeAmount(BigDecimal.ZERO);// 收取金额
                accRepayHistory.setTradeNo(tradeNo);// 交易号
                accRepayHistory.setMerchantNo(installmentBillInfo.getMerchantNo());// 商户号
                accRepayHistory.setRegId(installmentBillInfo.getRegId());// 手机号
                accRepayHistory.setRepayType(repayType);// 还款类型
                accRepayHistory.setBankCardNo(bankCardNo);// 银行卡号
                accRepayHistory.setBankName(bankName);// 银行名称
                /* 装填入list,等待更新 */
                accRepayHistoryList.add(accRepayHistory);
            }
            logger.debug("正常还款开始更新账单信息");
            long num = installmentBillInfoDao.updateForNormalRepayment(e.getValue());
            if (num == 0) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("msg", "该订单这个序号账单已还款");
                return result;
            }
            logger.debug("正常还款开始写入日志表");
            num = accRepayHistoryService.batchInsertAccRepayHistory(accRepayHistoryList);
            if (num > 0) {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("msg", "正常还款成功");
                result.put("billStatus", 3);
            } else {
                result.put("retCode", AccountConstant.ERROR);
                result.put("msg", "还款成功，插入日志表失败！");
            }
            // 清除redis
            String key = "AdvancedPayBills." + e.getKey();
            try {
                queryBillInfoCache.remove(key);
                logger.debug("清除redis结束");
            } catch (Exception exception) {
                logger.error("清除提前还款信息redis错误！");
            }
        }
        return result;
    }

    @Transactional
    @Override
    public Map<String, Object> allPrepayment(String orderId, String tradeNo, Date repayDate, String merchantNo,
            String repayType, String bankCardNo, String bankName, BigDecimal sumAmt) {
        logger.debug("提前还款模式开始......");
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", AccountConstant.SUCCESS);

        // 查询redis
        String key = "AdvancedPayBills." + orderId;
        String redisResult = null;
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);

        try {
            redisResult = queryBillInfoCache.get(key);
            if (redisResult == null || "".equals(redisResult)) {
                redisResult = this.getAdvancedPayBillsInfo(params);
            }
        } catch (Exception e) {
            logger.debug("获取redis值异常");
            redisResult = this.getAdvancedPayBillsInfo(params);
        }
        if (redisResult == null || "".equals(redisResult)) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "查询该订单账单信息错误！");

            // 写操作日志
            logger.debug("开始记录出错操作日志");
            SysOperateInfo journal = new SysOperateInfo();
            journal.setClassName("/consumer.service/src/main/java/com/iqb/consumer/service/account/BillBizServiceImpl");
            journal.setMethod("allPrepayment");
            journal.setReason("ERROR");
            journal.setCreateBy("OperatorJournalAdvice");
            this.journalService.insert(journal);

            return result;
        }
        AdvancedPayBillsDto advancedPayBillsDto = JSONObject.parseObject(redisResult, AdvancedPayBillsDto.class);

        /* 准备支付流水信息 */
        List<InstallmentBillInfo> installmentBillInfoList = new ArrayList<>();
        List<AccRepayHistory> accRepayHistoryList = new ArrayList<>();
        // installmentBillInfoList =
        // this.getAllInstallmentBillInfoListForOrderId(orderId);

        installmentBillInfoList = advancedPayBillsDto.getPayBills();

        if (installmentBillInfoList.size() == 0) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "这个订单号下没有未还账单");
        } else {

            for (int i = 0; i < installmentBillInfoList.size(); i++) {
                InstallmentBillInfo installmentBillInfo = installmentBillInfoList.get(i);
                if (i == 0) {
                    // 还款时间
                    installmentBillInfo.setCurRepayDate(repayDate);
                    // 支付流水
                    AccRepayHistory accRepayHistory = new AccRepayHistory();
                    accRepayHistory.setOrderId(installmentBillInfo.getOrderId());// 订单号
                    accRepayHistory.setRepayNo(installmentBillInfo.getRepayNo());// 序号
                    accRepayHistory.setOpenId(installmentBillInfo.getOpenId());// 开户号
                    accRepayHistory.setRepayAmt(sumAmt);// 真实还款金额
                    accRepayHistory.setChargeAmount(BigDecimal.ZERO);// 收取金额
                    accRepayHistory.setTradeNo(tradeNo);// 交易号
                    accRepayHistory.setMerchantNo(merchantNo);// 商户号
                    accRepayHistory.setRegId(installmentBillInfo.getRegId());// 手机号
                    accRepayHistory.setRepayType(repayType);// 还款类型
                    accRepayHistory.setBankCardNo(bankCardNo);// 银行卡号
                    accRepayHistory.setBankName(bankName);// 银行名称

                    /* 装填入list,等待更新 */
                    accRepayHistoryList.add(accRepayHistory);
                }
            }

            // 添加违约金记录
            if (advancedPayBillsDto.getBreachContract().compareTo(BigDecimal.ZERO) > 0) {
                AccRepayHistory accRepayHistory = new AccRepayHistory();
                accRepayHistory.setOrderId(orderId);// 订单号
                accRepayHistory.setOpenId(accRepayHistoryList.get(0).getOpenId());// 开户号
                accRepayHistory.setRegId(accRepayHistoryList.get(0).getRegId());// 手机号
                accRepayHistory.setRepayAmt(advancedPayBillsDto.getBreachContract());// 违约金
                accRepayHistory.setChargeAmount(BigDecimal.ZERO);// 收取金额
                accRepayHistory.setTradeNo(tradeNo);// 交易号
                accRepayHistory.setMerchantNo(merchantNo);// 商户号
                accRepayHistory.setRepayType(repayType);// 还款类型
                accRepayHistory.setBankCardNo(bankCardNo);// 银行卡号
                accRepayHistory.setBankName(bankName);// 银行名称
                accRepayHistory.setRemark("违约金记录");
                /* 装填入list,等待更新 */
                accRepayHistoryList.add(accRepayHistory);
            }
            logger.debug("提前还款开始更新账单信息");
            // result = this.paymentAdvance(installmentBillInfoList.get(0));
            /**
             * 更新订单明细状态：当期还款金额为总还款金额，还款状态为 全部还款 ,其他全部改为 账单失效 2017-05-04 haojinlong
             */
            long num = 0;
            for (int i = 0; i < installmentBillInfoList.size(); i++) {
                if (i == 0) {
                    InstallmentBillInfo installmentBillInfoA = installmentBillInfoList.get(0);
                    installmentBillInfoA.setStatus(AccountConstant.STATUS_ALLREPAY);
                    installmentBillInfoA.setCurRepayAmt(sumAmt);
                    installmentBillInfoA.setCurRepayDate(repayDate);
                    installmentBillInfoDao.updateForAdvanceRepaymentById(installmentBillInfoA);
                } else {
                    InstallmentBillInfo installmentBillInfo = installmentBillInfoList.get(i);
                    installmentBillInfo.setStatus(AccountConstant.STATUS_INVALID);
                    installmentBillInfo.setCurRepayAmt(BigDecimal.ZERO);
                    installmentBillInfo.setCurRepayDate(repayDate);
                    installmentBillInfoDao.updateForAdvanceRepaymentById(installmentBillInfo);
                }
                num++;
            }
            if (num > 0) {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("msg", "还款成功");
                result.put("billStatus", 3);
            } else {
                result.put("retCode", AccountConstant.ERROR);
                result.put("msg", "没有这个订单号");
            }
            logger.debug("提前还款开始写入日志表");
            num = accRepayHistoryService.batchInsertAccRepayHistory(accRepayHistoryList);
            if (num > 0) {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("msg", "提前还款成功");
                result.put("billStatus", 3);
            } else {
                result.put("retCode", AccountConstant.ERROR);
                result.put("msg", "没有这个订单+序号");
            }
        }
        return result;

    }

    // 提前结算相关信息
    @Override
    public Map<String, Object> balanceAdvance(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String orderId = (String) params.get("orderId");
        String regId = (String) params.get("regId");
        BigDecimal breachContract = BigDecimal.ZERO;// 实际应付违约金金额
        BigDecimal lastRepayAmt = BigDecimal.ZERO;// 应付金额(用于调用违约金计算服务)
        BigDecimal repayAmt = BigDecimal.ZERO;// 提前还款金额
        BigDecimal remainPrincipal = BigDecimal.ZERO;// 剩余本金
        // 获取未还款账单（可能是多批放款）
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);
        paramMap.put("regId", regId);
        paramMap.put("status", 5);
        List<InstallmentBillInfo> payBills = installmentBillInfoDao.listBy(paramMap);
        if (payBills == null || payBills.size() == 0) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "未查询到需还款账单");
            logger.info("提前还款->订单:{},未查询到需还款账单", orderId);
            return result;
        }
        // 已还期数 = 第一笔未还期数 - 1
        int hasRepayNo = payBills.get(0).getRepayNo() - 1;
        // 获取分期计划
        InstallmentPlan installmentPlan = installmentPlanDao.getById(payBills.get(0).getPlanId());
        // 校验账单
        result = validatePayBills(installmentPlan, payBills, orderId);
        if (AccountConstant.ERROR.equals(result.get("retCode"))) {
            return result;
        }
        // 获取免息处理的期数
        int repayNo = getRepayNo(orderId, payBills);
        boolean refundFreeFlag = installmentPlan.getRefundInterest() == 1;// 是否退息
        // boolean refundCostFlag = installmentPlan.getRefundCost() == 1;// 是否退费
        // RefundCost refundCost =
        // refundCostDao.getById(installmentPlan.getRefundCostId());// 退费计划
        RefundCost refundFree = refundCostDao.getById(installmentPlan.getFreeId());// 退息计划
        for (int i = 0; i < payBills.size(); i++) {
            InstallmentBillInfo installmentBillInfo = payBills.get(i);
            if (remainPrincipal == BigDecimal.ZERO) {
                remainPrincipal = installmentBillInfo.getRemainPrincipal();// 获取剩余本金，用于计算违约金
            }
            if (installmentBillInfo.getRepayNo() == repayNo) {
                // 应付金额(用于调用违约金计算服务) = 当期本金 + 利息
                lastRepayAmt = BigDecimalUtil.add(lastRepayAmt, installmentBillInfo.getCurRepayPrincipal(),
                        installmentBillInfo.getCurRepayInterest());
            }
            if (installmentBillInfo.getRepayNo() == repayNo && refundFreeFlag) {// 实际本期应还利息改变
                installmentBillInfo.setCurRepayInterest(this.calculateRealInterest(refundFree, installmentBillInfo));
                // 本期应还 = 本期应还本金 + 本期实际利息
                installmentBillInfo.setCurRepayAmt(BigDecimalUtil.add(installmentBillInfo.getCurRepayPrincipal(),
                        installmentBillInfo.getCurRepayInterest()));
            } else if (installmentBillInfo.getRepayNo() > repayNo) {
                // 实际本期应还即本期应还本金
                installmentBillInfo.setCurRepayAmt(installmentBillInfo.getCurRepayPrincipal());
            }

            // 3，计算实际应还费用 = 每期应还金额之和
            repayAmt = BigDecimalUtil.add(repayAmt, installmentBillInfo.getCurRepayAmt());
        }

        // 获取违约金
        if (installmentPlan.getBreachContract() == 1) {// 收取违约金
            breachContract =
                    BigDecimalUtil.add(
                            breachContract,
                            breachContractService.getBreachContract(remainPrincipal,
                                    payBills.get(0).getInstallSumAmt(), repayAmt, lastRepayAmt,
                                    installmentPlan.getBreachContractId()));
        }

        // 提前还款账单放入redis中30分钟
        this.saveAdvancedPayBillsInfo(orderId, payBills, breachContract);

        // 获取已还金额
        BigDecimal hasRepayAmt = this.getRealRepayAmtByOrderId(orderId);

        // 实际应还总费用
        repayAmt = BigDecimalUtil.div(BigDecimalUtil.add(repayAmt, breachContract), new BigDecimal("100"));
        remainPrincipal = BigDecimalUtil.div(remainPrincipal, new BigDecimal("100"));

        logger.info("提前还款->订单:{},违约金金额:{},总额(/100):{},剩余本金(/100):{},已还金额(/100):{}, 已还期数:{}", orderId, breachContract,
                repayAmt, remainPrincipal, hasRepayAmt, hasRepayNo);
        result.put("retCode", AccountConstant.SUCCESS);
        result.put("retMsg", "提前还款查询金额成功");
        result.put("repayAmt", repayAmt);// 应还金额
        result.put("orderId", orderId);
        result.put("remainPrincipal", remainPrincipal);// 剩余本金
        result.put("hasRepayNo", hasRepayNo);// 已还期数
        result.put("hasRepayAmt", hasRepayAmt);// 已还金额
        return result;
    }

    @Override
    public Map<String, Object> partPrepayment() {
        return null;
    }

    @Override
    public Long checkIntertemporal(String orderId, String repayNo) {
        return installmentBillInfoDao.checkIntertemporal(orderId, repayNo);
    }

    @Override
    public PageBean queryBillByParams(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        return installmentBillInfoDao.listPage(pageParam, params, SQL_QUERYBILLBYPARAMS);
    }

    @Override
    public Map<String, Object> getRepayNo(String orderId) {
        Map<String, Object> result = new HashMap<>();
        int num = installmentBillInfoDao.getRepayNo(orderId);
        Map<String, Object> map = installmentBillInfoDao.getRemainPrincipal(orderId);
        if (!CollectionUtils.isEmpty(map)) {
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("msg", "获得当前订单最后还款期数成功");
            result.put("num", num);
            result.put("remainPrincipal", map.get("remainPrincipal"));
        } else {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "获得当前订单最后还款期数失败");
        }
        return result;
    }

    @Override
    public BigDecimal getRealRepayAmtByOrderId(String orderId) {
        double hasRepayAmt = installmentBillInfoDao.getRealRepayAmtByOrderId(orderId);
        BigDecimal result = BigDecimalUtil.div(new BigDecimal(hasRepayAmt), new BigDecimal(100));
        return result;
    }

    @Override
    public List<ToRiskBillInfo> getToRiskBillInfo(Map<String, Object> params) {
        return installmentBillInfoDao.getToRiskBillInfo(params);
    }

    @Override
    public PageBean selectShouldDebtDetail(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        return installmentBillInfoDao.selectShouldDebtDetail(pageParam, params);
    }

    @Override
    public BigDecimal getTotalRepayAmt(Map<String, Object> params) {
        return installmentBillInfoDao.getTotalRepayAmt(params);
    }

    @Override
    public List<ShouldDebtDetail> selectShouldDebtDetail2(Map<String, Object> params) {
        return installmentBillInfoDao.selectShouldDebtDetail(params);
    }

    /**
     * 资产存量报表查询 变更历史：2017-06-22 将资产存量报表一次查询全部数据改为分段查询并合并数据
     * 
     * @author haojinlong
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月23日
     */
    @Override
    public PageBean listStockStatisticsPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());
        String curRepayDate = (String) params.get("curRepayDate");
        if (curRepayDate == null || curRepayDate.equals("")) {
            curRepayDate = DateUtil.format(new Date(), DateUtil.SHORT_DATE_FORMAT);
            params.put("curRepayDate", curRepayDate);
        }
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        /** 获取符合条件的存量数据 -账单信息 不包括期数金额 **/
        PageBean pageBeanList = installmentBillInfoDao.listStockStatisticsPageNew(pageParam, params);
        /** 获取符合条件的存量数据 -金额 **/
        PageBean pageBeanAmtList = installmentBillInfoDao.listStockStatisticsAmtPage(pageParam, params);
        /** 获取符合条件的存量数据 -期数 **/
        PageBean pageBeanNoList = installmentBillInfoDao.listStockStatisticsNosPage(pageParam, params);

        List<Object> list = pageBeanList.getRecordList();
        List<Object> amtList = pageBeanAmtList.getRecordList();
        List<Object> noList = pageBeanNoList.getRecordList();

        List<StockStatisticsBean> tempList = new ArrayList<>();
        List<Object> resultList = new ArrayList<>();
        // 将list与amtList合并
        if (list != null && amtList != null) {
            for (Object pageBean : list) {
                StockStatisticsBean bean = (StockStatisticsBean) pageBean;
                for (Object amtBean : amtList) {
                    StockStatisticsBean beanA = (StockStatisticsBean) amtBean;
                    if (bean.getOrderId().equals(beanA.getOrderId())) {
                        bean.setInstallSumAmt(beanA.getInstallSumAmt());
                        bean.setCurRepayPrincipal(beanA.getCurRepayPrincipal());
                        bean.setRemainPrincipal(beanA.getRemainPrincipal());
                    }
                }
                tempList.add(bean);
            }
        }
        // 将合并后的tempList与noList合并
        if (tempList != null && noList != null) {
            for (StockStatisticsBean bean : tempList) {
                for (Object noBean : noList) {
                    StockStatisticsBean beanB = (StockStatisticsBean) noBean;
                    if (bean.getOrderId().equals(beanB.getOrderId())) {
                        bean.setInstallTerms(beanB.getInstallTerms());
                        bean.setNonRepayno(beanB.getNonRepayno());
                        bean.setRepayNo(beanB.getRepayNo());
                    }
                }
                resultList.add(bean);
            }
        }
        return new PageBean(pageNum, numPerPage, pageBeanList.getTotalCount(), resultList);
    }

    /**
     * 存量统计报表(不分页)
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月29日
     */
    @Override
    public List<StockStatisticsBean> listStockStatistics(Map<String, Object> paramMap) {
        String curRepayDate = (String) paramMap.get("curRepayDate");
        if (curRepayDate == null || curRepayDate.equals("")) {
            curRepayDate = DateUtil.format(new Date(), DateUtil.SHORT_DATE_FORMAT);
            paramMap.put("curRepayDate", curRepayDate);
        }

        List<StockStatisticsBean> pageBeanList = installmentBillInfoDao.listStockStatistics(paramMap);
        List<StockStatisticsBean> pageBeanAmtList = installmentBillInfoDao.listStockStatisticsAmt(paramMap);
        List<StockStatisticsBean> pageBeanNoList = installmentBillInfoDao.listStockStatisticsNos(paramMap);

        List<StockStatisticsBean> tempList = new ArrayList<>();
        List<StockStatisticsBean> resultList = new ArrayList<>();
        // 将pageBeanList与pageBeanAmtList合并
        if (pageBeanList != null && pageBeanAmtList != null) {
            for (Object pageBean : pageBeanList) {
                StockStatisticsBean bean = (StockStatisticsBean) pageBean;
                for (Object amtBean : pageBeanAmtList) {
                    StockStatisticsBean beanA = (StockStatisticsBean) amtBean;
                    if (bean.getOrderId().equals(beanA.getOrderId())) {
                        bean.setInstallSumAmt(beanA.getInstallSumAmt());
                        bean.setCurRepayPrincipal(beanA.getCurRepayPrincipal());
                        bean.setRemainPrincipal(beanA.getRemainPrincipal());
                    }
                }
                tempList.add(bean);
            }
        }
        // 将合并后的tempList与pageBeanNoList合并
        if (tempList != null && pageBeanNoList != null) {
            for (StockStatisticsBean bean : tempList) {
                for (Object noBean : pageBeanNoList) {
                    StockStatisticsBean beanB = (StockStatisticsBean) noBean;
                    if (bean.getOrderId().equals(beanB.getOrderId())) {
                        bean.setInstallTerms(beanB.getInstallTerms());
                        bean.setNonRepayno(beanB.getNonRepayno());
                        bean.setRepayNo(beanB.getRepayNo());
                    }
                }
                resultList.add(bean);
            }
        }

        return resultList;
    }

    @Override
    public Map<String, Object> deratePenalty(JSONObject params) {
        Map<String, Object> result = new HashMap<>();
        if (params == null) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "传入数据为空");
            return result;
        }
        if (StringUtil.isEmpty(params.getString(DeratePenaltyConstant.INSTALLDETAILID_KEY))) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "传入分期id为空");
            return result;
        }
        /** 数据封装 **/
        BigDecimal cutInterest =
                params.getBigDecimal(DeratePenaltyConstant.CUTINTEREST_KEY) == null ? BigDecimal.ZERO : params
                        .getBigDecimal(DeratePenaltyConstant.CUTINTEREST_KEY);
        BigDecimal cutOverdueInterest =
                params.getBigDecimal(DeratePenaltyConstant.CUTOVERDUEINTEREST_KEY) == null ? BigDecimal.ZERO : params
                        .getBigDecimal(DeratePenaltyConstant.CUTOVERDUEINTEREST_KEY);
        BigDecimal cutFixedOverdueAmt =
                params.getBigDecimal(DeratePenaltyConstant.CUTFIXEDOVERDUEAMT_KEY) == null ? BigDecimal.ZERO : params
                        .getBigDecimal(DeratePenaltyConstant.CUTFIXEDOVERDUEAMT_KEY);
        BigDecimal cutCurRepayAmt = cutInterest.add(cutOverdueInterest).add(cutFixedOverdueAmt);
        params.put(DeratePenaltyConstant.CUTINTEREST_KEY, cutInterest.multiply(new BigDecimal(100)));
        params.put(DeratePenaltyConstant.CUTOVERDUEINTEREST_KEY, cutOverdueInterest.multiply(new BigDecimal(100)));
        params.put(DeratePenaltyConstant.CUTFIXEDOVERDUEAMT_KEY, cutFixedOverdueAmt.multiply(new BigDecimal(100)));
        params.put(DeratePenaltyConstant.CUTCURREPAYAMT_KEY, cutCurRepayAmt.multiply(new BigDecimal(100)));

        this.installmentBillInfoDao.deratePenalty(params);

        result.put("retCode", AccountConstant.SUCCESS);
        result.put("msg", "处理成功");
        return result;
    }

    /**
     * 抵押车业务查询-账单查询(带分页)
     * 
     * @param params
     * @return
     */
    @Override
    public PageBean queryBillByParamsForMortgage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        return installmentBillInfoDao.listPage(pageParam, params, SQL_QUERYBILLBYPARAMSFORMORTGAGE);
    }

    /**
     * 查询账户系统-订单要素 违约金 最迟还款日期 当期期数
     * 
     * @param params
     * @return
     */
    @Override
    public InstallmentBillInfo getFactors(Map<String, Object> params) {
        return installmentBillInfoDao.getFactors(params);
    }

    /**
     * 查询账户系统-订单要素 已还金额
     * 
     * @param params
     * @return
     */
    @Override
    public InstallmentBillInfo getPayAmt(Map<String, Object> params) {
        return installmentBillInfoDao.getPayAmt(params);
    }

    @Override
    public int repayAuthenticate(RepayAuthenticateRequestMessage requestMessage) {
        try {
            String orderId = requestMessage.getOrderId();
            if (requestMessage == null || StringUtil.isEmpty(orderId)) {
                return INVILAD_REQUEST;
            }
            List<InstallmentBillInfo> ibis = installmentBillInfoDao.getAllInstallmentBillInfoByOid(orderId);
            if (ibis == null || ibis.size() <= 0) {
                return INVILAD_SQL_DATA;
            }
            boolean status_0 = false; // 记录是否有逾期订单
            boolean status_1 = false; // 记录是否有未还款订单
            boolean flag = false; // 默认无3天内需要还款的订单
            for (InstallmentBillInfo ibi : ibis) {
                switch (ibi.getStatus()) {
                    case 0:
                        status_0 = true;
                        return INVILAD_OVERDUE_ORDER;
                    case 1:
                        status_1 = true;
                        break;
                    default:
                        break;
                }
                /** 未还款订单 && 当前时间之前的未还款订单 && 3天内需要还款的订单 **/
                if (ibi.getStatus() == 1 && ibi.getLastRepayDate().getTime() - System.currentTimeMillis() > 0
                        && (ibi.getLastRepayDate().getTime() - System.currentTimeMillis()) < 3 * 24 * 60 * 60 * 1000) {
                    flag = true;
                    return INVILAD_THREE_DAY_ORDER;
                }
            }
            // 有逾期订单 || 没有未还款订单 || 有3天内需要还款的订单
            if (status_0 || !status_1 || flag) {
                return INVILAD_NO_ORDERS_NEED_REPAY;
            }
            return SUCCESS;
        } catch (Exception e) {
            logger.error("BillBizServiceImpl[repayAuthenticate] ", e);
            return UNKNOWN_ERROR;
        }
    }

    /**
     * 以租代购-转租 1:将转租订单用户迁移为新用户。如果包含管理费需要在月供中减掉月供费用 2:将新订单解绑。
     * 3:将账户系统转租账单切换为新用户(inst_info,inst_detail,inst_billinfo,acc_repay_history)
     * 4:有展期的，需要添加对应展期期数，有管理费用的，需要在月供中减掉对应的月供。
     * 
     * @param params
     */
    @Override
    public void getSubletInfo(Map<String, Object> params) {
        logger.info("转租-BillBizServiceImpl-getSubletInfo--开始");
        try {
            // 1:根据orderId 调用中阁"查询转租记录"接口
            if (params != null) {
                String regId = (String) params.get("regId"); // 新用户手机号
                String subletOrderId = (String) params.get("subletOrderId"); // 转租人订单号
                // int rollOverFlag = (Integer) params.get("rollOverFlag"); // 是否展期
                int rollOverFlag =
                        params.get("rollOverFlag") == null ? 0 : Integer.parseInt(params.get("rollOverFlag") + ""); // 是否展期
                // int rollOverItems = (Integer) params.get("rollOverItems"); // 展期期数
                int rollOverItems =
                        params.get("rollOverItems") == null ? 0 : Integer.parseInt(params.get("rollOverItems") + ""); // 展期期数
                BigDecimal manageFee =
                        params.get("manageFee") == null
                                ? BigDecimal.ZERO
                                : new BigDecimal(params.get("manageFee") + ""); // 管理费用

                // 2：根据承租人手机号码查询承租人信息
                Map<String, Object> param = new HashMap<>();
                param.put("regId", regId);
                param.put("openId", FinanceUtil.OPENID_YZDS_NEW);
                OpenInfo openInfo = acctInfoService.queryAccByParams(param);

                // 3:根据订单号更新inst_billinfo 表中 REGID为承租人REGID以及月供
                updateInstBillinfo(subletOrderId, regId, manageFee);

                // 4:将新订单解绑 -中阁完成
                // unbundBillinfo(orderId);

                // 5:更新inst_info
                updateInstInfo(subletOrderId, openInfo);

                // 6:更新inst_detail
                updateInstDetail(subletOrderId, openInfo);

                // 7：展期
                rollOverBillInfo(rollOverFlag, subletOrderId, rollOverItems, manageFee);
            }

        } catch (Exception e) {
            logger.error("以租代购-转租出现异常：{}", e);
        }
        logger.info("转租-BillBizServiceImpl-getSubletInfo--结束");
    }

    /**
     * 根据订单号更新inst_billinfo 表中 REGID 月供 管理费
     * 
     * @param orderId 转租人订单id
     * @param regId 承租人手机号码
     * @param manageFee 管理费 void Author haojinlong Create Date: 2017年5月10日
     */
    private void updateInstBillinfo(String orderId, String regId, BigDecimal manageFee) {
        logger.info("转租-更新inst_billinfo开始");
        manageFee = manageFee != null ? BigDecimalUtil.expand(manageFee) : BigDecimal.ZERO;
        InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo();
        installmentBillInfo.setOrderId(orderId);
        installmentBillInfo.setRegId(regId);
        installmentBillInfo.setCurRepayAmt(manageFee);
        installmentBillInfo.setOffsetAmt(manageFee);
        installmentBillInfo.setUpdateTime(new Date());
        installmentBillInfoDao.updateForRepaymentByOrderId(installmentBillInfo);
        logger.info("转租-更新inst_billinfo结束");
    }

    /**
     * 更新inst_info
     * 
     * @param oldOrderId 转租人订单号
     * @param openInfo Author haojinlong Create Date: 2017年5月9日
     */
    private void updateInstInfo(String oldOrderId, OpenInfo openInfo) {
        logger.info("转租-更新inst_info开始");
        if (openInfo != null) {
            InstallmentInfo installmentInfo = new InstallmentInfo();
            installmentInfo.setOrderId(oldOrderId);
            installmentInfo.setRegId(openInfo.getRegId());
            installmentInfo.setBaseId(openInfo.getBid());
            installmentInfo.setUpdateTime(new Date());
            installmentInfoDao.updateInstallmentInfoByOrderId(installmentInfo);
        }
        logger.info("转租-更新inst_info结束");
    }

    /**
     * 更新inst_detail
     * 
     * @param oldOrderId 转租人订单号
     * @param openInfo Author haojinlong Create Date: 2017年5月9日
     */
    private void updateInstDetail(String oldOrderId, OpenInfo openInfo) {
        logger.info("转租-更新inst_detail开始");
        if (openInfo != null) {
            InstallmentDetail installmentDetail = new InstallmentDetail();
            installmentDetail.setOrderId(oldOrderId);
            installmentDetail.setRegId(openInfo.getRegId());
            installmentDetail.setUpdateTime(new Date());
            installmentDetailDao.updateInstallmentForOrderId(installmentDetail);
        }
        logger.info("转租-更新inst_detail结束");
    }

    /**
     * 转租-订单展期
     * 
     * @param rollOverFlag 展期标识
     * @param oldOrderId 转租人订单号
     * @param rollOverItems 展期数
     * @param manageFee 管理费 void Author haojinlong Create Date: 2017年5月10日
     */
    private void rollOverBillInfo(int rollOverFlag, String oldOrderId, int rollOverItems, BigDecimal manageFee) {
        logger.info("转租-订单展期开始");
        manageFee = manageFee != null ? BigDecimalUtil.expand(manageFee) : BigDecimal.ZERO;
        if (rollOverFlag == 1) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("orderId", oldOrderId);

            List<InstallmentBillInfo> installmentBillInfoTempList = new ArrayList<>();
            // 展期处理
            if (rollOverItems > 0) {
                // 将原来订单最后一期的REPAYNO更新为展期最后一期
                List<InstallmentBillInfo> installmentBillInfoList = installmentBillInfoDao.listBy(paramMap);
                if (installmentBillInfoList != null && installmentBillInfoList.size() > 0) {
                    InstallmentBillInfo billInfo = installmentBillInfoList.get(installmentBillInfoList.size() - 1);
                    billInfo.setRepayNo(installmentBillInfoList.size() + rollOverItems);
                    billInfo.setLastRepayDate(DateUtil.addMonths(billInfo.getLastRepayDate(), rollOverItems));
                    billInfo.setDelayBeginDate(DateUtil.addMonths(billInfo.getDelayBeginDate(), rollOverItems));
                    billInfo.setUpdateTime(new Date());
                    installmentBillInfoDao.updateForRepaymentById(billInfo);
                }

                for (int i = 0; i < rollOverItems; i++) {
                    installmentBillInfoList.clear();
                    installmentBillInfoList = installmentBillInfoDao.listBy(paramMap);
                    if (installmentBillInfoList != null && installmentBillInfoList.size() > 0) {
                        InstallmentBillInfo billInfo = installmentBillInfoList.get(installmentBillInfoList.size() - 2);
                        billInfo.setId(Long.valueOf(i));
                        billInfo.setRepayNo(installmentBillInfoList.size() + i);
                        billInfo.setLastRepayDate(DateUtil.addMonths(billInfo.getLastRepayDate(), i + 1));
                        billInfo.setDelayBeginDate(DateUtil.addMonths(billInfo.getDelayBeginDate(), i + 1));
                        billInfo.setCurItems(installmentBillInfoList.size() + rollOverItems);
                        billInfo.setPreOverdueInterest(BigDecimal.ZERO);
                        billInfo.setRemainPrincipal(BigDecimal.ZERO);
                        billInfo.setOffsetAmt(manageFee);
                        installmentBillInfoTempList.add(billInfo);
                    }
                }
                installmentBillInfoDao.insert(installmentBillInfoTempList);

            }
        }
        logger.info("转租-订单展期结束");
    }

    /**
     * 根据orderId集合获取逾期账单
     * 
     * @param params
     * @return 是否逾期标识
     * @Author haojinlong Create Date: 2017年5月27日
     */
    @Override
    public String getOverdueInstallmentBillInfo(Map<String, Object> params) {
        String result = "N";
        List<InstallmentBillInfo> billInfoList = installmentBillInfoDao.getOverdueInstallmentBillInfo(params);
        if (billInfoList != null && billInfoList.size() > 0) {
            result = "Y";
        }
        return result;
    }

    /**
     * 根据订单号获取全部未失效账单-中阁使用
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年5月31日
     */
    @Override
    public PageBean listInstallmentBillInfoPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        return installmentBillInfoDao.listInstallmentBillInfoPage(pageParam, params);
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
        String repayNo = (String) params.get("repayNo");
        String[] repayNos = StringUtil.split(repayNo, ",");
        params.put("repayNos", repayNos);
        return installmentBillInfoDao.getCurRepayAmtByRepayno(params);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月14日
     */
    @Override
    public List<InstallmentBillInfoNormalRepay> selectBillsByCondition(Map<String, Object> params) {
        String repayNo = (String) params.get("repayNo");
        String[] repayNos = StringUtil.split(repayNo, ",");
        params.put("repayNos", repayNos);
        return installmentBillInfoDao.selectBillsByCondition(params);
    }

    /**
     * 部分还款
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月21日
     */
    @Override
    public Map<String, Object> partPrepayment(Map<String, List<InstallmentBillInfo>> map, String tradeNo,
            String repayType, String bankCardNo, String bankName) {
        logger.debug("部分还款模式开始......");
        Map<String, Object> result = new HashMap<>();
        result.put("retCode", AccountConstant.SUCCESS);
        for (Entry<String, List<InstallmentBillInfo>> e : map.entrySet()) {
            /* 循环获取还款信息 */
            List<AccRepayHistory> accRepayHistoryList = new ArrayList<>();
            for (InstallmentBillInfo installmentBillInfo : e.getValue()) {

                // 金额放大100倍
                installmentBillInfo.setCurRealRepayamt(
                        BigDecimalUtil.mul(installmentBillInfo.getCurRealRepayamt(), new BigDecimal("100")));

                // 支付流水
                AccRepayHistory accRepayHistory = new AccRepayHistory();
                accRepayHistory.setOrderId(installmentBillInfo.getOrderId());// 订单号
                accRepayHistory.setRepayNo(installmentBillInfo.getRepayNo());// 序号
                accRepayHistory.setOpenId(installmentBillInfo.getOpenId());// 开户号
                accRepayHistory.setRepayAmt(installmentBillInfo.getCurRealRepayamt());// 还款金额
                accRepayHistory.setChargeAmount(BigDecimal.ZERO);// 收取金额
                accRepayHistory.setTradeNo(tradeNo);// 交易号
                accRepayHistory.setMerchantNo(installmentBillInfo.getMerchantNo());// 商户号
                accRepayHistory.setRegId(installmentBillInfo.getRegId());// 手机号
                accRepayHistory.setRepayType(repayType);// 还款类型
                accRepayHistory.setBankCardNo(bankCardNo);// 银行卡号
                accRepayHistory.setBankName(bankName);// 银行名称
                /* 装填入list,等待更新 */
                accRepayHistoryList.add(accRepayHistory);
            }
            logger.debug("部分还款开始更新账单信息");
            long num = installmentBillInfoDao.updateForPartRepayment(e.getValue());
            if (num == 0) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("msg", "该订单这个序号账单已还款");
                return result;
            }
            logger.debug("部分还款开始写入日志表");
            num = accRepayHistoryService.batchInsertAccRepayHistory(accRepayHistoryList);
            if (num > 0) {
                result.put("retCode", AccountConstant.SUCCESS);
                result.put("msg", "正常还款成功");
                result.put("billStatus", 2);
            } else {
                result.put("retCode", AccountConstant.ERROR);
                result.put("msg", "还款成功，插入日志表失败！");
            }
            // 清除redis
            String key = "AdvancedPayBills." + e.getKey();
            try {
                queryBillInfoCache.remove(key);
                logger.debug("清除redis结束");
            } catch (Exception exception) {
                logger.error("清除提前还款信息redis错误！");
            }
        }
        return result;
    }

    @Override
    public OverDueBillPojo overdueBillQuery(QueryCondition qc) {

        return installmentBillInfoDao.overdueBillQuery(qc);
    }

    /**
     * 
     * 查询每日到期账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月23日
     */
    @Override
    public PageBean selectEveryDayExpireBill(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());
        // 划扣标识 1 手动 2 自动
        int flag = params.get("flag") == null ? 0 : Integer.parseInt(params.get("flag").toString());
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        if (flag == 0 || flag == 1) {
            return installmentBillInfoDao.selectEveryDayExpireBill(pageParam, params);
        } else {
            return installmentBillInfoDao.selectEveryDayExpireBillForAuto(pageParam, params);
        }
    }

    /**
     * 
     * 
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月23日
     */
    @Override
    public List<InstallmentBillInfo> selectEveryDayExpireBill2(Map<String, Object> params) {
        params.get("haList");
        List<InstallmentBillInfo> parseArray =
                JSON.parseArray(params.get("haList").toString(), InstallmentBillInfo.class);
        List<InstallmentBillInfo> parseArray2 = new ArrayList<>();
        InstallmentBillInfo installmentBillInfo2;
        Map<String, Object> map = new HashMap<>();
        for (InstallmentBillInfo installmentBillInfo : parseArray) {
            map.put("orderId", installmentBillInfo.getOrderId().toString());
            map.put("repayNo", installmentBillInfo.getRepayNo() + "");
            installmentBillInfo2 = installmentBillInfoDao.queryBillInfoByOrderIdPage(map);
            if (null != installmentBillInfo2) {
                parseArray2.add(installmentBillInfo2);
            }
        }
        return parseArray2;
    }

    /**
     * 房贷-根据订单号获取账单详情
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月14日
     */
    @Override
    public List<Map<String, Object>> getMorgateOrderInfo(Map<String, Object> params) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        try {
            List<InstallmentBillInfo> subList = installmentBillInfoDao.getSubOrderNoByOrderNo(params);
            if (!CollectionUtils.isEmpty(subList)) {
                Map<String, Object> returnMap = null;
                for (InstallmentBillInfo installmentBillInfo : subList) {
                    returnMap = new HashMap<>();
                    String subOrderId = installmentBillInfo.getSubOrderId();

                    params.clear();
                    params.put("subOrderId", subOrderId);
                    List<InstallmentBillInfo> list = installmentBillInfoDao.getMorgateOrderInfoByOrderId(params);

                    returnMap.put("subOrderId", subOrderId);
                    returnMap.put("repayAmt", installmentBillInfo.getSumCurRepayAmt());
                    returnMap.put("billList", list);
                    returnList.add(returnMap);
                }
            }
        } catch (Exception e) {
            logger.error("根据订单号获取账单详情出现异常：{}", e);
        }
        return returnList;
    }

    @Override
    public BigDecimal getAddAmt(Integer hasRepayNo, String orderId) {
        BigDecimal amt = installmentBillInfoDao.getAddAmt(hasRepayNo, orderId);
        return amt == null ? BigDecimal.ZERO : amt;
    }

    /**
     * @author adam
     * @describe 已还的账单中的最后一期的最后还款日如果比现在大 ， 则当期已还
     */
    public PrepaymentAnalysisPojo getBoolMarkByOid(String orderId) {
        PrepaymentAnalysisPojo pap = installmentBillInfoDao.getLastRepayInfoByOId(orderId);
        if (pap != null) {
            pap.setMark(
                    pap.getLastRepayDate().getTime() > System.currentTimeMillis());
        } else {
            /** 如果 为空 肯定 当期未还 **/
            pap = new PrepaymentAnalysisPojo();
            pap.setMark(false);
            pap.setRepayNo(0);
        }
        return pap;
    }

    @Override
    public Map<String, Object> getPrepaymentResponsePojo(String orderId, String interestRate) {
        Map<String, Object> map = new HashMap<>();
        map.put("retCode", AccountConstant.ERROR);
        map.put("retMsg", "查询账户系统-查询订单要素数据失败,没有符合该订单的数据信息");
        map.put("result", null);
        try {
            PrepaymentAnalysisPojo pap = getBoolMarkByOid(orderId);
            PrepaymentResponsePojo prp = new PrepaymentResponsePojo();
            InstallmentBillInfo ibi = installmentBillInfoDao.getCurrentBill(orderId);
            /** 是否上收月供 **/
            boolean isTakeMonth = installmentBillInfoDao.isTakeMonth(orderId);

            /** 实际已还金额（前端展示使用） **/
            BigDecimal sjyhje = installmentBillInfoDao.getSjyhjeByOid(orderId);
            /** 已还本金（计算相关金额使用） **/
            BigDecimal yhbj = BigDecimal.ZERO;
            BigDecimal sywhbj = BigDecimal.ZERO;
            BigDecimal wyj = BigDecimal.ZERO;
            BigDecimal yhze = BigDecimal.ZERO;
            // 总罚息
            BigDecimal totalOverdueInterest = installmentBillInfoDao.getTotalOverDueInterest(orderId);
            // 逾期期数
            int overdueItems = installmentBillInfoDao.getOverdueItems(orderId);

            logger.debug("---是否上收月供---{}", isTakeMonth);
            logger.debug("---当期是否已还---{}", pap.getMark());
            if (pap.getMark()) {
                /** 01.已还本金 = 每月应还本金*已还期数 **/
                /** 02.剩余未还本金 =（订单金额-首付款）- 已还本金 **/
                /** 03.违约金 = 剩余未还本金*0.06 **/
                /** 04.应还总额 = 剩余未还本金 **/
                /** 05.实际已还本金 = 已还本金 **/
                /** 06.未还利息 = 未还期数*每月利息 **/

                yhbj = installmentBillInfoDao.sumYhbj(pap.getRepayNo(), orderId);
                sywhbj = ibi.getInstallAmt().subtract(yhbj);
                if (isTakeMonth) {
                    /** 如果上收月供，总本金 = ibi.getInstallAmt() + 第一期本金 **/
                    sywhbj = ibi.getInstallAmt().add(yhbj.divide(new BigDecimal(pap.getRepayNo()))).subtract(yhbj);
                }
                wyj = sywhbj.multiply(new BigDecimal(interestRate));
                yhze = sywhbj;
            } else {
                /** 01.已还本金 = 每月应还本金*已还期数 + 当期本金 **/
                /** 02.剩余未还本金 =（订单金额-首付款）- 已还本金 **/
                /** 03.违约金 = 剩余未还本金*0.06 **/
                /** 04.应还总额 = 剩余未还本金 + 本期应还本金 + 本期应还利息 **/
                /** 05.实际已还本金 != 已还本金 **/
                yhbj = installmentBillInfoDao.sumYhbj(pap.getRepayNo(), orderId);
                sywhbj = ibi.getInstallAmt().subtract(yhbj != null ? yhbj : BigDecimal.ZERO);
                if (isTakeMonth) {
                    /** 如果上收月供，总本金 = ibi.getInstallAmt() + 第一期本金 **/
                    sywhbj = ibi.getInstallAmt().add(ibi.getPrincipal()).subtract(yhbj);
                }
                logger.debug("---剩余未还本金---{}", sywhbj);
                wyj = sywhbj.multiply(new BigDecimal(interestRate));
                yhze = sywhbj.add(ibi.getCurRepayPrincipal()).add(ibi.getCurRepayInterest());
            }

            prp.setCurItems(ibi.getRepayNo());
            prp.setExpiryDate(ibi.getLastRepayDate());
            // 优化金额为null的时候不用缩小100
            prp.setHasRepayAmt(sjyhje == null ? BigDecimal.ZERO : BigDecimalUtil.narrow(sjyhje));
            prp.setOverdueAmt(wyj == null ? BigDecimal.ZERO : BigDecimalUtil.narrow(wyj));
            prp.setRemainPrincipal(sywhbj == null ? BigDecimal.ZERO : BigDecimalUtil.narrow(sywhbj));
            prp.setRepayAmt(yhze == null ? BigDecimal.ZERO : BigDecimalUtil.narrow(yhze));
            prp.setHasRepayNo(pap.getRepayNo());
            // 计算月供、利息、本金、总罚息、逾期期数
            prp.setMonthAmt(BigDecimalUtil.narrow(ibi.getCurRepayAmt()));
            prp.setMonthInterest(BigDecimalUtil.narrow(ibi.getCurRepayInterest()));
            prp.setMonthPrincipal(BigDecimalUtil.narrow(ibi.getCurRepayPrincipal()));
            prp.setTotalOverdueInterest(BigDecimalUtil.narrow(totalOverdueInterest));
            prp.setOverdueItems(overdueItems);
            prp.setStatus(ibi.getStatus());
            map.put("retCode", AccountConstant.SUCCESS);
            map.put("retMsg", "查询账户系统-查询订单要素数据成功");
            map.put("result", prp);
        } catch (Exception e) {
            logger.error("提前还款异常", e);
        }
        return map;
    }

    @Override
    public Map<String, Integer> finishBill(FinishBillRequestMessage fbrm) {
        InstallmentBillInfo ibi = installmentBillInfoDao.getCurrentBill(fbrm.getOrderId());
        if (ibi == null) {
            /** 如果当前期数不存在 1：订单号不存在； 2：当前期数超过次订单的分期 期数 **/
            ibi = installmentBillInfoDao.getLastBill(fbrm.getOrderId());
            if (ibi == null) {
                throw new InvalidParameterException("未查询到相关账单");
            }
        }

        ibi.setCurRepayDate(fbrm.getFinishTime());
        ibi.setCurRealRepayamt(fbrm.getAmt().multiply(new BigDecimal(100)));
        ibi.setStatus(3); // 全部还款

        /**  **/
        return installmentBillInfoDao.finishBill(ibi);
    }

    @Override
    public Map<String, Object> createLSBill(InstallmentInfo installmentInfo) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1.查询分期明细
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("installInfoId", installmentInfo.getId());
            paramMap.put("orderId", installmentInfo.getOrderId());
            List<InstallmentDetail> details = installmentDetailDao.listBy(paramMap);

            // 2.分期计划
            InstallmentPlan installmentPlan = installmentPlanDao.getById(installmentInfo.getPlanId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            List<InstallmentBillInfo> bills = new ArrayList<>(details.size());
            // 初始化剩余本息 by yeoman 20170620
            BigDecimal remainPriandInterest =
                    BigDecimalUtil.add(installmentInfo.getInstallAmt(), installmentInfo.getInterestAmt());// 总本金+总利息
            String curBillDate = null;
            for (int i = 0; i < details.size(); i++) {
                InstallmentDetail installmentDetail = details.get(i);
                // 修改账单日计算规则,入参:分期时间,期数,账单日生成计划ID
                String installDate = sdf.format(details.get(0).getInstallDate());// 分期时间
                String dynamicDays = details.get(0).getCycle() + ""; // 动态账单日天数
                if (installmentPlan.getRepayMethod() == 3) {
                    curBillDate =
                            billDateService.getBillDate(curBillDate, installDate,
                                    installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH, dynamicDays,
                                    installmentDetail.getInstallTerms());
                } else {
                    // 上收月供的情况需要判断上收期数然后开始计算最后还款日
                    if (installmentInfo.getTakeMonth() == 1) {
                        if (i + 1 <= installmentInfo.getTakePayment()) {
                            curBillDate = installDate;
                        } else {
                            curBillDate =
                                    billDateService.getBillDate(curBillDate, installDate,
                                            installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH,
                                            dynamicDays, i + 1 - installmentInfo.getTakePayment());
                        }
                    } else {
                        curBillDate =
                                billDateService.getBillDate(curBillDate, installDate,
                                        installmentPlan.getRepayDateId(), DateUtil.SHORT_DATE_FORMAT_NO_DASH,
                                        dynamicDays, i + 1);
                    }
                }
                Date lastRepayDate = sdf.parse(curBillDate);

                // 获取滞纳金计算时间
                Date delayBeginDate = DateUtil.addDayFromDate(lastRepayDate, installmentPlan.getGraceDays());
                // 获取月利息
                BigDecimal monthInterest = null;
                // 一次性还本付息的利息 = 月利息*期数
                if (installmentPlan.getRepayMethod() == 3) {
                    monthInterest = getMonthInterest(installmentInfo, installmentDetail, installmentPlan,
                            lastRepayDate);
                    monthInterest =
                            BigDecimalUtil.mul(monthInterest, new BigDecimal(installmentDetail.getInstallTerms()));
                    // 后续如果存在日利息，可能对日利息有影响,原因现在的利息包含了所有期数的,但是日利息算法为日利息 = 总利息/月天数。
                } else {
                    if (i < installmentInfo.getInterestTerms()) {
                        monthInterest =
                                BigDecimalUtil.div(installmentDetail.getInterestAmt(),
                                        new BigDecimal(installmentInfo.getInterestTerms()));
                    } else {
                        monthInterest = BigDecimal.ZERO;
                    }
                }
                // 本期应还总金额=(每期本金 + 月利息 + 滞纳金 + 其他费用 + 划扣费) by yeoman20170502
                // 最后一期不收 其他费用即gps流量费
                BigDecimal curRepayAmt = null;
                BigDecimal otherAmt = null;
                if (i == details.size() - 1 && installmentInfo.getTakeOtherAmt() == 1) {
                    // 最后一期且上收gps流量费的情况
                    curRepayAmt =
                            BigDecimalUtil.add(
                                    installmentDetail.getPrincipal(),
                                    monthInterest,
                                    BigDecimal.ZERO);
                    otherAmt = BigDecimal.ZERO;
                } else {
                    curRepayAmt =
                            BigDecimalUtil.add(
                                    installmentDetail.getPrincipal(),
                                    monthInterest,
                                    BigDecimal.ZERO,
                                    installmentDetail.getOtherAmt());
                    otherAmt = installmentDetail.getOtherAmt();
                }
                // 每天利息
                BigDecimal preInterest = getPreInterest(installmentInfo, installmentDetail, installmentPlan,
                        lastRepayDate, monthInterest);
                // 每期实际分摊金额
                BigDecimal realPayamt = getRealPayamt(installmentInfo, installmentDetail, monthInterest);
                // 滞纳金and违约金
                Map<String, BigDecimal> preOverdueMap =
                        overdueIterestService.calculateOverdueInterestWithFixed(
                                installmentDetail.getRemainPrincipal(), installmentDetail.getInstallAmt(), realPayamt,
                                installmentDetail.getContractAmt(), remainPriandInterest, 1,
                                installmentPlan.getOvedueId() + "");
                BigDecimal poi = preOverdueMap.get("Amt");
                BigDecimal fixedOverdueAmt = preOverdueMap.get("Fixed");

                // 获取账单状态
                int status = getStatus(i, installmentInfo, installmentDetail);
                // 本期实际还款金额
                BigDecimal curRealRepayamt = getCurRealRepayamt(i, installmentInfo, installmentDetail, curRepayAmt);

                InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo();
                installmentBillInfo.setCurRealRepayamt(curRealRepayamt);// 本期实际还款金额
                installmentBillInfo.setCurRepayAmt(curRepayAmt);// 本期应还总金额
                installmentBillInfo.setCurRepayInterest(monthInterest);// 本期应还利息
                installmentBillInfo.setCurRepayOverdueInterest(BigDecimal.ZERO);// 本期应还逾期金额
                installmentBillInfo.setCurRepayPrincipal(installmentDetail.getPrincipal());// 本期应还本金
                installmentBillInfo.setDelayBeginDate(delayBeginDate);// 滞纳金计算时间
                installmentBillInfo.setInstallAmt(installmentDetail.getInstallAmt());// 分期金额
                installmentBillInfo.setInstallDetailId(installmentDetail.getId());// 分期明细ID
                installmentBillInfo.setInstallInfoId(installmentInfo.getId());// 分期总账号ID
                installmentBillInfo.setLastRepayDate(lastRepayDate);// 最后还款日
                installmentBillInfo.setOpenId(installmentInfo.getOpenId());// 账户号
                installmentBillInfo.setOverdueDays(0);// 逾期天数
                installmentBillInfo.setPartPayment(installmentPlan.getPartpayMent());// 部分还款
                installmentBillInfo.setPreInterest(preInterest);// 每天的利息
                installmentBillInfo.setPreOverdueInterest(poi);// 每日滞纳金
                installmentBillInfo.setPrePayment(installmentPlan.getPrepayMent());// 提前还款
                installmentBillInfo.setPrincipal(installmentDetail.getPrincipal());// 本金
                installmentBillInfo.setRegId(installmentInfo.getRegId());// 注册号
                installmentBillInfo.setRemainPrincipal(installmentDetail.getRemainPrincipal());// 剩余本金
                installmentBillInfo.setRemainPriandInterest(remainPriandInterest);// 剩余本息
                // 下期账单对应的剩余本息 = 剩余本息-本期使用的本金-本期利息
                remainPriandInterest =
                        BigDecimalUtil.sub(remainPriandInterest,
                                BigDecimalUtil.add(installmentDetail.getPrincipal(), monthInterest));
                installmentBillInfo.setRepayNo(i + 1);// 还款序号
                installmentBillInfo.setInstOrder(installmentDetail.getInstOrder());// 放款顺序
                installmentBillInfo.setOrderId(installmentDetail.getOrderId());// 订单id
                installmentBillInfo.setSubOrderId(installmentDetail.getSubOrderId());// 子订单id
                installmentBillInfo.setOrderDate(installmentDetail.getOrderDate());// 订单时间
                installmentBillInfo.setCurRepayDate(null);// 还款时间
                installmentBillInfo.setRealPayamt(realPayamt);// 实际分摊金额
                installmentBillInfo.setOtherAmt(otherAmt);// 其他综合费用
                installmentBillInfo.setEarliestPayDate(lastRepayDate);// 最早还款日
                installmentBillInfo.setPlanId(installmentPlan.getId());// 计划id
                installmentBillInfo.setInstallSumAmt(installmentDetail.getInstallSumAmt());// 合同金额
                installmentBillInfo.setMerchantNo(installmentDetail.getMerchantNo());// 商户号
                installmentBillInfo.setFixedOverdueAmt(BigDecimal.ZERO);// 逾期固定收取金额，生成账单对应的违约金为0,逾期后会计算
                installmentBillInfo.setStatus(status);// 账单状态

                bills.add(installmentBillInfo);
            }
            installmentBillInfoDao.insert(bills);
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "账单生成成功");
        } catch (Exception e) {
            result.put("retCode", AccountConstant.ERROR);
            result.put("retMsg", "账单生成失败");
            logger.error("创建账单出现异常：{}", e);
        }
        return result;
    }

    /**
     * 根据订单号获取账单当前期数与账单状态
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月8日
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getBillStatusByOrderIds(Map<String, Object> params) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<String> orderList = (List<String>) params.get("orderList");
        if (!CollectionUtils.isEmpty(orderList)) {
            Map<String, Object> paramMap;
            Map<String, Object> returnMap;
            for (String orderId : orderList) {
                paramMap = new HashMap<>();
                returnMap = new HashMap<>();
                paramMap.put("orderId", orderId);
                paramMap.put("orderDate", DateUtil.format(DateUtil.afterOneMonth(Calendar.getInstance(), 1).getTime(),
                        DateUtil.SHORT_DATE_FORMAT));
                InstallmentBillInfo billInfo = installmentBillInfoDao.getAllInstallmentBillInfoByOrderId(paramMap);
                if (billInfo != null) {
                    paramMap.put("repayNo", billInfo.getRepayNo());
                    List<InstallmentBillInfo> billList =
                            installmentBillInfoDao.getAllInstallmentBillInfoByOrderIdList(paramMap);
                    returnMap.put("orderId", billInfo.getOrderId());
                    if (!CollectionUtils.isEmpty(billList)) {
                        for (InstallmentBillInfo bean : billList) {
                            int status = bean.getStatus();
                            if (status == 0) {
                                returnMap.put("billStatus", status);
                                returnMap.put("curItems", bean.getRepayNo());
                                break;
                            } else {
                                returnMap.put("billStatus", billInfo.getStatus());
                                returnMap.put("curItems", billInfo.getRepayNo());
                            }
                        }
                    } else {
                        returnMap.put("billStatus", billInfo.getStatus());
                        returnMap.put("curItems", billInfo.getRepayNo());
                    }
                    result.add(returnMap);
                }
            }
        }
        return result;
    }

    /**
     * 
     * Description:查询距离最迟还款日小于等于3天的账单，账单状态为：待还款；
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月23日 16:12:13
     */
    public List<InstallmentBillInfo> queryBillLastDateThree(Map<String, Object> params) {
        List<InstallmentBillInfo> billQueryAllDtoList = installmentBillInfoDao.queryBillLastDateThree(params);
        return billQueryAllDtoList;
    }

    @Override
    public List<InstallmentBillInfo> queryBillLastDateGreaterThanFive(Map<String, Object> params) {
        List<InstallmentBillInfo> billQueryAllDtoList = installmentBillInfoDao.queryBillLastDateGreaterThanFive(params);
        return billQueryAllDtoList;
    }

    /**
     * 获取以租代购所有待还款账单
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年4月27日
     */
    @Override
    public PageBean selectLatelyThreeDaysBill(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());

        PageParam pageParam = new PageParam(pageNum, numPerPage);
        return installmentBillInfoDao.selectLatelyThreeDaysBill(pageParam, params);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年5月2日
     */
    @Override
    public InstallmentBillInfo getInstallmentBillInfoByOrderId(JSONObject objs) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", objs.getString("orderId"));
        paramMap.put("repayNo", objs.getIntValue("repayNo"));
        return installmentBillInfoDao.getInstallmentBillInfoByOrderId(paramMap);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年5月24日
     */
    @Override
    public int batchUpdateBillSmsMobile(JSONObject objs) {
        int result = 0;
        String smsMobile = objs.getString("smsMobile");
        List<InstallmentBillInfo> orderList =
                JSON.parseArray(objs.get("orderList").toString(), InstallmentBillInfo.class);

        if (!CollectionUtils.isEmpty(orderList)) {
            InstallmentInfo installmentInfo = new InstallmentInfo();
            InstallmentBillInfo installmentBillInfo = null;
            for (InstallmentBillInfo bean : orderList) {
                installmentInfo = new InstallmentInfo();
                installmentInfo.setOrderId(bean.getOrderId());
                installmentInfo.setSmsMobile(smsMobile);

                result += installmentInfoDao.updateInstallmentInfoByOrderId(installmentInfo);

                installmentBillInfo = new InstallmentBillInfo();
                installmentBillInfo.setOrderId(bean.getOrderId());
                installmentBillInfo.setSmsMobile(smsMobile);
                result += installmentBillInfoDao.updateInstallmentBillInfoByOrderId(installmentBillInfo);
            }
        }
        return result;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月12日
     */
    @Override
    public List<Map<String, String>> getMaxRepayNosByOrderId(JSONObject objs) {
        List<Map<String, String>> list = new ArrayList<>();
        JSONArray orderList = objs.getJSONArray("orderList");
        if (!CollectionUtils.isEmpty(orderList)) {
            Map<String, String> map = null;
            for (int i = 0; i < orderList.size(); i++) {
                map = new HashMap<>();
                String orderId = orderList.getString(i);
                int num = installmentBillInfoDao.getRepayNo(orderId);
                map.put("orderId", orderId);
                map.put("repayNo", String.valueOf(num));
                list.add(map);
            }
        }
        return list;
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
    public List<Map<String, Object>> collectBillDataByOrder(JSONObject objs) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", objs.getString("orderId"));
        paramMap.put("orderIdList", objs.getObject("orderIdList", List.class));
        return installmentBillInfoDao.collectBillDataByOrder(paramMap);
    }

    /**
     * 根据订单号查询首次还款时间
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年8月16日
     */
    @Override
    public InstallmentBillInfo getFirstLastRepayDateByOrderId(JSONObject objs) {
        return installmentBillInfoDao.getFirstLastRepayDateByOrderId(objs.getString("orderId"));
    }

    @Override
    public Map<String, Object> getAllocationInfoOrderId(JSONObject objs) {
        Map<String, Object> returnMap = new HashMap<>();
        InstallmentBillInfo instanllemntBillA = installmentBillInfoDao.getLatelyInstallmentBillInfoDesc(objs);
        InstallmentBillInfo instanllemntBillB = installmentBillInfoDao.getLatelyInstallmentBillInfoAsc(objs);

        Integer billNums =
                installmentBillInfoDao.getBillNums(objs.getString("orderId"));

        long dayA = 0;
        long dayB = 0;
        if (null != instanllemntBillA) {
            dayA = DateUtil.diffDays(instanllemntBillA.getLastRepayDate(),
                    DateUtil.parseDate(objs.getString("loanDate"), DateUtil.SHORT_DATE_FORMAT));
        }
        if (null != instanllemntBillB) {
            dayB = DateUtil.diffDays(DateUtil.parseDate(objs.getString("loanDate"), DateUtil.SHORT_DATE_FORMAT),
                    instanllemntBillB.getLastRepayDate());
        }

        // 剩余本金
        BigDecimal remainPrincipal = BigDecimal.ZERO;
        int repayNo = 0;
        if (dayA > dayB) {
            remainPrincipal =
                    BigDecimalUtil.mul(instanllemntBillA.getCurRepayPrincipal(),
                            BigDecimal.valueOf(billNums - instanllemntBillA.getRepayNo()));
            repayNo = instanllemntBillB.getRepayNo();
        } else if (dayA < dayB) {
            remainPrincipal =
                    BigDecimalUtil.mul(instanllemntBillB.getCurRepayPrincipal(),
                            BigDecimal.valueOf(billNums - instanllemntBillB.getRepayNo() + 1));
            repayNo = instanllemntBillB.getRepayNo();
        } else {
            remainPrincipal =
                    BigDecimalUtil.mul(instanllemntBillB.getCurRepayPrincipal(),
                            BigDecimal.valueOf(billNums - instanllemntBillB.getRepayNo() + 1));
            repayNo = instanllemntBillA.getRepayNo();
        }
        remainPrincipal =
                BigDecimalUtil.div(remainPrincipal, BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        returnMap.put("repayNo", repayNo);
        returnMap.put("sbAmt", remainPrincipal);
        return returnMap;
    }
}
