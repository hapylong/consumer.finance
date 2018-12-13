/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月14日 下午4:23:22
 * @version V1.0
 */

package com.iqb.consumer.service.installment;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;
import com.iqb.consumer.service.AbstractServiceTest;
import com.iqb.consumer.service.account.InstallmentBizServiceImpl;
import com.iqb.consumer.service.account.dto.OrderInfoDto;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentBiz extends AbstractServiceTest {

    @Resource
    private InstallmentBizServiceImpl installmentBizServiceImpl;

    @Test
    public void test() {
        OrderInfoDto orderInfo = new OrderInfoDto();
        orderInfo.setId(111111111);
        orderInfo.setOrderNo("20160708111111000001");
        orderInfo.setSumAmount(new BigDecimal("12000.00"));
        orderInfo.setSumShareAmount(new BigDecimal("11000.00"));
        orderInfo.setSumPeriods(12);
        orderInfo.setRealPeriods(11);
        orderInfo.setInstallNo(1);// 分期计划1
        orderInfo.setEachAmount(new BigDecimal("1000.00"));// 每期金额
        orderInfo.setEachShareAmount(new BigDecimal("1000.00"));
        orderInfo.setMerchantNo("test001");
        orderInfo.setNeedShare(1);
        orderInfo.setInstallType(2);
        orderInfo.setPrePayment(new BigDecimal("0.00"));
        orderInfo.setMargin(new BigDecimal("0.00"));
        orderInfo.setServiceFee(new BigDecimal("0.00"));
        orderInfo.setDownPayment(new BigDecimal("0.00"));
        orderInfo.setFinalPayment(new BigDecimal("0.00"));
        orderInfo.setRefundAmount(new BigDecimal("0.00"));
        orderInfo.setRefundStatus(1);
        orderInfo.setFeeType(2);
        orderInfo.setOrderStatus(1);
        orderInfo.setUseFreeInterest(0);
        orderInfo.setFreeInterestCount(0);
        orderInfo.setMerchantRemark("纳美测试");
        orderInfo.setOrderInfo(null);
        orderInfo.setConUuid("12312313212321");
        orderInfo.setConRemark("测试");
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setId(1L);
        // installmentBizServiceImpl.paymentByInstall(orderInfo, accountInfo);
    }
}
