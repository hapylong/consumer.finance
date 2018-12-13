/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月14日 下午3:35:12
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentInfoDao;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentInfoDaoTest extends AbstractRepositoryTest {

    @Resource
    private InstallmentInfoDao installmentInfoDao;

    @Test
    public void insertInstallInfo() {
        InstallmentInfo install = new InstallmentInfo();
        install.setOrderId("20160714153900000001");// 订单ID
        install.setRegId("13800006002");
        install.setBaseId(1L);
        install.setOpenId("1");
        install.setBeginDate(new Date());
        install.setInstallSumAmt(new BigDecimal("2400000"));
        install.setInstallAmt(new BigDecimal("1200000"));// 分期总额
        install.setInstallTerms(12);// 分期期数
        install.setMerchantNo("cdhtc");
        install.setStatus(0);// 分期状态(正常,作废)
        install.setPlanId(1L);
        install.setUseFreeInterest(0);// 是否使用了免息卷(0、否 1、是)
        install.setFreeInterestCount(0);// 免息卷张数(必须小于<=分期期数)
        installmentInfoDao.insertInstalInfo(install);
    }
}
