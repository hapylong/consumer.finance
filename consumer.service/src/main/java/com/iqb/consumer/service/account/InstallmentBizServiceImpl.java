/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月14日 下午1:46:41
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import java.math.BigDecimal;
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

import com.iqb.consumer.common.constant.AccountConstant;
import com.iqb.consumer.common.enums.InstallTypeEnum;
import com.iqb.consumer.common.enums.StatusEnum;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.dao.AccountInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentDetailDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentPlanDao;
import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;
import com.iqb.consumer.service.account.dto.NoticeBeanDto;
import com.iqb.consumer.service.account.dto.OrderInfoDto;
import com.iqb.consumer.service.utils.NotifyUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class InstallmentBizServiceImpl implements InstallmentBizService {

    protected static final Logger logger = LoggerFactory.getLogger(InstallmentBizServiceImpl.class);

    @Resource
    private AccountInfoDao accountInfoDao;
    @Resource
    private InstallmentInfoDao installmentInfoDao;
    @Resource
    private InstallmentPlanDao installmentPlanDao;
    @Resource
    private InstallmentDetailDao installmentDetailDao;
    @Resource
    private JmsTemplate notifyJmsTemplate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Transactional
    public Map<String, Object> paymentByInstall(OrderInfoDto orderInfoDto, final String billUrl, final String smsUrl) {
        Map<String, Object> result = new HashMap<String, Object>();
        // AccountInfo accountInfo = accountInfoDao.getAccountInfoByUUID(orderInfoDto.getConUuid());
        // if (accountInfo == null) {
        // result.put("retCode", AccountConstant.ERROR);
        // result.put("msg", "请先开户,再分期");
        // return result;
        // }
        // // 第一步判断用户是否可以分期(1,用户存在 2,用户状态正常 3、用户剩余金额>订单金额)
        // result = judgeInstallStatus(orderInfoDto, accountInfo);
        // logger.debug("判断用户是否分期合法性结果:{}", result);
        // if (AccountConstant.SUCCESS.equals(result.get("retCode"))) {
        // // 添加分期总表
        // final InstallmentInfo installmentInfo = orderToInstallInfo(orderInfoDto, accountInfo);
        // long installID = installmentInfoDao.insertInstalInfo(installmentInfo);
        // // 查询分期计划
        // InstallmentPlan installmentPlan =
        // installmentPlanDao.getById(orderInfoDto.getInstallNo());
        // int periods = orderInfoDto.getRealPeriods();
        // List<InstallmentDetail> detailList = new ArrayList<InstallmentDetail>(periods);
        // BigDecimal sumPrincipal = new BigDecimal(0);
        // // 初始化日期
        // Calendar calendar = Calendar.getInstance();
        // calendar.setTime(new Date());
        // // 短信通知类
        // final NoticeBeanDto noticeBeanDto = new NoticeBeanDto();
        // noticeBeanDto.setRegID(installmentInfo.getRegID());
        // noticeBeanDto.setSumShareAmount(installmentInfo.getInstallAmt());
        // noticeBeanDto.setRealPeriods(installmentInfo.getInstallTerms());
        // for (int i = 1; i <= periods; i++) {
        // InstallmentDetail detail = new InstallmentDetail();
        // detail.setInstallID(installID);// 分期ID--->对应installInfo主键
        // detail.setConUUID(installmentInfo.getConUUID());// 用户唯一ID
        // detail.setRegID(installmentInfo.getRegID());// 注册号
        // detail.setAccID(accountInfo.getId());// 账户号-->对应AccountInfo主键
        // detail.setOrderID(installmentInfo.getOrderID());// 订单ID
        // if (InstallTypeEnum.ACTUALTIME.getValue() == installmentPlan.getInstallType()) {// 实时
        // Calendar afterCal = DateUtil.afterOneMonth(calendar, i - 1);
        // detail.setStageDate(afterCal.getTime());
        // } else if (InstallTypeEnum.EATCHMONTH.getValue() == installmentPlan.getInstallType()) {//
        // 隔月
        // Calendar afterCal = DateUtil.afterOneMonth(calendar, i);
        // detail.setStageDate(afterCal.getTime());
        // } else if (InstallTypeEnum.FIXEDDAY.getValue() == installmentPlan.getInstallType()) {//
        // 固定日期
        // // 咱不实现
        // }
        // detail.setInstallAmt(installmentInfo.getInstallAmt());// 分期总额
        // detail.setInstallTerms(installmentInfo.getInstallTerms());// 分期期数
        // detail.setMerchantNo(installmentInfo.getMerchantNo());
        // detail.setRepayNo(i);// 还款序号
        // detail.setInstallDate(installmentInfo.getInstallDate());// 分期日期
        // detail.setChargeAmt(installmentPlan.getFixedAmount());// 固定手续费
        // detail.setTermState(0);// 每期状态(提前还款,部分还款,正常)
        // detail.setStatus(0);
        // detail.setUseFreeInterest(0);// 是否使用了免息卷
        // // 计算分摊金额
        // BigDecimal principal = null;
        // if (i != periods) {// 非最后一期
        // // 分摊本金= 总金额/期数(保留2位 4舍五入)
        // principal =
        // BigDecimalUtil.div(installmentInfo.getInstallAmt(),
        // new BigDecimal(installmentInfo.getInstallTerms()));
        // sumPrincipal = BigDecimalUtil.add(sumPrincipal, principal);// 金额累加
        // } else {
        // // 最后一次分摊本金 = 总金额-已经分摊本金
        // principal = BigDecimalUtil.sub(installmentInfo.getInstallAmt(), sumPrincipal);
        // }
        // // 手续费
        // BigDecimal fee =
        // BigDecimalUtil.mul(installmentInfo.getInstallAmt(), new BigDecimal(
        // installmentPlan.getFeeRate() / 100));
        // BigDecimal origPayAmt = BigDecimalUtil.add(principal, fee,
        // installmentPlan.getFixedAmount());
        // BigDecimal freeAmt = new BigDecimal(0);
        // if (i <= installmentInfo.getFreeInterestCount()) {
        // freeAmt = fee;// 使用免息卷
        // }
        // BigDecimal realPayAmt = BigDecimalUtil.sub(origPayAmt, freeAmt);
        // detail.setPrincipal(principal);// 本金
        // detail.setFee(fee);
        // detail.setFreeAmt(freeAmt);
        // detail.setRealPayAmt(realPayAmt);// 原始分摊金额(总额/期数+总额*费率+固定手续费)
        // if (i == 1) {// 首期
        // noticeBeanDto.setFirstShareAmount(realPayAmt);
        // }
        // if (i == periods) {
        // noticeBeanDto.setLastShareAmount(realPayAmt);
        // }
        // detail.setInstallNo(installmentInfo.getInstallNo());
        // detailList.add(detail);
        // }
        // // 插入
        //
        // long resInt = installmentDetailDao.insert(detailList);
        //
        // logger.debug("插入分摊详情返回值:{}", resInt);
        // // 修改订单状态
        // accountInfo.setAvailLimit(BigDecimalUtil.sub(accountInfo.getAvailLimit(),
        // installmentInfo.getInstallAmt()));// 可用额度
        // accountInfo.setAccStatus(StatusEnum.ACC_NORMAL.getValue());// 账户状态(0、正常,1、冻结,2、注销)
        // accountInfo.setPointSum(0);// 累计积分
        // accountInfo.setFreeInterestCount(0);// 免息卷总数
        // accountInfo.setUpdateTime(new Date());
        // accountInfoDao.update(accountInfo);
        // // 通知消息队列账单生成
        // notifyJmsTemplate.send(new MessageCreator() {
        // public Message createMessage(Session session) throws JMSException {
        // return session.createTextMessage(NotifyUtil.formatBill(installmentInfo, billUrl));
        // }
        // });
        //
        // try {
        // if (InstallTypeEnum.ACTUALTIME.getValue() == installmentPlan.getInstallType()) {// 实时分期
        // // 发送短信不能阻止业务
        // notifyJmsTemplate.send(new MessageCreator() {
        // public Message createMessage(Session session) throws JMSException {
        // return session.createTextMessage(NotifyUtil.formatSMS(noticeBeanDto, smsUrl));
        // }
        // });
        // }
        // } catch (Exception e) {
        // logger.error("短信发送异常", e);
        // }
        // }
        return result;
    }

    private Map<String, Object> judgeInstallStatus(OrderInfoDto orderInfoDto, AccountInfo accountInfo) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("retCode", AccountConstant.SUCCESS);
        try {
            if (accountInfo == null) {
                result.put("retCode", AccountConstant.ERROR);
                result.put("msg", "用户未开户,请先开户");
                return result;
            } else {
                if (StatusEnum.ACC_NORMAL.getValue() != accountInfo.getAccStatus()) {
                    result.put("retCode", AccountConstant.ERROR);
                    result.put("msg", "用户账户状态不正常,无法分期");
                    return result;
                }
                BigDecimal sumShareAmount = orderInfoDto.getSumShareAmount();// 分期金额
                BigDecimal availLimit = accountInfo.getAvailLimit();// 剩余额度
                if (sumShareAmount.compareTo(availLimit) > 0) {// 不可分期
                    result.put("retCode", AccountConstant.ERROR);
                    result.put("msg", "用户剩余额度不足,无法分期");
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error("判断用户是否可以分期发送异常", e);
            result.put("retCode", AccountConstant.ERROR);
            result.put("msg", "判断用户是否可以分期发送异常");
        }
        return result;
    }

    private InstallmentInfo orderToInstallInfo(OrderInfoDto orderInfoDto, AccountInfo accountInfo) {
        InstallmentInfo install = new InstallmentInfo();
        // install.setConUUID(orderInfoDto.getConUuid());// 用户唯一ID
        // install.setRegID(orderInfoDto.getRegID());
        // install.setAccID(accountInfo.getId());
        // install.setInstallDate(new Date());// 分期日期
        // // 分期总额 扩大100倍，保留2位有效数字
        // BigDecimal sumShareAmount = BigDecimalUtil.mul(orderInfoDto.getSumShareAmount(), new
        // BigDecimal(100));
        // install.setInstallAmt(sumShareAmount);// 分期总额 扩大100倍，保留2位有效数字
        // install.setInstallTerms(orderInfoDto.getRealPeriods());// 分期期数
        // install.setMerchantNo(orderInfoDto.getMerchantNo());
        // install.setInstallNo(orderInfoDto.getInstallNo());
        // install.setStatus(0);// 分期状态(正常,作废)
        // install.setUseFreeInterest(0);// 是否使用了免息卷(0、否 1、是)
        // install.setFreeInterestCount(0);// 免息卷张数(必须小于<=分期期数)
        // install.setOrderID(orderInfoDto.getOrderNo());// 订单ID
        return install;
    }

}
