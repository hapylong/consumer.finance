package com.iqb.consumer.service.account;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.service.AbstractServiceTest;

/**
 * 
 * Description: 逾期调度测试
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月14日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class TestBizSchedulerOverdueService extends AbstractServiceTest {

    @Resource
    private BizSchedulerOverdueService bizSchedulerOverdueService;

    @Test
    public void testCalOverdueInterest() {
        bizSchedulerOverdueService.calOverdueInterest("", "", "", "20161229");
    }

}
