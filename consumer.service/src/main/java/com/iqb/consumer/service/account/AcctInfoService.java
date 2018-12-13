package com.iqb.consumer.service.account;

import java.util.Map;

import com.iqb.consumer.data.layer.mysql.bean.acc.OpenInfo;

public interface AcctInfoService {

    Map<String, String> openAccount(Map<String, Object> param);

    Map<String, String> queryAccount(Map<String, Object> param);

    Map<String, String> destroyAccount(Map<String, Object> param);

    /**
     * 通过参数查询开户信息
     * 
     * @param param
     * @return
     */
    OpenInfo queryAccByParams(Map<String, Object> param);

    boolean isOpenIdEnabled(String openId);
}
