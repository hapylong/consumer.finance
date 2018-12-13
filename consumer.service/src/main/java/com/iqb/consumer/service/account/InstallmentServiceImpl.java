/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:44:16
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.dao.AccountInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentPlanDao;
import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;

// import com.iqb.consumer.data.layer.mysql.domain.InstallmentPlan;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class InstallmentServiceImpl implements InstallmentService {

    protected static final Logger logger = LoggerFactory.getLogger(InstallmentServiceImpl.class);

    @Resource
    private AccountInfoDao accountInfoDao;
    @Resource
    private InstallmentPlanDao installmentPlanDao;

    @Override
    public AccountInfo getAccountInfoByUUID(String conUUID) {
        logger.debug("getAccountInfoByUUID conUUID:{}", conUUID);
        return accountInfoDao.getAccountInfoByUUID(conUUID);
    }

    @Override
    public int updateAccount(AccountInfo accountInfo) {
        return 0;
    }

    @Override
    public long insertAccountInfo(AccountInfo accountInfo) {
        return 0;
    }

    @Override
    public long insertInstalInfo(InstallmentInfo installmentInfo) {
        return 0;
    }

    // @Override
    // public long insertInstalDetail(InstallmentDetail installDetail) {
    // return 0;
    // }

    // @Override
    // public long insert(InstallmentPlan installmentPlan) {
    // return installmentPlanDao.insert(installmentPlan);
    // }

}
