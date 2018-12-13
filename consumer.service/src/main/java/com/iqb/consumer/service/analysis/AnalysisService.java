package com.iqb.consumer.service.analysis;

import com.alibaba.fastjson.JSONObject;

public interface AnalysisService {

    Object getRoverDueList(JSONObject requestMessage, Boolean isPage);

    Object getNewRoverDueList(JSONObject requestMessage, Boolean isPage);

    Object getTotalNewRoverDueList(JSONObject jo, Boolean isPage);

    Object getNoInsideTotalNewRoverDueList(JSONObject jo, Boolean isPage);

}
