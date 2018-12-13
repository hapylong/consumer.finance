package com.iqb.consumer.data.layer.mysql.dao.acc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.acc.OpenInfo;

@Repository
public class OpenInfoDaoImpl extends BaseDaoImpl<OpenInfo> implements OpenInfoDao {

    @Override
    public OpenInfo getByCondition(String regId, String idNo, String pid, int status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idNo", idNo);
        params.put("regId", regId);
        params.put("pid", pid);
        params.put("status", status);
        return (OpenInfo) super.selectOne(getStatement("getByCondition"), params);
    }

    @Override
    public List<OpenInfo> listByCondition(String regId, String idNo, int status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("regId", regId);
        params.put("idNo", idNo);
        params.put("status", status);
        return super.selectList(getStatement("listByCondition"), params);
    }

    @Override
    public List<OpenInfo> listOpenInfo4Base() {
        return super.selectList(getStatement("listOpenInfo4Base"));
    }

    @Override
    public long updateStatus(OpenInfo openInfo) {
        return super.getSqlSession().update(getStatement("updateStatus"), openInfo);
    }

}
