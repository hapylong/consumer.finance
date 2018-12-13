package com.iqb.consumer.service.product;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.service.AbstractServiceTest;

public class TestOverdueIterestService extends AbstractServiceTest {

    @Resource
    private OverdueIterestService overdueIterestService;

    @Test
    public void testOverdueIterestService() {
        String id = "1";
        overdueIterestService.calculateOverdueInterestWithFixed(new BigDecimal(100), new BigDecimal(200),
                new BigDecimal(300), new BigDecimal(100), new BigDecimal(200), 1, id);
    }
}
