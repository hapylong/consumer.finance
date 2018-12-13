/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月28日 下午6:40:28
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import java.util.Map;

import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface AccountInfoService {

    /**
     * @description 通过身份证查账户
     * @param IDNO
     * @return
     */
    Integer getByIDCard(String IDNO, String industryType);

    /**
     * @description 开户
     * @param accountInfo
     * @return 0 1
     */
    Map<String, Object> insertAccount(AccountInfo accountInfo, String url);

    long updateAccount(AccountInfo accountInfo);

    AccountInfo getByConUUID(String conUUID);
}
