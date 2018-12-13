package com.iqb.consumer.finance.batch.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.DelayPayment;
import com.iqb.consumer.finance.batch.bean.InstallmentBillInfo;
import com.iqb.consumer.finance.batch.bean.InstallmentPlan;
import com.iqb.consumer.finance.batch.constant.AccountConstant.OverdueConstant;
import com.iqb.consumer.finance.batch.dao.DelayPaymentDao;
import com.iqb.consumer.finance.batch.dao.DelayPaymentDaoImpl;
import com.iqb.consumer.finance.batch.dao.InstallmentBillInfoDao;
import com.iqb.consumer.finance.batch.dao.InstallmentBillInfoDaoImpl;
import com.iqb.consumer.finance.batch.dao.InstallmentPlanDao;
import com.iqb.consumer.finance.batch.dao.InstallmentPlanDaoImpl;
import com.iqb.consumer.finance.batch.util.BigDecimalUtil;
import com.iqb.consumer.finance.batch.util.DateUtil;
import com.iqb.consumer.finance.batch.util.SpringBeanUtil;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年10月19日下午3:27:25 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class DealInstallmentBillInfoTask implements Runnable {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DealInstallmentBillInfoTask.class);

    private InstallmentBillInfoDao installmentBillInfoDao;

    private DelayPaymentDao delayPaymentDao;

    private InstallmentPlanDao installmentPlanDao;

    private OverdueIterestService overdueIterestService;

    InstallmentBillInfo installmentBillInfo;
    Date delayDate;

    /**
     * @param installmentBillInfo
     * @param delayDate
     */
    public DealInstallmentBillInfoTask(InstallmentBillInfo installmentBillInfo, Date delayDate) {
        super();
        this.installmentBillInfo = installmentBillInfo;
        this.delayDate = delayDate;
        this.installmentBillInfoDao = (InstallmentBillInfoDao) SpringBeanUtil.getBean(InstallmentBillInfoDaoImpl.class);
        this.delayPaymentDao = (DelayPaymentDao) SpringBeanUtil.getBean(DelayPaymentDaoImpl.class);
        this.installmentPlanDao = (InstallmentPlanDao) SpringBeanUtil.getBean(InstallmentPlanDaoImpl.class);
        this.overdueIterestService = (OverdueIterestService) SpringBeanUtil.getBean(OverdueIterestServiceImpl.class);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年10月19日
     */
    @Override
    public void run() {
        this.dealInstallmentBillInfo(installmentBillInfo, delayDate);
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
                remainPriAndIntBillInfo == null
                        ? installmentBillInfo.getRemainPriAndInterest()
                        : remainPriAndIntBillInfo.getRemainPriAndInterest();
        logger.info("剩余本息金额为:{}", remainPriAndIntAmt);
        /**
         * 计算逾息
         */
        /** 剩余本金 **/
        BigDecimal remainPrincipal = this.getRemainPrincipal(installmentBillInfo);
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
        BigDecimal retRemainPrincipal = installmentBillInfo.getRemainPrincipal();
        /** 获取上一期的账单信息 **/
        InstallmentBillInfo lastInstallmentBillInfo =
                this.installmentBillInfoDao.getLastInstallmentBillInfo(installmentBillInfo.getOrderId(),
                        installmentBillInfo.getInstallInfoId(), installmentBillInfo.getRepayNo() - 1);
        // 判断上一期是否预期
        while (lastInstallmentBillInfo != null
                && lastInstallmentBillInfo.getCurRepayAmt().compareTo(lastInstallmentBillInfo.getCurRealRepayamt()) > 0) {
            retRemainPrincipal = lastInstallmentBillInfo.getRemainPrincipal();
            /** 获取上一期的上一期账单信息 **/
            lastInstallmentBillInfo =
                    this.installmentBillInfoDao.getLastInstallmentBillInfo(lastInstallmentBillInfo.getOrderId(),
                            lastInstallmentBillInfo.getInstallInfoId(), lastInstallmentBillInfo.getRepayNo() - 1);
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
}
