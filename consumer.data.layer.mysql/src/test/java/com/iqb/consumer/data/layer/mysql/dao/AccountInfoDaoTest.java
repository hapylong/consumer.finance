/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 下午2:02:54
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class AccountInfoDaoTest extends AbstractRepositoryTest {

    @Resource
    private AccountInfoDao accountInfoDao;

    @Test
    public void openAccount() {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setConUUID("test13800006001test");// 用户ID
        accountInfo.setRegID("13800006001");// 注册号
        accountInfo.setAccStatus(0);// 账户状态(0、正常,1、冻结,2、注销)
        accountInfo.setCreditLimit(new BigDecimal("10000.00"));// 授信额度
        accountInfo.setAvailLimit(new BigDecimal("10000.00"));// 可用额度
        accountInfo.setCurrType("RMB");// 币种
        accountInfo.setOpenDay(new Date());// 开户日期
        accountInfo.setFreeInterestCount(0);// 免息卷总数
        accountInfo.setPointSum(0);// 累计积分
        long id = accountInfoDao.insert(accountInfo);
        System.out.println("主键=" + id);
    }

    @Test
    public void getAccountInfoByUUID() {
        String conUUID = "test13800006001test";
        AccountInfo accountInfo = accountInfoDao.getAccountInfoByUUID(conUUID);
        System.out.println(accountInfo);
    }
}
