package com.iqb.consumer.data.layer.mysql.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.bean.NewRoverdueEntity;
import com.iqb.consumer.data.layer.mysql.bean.RoverdueEntity;

public interface AnalysisDataDao extends BaseDao<RoverdueEntity> {

    List<RoverdueEntity> getRoverDueList(JSONObject requestMessage);

    PageBean getPageRoverDueList(PageParam pageParam, JSONObject requestMessage);

}
