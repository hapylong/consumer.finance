package com.iqb.consumer.data.layer.mysql.dao.acc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccBaseInfo;

@Repository
public class AccBaseInfoDaoImpl extends BaseDaoImpl<AccBaseInfo> implements AccBaseInfoDao {

    @Override
    public AccBaseInfo getByIDCard(String idNo, int status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idNo", idNo);
        params.put("status", status);
        return (AccBaseInfo) super.selectOne(getStatement("getByIDCard"), params);
    }

}
