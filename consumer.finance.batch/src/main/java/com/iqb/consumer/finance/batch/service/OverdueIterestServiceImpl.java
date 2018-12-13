/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年11月09日 下午4:44:16
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.OverdueInterest;
import com.iqb.consumer.finance.batch.bean.OverdueRule;
import com.iqb.consumer.finance.batch.dao.OverdueInterestDao;
import com.iqb.consumer.finance.batch.exception.BizException;
import com.iqb.consumer.finance.batch.util.BeanUtil;
import com.iqb.consumer.finance.batch.util.BigDecimalUtil;
import com.iqb.consumer.finance.batch.util.StringUtil;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
@Service
public class OverdueIterestServiceImpl implements OverdueIterestService {

    protected static final Logger logger = LoggerFactory.getLogger(OverdueIterestServiceImpl.class);

    @Resource
    private OverdueInterestDao overdueInterestDao;

    /* 罚息基数_剩余本金 */
    protected static final int REMAIN_PRINCIPAL = 1;
    /* 罚息基数_实际分期金额 */
    protected static final int INSTALL_AMT = 4;
    /* 罚息基数_本期账单 */
    protected static final int PRINCIPAL = 3;
    /**
     * 罚息基数_合同金额
     */
    private static final int CONTRACTAMT = 2;
    /**
     * 罚息基数_剩余本息
     */
    private static final int REMAINPRIANDINTEREST = 5;

    /**
     * @params
     * @remainPrincipal 剩余本金
     * @installAmt 实际分期金额
     * @principal 本期分摊
     * @contractAmt 合同金额
     * @remainPriandInterest 剩余本息
     */
    @Override
    public Map<String, BigDecimal> calculateOverdueInterestWithFixed(BigDecimal remainPrincipal, BigDecimal installAmt,
            BigDecimal principal, BigDecimal contractAmt, BigDecimal remainPriandInterest, int overdueDays,
            String id) {

        logger.debug("开始进入逾期费用计算服务！ 剩余本金：{} 合同金额：{} 本期账单：{} 逾期费用计算id：{}", remainPrincipal, installAmt, principal, id);

        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        BigDecimal num = BigDecimal.ZERO;
        BigDecimal amt = BigDecimal.ZERO;
        BigDecimal fixedAmount = BigDecimal.ZERO;
        // 违约金规则
        OverdueInterest overdueInterest = new OverdueInterest();
        List<OverdueRule> overdueRuleS = null;// 逾期规则
        remainPrincipal = BigDecimalUtil.getFmtValResNonNegative(remainPrincipal);
        installAmt = BigDecimalUtil.getFmtValResNonNegative(installAmt);
        principal = BigDecimalUtil.getFmtValResNonNegative(principal);

        try {
            overdueInterest = overdueInterestDao.getOverdueInterestById(id);
            overdueRuleS = overdueInterestDao.getOverdueRuleSById(overdueInterest.getId() + "");
            // 第一步：计算逾期金额
            // 根据逾期天数锁定逾期规则
            OverdueRule overdueRule = getOverdueRule(overdueRuleS, overdueDays);
            BigDecimal baseAmt =
                    getBaseAmtType(overdueRule.getBaseAmtType(), remainPrincipal, installAmt, principal, contractAmt,
                            remainPriandInterest);
            double ratio = overdueRule.getRatio();// 逾期费率
            double floatRatio = overdueRule.getFloatRatio();// 浮动比例
            /* 利率转化 */
            double nRatio = ratio + ratio * floatRatio;
            BigDecimal bRatio = new BigDecimal(nRatio);
            logger.info("开始计算逾期费用，基础金额类型:{} 费率：{} 上浮比例：{}", overdueRule.getBaseAmtType(), ratio, floatRatio);
            // 逾期手续费 = 金额 * 实际费率
            num = BigDecimalUtil.mul(baseAmt, bRatio);
            // 判断下限
            BigDecimal overdueMinAmt = overdueRule.getMinAmt();// 下限金额
            if (num.compareTo(overdueMinAmt) < 0) {// -1表示小于,0是等于,1是大于
                logger.debug("逾期费用：{} 下限金额：{}", result, overdueMinAmt);
                num = overdueMinAmt;
            }
            num = num.setScale(2, BigDecimal.ROUND_HALF_UP);
            // 第二步：计算违约金
            // 违约金金额计算规则调整(1,固定类型 取固定值 2,比例获取比例类型)
            if (1 == overdueInterest.getFixedType()) { // 固定金额
                fixedAmount = overdueInterest.getFixedAmount();// 逾期固定金额
            } else {
                // 比例计算：违约金的基数
                BigDecimal fixedBaseAmt =
                        getBaseAmtType(overdueInterest.getFixedBaseAmtType(), remainPrincipal, installAmt, principal,
                                contractAmt, remainPriandInterest);
                // 根据基础金额*比例
                fixedAmount = BigDecimalUtil.mul(fixedBaseAmt, new BigDecimal(overdueInterest.getFixedRatio()));
                // 判断下限
                BigDecimal minAmt = overdueInterest.getMinAmt();// 下限金额
                if (fixedAmount.compareTo(minAmt) < 0) {// -1表示小于,0是等于,1是大于
                    logger.debug("逾期费用：{} 下限金额：{}", result, minAmt);
                    fixedAmount = minAmt;
                }
                fixedAmount = fixedAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            result.put("Amt", num);
            result.put("Fixed", fixedAmount);
            logger.debug("逾期费用计算服务成功结束！计算金额：{} 逾期费用：{} 逾期固定金额：{}", amt, num, fixedAmount);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 根据逾期天数获取对应的逾期规则
     * 
     * @param overdueRuleS
     * @param overdueDays
     * @return
     */
    public OverdueRule getOverdueRule(List<OverdueRule> overdueRuleS, int overdueDays) {
        for (OverdueRule overdueRule : overdueRuleS) {
            int overdueMinDays = overdueRule.getOverdueMinDays();
            int overdueMaxDays = overdueRule.getOverdueMaxDays();
            if (overdueMinDays <= overdueDays && (overdueDays <= overdueMaxDays || overdueMaxDays == 999)) {
                logger.info("逾期{}天,选择方案为:{}", overdueDays, overdueRule.getId());
                return overdueRule;
            }
        }
        return null;
    }

    /**
     * 通过类型获取对应的基数金额
     * 
     * @param baseAmtType
     * @param remainPrincipal
     * @param installAmt
     * @param principal
     * @param contractAmt
     * @param remainPriandInterest
     * @return
     */
    private BigDecimal getBaseAmtType(int baseAmtType, BigDecimal remainPrincipal, BigDecimal installAmt,
            BigDecimal principal, BigDecimal contractAmt, BigDecimal remainPriandInterest) {
        BigDecimal amt = BigDecimal.ZERO;
        /* 判断罚息基数 */
        switch (baseAmtType) {
            case REMAIN_PRINCIPAL:
                logger.debug("罚息基数_剩余本金,剩余本金：{}", remainPrincipal);
                amt = remainPrincipal;
                break;
            case INSTALL_AMT:
                logger.debug("罚息基数_合同金额,合同金额 ：{}", installAmt);
                amt = installAmt;
                break;
            case PRINCIPAL:
                logger.debug("罚息基数_本期账单,本期账单：{}", principal);
                amt = principal;
                break;
            case CONTRACTAMT:
                logger.debug("罚息基数_合同金额,合同金额：{}", principal);
                amt = contractAmt;
                break;
            case REMAINPRIANDINTEREST:
                logger.debug("罚息基数_剩余本息,剩余本息：{}", principal);
                amt = remainPriandInterest;
                break;
            default:
                logger.error("没有找到对应的罚息基数 ");
                return BigDecimal.ZERO;
        }
        return amt == null ? BigDecimal.ZERO : amt;
    }

    @Override
    public long deleteById(JSONObject objs) {
        this.checkEmpty(objs);
        return overdueInterestDao.deleteById(Integer.parseInt(objs.getString("id")));
    }

    @Override
    public long updateById(JSONObject objs) {
        this.checkEmpty(objs);
        OverdueInterest overdueInterest = BeanUtil.mapToBean(objs, OverdueInterest.class);
        return overdueInterestDao.update(overdueInterest);
    }

    @Override
    public long insert(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        OverdueInterest overdueInterest = BeanUtil.mapToBean(objs, OverdueInterest.class);
        return overdueInterestDao.insert(overdueInterest);
    }

    /**
     * 
     * Description: 校验非空
     * 
     * @param
     * @return void
     * @throws @Author wangxinbang Create Date: 2016年11月17日 上午10:24:53
     */
    private void checkEmpty(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        if (StringUtil.isEmpty(objs.getString("id"))) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
    }
}
