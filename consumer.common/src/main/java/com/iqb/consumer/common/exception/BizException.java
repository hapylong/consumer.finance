/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月26日 下午8:24:09
 * @version V1.0
 */

package com.iqb.consumer.common.exception;

import java.io.Serializable;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class BizException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -360968392172915768L;

    /**
     * 数据库操作,insert返回0
     */
    public static final BizException DB_INSERT_RESULT_0 = new BizException(90040001, "数据库操作,insert返回0");

    /**
     * 数据库操作,update返回0
     */
    public static final BizException DB_UPDATE_RESULT_0 = new BizException(90040002, "数据库操作,update返回0");

    /**
     * 数据库操作,selectOne返回null
     */
    public static final BizException DB_SELECTONE_IS_NULL = new BizException(90040003, "数据库操作,selectOne返回null");

    /**
     * 数据库操作,list返回null
     */
    public static final BizException DB_LIST_IS_NULL = new BizException(90040004, "数据库操作,list返回null");

    /**
     * Token 验证不通过
     */
    public static final BizException TOKEN_IS_ILLICIT = new BizException(90040005, "Token 验证非法");
    /**
     * 会话超时　获取session时，如果是空，throws 下面这个异常 拦截器会拦截爆会话超时页面
     */
    public static final BizException SESSION_IS_OUT_TIME = new BizException(90040006, "会话超时");

    /**
     * 获取序列出错
     */
    public static final BizException DB_GET_SEQ_NEXT_VALUE_ERROR = new BizException(90040007, "获取序列出错");

    /**
     * 账单-终止分期
     */
    public static final BizException BILL_STOP_PERIODIZATION_PARAM_ERROR = new BizException(90040008, "传入参数校验异常");

    /**
     * 未查询到有效账单信息
     */
    public static final BizException BILL_STOP_PERIODIZATION_BILL_NULL_ERROR = new BizException(90040009, "未查询到有效账单信息");

    /**
     * 传入参数map为空
     */
    public static final BizException COMMON_PARAM_NULL_ERROR = new BizException(90040010, "参数为空异常");

    /** 计算公式 **/
    public static final BizException CALCULATE_NULL_PARAM = new BizException(10000001, "计算参数为空,无法计算");
    public static final BizException CALCULATE_NULL_PLAN = new BizException(10000002, "分期计划参数为空,无法计算");
    public static final BizException CALCULATE_NOTEXIT_PLAN = new BizException(10000003, "账务系统分期计划不存在");
    public static final BizException CALCULATE_ZERO_AMT = new BizException(10000004, "金额为零,无需计算");

    /**
     * 异常信息
     */
    protected String msg;

    /**
     * 具体异常码
     */
    protected int code;

    public BizException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public BizException() {
        super();
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    /**
     * 实例化异常
     * 
     * @param msgFormat
     * @param args
     * @return
     */
    public BizException newInstance(String msgFormat, Object... args) {
        return new BizException(this.code, msgFormat, args);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message) {
        super(message);
    }
}
