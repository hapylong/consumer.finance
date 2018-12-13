package com.iqb.consumer.service.product;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.service.AbstractServiceTest;

/**
 * 
 * Description: 违约金比例服务测试用例
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
public class BreachContractServiceTest extends AbstractServiceTest {

    /**
     * 注入账单服务
     */
    @Resource
    private IBreachContractService breachContractService;

    @Test
    public void testGetBillDate() {
        BigDecimal remainPrincipal = new BigDecimal("5555.55");
        BigDecimal contractAmt = new BigDecimal("12222.22");
        BigDecimal repaymentAmt = new BigDecimal("7777.77");
        BigDecimal currentAmt = new BigDecimal("1111.11");
        BigDecimal breachContract =
                breachContractService.getBreachContract(remainPrincipal, contractAmt, repaymentAmt, currentAmt, 4);
        System.out.println(breachContract);
    }

}
