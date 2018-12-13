/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月22日 下午5:11:52
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iqb.consumer.common.enums.StatusEnum;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.mysql.dao.DelayPaymentDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.DelayBillInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentBillInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentPlanDao;
import com.iqb.consumer.data.layer.mysql.domain.DelayBillInfo;
import com.iqb.consumer.data.layer.mysql.domain.DelayPayment;
import com.iqb.consumer.data.layer.mysql.domain.InstallmentBillInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class BizSchedulerServiceImpl implements BizSchedulerService {

    protected static final Logger logger = LoggerFactory.getLogger(BizSchedulerServiceImpl.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    @Resource
    private InstallmentBillInfoDao installmentBillInfoDao;
    @Resource
    private InstallmentPlanDao installmentPlanDao;
    @Resource
    private DelayPaymentDao delayPaymentDao;
    @Resource
    private DelayBillInfoDao delayBillInfoDao;

    @Override
    public Map<String, Object> calculeDelay(String endDate) {
        Map<String, Object> result = new HashMap<String, Object>();
        // // 该时间段需要进行库锁,通过Redis变量控制进行封库
        // int pageNum = 1;
        // int numPerPage = 500;
        // PageParam pageParam = new PageParam(pageNum, numPerPage);
        // Map<String, Object> paramMap = new HashMap<String, Object>();
        // paramMap.put("status", StatusEnum.BILL_USING.getValue());
        // paramMap.put("endDate", endDate);
        // // 查询账单期的账单
        // PageBean pageBean = installmentBillInfoDao.listPage(pageParam, paramMap);
        // Date delayDate = null;
        // try {
        // delayDate = sdf.parse(endDate);
        // } catch (ParseException e) {
        // delayDate = new Date();
        // logger.error("BizSchedulerServiceImpl 时间转换异常", e);
        // }
        // while (pageNum <= pageBean.getEndPageIndex()) {
        // List<Object> list = pageBean.getRecordList();
        // for (int i = 0; i < list.size(); i++) {
        // InstallmentBillInfo installmentBillInfo = (InstallmentBillInfo) list.get(i);
        // // 查询分期计划
        // InstallmentPlan installmentPlan =
        // installmentPlanDao.getById(installmentBillInfo.getInstallNo());
        // // 如果账单为最后一天滞纳金
        // if (delayDate.compareTo(installmentBillInfo.getLastPayDay()) >= 0) {
        // // 计算滞纳金,将本息归纳放置下一期,并将下一期滞纳金时间重置为和账单日一样,同时将当期账单置为过期,并添加一期冻结用户。
        //
        // } else if (delayDate.compareTo(installmentBillInfo.getLastPayDay()) < 0) {// 正常计算滞纳金
        // // 将dalayDate + 1
        // // 违约金
        // BigDecimal delayAmt =
        // BigDecimalUtil.mul(installmentBillInfo.getRemainAmt(),
        // new BigDecimal(installmentPlan.getOverdueRate() / 100));// 剩余本金*违约金费率
        // delayAmt = BigDecimalUtil.add(delayAmt, installmentPlan.getOverdueAmount());
        // // 添加违约金记录,修改账单金额
        // DelayPayment delayPayment = new DelayPayment();
        // delayPayment.setBillID(installmentBillInfo.getId());
        // delayPayment.setPrincipalAmt(installmentBillInfo.getRemainAmt());
        // delayPayment.setOverdueRate(installmentPlan.getOverdueRate());
        // delayPayment.setDelayAmt(delayAmt);
        // delayPayment.setDelayDate(installmentBillInfo.getDelayDate());
        // delayPaymentDao.insert(delayPayment);
        // //
        // BigDecimal curSumLateFee = BigDecimalUtil.add(installmentBillInfo.getCurSumLateFee(),
        // delayAmt);
        // // 保存新的滞纳金,新的应还金额
        // installmentBillInfo.setCurSumLateFee(curSumLateFee);
        // installmentBillInfo.setSumBillAmt(BigDecimalUtil.add(installmentBillInfo.getSumBillAmt(),
        // installmentBillInfo.getPreSumBillAmt(), curSumLateFee));
        // installmentBillInfo.setUpdateTime(new Date());
        // installmentBillInfoDao.update(installmentBillInfo);
        // }
        //
        // }
        // pageNum++;
        // logger.info(String.format("调用通知服务.notifyFacade.notiFyReCordListPage(%s, %s, %s)",
        // pageNum, numPerPage,
        // paramMap));
        //
        // }
        return result;
    }

    private void calculeDelay() {
        // 计算滞纳金,将本息归纳放置下一期,并将下一期滞纳金时间重置为和账单日一样,同时将当期账单置为过期,并添加一期冻结用户。

    }

    @Override
    public Map<String, Object> batchCalDelay(String endDate) {
        Map<String, Object> result = new HashMap<String, Object>();
        // logger.info("开始批量计算违约金(滞纳金),计算到:{}", endDate);
        // int pageNum = 1;
        // int numPerPage = 500;
        // PageParam pageParam = new PageParam(pageNum, numPerPage);
        // Map<String, Object> paramMap = new HashMap<String, Object>();
        // paramMap.put("status", StatusEnum.BILL_USING.getValue());
        // paramMap.put("endDate", endDate);
        // // 查询滞纳金生成时间小于等于endDate的所有账单数据,分页进行处理
        // PageBean pageBean = installmentBillInfoDao.listPage(pageParam, paramMap);
        // Date endDelayDate = null;// 滞纳金计算截止日期(包含当天)
        // try {
        // endDelayDate = sdf.parse(endDate);
        // } catch (ParseException e) {
        // endDelayDate = new Date();
        // logger.error("BizSchedulerServiceImpl 时间转换异常", e);
        // }
        // while (pageNum <= pageBean.getEndPageIndex()) {
        // List<Object> list = pageBean.getRecordList();
        // for (int i = 0; i < list.size(); i++) {
        // // 账单
        // InstallmentBillInfo installmentBillInfo = (InstallmentBillInfo) list.get(i);
        // // 查询分期计划
        // InstallmentPlan installmentPlan =
        // installmentPlanDao.getById(installmentBillInfo.getInstallNo());
        // // 一个账单一个账单的处理。直到滞纳金时间大于endDate
        // // 判断用户是否存在延期
        // // Map<String, Object> param = new HashMap<String, Object>();
        // // param.put("conUUID", installmentBillInfo.getConUUID());
        // // DelayBillInfo delayBillInfo = delayBillInfoDao.getBy(paramMap);
        // while (true) {
        // BigDecimal sumDelayAmt = new BigDecimal(0);
        // if (endDelayDate.compareTo(installmentBillInfo.getDelayDate()) < 0) {
        // break;
        // }
        // long delayDays;
        // // endDate>lastDate,将滞纳金计算到lastDate,本期账单封期,起下期账单。
        // if (endDelayDate.compareTo(installmentBillInfo.getLastPayDay()) > 0) {
        // // 判断是否延期记录
        // if (installmentBillInfo.getDelayStatus() != 0) {// 延期
        // // 滞纳金天数=lastDate-billDate
        // if (installmentBillInfo.getCurSumLateFee().compareTo(BigDecimal.ZERO) == 0) {// 未开始计算滞纳金
        // delayDays =
        // DateUtil.diffDays(installmentBillInfo.getBillDate(),
        // installmentBillInfo.getLastPayDay()) + 1;
        // } else {// 计算过滞纳金
        // delayDays =
        // DateUtil.diffDays(installmentBillInfo.getDelayDate(),
        // installmentBillInfo.getLastPayDay()) + 1;
        // }
        // } else { // 曾经未延期
        // // 滞纳金开始于delayDate
        // delayDays =
        // DateUtil.diffDays(installmentBillInfo.getDelayDate(),
        // installmentBillInfo.getLastPayDay()) + 1;
        // // 插入一条新延期记录
        // DelayBillInfo delayBillInfo = new DelayBillInfo();
        // delayBillInfo.setConUUID(installmentBillInfo.getConUUID());// 用户唯一ID
        // delayBillInfo.setAccID(installmentBillInfo.getAccID());// 账户ID
        // delayBillInfo.setDelayDate(new Date());// 账单延期日期
        // delayBillInfoDao.insert(delayBillInfo);
        // }
        // // 更新账单信息
        // // 基础滞纳金
        // BigDecimal baseDelayAmt =
        // BigDecimalUtil.mul(installmentBillInfo.getRemainAmt(),
        // new BigDecimal(installmentPlan.getOverdueRate() / 100));
        // baseDelayAmt = BigDecimalUtil.add(baseDelayAmt, installmentPlan.getOverdueAmount());
        //
        // BigDecimal delayAmt = BigDecimalUtil.mul(baseDelayAmt, new BigDecimal(delayDays));
        // sumDelayAmt = BigDecimalUtil.add(sumDelayAmt, delayAmt);
        // BigDecimal curSumLateFee = BigDecimalUtil.add(delayAmt,
        // installmentBillInfo.getCurSumLateFee());
        // // 应还金额
        // BigDecimal sumBillAmt =
        // BigDecimalUtil.add(installmentBillInfo.getCurrentAmt(), curSumLateFee,
        // installmentBillInfo.getPreSumBillAmt());
        // // 应还最低比例
        // BigDecimal minRepayAmt =
        // BigDecimalUtil.mul(sumBillAmt, new BigDecimal(installmentBillInfo.getMinRate() / 100));
        // installmentBillInfo.setMinRepayAmt(minRepayAmt);
        // installmentBillInfo.setCurSumLateFee(curSumLateFee);
        // installmentBillInfo.setDelayDate(DateUtil.addDays(installmentBillInfo.getLastPayDay(), 1)
        // .getTime());// +1天
        // installmentBillInfo.setStatus(StatusEnum.BILL_OVERDUE.getValue());
        // installmentBillInfo.setDelayStatus(1);
        // installmentBillInfo.setSumBillAmt(sumBillAmt);
        // installmentBillInfo.setUpdateTime(new Date());
        // installmentBillInfoDao.update(installmentBillInfo);
        // // 查询下一期账单
        // InstallmentBillInfo nexBillInfo =
        // installmentBillInfoDao.getByInstallIDAndRepayNo(installmentBillInfo.getRepayNo() + 1,
        // installmentBillInfo.getInstallID(), installmentBillInfo.getConUUID());
        // if (nexBillInfo != null) {// 计算第一天的，其他归属到当期
        // // 修改滞纳金时间
        // Date nexdelayDate = DateUtil.addDays(nexBillInfo.getBillDate(), 1).getTime();
        // nexBillInfo.setPreSumBillAmt(installmentBillInfo.getSumBillAmt());// 上期未还
        // // 剩余本金 = 上期本息+本期剩余本金
        // BigDecimal remainAmt =
        // BigDecimalUtil.add(nexBillInfo.getRemainAmt(),
        // installmentBillInfo.getSumBillAmt());
        // // 计算滞纳金
        // curSumLateFee =
        // BigDecimalUtil.mul(remainAmt,
        // new BigDecimal(installmentPlan.getOverdueRate() / 100));// 剩余本金*费率
        // nexBillInfo.setRemainAmt(remainAmt);
        // nexBillInfo.setCurSumLateFee(curSumLateFee);// 预算滞纳金
        // nexBillInfo.setDelayDate(nexdelayDate);
        // nexBillInfo.setCurrentLateFee(curSumLateFee);// 逾期每天的滞纳金
        // nexBillInfo.setDelayStatus(1);// 产生滞纳金
        // nexBillInfo.setStatus(StatusEnum.BILL_USING.getValue());
        // // 下期应还金额=上期应还+本期应还 +滞纳金
        // BigDecimal nextBillAmt =
        // BigDecimalUtil.add(nexBillInfo.getSumBillAmt(),
        // installmentBillInfo.getSumBillAmt(), curSumLateFee);
        // nexBillInfo.setSumBillAmt(nextBillAmt);
        // installmentBillInfo = nexBillInfo;// 新的账单
        // sumDelayAmt = BigDecimalUtil.add(sumDelayAmt, curSumLateFee);
        // installmentBillInfoDao.update(installmentBillInfo);
        // }
        // } else {// 当期账单
        // logger.debug("处理当期账单滞纳金:{}", installmentBillInfo.getConUUID());
        // delayDays =
        // DateUtil.diffDays(installmentBillInfo.getDelayDate(), endDelayDate) + 1;
        // if (installmentBillInfo.getDelayStatus() == 0) {// 未延期过,第一次延期
        // // 插入一条新延期记录
        // DelayBillInfo delayBillInfo = new DelayBillInfo();
        // delayBillInfo.setConUUID(installmentBillInfo.getConUUID());// 用户唯一ID
        // delayBillInfo.setAccID(installmentBillInfo.getAccID());// 账户ID
        // delayBillInfo.setDelayDate(endDelayDate);// 账单延期日期
        // delayBillInfoDao.insert(delayBillInfo);
        // }
        // // 基础滞纳金
        // BigDecimal baseDelayAmt =
        // BigDecimalUtil.mul(installmentBillInfo.getRemainAmt(),
        // new BigDecimal(installmentPlan.getOverdueRate() / 100));
        // baseDelayAmt = BigDecimalUtil.add(baseDelayAmt, installmentPlan.getOverdueAmount());
        //
        // BigDecimal delayAmt = BigDecimalUtil.mul(baseDelayAmt, new BigDecimal(delayDays));
        // sumDelayAmt = BigDecimalUtil.add(sumDelayAmt, delayAmt);
        // BigDecimal curSumLateFee = BigDecimalUtil.add(delayAmt,
        // installmentBillInfo.getCurSumLateFee());
        // // 应还金额
        // BigDecimal sumBillAmt =
        // BigDecimalUtil.add(installmentBillInfo.getCurrentAmt(), curSumLateFee,
        // installmentBillInfo.getPreSumBillAmt());
        // // 应还最低比例
        // BigDecimal minRepayAmt =
        // BigDecimalUtil.mul(sumBillAmt, new BigDecimal(installmentBillInfo.getMinRate() / 100));
        // installmentBillInfo.setMinRepayAmt(minRepayAmt);
        // installmentBillInfo.setCurSumLateFee(curSumLateFee);
        // Date nextDelayDate =
        // DateUtil.addDays(installmentBillInfo.getDelayDate(), (int) delayDays).getTime();
        // installmentBillInfo.setDelayDate(nextDelayDate);
        // installmentBillInfo.setStatus(StatusEnum.BILL_USING.getValue());
        // installmentBillInfo.setDelayStatus(1);
        // installmentBillInfo.setSumBillAmt(sumBillAmt);
        // installmentBillInfo.setUpdateTime(new Date());
        // installmentBillInfoDao.update(installmentBillInfo);
        // }
        // // 添加违约金记录,修改账单金额
        // DelayPayment delayPayment = new DelayPayment();
        // delayPayment.setBillID(installmentBillInfo.getId());
        // delayPayment.setPrincipalAmt(installmentBillInfo.getRemainAmt());
        // delayPayment.setOverdueRate(installmentPlan.getOverdueRate());
        // delayPayment.setDelayAmt(sumDelayAmt);
        // delayPayment.setDelayDate(installmentBillInfo.getDelayDate());
        // delayPaymentDao.insert(delayPayment);
        // }
        // }
        // pageNum++;// 翻页
        // pageBean = installmentBillInfoDao.listPage(pageParam, paramMap);
        // }
        // // 查询所有需要
        return result;
    }
}
