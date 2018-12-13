package com.iqb.consumer.service.account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant.OverdueConstant;
import com.iqb.consumer.common.constant.AccountConstant.RedisLockConstant;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentPlan;
import com.iqb.consumer.data.layer.mysql.bean.overdue.DelayPayment;
import com.iqb.consumer.data.layer.mysql.bean.product.LockRepayDate;
import com.iqb.consumer.data.layer.mysql.dao.DelayPaymentDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentBillInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentPlanDao;
import com.iqb.consumer.data.layer.mysql.dao.product.OverdueInterestDao;
import com.iqb.consumer.finance.cache.kv.KVCache;
import com.iqb.consumer.service.product.OverdueIterestService;
import com.iqb.consumer.service.sms.SendSmsService;

/**
 * 
 * Description: 逾期调度服务实现类
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月11日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Service
public class BizSchedulerOverdueServiceImpl implements BizSchedulerOverdueService {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(BizSchedulerOverdueServiceImpl.class);

    /** 每页最大数量 **/
    private int numPerPage = 500;

    @Resource
    private InstallmentBillInfoDao installmentBillInfoDao;

    @Resource
    private OverdueIterestService overdueIterestService;

    @Resource
    private InstallmentPlanDao installmentPlanDao;

    @Resource
    private InstallmentInfoDao installmentInfoDao;

    @Resource
    private DelayPaymentDao delayPaymentDao;

    @Resource
    private KVCache<LockRepayDate> lockRepayDateCache;

    @Resource
    private KVCache<Integer> lockLockedCache;

    @Resource
    private SendSmsService sendSmsService;

    @Resource
    private OverdueInterestDao overdueInterestDao;

    @Override
    public void calOverdueInterest(String orderId, String instBillId, String startDate, String endDate) {
        try {
            /** 加逾期调度redis锁 **/
            lockLockedCache.putForever(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK_KEY,
                    Integer.parseInt(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK));

            Long startT = System.currentTimeMillis();

            if (StringUtil.isEmpty(endDate)) {
                /** 每日调度 **/
                endDate = DateUtil.getDateString(new Date(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            } else {
                /** 添加跑批时间 加入redis控制 **/
                lockLockedCache.putForever(RedisLockConstant.BIZ_LOCKED_DAY_LOCK_KEY, Integer.parseInt(endDate));
            }
            if (StringUtil.isEmpty(startDate)) {
                startDate = DateUtil.format(DateUtil.getYesterdayDate(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            }
            logger.info("逾期调度服务开始：{}", DateUtil.format(new Date(startT), DateUtil.SIMPLE_DATE_FORMAT));
            List<Date> dateList =
                    DateUtil.getDateListBetween(DateUtil.parseDate(startDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH),
                            DateUtil.parseDate(endDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH));
            if (dateList == null || dateList.size() == 0) {
                logger.error("日期区间为空！！！");
            } else {
                dateList.remove(dateList.size() - 1);
                if (dateList.size() > 0) {
                    for (Date d : dateList) {
                        this.calOverdueInterestEve(orderId, instBillId,
                                DateUtil.getDateString(d, DateUtil.SHORT_DATE_FORMAT_NO_DASH));
                    }
                }
            }
            Long endT = System.currentTimeMillis();
            logger.info("---逾期调度服务完成：{}", DateUtil.format(new Date(endT), DateUtil.SIMPLE_DATE_FORMAT));
            logger.info(endDate + "---逾期调度服务完成所花时间：{} 秒", (endT - startT) / 1000);
        } catch (Exception e) {
            logger.error("逾期调度服务异常：", e);
        } finally {
            /** 清除逾期调度redis锁 **/
            lockLockedCache.remove(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK_KEY);
        }
    }

    private void calOverdueInterest4Test(String orderId, String instBillId, String endDate) {
        try {
            /** 加逾期调度redis锁 **/
            lockRepayDateCache.put(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK_KEY,
                    RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK,
                    Integer.parseInt(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK_TIME));

            Long startT = System.currentTimeMillis();
            if (StringUtil.isEmpty(endDate)) {
                endDate = DateUtil.getDateString(DateUtil.getYesterdayDate(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            }
            logger.info("逾期调度服务开始：" + startT);

            /** 该时间段需要进行库锁,通过Redis变量控制进行封库 **/
            logger.info("计算指定日期逾期利息:orderId->" + orderId + ",instBillId->" + instBillId + ",calDate->" + endDate);
            /** 页数 **/
            int pageNum = 1;
            PageParam pageParam = new PageParam(pageNum, numPerPage);
            /** 根据查询参数查询账单期的账单 **/
            Date delayDate = null;
            if (StringUtil.isEmpty(endDate)) {
                delayDate = DateUtil.getYesterdayDate();
            } else {
                delayDate = DateUtil.parseDate(endDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            }
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("orderId", orderId);
            paramMap.put("instBillId", instBillId);
            paramMap.put("delayDate", delayDate);
            /** 处理账单期账单 **/
            PageBean pageBean = this.installmentBillInfoDao.listOverdueInterestPage(pageParam, paramMap);
            logger.info("查询账单期的账单:pageBean->" + JSONObject.toJSONString(pageBean));
            while (pageNum <= pageBean.getEndPageIndex()) {
                List<Object> list = pageBean.getRecordList();
                for (int i = 0; i < list.size(); i++) {
                    InstallmentBillInfo installmentBillInfo = (InstallmentBillInfo) list.get(i);
                    if (installmentBillInfo == null) {
                        return;
                    }
                    List<Date> dateList =
                            DateUtil.getDateListBetween(installmentBillInfo.getDelayBeginDate(),
                                    DateUtil.parseDate(endDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH));
                    /** 获取上一期的账单信息 **/
                    InstallmentBillInfo lastInstallmentBillInfo =
                            this.installmentBillInfoDao.getLastInstallmentBillInfo(installmentBillInfo.getOrderId(),
                                    installmentBillInfo.getInstallInfoId(), installmentBillInfo.getRepayNo() - 1);
                    /** 判断上期账单是否逾期 **/
                    if (lastInstallmentBillInfo == null
                            || lastInstallmentBillInfo.getCurRepayAmt().compareTo(
                                    lastInstallmentBillInfo.getCurRealRepayamt()) <= 0) {} else {
                        dateList =
                                DateUtil.getDateListBetween(installmentBillInfo.getLastRepayDate(),
                                        DateUtil.parseDate(endDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH));
                    }
                    /**
                     * 计算逾息
                     */
                    int overDues = dateList.size();
                    BigDecimal overAmt = installmentBillInfo.getPreOverdueInterest().multiply(new BigDecimal(overDues));

                    /** 剩余本金 **/
                    installmentBillInfo.setCurRepayAmt(installmentBillInfo.getCurRepayAmt().add(overAmt)
                            .add(installmentBillInfo.getFixedOverdueAmt()));
                    installmentBillInfo.setCurRepayOverdueInterest(overAmt);
                    installmentBillInfo.setOverdueDays(overDues);
                    installmentBillInfo.setUpdateTime(new Date());

                    /** 插入逾期信息 **/
                    DelayPayment delayPayment = new DelayPayment();
                    delayPayment.setRegId(installmentBillInfo.getRegId());
                    delayPayment.setOpenId(installmentBillInfo.getOpenId());
                    delayPayment.setRepayNo(installmentBillInfo.getRepayNo());
                    delayPayment.setBillId(installmentBillInfo.getId());
                    delayPayment.setPrincipalAmt(installmentBillInfo.getRemainPrincipal());
                    delayPayment.setDelayAmt(installmentBillInfo.getCurRepayAmt().subtract(
                            installmentBillInfo.getCurRealRepayamt()));
                    delayPayment.setDelayDate(delayDate);

                    /** 事务控制数据库的操作 **/
                    this.operateDb(installmentBillInfo, delayPayment, null, null);
                }
                pageNum++;
                pageParam.setPageNum(pageNum);
                pageBean = this.installmentBillInfoDao.listOverdueInterestPageRepayNoDesc(pageParam, paramMap);
                logger.info("查询账单期的账单:pageBean->" + JSONObject.toJSONString(pageBean));
                logger.info("当前账单页数:pageNum->" + pageNum);
            }
            Long endT = System.currentTimeMillis();
            logger.info("逾期调度服务完成：" + endT);
            logger.info(endDate + "~逾期调度服务完成所花时间：" + (endT - startT) + "毫秒");
        } catch (Exception e) {
            logger.error("逾期调度服务异常：", e);
        } finally {
            /** 清除逾期调度redis锁 **/
            lockRepayDateCache.remove(RedisLockConstant.BIZ_SCHEDULER_OVERDUE_LOCK_KEY);
        }
    }

    /**
     * 
     * Description: 计算指定日期逾期利息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月11日 下午6:13:02
     */
    private void calOverdueInterestEve(String orderId, String instBillId, String calDate) {
        /** 该时间段需要进行库锁,通过Redis变量控制进行封库 **/
        logger.info("计算指定日期逾期利息:orderId->" + orderId + ",instBillId->" + instBillId + ",calDate->" + calDate);
        /** 页数 **/
        int pageNum = 1;
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        /** 根据查询参数查询账单期的账单 **/
        Date delayDate = null;
        if (StringUtil.isEmpty(calDate)) {
            delayDate = DateUtil.getYesterdayDate();
        } else {
            delayDate = DateUtil.parseDate(calDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId);
        paramMap.put("instBillId", instBillId);
        paramMap.put("delayDate", delayDate);
        /** 处理账单期账单 **/
        PageBean pageBean = this.installmentBillInfoDao.listOverdueInterestPageRepayNoDesc(pageParam, paramMap);
        logger.info("查询账单期的账单:pageBean->" + JSONObject.toJSONString(pageBean));
        while (pageNum <= pageBean.getEndPageIndex()) {
            List<Object> list = pageBean.getRecordList();
            for (int i = 0; i < list.size(); i++) {
                InstallmentBillInfo installmentBillInfo = (InstallmentBillInfo) list.get(i);
                try {
                    // 已入库订单不再处理逾期(逾期天数以及罚息)
                    if (!sendSmsService.validateCarStatusStoraged(installmentBillInfo.getOrderId())) {
                        this.dealInstallmentBillInfo(installmentBillInfo, delayDate);
                    }
                } catch (Exception e) {
                    logger.error("账单计算异常：" + JSONObject.toJSONString(installmentBillInfo), e);
                }
            }
            pageNum++;
            pageParam.setPageNum(pageNum);
            pageBean = this.installmentBillInfoDao.listOverdueInterestPageRepayNoDesc(pageParam, paramMap);
            logger.info("查询账单期的账单:pageBean->" + JSONObject.toJSONString(pageBean));
            logger.info("当前账单页数:pageNum->" + pageNum);
        }
    }

    /**
     * 
     * Description: 处理账单信息
     * 
     * @param delayDate
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 上午10:59:22
     */
    private void dealInstallmentBillInfo(InstallmentBillInfo installmentBillInfo, Date delayDate) {
        logger.info("正在处理账单：" + JSONObject.toJSONString(installmentBillInfo));
        if (installmentBillInfo == null) {
            return;
        }
        /** 判断该笔逾期是否已经计算过 **/
        DelayPayment delayPaymentDb =
                delayPaymentDao.getDelayPayment(Long.toString(installmentBillInfo.getId()), delayDate);
        if (delayPaymentDb != null) {
            logger.error("该笔逾期已经计算过！" + JSONObject.toJSONString(delayPaymentDb));
            return;
        }
        /** 获取上一期的账单信息 **/
        InstallmentBillInfo lastInstallmentBillInfo =
                this.installmentBillInfoDao.getLastInstallmentBillInfo(installmentBillInfo.getOrderId(),
                        installmentBillInfo.getInstallInfoId(), installmentBillInfo.getRepayNo() - 1);
        /** 获取分期计划 **/
        InstallmentPlan installmentPlan = this.installmentPlanDao.getById(installmentBillInfo.getPlanId());
        logger.info("正在处理账单明细信息：installmentPlan->" + JSONObject.toJSONString(installmentBillInfo)
                + ", installmentPlan->" + JSONObject.toJSONString(installmentPlan) + ", lastInstallmentBillInfo->"
                + JSONObject.toJSONString(lastInstallmentBillInfo));
        /** 判断上期账单是否逾期 **/
        if (lastInstallmentBillInfo == null
                || lastInstallmentBillInfo.getCurRepayAmt().compareTo(lastInstallmentBillInfo.getCurRealRepayamt()) <= 0) {
            /** 如果没有逾息,判断是否超过逾期日期 **/
            if (DateUtil.diffDays(installmentBillInfo.getDelayBeginDate(), delayDate) < 0) {
                return;
            }
        }
        int overdueDays = installmentBillInfo.getOverdueDays() + 1; // 计算逾期天数
        /** 获取剩余本息 举例：1期剩余本期5K,逾期 2期剩余本息应该为5K,3期剩余本息为5K **/
        InstallmentBillInfo remainPriAndIntBillInfo =
                this.installmentBillInfoDao.getRemainPriAndInterest(installmentBillInfo.getOrderId());
        BigDecimal remainPriAndIntAmt =
                remainPriAndIntBillInfo == null ? installmentBillInfo.getRemainPriandInterest()
                        : remainPriAndIntBillInfo.getRemainPriandInterest();
        logger.info("剩余本息金额为:{}", remainPriAndIntAmt);
        /**
         * 计算逾息
         */
        /** 剩余本金 **/
        BigDecimal remainPrincipal = this.getRemainPrincipal(installmentBillInfo);
        logger.info("---获取账单{}第{}期剩余未还本金{}=", installmentBillInfo.getOrderId(), installmentBillInfo.getRepayNo(),
                remainPrincipal);
        /** 合同金额 **/
        BigDecimal installAmt = installmentBillInfo.getInstallAmt();
        /** 日复利方式计算逾息 **/
        if (installmentPlan.getOverdueMethod() == Integer.parseInt(OverdueConstant.OVERDUE_METHOD_COMPOUND)) {
            BigDecimal beforeOverdueInterest = this.getBeforeOverdueInterest(installmentBillInfo);
            remainPrincipal = remainPrincipal.add(beforeOverdueInterest);
            installAmt = installAmt.add(beforeOverdueInterest);
        }
        /** 月复利方式计算逾息 **/
        if (installmentPlan.getOverdueMethod() == Integer.parseInt(OverdueConstant.MONTH_OVERDUE_METHOD_COMPOUND)) {
            BigDecimal beforeOverdueInterest = this.getBeforeMonthOverdueInterest(installmentBillInfo);
            remainPrincipal = remainPrincipal.add(beforeOverdueInterest);
            installAmt = installAmt.add(beforeOverdueInterest);
        }
        /** 剩余本金 **/
        // 计算违约金 由于违约金涉及基础金额包含(剩余本金和剩余本息，需要关联其他期数) by yeoman@20170628
        Map<String, BigDecimal> overDueMap =
                this.overdueIterestService.calculateOverdueInterestWithFixed(
                        remainPrincipal,
                        installAmt,
                        installmentBillInfo
                                .getCurRepayPrincipal().add(installmentBillInfo.getCurRepayInterest())
                                .add(installmentBillInfo.getOtherAmt()),
                        installmentBillInfo.getContractAmt(), remainPriAndIntAmt, overdueDays, Integer
                                .toString(installmentPlan.getOvedueId()));
        BigDecimal overdueIterest = overDueMap.get("Amt");
        BigDecimal fixedOverdueAmt = overDueMap.get("Fixed");
        installmentBillInfo.setCurRepayAmt(BigDecimalUtil.format(installmentBillInfo.getCurRepayAmt().add(
                overdueIterest)));
        /** 上一期账单信息 **/
        InstallmentBillInfo lastInstallmentBillInfoU = null;

        /**
         * 违约金历史规则，第一次逾期的时候在应还款中添加固定违约金，现在由于需求变更违约金在剩余本金和剩余本息的情况下，违约金不在是固定金额，所有每期的计算公式发生变化为 应还款金额 =
         * 昨日应还款金额-昨日违约金+现在计算出来的违约金
         */

        if (installmentBillInfo.getOverdueDays() == 0) {
            installmentBillInfo.setCurRepayAmt(BigDecimalUtil.format(installmentBillInfo.getCurRepayAmt().add(
                    fixedOverdueAmt)));
        } else {
            BigDecimal curRepayAmt =
                    installmentBillInfo
                            .getCurRepayAmt()
                            .add(fixedOverdueAmt)
                            .subtract(
                                    installmentBillInfo.getFixedOverdueAmt() == null
                                            ? BigDecimal.ZERO
                                            : installmentBillInfo.getFixedOverdueAmt());
            installmentBillInfo.setCurRepayAmt(BigDecimalUtil.format(curRepayAmt));
        }
        InstallmentBillInfo nextInstallmentBillInfo = this.getNextMonthInstallmentBillInfo(installmentBillInfo);
        if (nextInstallmentBillInfo != null
                && DateUtil.getDateString(delayDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH).equals(
                        DateUtil.getDateString(nextInstallmentBillInfo.getLastRepayDate() == null
                                ? null
                                : nextInstallmentBillInfo.getLastRepayDate(), DateUtil.SHORT_DATE_FORMAT_NO_DASH))) {
            installmentBillInfo.setMonthOverdueAmt(installmentBillInfo.getCurRepayOverdueInterest());
            nextInstallmentBillInfo.setRemainPrincipal(installmentBillInfo.getRemainPrincipal());
        }
        installmentBillInfo.setCurRepayOverdueInterest(BigDecimalUtil.format(installmentBillInfo
                .getCurRepayOverdueInterest().add(overdueIterest)));
        installmentBillInfo.setOverdueDays(overdueDays);
        installmentBillInfo.setFixedOverdueAmt(fixedOverdueAmt);// 逾期更新固定违约金
        installmentBillInfo.setStatus(0);// 逾期状态
        installmentBillInfo.setUpdateTime(new Date());

        /** 插入逾期信息 **/
        DelayPayment delayPayment = new DelayPayment();
        delayPayment.setRegId(installmentBillInfo.getRegId());
        delayPayment.setOpenId(installmentBillInfo.getOpenId());
        delayPayment.setRepayNo(installmentBillInfo.getRepayNo());
        delayPayment.setBillId(installmentBillInfo.getId());
        delayPayment.setPrincipalAmt(BigDecimalUtil.format(installmentBillInfo.getRemainPrincipal()));
        delayPayment.setDelayAmt(BigDecimalUtil.format(installmentBillInfo.getCurRepayAmt().subtract(
                installmentBillInfo.getCurRealRepayamt())));
        delayPayment.setDelayDate(delayDate);

        /** 事务控制数据库的操作 **/
        this.operateDb(installmentBillInfo, delayPayment, lastInstallmentBillInfoU, null);
    }

    /**
     * 
     * Description: 获取剩余本金,最近好累啊。
     * <p>
     * 双击666
     * </p>
     * 
     * @param installmentBillInfo
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2017年3月22日 下午4:02:39
     */
    private BigDecimal getRemainPrincipal(InstallmentBillInfo installmentBillInfo) {
        BigDecimal retRemainPrincipal = BigDecimal.ZERO;
        // 校验该产品是否上收月供,true 是 false 否
        if (validateTakeMonth(installmentBillInfo.getOrderId())) {
            // 剩余本金取当前账单的上一期
            retRemainPrincipal =
                    BigDecimalUtil
                            .add(installmentBillInfo.getRemainPrincipal(), installmentBillInfo.getCurRepayPrincipal());
            /** 获取上一期的账单信息 **/
            InstallmentBillInfo lastInstallmentBillInfo =
                    this.installmentBillInfoDao.getLastInstallmentBillInfo(installmentBillInfo.getOrderId(),
                            installmentBillInfo.getInstallInfoId(), installmentBillInfo.getRepayNo() - 1);
            // 判断上一期是否逾期
            while (lastInstallmentBillInfo != null
                    && lastInstallmentBillInfo.getCurRepayAmt().compareTo(lastInstallmentBillInfo.getCurRealRepayamt()) > 0) {
                retRemainPrincipal =
                        BigDecimalUtil
                                .add(lastInstallmentBillInfo.getRemainPrincipal(),
                                        lastInstallmentBillInfo.getCurRepayPrincipal());
                /** 获取上一期的上一期账单信息 **/
                lastInstallmentBillInfo =
                        this.installmentBillInfoDao.getLastInstallmentBillInfo(lastInstallmentBillInfo.getOrderId(),
                                lastInstallmentBillInfo.getInstallInfoId(), lastInstallmentBillInfo.getRepayNo() - 1);
            }
        } else {
            retRemainPrincipal = installmentBillInfo.getRemainPrincipal();
            /** 获取上一期的账单信息 **/
            InstallmentBillInfo lastInstallmentBillInfo =
                    this.installmentBillInfoDao.getLastInstallmentBillInfo(installmentBillInfo.getOrderId(),
                            installmentBillInfo.getInstallInfoId(), installmentBillInfo.getRepayNo() - 1);
            // 判断上一期是否逾期
            while (lastInstallmentBillInfo != null
                    && lastInstallmentBillInfo.getCurRepayAmt().compareTo(lastInstallmentBillInfo.getCurRealRepayamt()) > 0) {
                retRemainPrincipal = lastInstallmentBillInfo.getRemainPrincipal();
                /** 获取上一期的上一期账单信息 **/
                lastInstallmentBillInfo =
                        this.installmentBillInfoDao.getLastInstallmentBillInfo(lastInstallmentBillInfo.getOrderId(),
                                lastInstallmentBillInfo.getInstallInfoId(), lastInstallmentBillInfo.getRepayNo() - 1);
            }
        }

        return retRemainPrincipal;
    }

    /**
     * 
     * Description: 获取传入账单之前的所有期数的逾期利息
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 下午2:08:34
     */
    private BigDecimal getBeforeOverdueInterest(InstallmentBillInfo installmentBillInfo) {
        BigDecimal retInterest = BigDecimal.ZERO;
        while (installmentBillInfo != null
                && installmentBillInfo.getCurRepayAmt().compareTo(installmentBillInfo.getCurRealRepayamt()) > 0) {
            /** 获取上一期的账单信息 **/
            installmentBillInfo =
                    this.installmentBillInfoDao.getLastInstallmentBillInfo(installmentBillInfo.getOrderId(),
                            installmentBillInfo.getInstallInfoId(), installmentBillInfo.getRepayNo() - 1);
            /** 判断上期账单是否逾期，如果发生了逾期，则累加 **/
            if (installmentBillInfo != null
                    && installmentBillInfo.getCurRepayAmt().compareTo(installmentBillInfo.getCurRealRepayamt()) > 0) {
                retInterest = retInterest.add(installmentBillInfo.getCurRepayOverdueInterest());
            }
        }
        return retInterest;
    }

    /**
     * 
     * Description: 获取传入账单之前的所有期数的逾期利息(月复利)
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 下午2:08:34
     */
    private BigDecimal getBeforeMonthOverdueInterest(InstallmentBillInfo installmentBillInfo) {
        BigDecimal monthOverdueInterest = BigDecimal.ZERO;
        while (installmentBillInfo != null
                && installmentBillInfo.getCurRepayAmt().compareTo(installmentBillInfo.getCurRealRepayamt()) > 0) {
            /** 获取上一期的账单信息 **/
            installmentBillInfo =
                    this.installmentBillInfoDao.getLastInstallmentBillInfo(installmentBillInfo.getOrderId(),
                            installmentBillInfo.getInstallInfoId(), installmentBillInfo.getRepayNo() - 1);
            /** 判断上期账单是否逾期，如果发生了逾期，则累加 **/
            if (installmentBillInfo != null
                    && installmentBillInfo.getCurRepayAmt().compareTo(installmentBillInfo.getCurRealRepayamt()) > 0) {
                monthOverdueInterest = monthOverdueInterest.add(installmentBillInfo.getMonthOverdueAmt());
            }
        }
        return monthOverdueInterest;
    }

    /**
     * 
     * Description: 获取下一期账单信息
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年12月2日 上午11:54:00
     */
    private InstallmentBillInfo getNextMonthInstallmentBillInfo(InstallmentBillInfo installmentBillInfo) {
        /** 获取下一期的账单信息 **/
        InstallmentBillInfo lastInstallmentBillInfo =
                this.installmentBillInfoDao.getLastInstallmentBillInfo(installmentBillInfo.getOrderId(),
                        installmentBillInfo.getInstallInfoId(), installmentBillInfo.getRepayNo() + 1);
        if (lastInstallmentBillInfo == null) {
            return null;
        }
        return lastInstallmentBillInfo;
    }

    /**
     * 
     * Description: 操作数据库
     * 
     * @param lastInstallmentBillInfoU
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 下午3:49:23
     */
    @Transactional
    private void operateDb(InstallmentBillInfo installmentBillInfo, DelayPayment delayPayment,
            InstallmentBillInfo lastInstallmentBillInfoU, InstallmentBillInfo nextInstallmentBillInfo) {
        /** 更新本期账单的本期应还总额、本期逾期利息、更新时间 **/
        this.installmentBillInfoDao.update(installmentBillInfo);
        this.delayPaymentDao.insert(delayPayment);
        if (lastInstallmentBillInfoU != null) {
            this.installmentBillInfoDao.update(lastInstallmentBillInfoU);
        }
        if (nextInstallmentBillInfo != null) {
            this.installmentBillInfoDao.update(nextInstallmentBillInfo);
        }
        logger.info("账单处理完成：" + JSONObject.toJSONString(installmentBillInfo));
    }

    @Override
    public void calOverdueInterest(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            objs = new JSONObject();
        }
        this.calOverdueInterest(objs.getString("orderId"), objs.getString("instBillId"), objs.getString("startDate"),
                objs.getString("endDate"));
    }

    @Override
    public void calOverdueInterest4Test(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            objs = new JSONObject();
        }
        this.calOverdueInterest4Test(objs.getString("orderId"), objs.getString("instBillId"), objs.getString("endDate"));
    }

    /**
     * 
     * Description:校验订单对应的产品方案是否是上收月供
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月30日
     */
    private boolean validateTakeMonth(String orderId) {
        int takePayment = overdueInterestDao.getTakePayment(orderId);
        return takePayment == 1 ? true : false;
    }
}
