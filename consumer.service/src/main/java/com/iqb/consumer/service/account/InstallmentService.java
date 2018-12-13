/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:29:42
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface InstallmentService {

    /**
     * @description 通过用户UUID查询开户信息
     * @param conUUID
     * @return bean
     */
    AccountInfo getAccountInfoByUUID(String conUUID);

    /**
     * @description 更新账户信息
     * @param accountInfo
     * @return 0 1
     */
    int updateAccount(AccountInfo accountInfo);

    /**
     * @description 新增账户信息
     * @param accountInfo
     * @return 返回主键
     */
    long insertAccountInfo(AccountInfo accountInfo);

    /**
     * @description 保存分期信息
     * @param installmentInfo
     * @return 返回主键
     */
    long insertInstalInfo(InstallmentInfo installmentInfo);

    /**
     * @description 保存分摊详情
     * @param installDetail
     * @return 返回主键
     */
    // long insertInstalDetail(InstallmentDetail installDetail);

    /**
     * @description 添加分期计划
     * @param installmentPlan
     * @return
     */
    // long insert(InstallmentPlan installmentPlan);

}
