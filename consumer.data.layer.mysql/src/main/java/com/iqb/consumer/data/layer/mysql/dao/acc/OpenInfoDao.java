package com.iqb.consumer.data.layer.mysql.dao.acc;

import java.util.List;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.bean.acc.OpenInfo;

public interface OpenInfoDao extends BaseDao<OpenInfo> {

    // 根据手机号和pid获取开户信息
    OpenInfo getByCondition(String regId, String idNo, String pid, int status);

    // 根据手机号获取开户信息列表
    List<OpenInfo> listByCondition(String regId, String idNo, int status);

    // 获取列出开户表的行业信息列表
    List<OpenInfo> listOpenInfo4Base();

    // 销户
    long updateStatus(OpenInfo openInfo);
}
