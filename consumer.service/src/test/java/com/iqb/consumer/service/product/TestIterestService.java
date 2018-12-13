package com.iqb.consumer.service.product;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.service.AbstractServiceTest;

public class TestIterestService extends AbstractServiceTest {

    @Resource
    private IterestService iterestService;

    @Test
    public void testIterestService() {
        String id = "1";

        // 月利息
        BigDecimal count = iterestService.calculateInterest(new BigDecimal(10000000), id, new Date());
        System.out.println(count);

        // 天利息
        count = iterestService.calculateInterestForDay(new BigDecimal(1000000), id, new Date());
        System.out.println(count);
    }
}
