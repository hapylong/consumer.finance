package com.iqb.consumer.data.layer.mysql.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.bean.NewRoverdueEntity;

public interface AnalysisDataNewDao extends BaseDao<NewRoverdueEntity> {

    List<NewRoverdueEntity> getNewRoverDueList(JSONObject requestMessage);

    PageBean getNewPageRoverDueList(PageParam pageParam, JSONObject requestMessage);

    Object getTotalNewPageRoverDueList(JSONObject jo);

    Object getNoInsideTotalNewRoverDueList(JSONObject jo);

}
