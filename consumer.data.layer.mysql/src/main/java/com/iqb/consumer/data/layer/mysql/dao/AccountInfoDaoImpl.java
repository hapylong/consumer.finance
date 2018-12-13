/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:48:28
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class AccountInfoDaoImpl extends BaseDaoImpl<AccountInfo> implements AccountInfoDao {

    @Override
    public int updateAccount(AccountInfo accountInfo) {
        return 0;
    }

    @Override
    public long insertAccount(AccountInfo accountInfo) {
        return super.insert(accountInfo);
    }

    @Override
    public AccountInfo getAccountInfoByUUID(String conUUID) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("conUUID", conUUID);
        return super.getBy(paramMap);
    }

    @Override
    public Integer getByIDCard(String idNo, String industryType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idNo", idNo);
        params.put("industryType", industryType);
        // AccountInfo accountInfo = new AccountInfo();
        // accountInfo.setIDNO(idNo);
        return (Integer) super.selectOne(getStatement("getByIDCard"), params);
    }

    @Override
    public AccountInfo getByConUUID(String conUUID) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setConUUID(conUUID);
        return (AccountInfo) super.selectOne(getStatement("getByConUUID"), accountInfo);
    }

}
