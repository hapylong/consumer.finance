package com.iqb.consumer.data.layer.mysql.dao.acc;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccBaseInfo;

public interface AccBaseInfoDao extends BaseDao<AccBaseInfo> {

    // 根据身份证获取基础信息
    AccBaseInfo getByIDCard(String idNo, int status);

}
