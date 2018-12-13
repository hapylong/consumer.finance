package com.iqb.consumer.data.layer.mysql.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.bean.NewRoverdueEntity;
import com.iqb.consumer.data.layer.mysql.bean.RoverdueEntity;

@Repository
public class AnalysisDataDaoImpl extends BaseDaoImpl<RoverdueEntity> implements AnalysisDataDao {

    @Override
    public List<RoverdueEntity> getRoverDueList(JSONObject requestMessage) {
        return selectList(getStatement("getRoverDueList"), requestMessage);
    }

    @Override
    public PageBean getPageRoverDueList(PageParam pageParam, JSONObject requestMessage) {
        return listPage(pageParam, requestMessage, "getRoverDueList");
    }

}
