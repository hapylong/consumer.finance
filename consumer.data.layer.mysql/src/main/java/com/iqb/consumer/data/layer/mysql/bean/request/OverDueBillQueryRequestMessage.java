package com.iqb.consumer.data.layer.mysql.bean.request;

import java.util.List;

public class OverDueBillQueryRequestMessage {

    List<QueryCondition> conditions;

    public List<QueryCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<QueryCondition> conditions) {
        this.conditions = conditions;
    }
}
