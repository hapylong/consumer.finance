package com.iqb.consumer.service.account;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.service.AbstractServiceTest;

public class TestAccountInfoService extends AbstractServiceTest {

    @Resource
    private AccountInfoService accountInfoService;

    @Test
    public void testOpenAccountYN() {
        String idNo = "362204199002277414";
        String industryType = "CarLoan";
        int count = accountInfoService.getByIDCard(idNo, industryType);
        System.out.println(count);
    }
}
