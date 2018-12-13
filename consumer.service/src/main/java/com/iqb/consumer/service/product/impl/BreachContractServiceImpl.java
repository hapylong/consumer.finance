package com.iqb.consumer.service.product.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant.BreachContractConstant;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.utils.BeanUtil;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.mysql.bean.product.BreachContract;
import com.iqb.consumer.data.layer.mysql.dao.product.BreachContractDao;
import com.iqb.consumer.service.product.IBreachContractService;

/**
 * 
 * Description: 违约金比例服务接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Service
public class BreachContractServiceImpl implements IBreachContractService {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(BreachContractServiceImpl.class);

    @Resource
    private BreachContractDao breachContractDao;

    @Override
    public BigDecimal getBreachContract(BigDecimal remainPrincipal, BigDecimal contractAmt, BigDecimal repaymentAmt,
            BigDecimal currentAmt, int bid) {
        logger.info("计算违约金接口访问参数：remainPrincipal:" + remainPrincipal + ",contractAmt:" + contractAmt
                + ",repaymentAmt:" + repaymentAmt + ",currentAmt:" + currentAmt + ",bid:" + bid);
        /** 传入参数格式规范处理 **/
        remainPrincipal = BigDecimalUtil.getFmtValResNonNegative(remainPrincipal);
        contractAmt = BigDecimalUtil.getFmtValResNonNegative(contractAmt);
        repaymentAmt = BigDecimalUtil.getFmtValResNonNegative(repaymentAmt);
        currentAmt = BigDecimalUtil.getFmtValResNonNegative(currentAmt);

        /** 根据计划id查询违约金计划信息 **/
        BreachContract breachContract = breachContractDao.getById(bid);
        if (breachContract == null) {
            logger.error("获取违约金:从数据库查询违约金计划为空");
            return null;
        }
        /** 初始化比例违约金 **/
        BigDecimal baseAmt = BigDecimal.ZERO;
        /** 获取违约金比例系数 **/
        switch (breachContract.getBaseAmtType()) {
        /** 剩余本金比例 **/
            case BreachContractConstant.BASE_AMT_TYPE_REMAIN_AMT:
                baseAmt = this.calRemainAmtOfBaseAmt(remainPrincipal, breachContract);
                break;
            /** 合同金额比例 **/
            case BreachContractConstant.BASE_AMT_TYPE_CONTRACT_AMT:
                baseAmt = this.calContractAmtOfBaseAmt(contractAmt, breachContract);
                break;
            /** 还款额比例 **/
            case BreachContractConstant.BASE_AMT_TYPE_REPAYMENT_AMT:
                baseAmt = this.calRepaymentAmtOfBaseAmt(repaymentAmt, breachContract);
                break;
            default:
                logger.error("获取账单日:没有找到账单日对应类型");
                baseAmt = BigDecimal.ZERO;
        }
        /** 违约金固定金额 **/
        BigDecimal fixedAmount = BigDecimalUtil.getFmtValResNonNegative(breachContract.getFixedAmount());
        /** 获取固定期数违约金 **/
        Integer fixedNper = breachContract.getFixedNper();
        if (fixedNper == null) {
            fixedNper = 0;
        }
        BigDecimal fiedNperAmount = BigDecimalUtil.mul(currentAmt, new BigDecimal(fixedNper));
        return BigDecimalUtil.format(BigDecimalUtil.add(baseAmt, fixedAmount, fiedNperAmount));
    }

    /**
     * 
     * Description: 剩余本金比例
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年11月9日 下午12:01:45
     */
    private BigDecimal calRemainAmtOfBaseAmt(BigDecimal remainPrincipal, BreachContract breachContract) {
        /** 获取违约金计算比例 **/
        BigDecimal ratio = breachContract.getRatio();
        /** 计算违约金 **/
        ratio = BigDecimalUtil.getFmtValResNonNegative(ratio);
        BigDecimal bcAmt = BigDecimalUtil.mul(remainPrincipal, ratio);
        /** 上下限规则 **/
        if (!BreachContractConstant.MAX_MIN_AMT_ENABLE.equals(breachContract.getEnable())) {
            return bcAmt;
        }
        BigDecimal minAmt = BigDecimalUtil.getFmtValResNonNegative(breachContract.getMinAmt());
        BigDecimal maxAmt = BigDecimalUtil.getFmtValResNonNegative(breachContract.getMaxAmt());
        if (bcAmt.compareTo(minAmt) < 0) {
            return minAmt;
        }
        if (bcAmt.compareTo(maxAmt) > 0) {
            return maxAmt;
        }
        return bcAmt;
    }

    /**
     * 
     * Description: 合同金额比例
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年11月9日 下午1:42:00
     */
    private BigDecimal calContractAmtOfBaseAmt(BigDecimal remainPrincipal, BreachContract breachContract) {
        /** 获取违约金计算比例 **/
        BigDecimal ratio = breachContract.getRatio();
        /** 计算违约金 **/
        ratio = BigDecimalUtil.getFmtValResNonNegative(ratio);
        BigDecimal bcAmt = BigDecimalUtil.mul(remainPrincipal, ratio);
        /** 上下限规则 **/
        if (!BreachContractConstant.MAX_MIN_AMT_ENABLE.equals(breachContract.getEnable())) {
            return bcAmt;
        }
        BigDecimal minAmt = BigDecimalUtil.getFmtValResNonNegative(breachContract.getMinAmt());
        BigDecimal maxAmt = BigDecimalUtil.getFmtValResNonNegative(breachContract.getMaxAmt());
        if (bcAmt.compareTo(minAmt) < 0) {
            return minAmt;
        }
        if (bcAmt.compareTo(maxAmt) > 0) {
            return maxAmt;
        }
        return bcAmt;
    }

    /**
     * 
     * Description: 还款额比例
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年11月9日 下午1:42:00
     */
    private BigDecimal calRepaymentAmtOfBaseAmt(BigDecimal remainPrincipal, BreachContract breachContract) {
        /** 获取违约金计算比例 **/
        BigDecimal ratio = breachContract.getRatio();
        /** 计算违约金 **/
        ratio = BigDecimalUtil.getFmtValResNonNegative(ratio);
        BigDecimal bcAmt = BigDecimalUtil.mul(remainPrincipal, ratio);
        /** 上下限规则 **/
        if (!BreachContractConstant.MAX_MIN_AMT_ENABLE.equals(breachContract.getEnable())) {
            return bcAmt;
        }
        BigDecimal minAmt = BigDecimalUtil.getFmtValResNonNegative(breachContract.getMinAmt());
        BigDecimal maxAmt = BigDecimalUtil.getFmtValResNonNegative(breachContract.getMaxAmt());
        if (bcAmt.compareTo(minAmt) < 0) {
            return minAmt;
        }
        if (bcAmt.compareTo(maxAmt) > 0) {
            return maxAmt;
        }
        return bcAmt;
    }

    @Override
    public long deleteById(JSONObject objs) {
        this.checkEmpty(objs);
        return breachContractDao.deleteById(Integer.parseInt(objs.getString("id")));
    }

    @Override
    public long updateById(JSONObject objs) {
        this.checkEmpty(objs);
        BreachContract breachContract = BeanUtil.mapToBean(objs, BreachContract.class);
        return breachContractDao.update(breachContract);
    }

    @Override
    public long insert(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        BreachContract breachContract = BeanUtil.mapToBean(objs, BreachContract.class);
        return breachContractDao.insert(breachContract);
    }

    /**
     * 
     * Description: 校验非空
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:24:53
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
