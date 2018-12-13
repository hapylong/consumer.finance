package com.iqb.consumer.data.layer.mysql.bean.pojo;

public class GenerallyResponseMessage {

    public static enum StatusEnum {
        OK, FAIL
    }

    private StatusEnum status;

    private String errorMsg;

    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
