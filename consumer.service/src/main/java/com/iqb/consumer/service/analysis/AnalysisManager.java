package com.iqb.consumer.service.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.dao.AnalysisDataDao;
import com.iqb.consumer.data.layer.mysql.dao.AnalysisDataNewDao;

@Component
public class AnalysisManager {

    @Autowired
    private AnalysisDataDao analysisDataDaoImpl;
    @Autowired
    private AnalysisDataNewDao analysisDataNewDaoImpl;

    public Object getRoverDueList(JSONObject requestMessage) {
        return analysisDataDaoImpl.getRoverDueList(requestMessage);
    }

    public PageBean getPageRoverDueList(JSONObject requestMessage) {
        int pageNum =
                requestMessage.get("pageNum") == null ? 1 : Integer.parseInt(requestMessage.get("pageNum").toString());
        int pageSize = requestMessage.get("pageSize") == null
                ? 10
                : Integer.parseInt(requestMessage.get("pageSize").toString());
        PageParam pp = new PageParam(pageNum, pageSize);
        return analysisDataDaoImpl.getPageRoverDueList(pp, requestMessage);
    }

    public Object getNewRoverDueList(JSONObject requestMessage) {
        return analysisDataNewDaoImpl.getNewRoverDueList(requestMessage);
    }

    public PageBean getNewPageRoverDueList(JSONObject requestMessage) {
        int pageNum =
                requestMessage.get("pageNum") == null ? 1 : Integer.parseInt(requestMessage.get("pageNum").toString());
        int pageSize = requestMessage.get("pageSize") == null
                ? 10
                : Integer.parseInt(requestMessage.get("pageSize").toString());
        PageParam pp = new PageParam(pageNum, pageSize);
        return analysisDataNewDaoImpl.getNewPageRoverDueList(pp, requestMessage);
    }

    public Object getTotalNewPageRoverDueList(JSONObject jo) {
        return analysisDataNewDaoImpl.getTotalNewPageRoverDueList(jo);
    }

    public Object getNoInsideTotalNewRoverDueList(JSONObject jo) {
        return analysisDataNewDaoImpl.getNoInsideTotalNewRoverDueList(jo);
    }

}
