package com.iqb.consumer.data.layer.mysql.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.bean.NewRoverdueEntity;

@Repository
public class AnalysisDataNewDaoImpl extends BaseDaoImpl<NewRoverdueEntity> implements AnalysisDataNewDao {

    @Override
    public List<NewRoverdueEntity> getNewRoverDueList(JSONObject requestMessage) {
        return selectList(getStatement("getNewRoverDueList"), requestMessage);
    }

    @Override
    public PageBean getNewPageRoverDueList(PageParam pageParam, JSONObject requestMessage) {
        return listPage(pageParam, requestMessage, "getNewRoverDueList");
    }

    @Override
    public Object getTotalNewPageRoverDueList(JSONObject jo) {
        return selectList(getStatement("getTotalNewPageRoverDueList"), jo);
    }

    @Override
    public Object getNoInsideTotalNewRoverDueList(JSONObject jo) {
        return selectList(getStatement("getNoInsideTotalNewRoverDueList"), jo);
    }

}
