/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月28日 下午6:32:03
 * @version V1.0
 */

package com.iqb.consumer.virtual.account.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfoNormalRepay;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.service.account.BillBizService;
import com.iqb.consumer.service.account.dto.InstallmentPlanDto;
import com.iqb.consumer.service.account.dto.OrderInfoDto;
import com.iqb.consumer.virtual.account.dto.InstallmentDto;
import com.iqb.consumer.virtual.account.dto.MortgageInstDto;
import com.iqb.consumer.virtual.account.dto.refund.PaymentDto;
import com.iqb.consumer.virtual.account.dto.refund.RepayList;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class ConvertObjectUtils {

    protected static final Logger logger = LoggerFactory.getLogger(ConvertObjectUtils.class);

    @Resource
    private BillBizService billBizService;

    public static Map<String, Object> object2Map(Object object) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (object instanceof OrderInfoDto) {
            OrderInfoDto orderInfo = (OrderInfoDto) object;
            param.put("id", orderInfo.getId());
            param.put("orderNo", orderInfo.getOrderNo());
            param.put("sumAmount", orderInfo.getSumAmount());// 账单总金额
            param.put("sumShareAmount", orderInfo.getSumShareAmount());// 预分期总额
            param.put("sumPeriods", orderInfo.getSumPeriods());// 总分期周期
            param.put("realPeriods", orderInfo.getRealPeriods());// 实际分期期数
            param.put("eachAmount", orderInfo.getEachAmount());// 每期金额
            param.put("eachShareAmount", orderInfo.getEachShareAmount());// 分期分摊金额
            param.put("needShare", orderInfo.getNeedShare());// 需后台分摊(0、不需要,1、需要)
            param.put("prePayment", orderInfo.getPrePayment());// 首付金额
            param.put("margin", orderInfo.getMargin());// 保证金
            param.put("serviceFee", orderInfo.getServiceFee());// 服务费
            param.put("downPayment", orderInfo.getDownPayment());// 定金
            param.put("finalPayment", orderInfo.getFinalPayment());// 尾款
            param.put("refundAmount", orderInfo.getRefundAmount());// 退款金额
            param.put("refundStatus", orderInfo.getRefundStatus());// 退款状态
            param.put("feeType", orderInfo.getFeeType());// 费率类型(年费率,月费率,日费率)
            param.put("installType", orderInfo.getInstallType());// 分期类型(固定日期账单,月对月账单,隔日账单,实时账单)
            param.put("orderStatus", orderInfo.getOrderStatus());// 订单状态
            param.put("useFreeInterest", orderInfo.getUseFreeInterest());// 使用免息卷
            param.put("freeInterestCount", orderInfo.getFreeInterestCount());// 免息卷张数(必须小于<=分期期数)
            param.put("installNo", orderInfo.getInstallNo());// 分期计划
            param.put("merchantNo", orderInfo.getMerchantNo());// 商户ID
            param.put("merchantRemark", orderInfo.getMerchantRemark());// 商户备注
            param.put("orderInfo", orderInfo.getOrderInfo());// 订单详情
            param.put("conUuid", orderInfo.getConUuid());// 用户
            param.put("conRemark", orderInfo.getConRemark());// 用户备注
        } else if (object instanceof InstallmentPlanDto) {
            InstallmentPlanDto installmentPlanDto = (InstallmentPlanDto) object;
            param.put("id", installmentPlanDto.getId());
            param.put("merchantNo", installmentPlanDto.getMerchantNo());// 商户号
            param.put("feeRate", installmentPlanDto.getFeeRate());// 手续费费率(50.2% 50.2)
            param.put("feeType", installmentPlanDto.getFeeType());// 手续费费率类型(0y,1m,2d)
            param.put("overdueRate", installmentPlanDto.getOverdueRate());// 逾期费率(滞纳金)
            param.put("overdueAmount", installmentPlanDto.getOverdueAmount());// 逾期固定费用
            param.put("periods", installmentPlanDto.getPeriods());// 分期周期(非必要字段)
            param.put("fixedAmount", installmentPlanDto.getFixedAmount());// 固定金额
            param.put("installType", installmentPlanDto.getInstallType());// 分期类型(固定日期账单,月对月账单,隔日账单,实时账单)
            param.put("planName", installmentPlanDto.getPlanName());// 分期计划名称
            param.put("minAmt", installmentPlanDto.getMinAmt());// 每期最少分期金额
            param.put("chkAccStatus", installmentPlanDto.getChkAccStatus());// 分期是否检查用户状态(0、检查,1、不检查)
            param.put("graceDays", installmentPlanDto.getGraceDays());// 宽限天数
            param.put("needShare", installmentPlanDto.getNeedShare());// 需后台分摊(0、不需要,1、需要)
        }
        return param;
    }

    /**
     * 交互数据转后台Bean
     * 
     * @param object
     * @return
     */
    public static Object dto2Bean(Object object) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (object instanceof InstallmentDto) {
            InstallmentDto instDto = (InstallmentDto) object;
            Date orderDate = null;
            Date beginDate = null;
            try {
                orderDate = sdf.parse(instDto.getOrderDate());
                if (StringUtils.isEmpty(instDto.getBeginDate())) {
                    beginDate = new Date();
                } else {
                    beginDate = sdf.parse(instDto.getBeginDate());
                }
            } catch (ParseException e) {
                logger.error("订单ID:{},订单时间:{}错误", instDto.getOrderId(), instDto.getOrderDate());
                orderDate = new Date();
            }
            InstallmentInfo installmentInfo = new InstallmentInfo();
            installmentInfo.setOrderId(instDto.getOrderId());
            installmentInfo.setOrderDate(orderDate);
            installmentInfo.setRegId(instDto.getRegId());
            installmentInfo.setOpenId(instDto.getOpenId());
            installmentInfo.setBeginDate(beginDate);
            installmentInfo.setMerchantNo(instDto.getMerchantNo());
            installmentInfo.setContractAmt(instDto.getContractAmt());// 合同金额
            installmentInfo.setInstallSumAmt(BigDecimalUtil.mul(instDto.getInstallSumAmt(), new BigDecimal(100)));
            installmentInfo.setInstallAmt(BigDecimalUtil.mul(instDto.getInstallAmt(), new BigDecimal(100)));
            installmentInfo.setInstallTerms(instDto.getInstallTerms());
            installmentInfo.setPlanId(instDto.getPlanId());
            installmentInfo.setTakeInterest(instDto.getTakeInterest());
            installmentInfo.setTakeMonth(instDto.getTakeMonth());
            installmentInfo.setTakePayment(instDto.getTakePayment());
            installmentInfo.setTakePaymentAmt(instDto.getTakePaymentAmt() == null ? BigDecimal.ZERO : BigDecimalUtil
                    .mul(instDto.getTakePaymentAmt(), new BigDecimal(100)));
            installmentInfo.setInterestAmt(instDto.getInterestAmt() == null ? BigDecimal.ZERO : BigDecimalUtil.mul(
                    instDto.getInterestAmt(), new BigDecimal(100)));
            installmentInfo.setOtherAmt(instDto.getOtherAmt() == null ? BigDecimal.ZERO : BigDecimalUtil.mul(
                    instDto.getOtherAmt(), new BigDecimal(100)));
            installmentInfo.setTakeOtherAmt(instDto.getTakeOtherAmt() == 0 ? 2 : instDto.getTakeOtherAmt());// 默认不上收
            installmentInfo.setInterestTerms(instDto.getInterestTerms() == 0 ? instDto.getInstallTerms() : instDto
                    .getInterestTerms());// 剩余期数默认为总期数
            installmentInfo.setSourcesFunding(instDto.getSourcesFunding());// 资金来源
            installmentInfo.setFundId(instDto.getFundId());// 上标ID
            installmentInfo.setSmsMobile(instDto.getSmsMobile());// 接收短信手机号码
            return installmentInfo;
        } else if (object instanceof MortgageInstDto) {
            MortgageInstDto instDto = (MortgageInstDto) object;
            Date orderDate = null;
            Date beginDate = null;
            try {
                orderDate = sdf.parse(instDto.getOrderDate());
                if (StringUtils.isEmpty(instDto.getBeginDate())) {
                    beginDate = new Date();
                } else {
                    beginDate = sdf.parse(instDto.getBeginDate());
                }
            } catch (ParseException e) {
                logger.error("订单ID:{},订单时间:{}错误", instDto.getOrderId(), instDto.getOrderDate());
                orderDate = new Date();
            }
            InstallmentInfo installmentInfo = new InstallmentInfo();
            installmentInfo.setOrderId(instDto.getOrderId());
            installmentInfo.setOrderDate(orderDate);
            installmentInfo.setRegId(instDto.getRegId());
            installmentInfo.setOpenId(instDto.getOpenId());
            installmentInfo.setBeginDate(beginDate);
            installmentInfo.setMerchantNo(instDto.getMerchantNo());
            installmentInfo.setContractAmt(instDto.getContractAmt());// 核准金额
            installmentInfo.setInstallSumAmt(BigDecimalUtil.mul(instDto.getInstallSumAmt(), new BigDecimal(100)));
            installmentInfo.setInstallAmt(BigDecimalUtil.mul(instDto.getInstallAmt(), new BigDecimal(100)));
            installmentInfo.setInstallTerms(instDto.getInstallTerms());
            installmentInfo.setPlanId(instDto.getPlanId());
            installmentInfo.setTakeInterest(instDto.getTakeInterest());
            installmentInfo.setTakeMonth(instDto.getTakeMonth());
            installmentInfo.setInstallUnit(instDto.getInstallUnit());
            // 如果installUnit为2的时候需要遍历期数获取数据
            if ("2".equals(installmentInfo.getInstallUnit())) {
                List<InstDetail> instDetails = instDto.getInstDetails();
                List<InstDetail> newInstDetails = new ArrayList<>();
                for (int i = 1; i <= installmentInfo.getInstallTerms() - 1; i++) {
                    InstDetail instDetail = new InstDetail();
                    instDetail.setInterest(instDetails.get(0).getInterest() == null ? BigDecimal.ZERO : BigDecimalUtil
                            .mul(
                                    instDetails.get(0).getInterest(), new BigDecimal(100)));
                    instDetail.setRepayNo(i);
                    instDetail.setDays(instDetails.get(0).getDays());
                    newInstDetails.add(instDetail);
                }
                instDto.setInstDetails(newInstDetails);
            } else {
                for (InstDetail instDetail : instDto.getInstDetails()) {
                    instDetail.setInterest(instDetail.getInterest() == null ? BigDecimal.ZERO : BigDecimalUtil.mul(
                            instDetail.getInterest(), new BigDecimal(100)));
                }
            }
            installmentInfo.setInstDetails(JSONObject.toJSONString(instDto.getInstDetails()));
            installmentInfo.setTakePayment(instDto.getTakePayment());
            installmentInfo.setTakePaymentAmt(instDto.getTakePaymentAmt() == null ? BigDecimal.ZERO : BigDecimalUtil
                    .mul(instDto.getTakePaymentAmt(), new BigDecimal(100)));
            installmentInfo.setInterestAmt(instDto.getInterestAmt() == null ? BigDecimal.ZERO : BigDecimalUtil.mul(
                    instDto.getInterestAmt(), new BigDecimal(100)));
            installmentInfo.setOtherAmt(instDto.getOtherAmt() == null ? BigDecimal.ZERO : BigDecimalUtil.mul(
                    instDto.getOtherAmt(), new BigDecimal(100)));
            installmentInfo.setSourcesFunding(instDto.getSourcesFunding());// 资金来源
            installmentInfo.setTakeOtherAmt(instDto.getTakeOtherAmt() == 0 ? 2 : instDto.getTakeOtherAmt());// 默认不上收
            installmentInfo.setInterestTerms(instDto.getInterestTerms() == 0 ? instDto.getInstallTerms() : instDto
                    .getInterestTerms());// 剩余期数默认为总期数
            installmentInfo.setFundId(instDto.getFundId());// 上标ID
            return installmentInfo;
        }
        return null;
    }

    public static Map<String, List<InstallmentBillInfo>> listDto2ListBean(List<PaymentDto> list) {
        Map<String, List<InstallmentBillInfo>> resultMap = new HashMap<String, List<InstallmentBillInfo>>(list.size());
        for (PaymentDto paymentDto : list) {// 订单层
            List<InstallmentBillInfo> billList = new ArrayList<InstallmentBillInfo>();
            for (RepayList repayList : paymentDto.getRepayList()) {
                InstallmentBillInfo billInfo = new InstallmentBillInfo();
                billInfo.setOrderId(paymentDto.getOrderId());// 订单号
                billInfo.setSubOrderId(paymentDto.getSubOrderId());// 子订单号
                billInfo.setRepayNo(repayList.getRepayNo());// 账单期数
                billInfo.setCurRealRepayamt(repayList.getAmt());// 金额
                billInfo.setCurRepayDate(paymentDto.getRepayDate());// 还款时间
                billInfo.setStatus(3);// 账单状态 1，未还款，2,部分还款 3，全部还款，4，失效账单
                billInfo.setCurRealRepayamt(repayList.getAmt());// 合并后的账单总金额
                billInfo.setRegId(paymentDto.getRegId());// 手机号
                billInfo.setMerchantNo(paymentDto.getMerchantNo());// 商户号
                billInfo.setOpenId(paymentDto.getOpenId());// 行业号
                billList.add(billInfo);
            }
            resultMap.put(paymentDto.getOrderId(), billList);
        }
        return resultMap;
    }

    /**
     * 
     * Description:部分还款list转map方法
     * 
     * @param objs
     * @param request
     * @return
     */
    public Map<String, List<InstallmentBillInfo>> listDto2ListBeanForPay(List<PaymentDto> list) {
        logger.info("部分还款---参数--{}", JSONObject.toJSONString(list));
        Map<String, List<InstallmentBillInfo>> resultMap = new HashMap<String, List<InstallmentBillInfo>>(list.size());
        for (PaymentDto paymentDto : list) {// 订单层
            List<InstallmentBillInfo> billList = new ArrayList<InstallmentBillInfo>();
            for (RepayList repayList : paymentDto.getRepayList()) {
                InstallmentBillInfo billInfo = new InstallmentBillInfo();
                logger.info("部分还款---repayList---参数--{}", JSONObject.toJSONString(repayList));
                Map<String, Object> params = new HashMap<>();
                params.put("orderId", paymentDto.getOrderId());
                params.put("regId", paymentDto.getRegId());
                params.put("repayNo", String.valueOf(repayList.getRepayNo()));
                List<InstallmentBillInfoNormalRepay> payList = billBizService.selectBillsByCondition(params);
                if (payList != null) {
                    InstallmentBillInfoNormalRepay billInfoRepay = payList.get(0);
                    BigDecimal repayAmt = new BigDecimal(billInfoRepay.getAmt()); // 待还款金额
                    BigDecimal realRepayAmt = repayList.getAmt(); // 实际还款金额
                    logger.info("部分还款---待还款金额---参数--{}", repayAmt);
                    logger.info("部分还款---实际还款金额---参数--{}", realRepayAmt);
                    // 待还款金额与实际还款金额进行比较
                    if (repayAmt.compareTo(realRepayAmt) > 0) {
                        billInfo.setStatus(2);// 账单状态 1，未还款，2,部分还款 3，全部还款，4，失效账单
                    } else {
                        billInfo.setStatus(3);// 账单状态 1，未还款，2,部分还款 3，全部还款，4，失效账单
                    }
                    billInfo.setOrderId(paymentDto.getOrderId());// 订单号
                    billInfo.setRepayNo(repayList.getRepayNo());// 账单期数
                    billInfo.setCurRealRepayamt(repayList.getAmt());// 实际还款金额
                    billInfo.setCurRepayDate(new Date());// 还款时间
                    billInfo.setRegId(paymentDto.getRegId());// 手机号
                    billInfo.setMerchantNo(paymentDto.getMerchantNo());// 商户号
                    billInfo.setOpenId(paymentDto.getOpenId());// 行业号
                    billList.add(billInfo);
                } else {
                    logger.error("部分还款---账户系统查询此订单{},第{}期已还款", paymentDto.getOrderId(), repayList.getRepayNo());
                }
            }
            resultMap.put(paymentDto.getOrderId(), billList);
        }
        return resultMap;
    }
}
