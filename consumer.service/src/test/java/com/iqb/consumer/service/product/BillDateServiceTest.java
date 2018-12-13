package com.iqb.consumer.service.product;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.service.AbstractServiceTest;

/**
 * 
 * Description: 账单日服务相关测试用例
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月8日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class BillDateServiceTest extends AbstractServiceTest {

    /**
     * 注入账单服务
     */
    @Resource
    private IBillDateService billDateService;

    @Test
    public void testGetBillDate() {
        String retBillDate = billDateService.getBillDate("20161116", 1);
        System.out.println(retBillDate);
    }

    @Test
    public void testSGetBillDate() {
        String retBillDate = billDateService.getBillDate("20161116", 1, "20161217", 1);
        System.out.println(retBillDate);
    }

}
