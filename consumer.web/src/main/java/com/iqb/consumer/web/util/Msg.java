package com.iqb.consumer.web.util;

import java.io.Serializable;

/**
 * 消息实体
 * 
 * @author baiyanbing
 * @createTime 2016.03.24
 */
public class Msg {

    /** 错误消息 */
    private static final int error = 0;

    /** 普通消息 */
    private static final int info = 1;

    /** 登录超时 */
    private static final int offline = 2;

    /** 未实名认证 */
    private static final int nocert = 3;

    /**
     * 错误消息(代码默认为 {@link Msg.error} )
     * 
     * @param msg
     * @return
     */
    public static MsgInfo error() {
        return new MsgInfo(error, null);
    }

    /**
     * 错误消息(代码默认为 {@link Msg.error} )
     * 
     * @param msg
     * @return
     */
    public static MsgInfo error(Object msg) {
        return new MsgInfo(error, msg);
    }

    /**
     * 错误消息
     * 
     * @param code 错误代码
     * @param msg 错误消息
     * @return
     */
    public static MsgInfo error(int code, Object msg) {
        return new MsgInfo(code, msg);
    }

    /**
     * 成功消息(代码默认为 {@link Msg.info} )
     * 
     * @param result
     * @return
     */
    public static MsgInfo info() {
        return info(null, null);
    }

    /**
     * 成功消息(代码默认为 {@link Msg.info} )
     * 
     * @param result
     * @return
     */
    public static MsgInfo info(Object msg) {
        return info(msg, null);
    }

    /**
     * 成功消息(代码默认为 {@link Msg.info} )
     * 
     * @param msg
     * @param result
     * @return
     */
    public static MsgInfo info(Object msg, Object result) {
        return new MsgInfo(info, msg, result);
    }

    public static MsgInfo offline() {
        return error(offline, "登录超时！");
    }

    /**
     * 未认证消息(代码默认为 {@link Msg.info} )
     * 
     * @param result
     * @return
     */
    public static MsgInfo nocert(Object msg) {
        return new MsgInfo(nocert, msg);
    }

    /**
     * 未认证消息(代码默认为 {@link Msg.info} )
     * 
     * @param result
     * @return
     */
    public static MsgInfo nocert() {
        return new MsgInfo(nocert, "您尚未实名认证！");
    }

}


class MsgInfo implements Serializable {

    private static final long serialVersionUID = -6349512635884943016L;

    private int code;
    private Object msg = null;
    private Object result = null;

    public MsgInfo(int code, Object msg) {
        this.code = code;
        this.msg = msg;
    }

    public MsgInfo(int code, Object msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
