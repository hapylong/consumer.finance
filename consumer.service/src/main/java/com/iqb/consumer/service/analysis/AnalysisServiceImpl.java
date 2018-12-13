package com.iqb.consumer.service.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private AnalysisManager analysisManager;

    @Override
    public Object getRoverDueList(JSONObject requestMessage, Boolean isPage) {
        if (isPage) {
            // 分页查询
            return analysisManager.getPageRoverDueList(requestMessage);
        } else {
            return analysisManager.getRoverDueList(requestMessage);
        }

    }

    @Override
    public Object getNewRoverDueList(JSONObject requestMessage, Boolean isPage) {
        if (isPage) {
            // 分页查询
            return analysisManager.getNewPageRoverDueList(requestMessage);
        } else {
            return analysisManager.getNewRoverDueList(requestMessage);
        }

    }

    @Override
    public Object getTotalNewRoverDueList(JSONObject jo, Boolean isPage) {
        if (isPage) {
            // 分页查询
            return analysisManager.getTotalNewPageRoverDueList(jo);
        } else {
            return analysisManager.getTotalNewPageRoverDueList(jo);
        }
    }

    @Override
    public Object getNoInsideTotalNewRoverDueList(JSONObject jo, Boolean isPage) {
        if (isPage) {
            // 分页查询
            return analysisManager.getNoInsideTotalNewRoverDueList(jo);
        } else {
            return analysisManager.getNoInsideTotalNewRoverDueList(jo);
        }
    }
}
