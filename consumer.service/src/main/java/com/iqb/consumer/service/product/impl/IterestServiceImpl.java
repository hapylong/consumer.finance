/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年11月09日 下午4:44:16
 * @version V1.0
 */

package com.iqb.consumer.service.product.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.utils.BeanUtil;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.mysql.dao.product.InterestDao;
import com.iqb.consumer.data.layer.mysql.domain.product.Interest;
import com.iqb.consumer.service.product.IterestService;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
@Service
public class IterestServiceImpl implements IterestService {

    protected static final Logger logger = LoggerFactory.getLogger(IterestServiceImpl.class);

    /* 利率类型_月利率 */
    protected static final int RATIO_TYPE_MOUTCH = 1;
    /* 利率类型_日利率 */
    protected static final int RATIO_TYPE_DAY = 2;
    /* 利率类型_年利率 */
    protected static final int RATIO_TYPE_YEAR = 3;

    @Resource
    private InterestDao interestDao;

    @Override
    public BigDecimal calculateInterest(BigDecimal amt, String id, Date date) {
        logger.debug("开始进入利息计算服务！ 金额：{} 利息计算id：{} 日期：{}", amt, id, date);

        BigDecimal result = BigDecimal.ZERO;
        Interest interest = new Interest();
        amt = BigDecimalUtil.getFmtValResNonNegative(amt);

        try {
            interest = interestDao.getInterestById(id);
            double ratio = interest.getRatio();// 手续费率
            int ratioType = interest.getRatioType();// 计算方式
            int day2month = interest.getDay2month();// 月天数
            // int day2year = interest.getDay2year();// 年天数
            BigDecimal minAmt = interest.getMinAmt();// 下限金额
            BigDecimal maxAmt = interest.getMaxAmt();// 上限金额
            int enable = interest.getEnable();// 是否启用上下限

            /* 判断值合法性 */

            /* 利率转化 */
            BigDecimal bRatio = new BigDecimal(ratio);

            /* 日期计算相关 */
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int mouthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            // int yearDays = cal.getActualMaximum(Calendar.DAY_OF_YEAR);

            logger.debug("开始计算利息，费率：{}", ratio);
            /* 判断利率类型 */
            switch (ratioType) {
                case RATIO_TYPE_MOUTCH:// 月利率
                    logger.debug("月利率模式...");
                    // 利息 = 金额 * 月利率
                    result = BigDecimalUtil.mulForRate(amt, bRatio);
                    break;
                case RATIO_TYPE_DAY:// 日利率
                    logger.debug("日利率模式...");
                    if (day2month == 1) {// 1-30天
                        logger.debug("日利率模式，按：30天处理...");
                        // 利息 = 金额 * 日利率 * 30
                        BigDecimal nRatio = BigDecimalUtil.mulForRate(bRatio, new BigDecimal(30));
                        result = BigDecimalUtil.mulForRate(amt, nRatio);
                    } else {// 2-实际天数
                        logger.debug("日利率模式，按本月实际天数：{}天处理...", mouthDays);
                        // 利息 = 金额 * 日利率 * 本月实际天数
                        BigDecimal nRatio = BigDecimalUtil.mulForRate(bRatio, new BigDecimal(mouthDays));
                        result = BigDecimalUtil.mulForRate(amt, nRatio);
                    }
                    break;
                case RATIO_TYPE_YEAR:// 年利率
                    logger.debug("年利率模式...");
                    // if (day2year == 1) {// 1-365天
                    logger.debug("年利率模式，按：365天处理");
                    // 利息 = 金额 * (年利息 / 365) * 本月实际天数
                    // BigDecimal nRatio =
                    // BigDecimalUtil.mulForRate(BigDecimalUtil.divForRate(bRatio, new
                    // BigDecimal(365)),
                    // new BigDecimal(mouthDays));
                    BigDecimal nRatio = BigDecimalUtil.divForRate(bRatio, new BigDecimal(12));
                    result = BigDecimalUtil.mulForRate(amt, nRatio);

                    // BigDecimal nRatio = bRatio.divide(new BigDecimal(365)).multiply(new
                    // BigDecimal(mouthDays));
                    // result = amt.multiply(nRatio);
                    // } else if (day2year == 2) {// 2-360天
                    // logger.debug("年利率模式，按：360天处理");
                    // // 利息 = 金额 * (年利息 / 360) * 本月实际天数
                    // BigDecimal nRatio =
                    // BigDecimalUtil.mulForRate(BigDecimalUtil.divForRate(bRatio, new
                    // BigDecimal(360)),
                    // new BigDecimal(mouthDays));
                    // result = BigDecimalUtil.mulForRate(amt, nRatio);
                    //
                    // // BigDecimal nRatio = bRatio.divide(new BigDecimal(360)).multiply(new
                    // // BigDecimal(mouthDays));
                    // // result = amt.multiply(nRatio);
                    // } else {// 3-实际天数
                    // logger.debug("年利率模式，按实际天数：{}处理,本月天数：{}", yearDays, mouthDays);
                    // // 利息 = 金额 * (年利息 / 本年实际天数) * 本月实际天数
                    // BigDecimal nRatio =
                    // BigDecimalUtil.mulForRate(BigDecimalUtil.divForRate(bRatio, new
                    // BigDecimal(yearDays)),
                    // new BigDecimal(mouthDays));
                    // result = BigDecimalUtil.mulForRate(amt, nRatio);
                    //
                    // // BigDecimal nRatio = bRatio.divide(new
                    // BigDecimal(yearDays)).multiply(new
                    // // BigDecimal(mouthDays));
                    // // result = amt.multiply(nRatio);
                    // }
                    break;
                default:
                    logger.error("没有找到对应的利率类型 ");
                    return null;
            }
            /* 判断上下限 */
            if (enable == 1) {// 1-启用 2-未启用
                if (result.compareTo(minAmt) < 0) {// -1表示小于,0是等于,1是大于
                    logger.debug("费用：{} 下限金额：{}", result, minAmt);
                    result = minAmt;
                } else if (result.compareTo(maxAmt) > 0) {// -1表示小于,0是等于,1是大于
                    logger.debug("费用：{} 上限金额：{}", result, maxAmt);
                    result = maxAmt;
                }
            }

            logger.debug("结束计算利息");
        } catch (

        Exception e) {
            throw e;
        }

        /* 装换为长整型 */
        // result = new BigDecimal(result.longValue());
        result = result.setScale(2, BigDecimal.ROUND_HALF_UP);

        logger.debug("利息计算服务成功结束！金额：{} 利息：{}", amt, result);

        return result;
    }

    @Override
    public BigDecimal calculateInterestForDay(BigDecimal amt, String id, Date date) {
        logger.debug("开始进入按天计算利息服务！ 金额：{} 利息计算id：{} 日期：{}", amt, id, date);

        BigDecimal result = BigDecimal.ZERO;
        Interest interest = new Interest();
        amt = BigDecimalUtil.getFmtValResNonNegative(amt);

        try {
            interest = interestDao.getInterestById(id);
            double ratio = interest.getRatio();// 手续费率
            int ratioType = interest.getRatioType();// 计算方式
            int day2month = interest.getDay2month();// 月天数
            int day2year = interest.getDay2year();// 年天数
            // BigDecimal minAmt = interest.getMinAmt();// 下限金额
            // BigDecimal maxAmt = interest.getMaxAmt();// 上限金额
            // int enable = interest.getEnable();// 是否启用上下限

            /* 判断值合法性 */

            /* 利率转化 */
            BigDecimal bRatio = new BigDecimal(ratio);

            /* 日期计算相关 */
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int mouthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int yearDays = cal.getActualMaximum(Calendar.DAY_OF_YEAR);

            logger.debug("开始计算利息，费率：{}", ratio);
            /* 判断利率类型 */
            switch (ratioType) {
                case RATIO_TYPE_MOUTCH:// 月利率
                    logger.debug("月利率模式...");
                    if (day2month == 1) {// 1-30天
                        logger.debug("月利率模式，按：30天处理...");
                        // 利息 = 金额 * 月利率 / 30
                        BigDecimal nRatio = BigDecimalUtil.divForRate(bRatio, new BigDecimal(30));
                        result = BigDecimalUtil.mulForRate(amt, nRatio);
                    } else {// 2-实际天数
                        logger.debug("月利率模式，按本月实际天数：{}天处理...", mouthDays);
                        // 利息 = 金额 * 月利率 / 本月实际天数
                        BigDecimal nRatio = BigDecimalUtil.divForRate(bRatio, new BigDecimal(mouthDays));
                        result = BigDecimalUtil.mulForRate(amt, nRatio);
                    }
                    break;
                case RATIO_TYPE_DAY:// 日利率
                    logger.debug("日利率模式...");
                    // 利息 = 金额 * 日利率
                    result = BigDecimalUtil.mulForRate(amt, bRatio);
                    break;
                case RATIO_TYPE_YEAR:// 年利率
                    logger.debug("年利率模式...");
                    if (day2year == 1) {// 1-365天
                        logger.debug("年利率模式，按：365天处理");
                        // 利息 = 金额 * (年利息 / 365)
                        BigDecimal nRatio = BigDecimalUtil.divForRate(bRatio, new BigDecimal(365));
                        result = BigDecimalUtil.mulForRate(amt, nRatio);
                    } else if (day2year == 2) {// 2-360天
                        logger.debug("年利率模式，按：360天处理");
                        // 利息 = 金额 * (年利息 / 360)
                        BigDecimal nRatio = BigDecimalUtil.divForRate(bRatio, new BigDecimal(360));
                        result = BigDecimalUtil.mulForRate(amt, nRatio);
                    } else {// 3-实际天数
                        logger.debug("年利率模式，按实际天数：{}处理", yearDays);
                        // 利息 = 金额 * (年利息 / 本年实际天数)
                        BigDecimal nRatio = BigDecimalUtil.divForRate(bRatio, new BigDecimal(yearDays));
                        result = BigDecimalUtil.mulForRate(amt, nRatio);
                    }
                    break;
                default:
                    logger.error("没有找到对应的利率类型 ");
                    return null;
            }
            /* 判断上下限 */
            // if (enable == 1) {// 1-启用 2-未启用
            // if (result.compareTo(minAmt) < 0) {// -1表示小于,0是等于,1是大于
            // logger.debug("费用：{} 下限金额：{}", result, minAmt);
            // result = minAmt;
            // } else if (result.compareTo(maxAmt) > 0) {// -1表示小于,0是等于,1是大于
            // logger.debug("费用：{} 上限金额：{}", result, maxAmt);
            // result = maxAmt;
            // }
            // }

            logger.debug("结束按天计算利息");
        } catch (Exception e) {
            throw e;
        }

        /* 装换为长整型 */
        // result = new BigDecimal(result.longValue());
        result = result.setScale(2, BigDecimal.ROUND_HALF_UP);

        logger.debug("按天计算利息服务成功结束！金额：{} 利息：{}", amt, result);

        return result;
    }

    @Override
    public long deleteById(JSONObject objs) {
        this.checkEmpty(objs);
        return interestDao.deleteById(Integer.parseInt(objs.getString("id")));
    }

    @Override
    public long updateById(JSONObject objs) {
        this.checkEmpty(objs);
        Interest interest = BeanUtil.mapToBean(objs, Interest.class);
        return interestDao.update(interest);
    }

    @Override
    public long insert(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        Interest interest = BeanUtil.mapToBean(objs, Interest.class);
        return interestDao.insert(interest);
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
