/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:48:15
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface AccountInfoDao extends BaseDao<AccountInfo> {

    /**
     * @description 通过用户UUID查询开户信息
     * @param conUUID
     * @return bean
     */
    AccountInfo getAccountInfoByUUID(String conUUID);

    // 通过身份证查账户
    Integer getByIDCard(String idNo, String industryType);

    // 通过用户UUID查询开户信息
    AccountInfo getByConUUID(String conUUID);

    /**
     * @description 更新账户信息
     * @param accountInfo
     * @return 0 1
     */
    int updateAccount(AccountInfo accountInfo);

    /**
     * @description 更新账户信息
     * @param accountInfo
     * @return 0 1
     */
    long insertAccount(AccountInfo accountInfo);

}
