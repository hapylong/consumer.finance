/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月9日 上午11:31:39
 * @version V1.0
 */
package com.iqb.consumer.service.inst.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant;
import com.iqb.consumer.common.journal.InsertJournal;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentPlan;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentBillInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentDetailDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentPlanDao;
import com.iqb.consumer.service.conf.MsgConfig;
import com.iqb.consumer.service.consumer.dto.CalculateAmt;
import com.iqb.consumer.service.consumer.dto.InstPlan;
import com.iqb.consumer.service.consumer.dto.fastBill.FastBillBean;
import com.iqb.consumer.service.consumer.dto.fastBill.Plan;
import com.iqb.consumer.service.inst.IInstInfoService;
import com.iqb.consumer.service.product.IBillDateService;
import com.iqb.consumer.service.product.LockRepayDateService;
import com.iqb.consumer.service.product.OverdueIterestService;
import com.iqb.consumer.service.utils.NotifyUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class InstInfoServiceImpl implements IInstInfoService {

    protected static final Logger logger = LoggerFactory.getLogger(InstInfoServiceImpl.class);

    @Resource
    private InstallmentInfoDao installmentInfoDao;
    @Resource
    private JmsTemplate notifyJmsTemplate;
    @Resource
    private MsgConfig msgConfig;
    @Resource
    private InstallmentDetailDao installmentDetailDao;
    @Resource
    private InstallmentPlanDao installmentPlanDao;
    @Resource
    private IBillDateService billDateService;
    @Resource
    private InstallmentBillInfoDao installmentBillInfoDao;
    @Resource
    private OverdueIterestService overdueIterestService;
    @Resource
    private LockRepayDateService lockRepayDateService;

    @InsertJournal
    @Transactional
    @Override
    public long insertInstalInfo(final InstallmentInfo installmentInfo) {
        long instId = installmentInfoDao.insertInstalInfo(installmentInfo);
        installmentInfo.setId(instId);
        // 通知消息队列生成分摊详情
        notifyJmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(NotifyUtil.formatInstInfo(installmentInfo, msgConfig.getBaseUrl()));
            }
        });
        return instId;
    }

    @InsertJournal
    @Transactional
    @Override
    public long insertMortgageInstInfo(final InstallmentInfo installmentInfo) {
        long instId = installmentInfoDao.insertInstalInfo(installmentInfo);
        installmentInfo.setId(instId);
        // 通知消息队列生成分摊详情
        notifyJmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(NotifyUtil.formatMortgageInfo(installmentInfo, msgConfig.getBaseUrl()));
            }
        });
        return instId;
    }

    @Override
    public List<InstallmentInfo> getInstInfoByParams(Map<String, Object> params) {
        return installmentInfoDao.getInstInfoByParams(params);
    }

    @Override
    public InstallmentInfo repeatInstInfo(Map<String, Object> params, InstallmentInfo installmentInfo) {
        List<InstallmentInfo> exitListInstInfo = installmentInfoDao.getInstInfoByParams(params);
        if (exitListInstInfo == null || exitListInstInfo.size() == 0) {
            installmentInfo.setInstOrder(1);// 首次分期
            // 子订单号,房贷该字段生效 add by 20170811
            String subOrderId = installmentInfo.getOrderId() + "01";
            installmentInfo.setSubOrderId(subOrderId);
            return installmentInfo;
        } else {
            BigDecimal sumRealInstAmt = installmentInfo.getInstallAmt();
            for (InstallmentInfo exitInstInfo : exitListInstInfo) {
                sumRealInstAmt = BigDecimalUtil.add(sumRealInstAmt, exitInstInfo.getInstallAmt());
            }
            if (exitListInstInfo.get(0).getInstallSumAmt().compareTo(sumRealInstAmt) >= 0) {
                installmentInfo.setInstOrder(exitListInstInfo.size() + 1);
                // 子订单号,房贷字段才生效 add by 20170811
                String subOrderId = null;
                if (exitListInstInfo.size() + 1 < 10) {
                    subOrderId = installmentInfo.getOrderId() + "0" + (exitListInstInfo.size() + 1);
                } else {
                    subOrderId = installmentInfo.getOrderId() + (exitListInstInfo.size() + 1);
                }
                installmentInfo.setSubOrderId(subOrderId);
                return installmentInfo;
            } else {
                return null;// 超分期限额
            }
        }
    }

    /**
     * 生成房贷分期分摊表
     */
    @Override
    public Map<String, Object> genMortgageInstDetail(final InstallmentInfo installmentInfo) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<InstallmentDetail> detailList = getMortgageDetails(installmentInfo);
            installmentDetailDao.insert(detailList);
            // 通知消息队列生成账单详情
            notifyJmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(NotifyUtil.formatMortgageBill(installmentInfo,
                            msgConfig.getBaseUrl()));
                }
            });
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "分摊详情生成成功");
        } catch (Exception e) {
            logger.error("插入分摊详情发生异常", e);
            result.put("retCode", AccountConstant.ERROR);
        }
        return result;
    }

    /**
     * 生成分期分摊表
     */
    @Override
    public Map<String, Object> genInstDetail(final InstallmentInfo installmentInfo) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<InstallmentDetail> detailList = getInstDetails(installmentInfo);
            installmentDetailDao.insert(detailList);
            // 通知消息队列生成账单详情
            notifyJmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(NotifyUtil.formatBill(installmentInfo, msgConfig.getBaseUrl()));
                }
            });
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "分摊详情生成成功");
        } catch (Exception e) {
            logger.error("插入分摊详情发生异常", e);
            result.put("retCode", AccountConstant.ERROR);
        }
        return result;
    }

    /**
     * 房贷分期规则List2Map
     * 
     * @param arrayInsts
     * @return
     */
    private Map<Integer, InstDetail> list2Map(List<InstDetail> arrayInsts) {
        Map<Integer, InstDetail> result = new HashMap<Integer, InstDetail>();
        for (InstDetail instDetail : arrayInsts) {
            result.put(instDetail.getRepayNo(), instDetail);
        }
        return result;
    }

    /**
     * 生成账单分摊详情
     * 
     * @author Yeoman
     * @param installmentInfo
     * @return
     */
    private List<InstallmentDetail> getMortgageDetails(InstallmentInfo installmentInfo) {
        List<InstallmentDetail> detailList = null;
        BigDecimal installAmt = installmentInfo.getInstallAmt();
        int installTerms = installmentInfo.getInstallTerms();
        long planId = installmentInfo.getPlanId();// 分期方案ID
        InstallmentPlan installmentPlan = installmentPlanDao.getById(planId);
        BigDecimal remainPrincipal = installAmt;// 初始化剩余本金
        List<InstDetail> arrayInsts = JSONObject.parseArray(installmentInfo.getInstDetails(), InstDetail.class);
        // 房贷分期细则转Map key=期数repayNo
        Map<Integer, InstDetail> list2Map = list2Map(arrayInsts);
        int sTerms = 0;
        // if (installmentInfo.getInstOrder() != 1) {// 多次分期
        //
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // // 判断实际期数
        // // 查询第一次信息
        // InstallmentInfo instInfo =
        // installmentInfoDao.getInstInfoByOrderId(installmentInfo.getOrderId());
        // /* 最后还款日 */
        // String lastRepayDate =
        // billDateService.getBillDate(sdf.format(instInfo.getBeginDate()),
        // installmentPlan.getRepayDateId(),
        // sdf.format(installmentInfo.getBeginDate()), installmentPlan.getSecondRepayDateId());
        // String firstRepayDate =
        // billDateService.getBillDate(sdf.format(instInfo.getBeginDate()),
        // installmentPlan.getRepayDateId());
        // try {
        // sTerms = DateUtil.getMonthSpace(firstRepayDate, lastRepayDate);
        // } catch (ParseException e) {
        // logger.error("多次分期计算期数差发送异常", e);
        // }
        // installTerms = installTerms - sTerms;
        // }
        detailList = new ArrayList<InstallmentDetail>(installTerms);
        BigDecimal principal = calPrincipal(installmentInfo, installmentPlan, installAmt, installTerms);// 需要修改关于本金，剩余本金计算
        // 根据不同还款方式生成详情信息(先息后本，等本等息,一次性还本付息,等额本金,等额本息等)
        if (installmentPlan.getRepayMethod() == 3) { // 一次性还本付息,不存在上收情况
            // 仅有一条还款计划
            InstallmentDetail installmentDetail = createDetail(installmentInfo, installmentPlan);
            installmentDetail.setCycle(getCycle(list2Map));
            detailList.add(installmentDetail);
        } else {
            for (int i = 1; i <= installTerms; i++) {
                InstallmentDetail installmentDetail = new InstallmentDetail();
                installmentDetail.setOrderId(installmentInfo.getOrderId());
                installmentDetail.setSubOrderId(installmentInfo.getSubOrderId());
                installmentDetail.setOrderDate(installmentInfo.getOrderDate());// 订单时间
                installmentDetail.setRepayNo(i + sTerms);
                installmentDetail.setInstallInfoId(installmentInfo.getId());
                installmentDetail.setRegId(installmentInfo.getRegId());
                installmentDetail.setOpenId(installmentInfo.getOpenId());
                installmentDetail.setInstallDate(installmentInfo.getBeginDate() == null ? new Date() : installmentInfo
                        .getBeginDate());
                if (installmentPlan.getRepayMethod() == 1 && i == installTerms) {// 先息后本
                    installmentDetail.setPrincipal(installAmt);// 本期分期本金
                } else {// 等本等息
                    installmentDetail.setPrincipal(principal);// 本期分期本金
                }
                if (i > 1) {
                    remainPrincipal = BigDecimalUtil.sub(remainPrincipal, principal);
                }
                installmentDetail.setRemainPrincipal(remainPrincipal);// 剩余本金
                installmentDetail.setRealPayamt(principal);// 真实分摊金额
                installmentDetail.setPlanId(planId);
                installmentDetail.setInstallSumAmt(installmentInfo.getInstallSumAmt());
                installmentDetail.setInstallAmt(installAmt);//
                installmentDetail.setTakeInterest(installmentInfo.getTakeInterest());// 是否上收利息
                installmentDetail.setInterestAmt(installmentInfo.getInterestAmt());// 上收利息
                installmentDetail.setTakeMonth(installmentInfo.getTakeMonth());// 是否上收月供
                installmentDetail.setTakePaymentAmt(installmentInfo.getTakePaymentAmt());// 上收月供金额
                installmentDetail.setTakePayment(installmentInfo.getTakePayment());// 上收月供期数
                installmentDetail.setOtherAmt(installmentInfo.getOtherAmt());// 其他费用
                installmentDetail.setInstallTerms(installTerms);
                installmentDetail.setMerchantNo(installmentInfo.getMerchantNo());
                installmentDetail.setInstOrder(installmentInfo.getInstOrder());
                installmentDetail.setStatus(1);
                installmentDetail.setCycle(getCycle(installmentInfo, list2Map, i));// 动态周期
                installmentDetail.setUseFreeInterest(0);// 不实现免息卷
                detailList.add(installmentDetail);
            }
        }
        return detailList;
    }

    /**
     * 获取动态周期
     * 
     * @param installmentInfo
     * @param list2Map
     * @param repayNo
     * @return
     */
    private Integer getCycle(InstallmentInfo installmentInfo, Map<Integer, InstDetail> list2Map, int repayNo) {
        if (installmentInfo.getTakeMonth() == 1) {
            // 上收月供的情况,首期动态周期为0，其他根据list2Map获取
            if (repayNo == 1) {
                // 首期
                return 0;
            } else {
                if (list2Map.get(repayNo - 1) == null) {
                    return 0;
                }
                return list2Map.get(repayNo - 1).getDays();
            }
        } else {
            // 非上收月供
            if (list2Map.get(repayNo) == null) {
                return 0;
            }
            return list2Map.get(repayNo).getDays() == null ? 0 : list2Map.get(repayNo).getDays();
        }

    }

    /**
     * 获取总天数
     * 
     * @param list2Map
     * @return
     */
    private Integer getCycle(Map<Integer, InstDetail> list2Map) {
        int days = 0;
        for (int i = 1; i <= list2Map.size(); i++) {
            days += list2Map.get(i).getDays();
        }
        return days;
    }

    /**
     * 生成账单分摊详情
     * 
     * @author Yeoman
     * @param installmentInfo
     * @return
     */
    private List<InstallmentDetail> getInstDetails(InstallmentInfo installmentInfo) {
        List<InstallmentDetail> detailList = null;
        BigDecimal installAmt = installmentInfo.getInstallAmt();
        int installTerms = installmentInfo.getInstallTerms();
        long planId = installmentInfo.getPlanId();// 分期方案ID
        InstallmentPlan installmentPlan = installmentPlanDao.getById(planId);
        BigDecimal remainPrincipal = installAmt;// 初始化剩余本金
        int sTerms = 0;
        if (installmentInfo.getInstOrder() != 1) {// 多次分期

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // 判断实际期数
            // 查询第一次信息
            InstallmentInfo instInfo = installmentInfoDao.getInstInfoByOrderId(installmentInfo.getOrderId());
            /* 最后还款日 */
            String lastRepayDate =
                    billDateService.getBillDate(sdf.format(instInfo.getBeginDate()), installmentPlan.getRepayDateId(),
                            sdf.format(installmentInfo.getBeginDate()), installmentPlan.getSecondRepayDateId());
            String firstRepayDate =
                    billDateService.getBillDate(sdf.format(instInfo.getBeginDate()), installmentPlan.getRepayDateId());
            try {
                sTerms = DateUtil.getMonthSpace(firstRepayDate, lastRepayDate);
            } catch (ParseException e) {
                logger.error("多次分期计算期数差发送异常", e);
            }
            installTerms = installTerms - sTerms;
        }
        detailList = new ArrayList<InstallmentDetail>(installTerms);
        BigDecimal principal = calPrincipal(installmentInfo, installmentPlan, installAmt, installTerms);// 需要修改关于本金，剩余本金计算
        // 根据不同还款方式生成详情信息(先息后本，等本等息,一次性还本付息,等额本金,等额本息等)
        if (installmentPlan.getRepayMethod() == 3) { // 一次性还本付息
            // 仅有一条还款计划
            InstallmentDetail installmentDetail = createDetail(installmentInfo, installmentPlan);
            detailList.add(installmentDetail);
        } else {
            for (int i = 1; i <= installTerms; i++) {
                InstallmentDetail installmentDetail = new InstallmentDetail();
                installmentDetail.setOrderId(installmentInfo.getOrderId());
                installmentDetail.setSubOrderId(installmentInfo.getSubOrderId());
                installmentDetail.setOrderDate(installmentInfo.getOrderDate());// 订单时间
                installmentDetail.setRepayNo(i + sTerms);
                installmentDetail.setInstallInfoId(installmentInfo.getId());
                installmentDetail.setRegId(installmentInfo.getRegId());
                installmentDetail.setOpenId(installmentInfo.getOpenId());
                installmentDetail.setInstallDate(installmentInfo.getBeginDate() == null ? new Date() : installmentInfo
                        .getBeginDate());
                if (installmentPlan.getRepayMethod() == 1 && i == installTerms) {// 先息后本
                    installmentDetail.setPrincipal(installAmt);// 本期分期本金
                } else {// 等本等息
                    installmentDetail.setPrincipal(principal);// 本期分期本金
                }
                if (i > 1) {
                    remainPrincipal = BigDecimalUtil.sub(remainPrincipal, principal);
                }
                installmentDetail.setRemainPrincipal(remainPrincipal);// 剩余本金
                installmentDetail.setRealPayamt(principal);// 真实分摊金额
                installmentDetail.setPlanId(planId);
                installmentDetail.setInstallSumAmt(installmentInfo.getInstallSumAmt());
                installmentDetail.setInstallAmt(installAmt);//
                installmentDetail.setTakeInterest(installmentInfo.getTakeInterest());// 是否上收利息
                installmentDetail.setInterestAmt(installmentInfo.getInterestAmt());// 上收利息
                installmentDetail.setTakeMonth(installmentInfo.getTakeMonth());// 是否上收月供
                installmentDetail.setTakePaymentAmt(installmentInfo.getTakePaymentAmt());// 上收月供金额
                installmentDetail.setTakePayment(installmentInfo.getTakePayment());// 上收月供期数
                installmentDetail.setOtherAmt(installmentInfo.getOtherAmt());// 其他费用
                installmentDetail.setTakeOtherAmt(installmentInfo.getTakeOtherAmt());// 是否上收其他费用
                installmentDetail.setInterestTerms(installmentInfo.getInterestTerms());// 剩余利息期数
                installmentDetail.setInstallTerms(installTerms);
                installmentDetail.setMerchantNo(installmentInfo.getMerchantNo());
                installmentDetail.setInstOrder(installmentInfo.getInstOrder());
                installmentDetail.setStatus(1);
                installmentDetail.setUseFreeInterest(0);// 不实现免息卷
                installmentDetail.setSmsMobile(installmentInfo.getSmsMobile());
                detailList.add(installmentDetail);
            }
        }
        return detailList;
    }

    /**
     * 生成detail类
     * 
     * @param installmentInfo
     * @return
     */
    private InstallmentDetail createDetail(InstallmentInfo installmentInfo, InstallmentPlan installmentPlan) {
        InstallmentDetail installmentDetail = new InstallmentDetail();
        installmentDetail.setOrderId(installmentInfo.getOrderId());
        installmentDetail.setSubOrderId(installmentInfo.getSubOrderId());
        installmentDetail.setOrderDate(installmentInfo.getOrderDate());// 订单时间
        installmentDetail.setRepayNo(1);
        installmentDetail.setInstallInfoId(installmentInfo.getId());
        installmentDetail.setRegId(installmentInfo.getRegId());
        installmentDetail.setOpenId(installmentInfo.getOpenId());
        installmentDetail.setInstallDate(installmentInfo.getBeginDate() == null ? new Date() : installmentInfo
                .getBeginDate());
        installmentDetail.setPrincipal(installmentInfo.getInstallAmt());// 本期分期本金
        installmentDetail.setRemainPrincipal(installmentInfo.getInstallAmt());// 剩余本金
        installmentDetail.setRealPayamt(installmentInfo.getInstallAmt());// 真实分摊金额
        installmentDetail.setPlanId(installmentPlan.getId());
        installmentDetail.setInstallSumAmt(installmentInfo.getInstallSumAmt());
        installmentDetail.setInstallAmt(installmentInfo.getInstallAmt());//
        installmentDetail.setTakeInterest(installmentInfo.getTakeInterest());// 是否上收利息
        installmentDetail.setInterestAmt(installmentInfo.getInterestAmt());// 上收利息
        installmentDetail.setTakeMonth(installmentInfo.getTakeMonth());// 是否上收月供
        installmentDetail.setTakePaymentAmt(installmentInfo.getTakePaymentAmt());// 上收月供金额
        installmentDetail.setTakePayment(installmentInfo.getTakePayment());// 上收月供期数
        installmentDetail.setOtherAmt(installmentInfo.getOtherAmt());// 其他费用
        installmentDetail.setInstallTerms(installmentInfo.getInstallTerms());// 分期期数
        installmentDetail.setMerchantNo(installmentInfo.getMerchantNo());
        installmentDetail.setInstOrder(installmentInfo.getInstOrder());
        installmentDetail.setStatus(1);
        installmentDetail.setUseFreeInterest(0);// 不实现免息卷
        return installmentDetail;
    }

    /**
     * 计算每期本金
     * 
     * @author Yeoman
     * @param installmentPlan
     * @param installAmt
     * @param installTerms
     * @return
     */
    private BigDecimal calPrincipal(InstallmentInfo installmentInfo, InstallmentPlan installmentPlan,
            BigDecimal installAmt, int installTerms) {
        if (installmentPlan.getRepayMethod() == 1) {// 先息后本
            return BigDecimal.ZERO;
        } else {// 等本等息
            // 是否上收月供
            if (installmentInfo.getTakeMonth() == 1) {// 上收月供
                return BigDecimalUtil.div(installAmt, new BigDecimal(installTerms - installmentInfo.getTakePayment()));
            } else {
                return BigDecimalUtil.div(installAmt, new BigDecimal(installTerms));
            }

        }
    }

    @Override
    public void resetInstInfo(final InstallmentInfo installmentInfo) {
        // 通知消息队列生成分摊详情
        notifyJmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(NotifyUtil.formatInstInfo(installmentInfo, msgConfig.getBaseUrl()));
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> fastGenRepayBill(FastBillBean fbb) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 第一步保存inst_info 信息
        InstallmentInfo installmentInfo = fbb2Object(fbb);
        try {
            installmentInfoDao.insertInstalInfo(installmentInfo);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        InstallmentPlan installmentPlan = installmentPlanDao.getById(fbb.getPlanId());
        // 第二步保存inst_detail信息
        List<InstallmentDetail> detailList = inst2AllDetail(fbb, installmentInfo, installmentPlan);
        installmentDetailDao.insert(detailList);
        // 第三步保存inst_billinfo信息
        Map<Integer, InstallmentDetail> detailMap = new HashMap<Integer, InstallmentDetail>(detailList.size());
        for (InstallmentDetail installmentDetail : detailList) {
            detailMap.put(installmentDetail.getRepayNo(), installmentDetail);
        }
        List<InstallmentBillInfo> billList = null;
        try {
            billList = inst2AllBills(fbb, detailMap, installmentInfo, installmentPlan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        installmentBillInfoDao.insert(billList);
        result.put("retCode", AccountConstant.SUCCESS);
        result.put("retMsg", "账单生成成功");
        return result;
    }

    private List<InstallmentBillInfo> inst2AllBills(FastBillBean fbb, Map<Integer, InstallmentDetail> detailMap,
            InstallmentInfo installmentInfo, InstallmentPlan installmentPlan) throws ParseException {
        List<InstallmentBillInfo> bills = new ArrayList<InstallmentBillInfo>(detailMap.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 初始化剩余本息 = 实际分期金额+剩余到账户系统分期的利息
        BigDecimal remainPriandInterest =
                BigDecimalUtil.add(installmentInfo.getInstallAmt(), installmentInfo.getInterestAmt());
        for (int i = 1; i <= fbb.getPlan().size(); i++) {
            Plan plan = fbb.getPlan().get(i - 1);
            InstallmentDetail installmentDetail = detailMap.get(Integer.parseInt(plan.getNumber()));
            // 月利息
            BigDecimal repayInterest = BigDecimalUtil.mul(new BigDecimal(plan.getInterest()), new BigDecimal(100));
            // 月本金
            BigDecimal principal = BigDecimalUtil.mul(new BigDecimal(plan.getCapital()), new BigDecimal(100));
            BigDecimal curRepayAmt = BigDecimalUtil.add(repayInterest, principal);
            // 每日滞纳金
            Map<String, BigDecimal> preOverdueMap =
                    overdueIterestService.calculateOverdueInterestWithFixed(
                            installmentDetail.getRemainPrincipal(), installmentDetail.getInstallAmt(), curRepayAmt,
                            installmentDetail.getContractAmt(), remainPriandInterest, 1,
                            installmentPlan.getOvedueId() + "");
            BigDecimal poi = preOverdueMap.get("Amt");
            BigDecimal fixedOverdueAmt = preOverdueMap.get("Fixed");

            // 最早还款日期
            Date billDate = new Date(Long.parseLong(plan.getFinalrepaymentdate()));
            // 获取第一期最早还款日，方便计算遍历中的账单日期
            String earlyPayDate = getEarlyPayDate(sdf.format(billDate), installmentPlan);
            Date earliestPayDate = null;
            if (earlyPayDate != null) {
                Calendar earliestPayDateCal = DateUtil.parseCalendar(earlyPayDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
                earlyPayDate = DateUtil.format(earliestPayDateCal, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
                earliestPayDate = sdf.parse(earlyPayDate);
            }
            InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo();
            installmentBillInfo.setCurRealRepayamt(BigDecimal.ZERO);// 本期实际还款金额
            installmentBillInfo.setCurRepayPrincipal(principal);// 本期应还本金
            installmentBillInfo.setCurRepayInterest(repayInterest);// 本期应还利息
            installmentBillInfo.setCurRepayAmt(curRepayAmt);// 本期应还总金额
            installmentBillInfo.setCurRepayOverdueInterest(BigDecimal.ZERO);// 本期应还逾期金额
            installmentBillInfo.setDelayBeginDate(sdf.parse(plan.getFinalrepaymentdate()));// 滞纳金计算时间
            installmentBillInfo.setInstallAmt(installmentDetail.getInstallAmt());// 分期金额
            installmentBillInfo.setInstallDetailId(1L);// 分期明细ID
            installmentBillInfo.setInstallInfoId(installmentInfo.getId());// 分期总账号ID
            installmentBillInfo.setLastRepayDate(sdf.parse(plan.getFinalrepaymentdate()));// 最后还款日
            installmentBillInfo.setOpenId(installmentInfo.getOpenId());// 账户号
            installmentBillInfo.setOverdueDays(0);// 逾期天数
            installmentBillInfo.setPartPayment(0);// 部分还款
            installmentBillInfo.setPreInterest(BigDecimal.ZERO);// 每天的利息
            installmentBillInfo.setPreOverdueInterest(poi);// 每日滞纳金
            installmentBillInfo.setPrePayment(installmentPlan.getPrepayMent());// 提前还款
            installmentBillInfo.setPrincipal(installmentDetail.getPrincipal());// 本金
            installmentBillInfo.setRegId(installmentInfo.getRegId());// 注册号
            installmentBillInfo.setRemainPrincipal(installmentDetail.getRemainPrincipal());// 剩余本金
            installmentBillInfo.setRemainPriandInterest(remainPriandInterest);// 剩余本息
            // 下一期剩余本息 = 上期剩余本期-(本金+利息)
            remainPriandInterest =
                    BigDecimalUtil.sub(remainPriandInterest, BigDecimalUtil.add(principal, repayInterest));
            installmentBillInfo.setRepayNo(Integer.parseInt(plan.getNumber()));// 还款序号
            installmentBillInfo.setInstOrder(installmentDetail.getInstOrder());// 放款顺序
            installmentBillInfo.setOrderId(installmentDetail.getOrderId());// 订单id
            installmentBillInfo.setOrderDate(installmentDetail.getOrderDate());// 订单时间
            installmentBillInfo.setCurRepayDate(null);// 还款时间
            installmentBillInfo.setRealPayamt(curRepayAmt);// 实际分摊金额 = 本金+利息
            installmentBillInfo.setEarliestPayDate(earliestPayDate);// 最早还款日
            installmentBillInfo.setPlanId(installmentPlan.getId());// 计划id
            installmentBillInfo.setInstallSumAmt(installmentDetail.getInstallSumAmt());// 合同金额
            installmentBillInfo.setMerchantNo(installmentDetail.getMerchantNo());// 商户号
            installmentBillInfo.setFixedOverdueAmt(fixedOverdueAmt);// 逾期固定收取金额
            installmentBillInfo.setStatus(1);// 账单状态
            bills.add(installmentBillInfo);
        }
        return bills;
    }

    /**
     * 快速生成分期详情列表
     * 
     * @param fbb
     * @param installmentInfo
     * @param installmentPlan
     * @return
     */
    private List<InstallmentDetail> inst2AllDetail(FastBillBean fbb, InstallmentInfo installmentInfo,
            InstallmentPlan installmentPlan) {
        List<InstallmentDetail> detailList = new ArrayList<InstallmentDetail>(fbb.getPlan().size());
        BigDecimal remainPrincipal = installmentInfo.getInstallAmt();// 初始化剩余本金
        for (int i = 1; i <= fbb.getPlan().size(); i++) {
            Plan plan = fbb.getPlan().get(i - 1);
            BigDecimal principal = new BigDecimal(plan.getCapital());
            InstallmentDetail installmentDetail = new InstallmentDetail();
            installmentDetail.setOrderId(installmentInfo.getOrderId());
            installmentDetail.setOrderDate(installmentInfo.getOrderDate());// 订单时间
            installmentDetail.setRepayNo(Integer.parseInt(plan.getNumber()));
            installmentDetail.setInstallInfoId(installmentInfo.getId());
            installmentDetail.setRegId(installmentInfo.getRegId());
            installmentDetail.setOpenId(installmentInfo.getOpenId());
            installmentDetail.setInstallDate(installmentInfo.getBeginDate() == null ? new Date() : installmentInfo
                    .getBeginDate());
            if (installmentPlan.getRepayMethod() == 1) {// 先息后本
                installmentDetail.setPrincipal(principal);// 本期分期本金
            } else {// 等本等息
                installmentDetail.setPrincipal(principal);// 本期分期本金
            }
            if (i > 1) {
                remainPrincipal = BigDecimalUtil.sub(remainPrincipal, principal);
            }
            installmentDetail.setRemainPrincipal(remainPrincipal);// 剩余本金
            installmentDetail.setRealPayamt(principal);// 真实分摊本金额
            installmentDetail.setPlanId(fbb.getPlanId());
            installmentDetail.setInstallSumAmt(installmentInfo.getInstallSumAmt());
            installmentDetail.setInstallAmt(installmentInfo.getInstallAmt());//
            installmentDetail.setTakeInterest(installmentInfo.getTakeInterest());// 是否上收利息
            installmentDetail.setInterestAmt(installmentInfo.getInterestAmt());// 上收利息
            installmentDetail.setTakeMonth(installmentInfo.getTakeMonth());// 是否上收月供
            installmentDetail.setTakePaymentAmt(installmentInfo.getTakePaymentAmt());// 上收月供金额
            installmentDetail.setTakePayment(installmentInfo.getTakePayment());// 上收月供期数
            installmentDetail.setInstallTerms(fbb.getInstallTerms());
            installmentDetail.setMerchantNo(installmentInfo.getMerchantNo());
            installmentDetail.setInstOrder(installmentInfo.getInstOrder());
            installmentDetail.setStatus(1);
            installmentDetail.setUseFreeInterest(0);// 不实现免息卷
            detailList.add(installmentDetail);
        }
        return detailList;
    }

    /**
     * FastBillBean 转 InstallmentInfo
     * 
     * @param fbb
     * @return
     */
    private InstallmentInfo fbb2Object(FastBillBean fbb) {
        InstallmentInfo installmentInfo = new InstallmentInfo();
        installmentInfo.setOrderId(fbb.getOrderId());
        installmentInfo.setOrderDate(fbb.getOrderDate());
        installmentInfo.setRegId(fbb.getRegId());
        installmentInfo.setOpenId(fbb.getOpenId());
        installmentInfo.setBeginDate(fbb.getBeginDate());
        installmentInfo.setMerchantNo(fbb.getMerchantNo());
        installmentInfo.setContractAmt(fbb.getContractAmt());// 合同金额
        installmentInfo.setInstallSumAmt(BigDecimalUtil.mul(fbb.getInstallSumAmt(), new BigDecimal(100)));
        installmentInfo.setInstallAmt(BigDecimalUtil.mul(fbb.getInstallAmt(), new BigDecimal(100)));
        installmentInfo.setInstallTerms(fbb.getInstallTerms());
        installmentInfo.setPlanId(fbb.getPlanId());
        installmentInfo.setTakeInterest(0);
        installmentInfo.setTakeMonth(0);
        installmentInfo.setTakePayment(0);
        installmentInfo.setTakePaymentAmt(BigDecimal.ZERO);
        installmentInfo.setSourcesFunding(fbb.getSourcesFunding());// 资金来源
        installmentInfo.setFundId(fbb.getFundId());// 上标ID
        return installmentInfo;
    }

    // 获取第一期最早还款日
    private String getEarlyPayDate(String billDate, InstallmentPlan installmentPlan) {
        String earlyPayDate = null;
        if (installmentPlan.getLockRepayDate() == 2) {
            earlyPayDate = lockRepayDateService.getLockDays(billDate, installmentPlan.getLockRepayDateId());
        }
        return earlyPayDate;
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
        return installmentInfoDao.updateInstallmentInfoByCondition(installmentInfo);
    }

    @Override
    public Map<String, BigDecimal> calculateAmt(CalculateAmt calculateAmt) {
        InstPlan instPlan = calculateAmt.getInstPlan();
        // 通过planId 查询 账务中的Plan
        InstallmentPlan installmentPlan = installmentPlanDao.getByPlanId(instPlan.getPlanId());
        try {
            return getDetail(calculateAmt.getAmt(), instPlan, installmentPlan);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, BigDecimal> getDetail(BigDecimal orderAmt, InstPlan instPlan, InstallmentPlan installmentPlan) {
        Map<String, BigDecimal> detailMap = new HashMap<String, BigDecimal>();
        // 首付
        BigDecimal downPayment = BigDecimalUtil
                .mul(orderAmt, new BigDecimal(instPlan.getDownPaymentRatio()).divide(new BigDecimal(100), 5,
                        BigDecimal.ROUND_HALF_UP));
        // 服务费
        BigDecimal serviceFee = BigDecimalUtil.mul(orderAmt,
                new BigDecimal(instPlan.getServiceFeeRatio()).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP));
        // 保证金
        BigDecimal margin =
                BigDecimalUtil.mul(
                        orderAmt,
                        BigDecimalUtil.add(new BigDecimal(instPlan.getMarginRatio()),
                                new BigDecimal(instPlan.getFloatServiceFeeRatio())).divide(new BigDecimal(100), 5,
                                BigDecimal.ROUND_HALF_UP));

        // 上收息
        // String feeAmount =
        // 剩余本金金额 = (总金额-首付)
        BigDecimal leftAmt = BigDecimalUtil.sub(orderAmt, downPayment);
        // 剩余期数 = (总期数-上收期数)
        int leftTerms = instPlan.getInstallPeriods() - instPlan.getFeeYear();
        // 月利息 = (总金额-首付)*利息率
        BigDecimal feeCount = BigDecimalUtil.mul(leftAmt,
                new BigDecimal(instPlan.getFeeRatio()).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP));
        // 剩余利息 = 总利息*(总期数-上收期数)
        BigDecimal leftFee = BigDecimalUtil.mul(feeCount, new BigDecimal(leftTerms));
        // 月上收利息 = (总金额-首付)*上收利率
        BigDecimal upInterestCount = BigDecimal.ZERO;
        if (instPlan.getUpInterestFee() != null) {
            upInterestCount =
                    BigDecimalUtil.mul(leftAmt,
                            new BigDecimal(instPlan.getUpInterestFee()).divide(new BigDecimal(100), 5,
                                    BigDecimal.ROUND_HALF_UP));
        }
        // 上收利息 = 月上收利息 *上收月数
        BigDecimal feeAmount = BigDecimalUtil.mul(upInterestCount, new BigDecimal(instPlan.getFeeYear()));
        // 月供 = (剩余金额 + 剩余利息)/分期期数,等本等息,先息后本和一次性还本付息的月供不一样
        BigDecimal monthMake = BigDecimal.ZERO;
        if (1 == installmentPlan.getRepayMethod()) {
            if (instPlan.getInstallPeriods() != 0) {
                // 先息后本[先息后本 每月利息=剩余利息/*剩余期数 例如：借款金额1000 期数6期 月利率：1% 前5期利息都是100 第六期：1100]
                monthMake = leftFee.divide(new BigDecimal(instPlan.getInstallPeriods()), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else if (2 == installmentPlan.getRepayMethod()) {
            // 等本等息
            monthMake =
                    BigDecimalUtil.add(leftAmt, leftFee).divide(new BigDecimal(instPlan.getInstallPeriods()), 2,
                            BigDecimal.ROUND_HALF_UP);
        } else if (3 == installmentPlan.getRepayMethod()) {
            // 一次性还本付息[一次性还本付息：月供=本金+本金*月利率*期数]
            monthMake = BigDecimalUtil.add(leftAmt, leftFee);
        }
        // 上收月供 = 月供*上收期数
        BigDecimal monthAmount = BigDecimalUtil.mul(monthMake, new BigDecimal(instPlan.getTakePayment()));

        // 预付款 = 上收息+首付+保证金+服务费+上收月供
        BigDecimal preAmount = BigDecimalUtil.add(feeAmount, downPayment, margin, serviceFee, monthAmount);
        // 上标金额
        // BigDecimal sbAmt = BigDecimalUtil.add(orderAmt, serviceFee);
        // 临时修改
        BigDecimal sbAmt = BigDecimalUtil.add(orderAmt, BigDecimal.ZERO);
        // 放款金额
        BigDecimal loanAmt = BigDecimalUtil.sub(orderAmt, BigDecimal.ZERO);
        detailMap.put("downPayment", downPayment == null ? BigDecimal.ZERO : downPayment);// 首付
        detailMap.put("serviceFee", serviceFee == null ? BigDecimal.ZERO : serviceFee);// 服务费
        detailMap.put("margin", margin == null ? BigDecimal.ZERO : margin);// 保证金
        detailMap.put("leftAmt", leftAmt == null ? BigDecimal.ZERO : leftAmt);// 剩余期数
        detailMap.put("feeAmount", feeAmount == null ? BigDecimal.ZERO : feeAmount);// 上收利息
        detailMap.put("monthMake", monthMake == null ? BigDecimal.ZERO : monthMake);// 月供
        detailMap.put("monthAmount", monthAmount == null ? BigDecimal.ZERO : monthAmount);// 上收月供
        detailMap.put("preAmount", preAmount == null ? BigDecimal.ZERO : preAmount);// 预付款
        detailMap.put("sbAmt", sbAmt == null ? BigDecimal.ZERO : sbAmt);// 上标金额
        detailMap.put("contractAmt", sbAmt == null ? BigDecimal.ZERO : sbAmt);// 合同金额
        detailMap.put("loanAmt", loanAmt == null ? BigDecimal.ZERO : loanAmt);// 放款金额
        return detailMap;
    }

    @Override
    public long saveInstalInfo(final InstallmentInfo installmentInfo) {
        long instId = installmentInfoDao.insertInstalInfo(installmentInfo);
        installmentInfo.setId(instId);
        // 通知消息队列生成分摊详情
        notifyJmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(NotifyUtil.formatLSInstInfo(installmentInfo, msgConfig.getBaseUrl()));
            }
        });
        return instId;
    }

    @Override
    public Map<String, Object> genLSInstDetail(final InstallmentInfo installmentInfo) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<InstallmentDetail> detailList = getInstDetails(installmentInfo);
            installmentDetailDao.insert(detailList);
            // 通知消息队列生成账单详情
            notifyJmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(NotifyUtil.formatLSBill(installmentInfo, msgConfig.getBaseUrl()));
                }
            });
            result.put("retCode", AccountConstant.SUCCESS);
            result.put("retMsg", "分摊详情生成成功");
        } catch (Exception e) {
            logger.error("插入分摊详情发生异常", e);
            result.put("retCode", AccountConstant.ERROR);
        }
        return result;
    }
}
