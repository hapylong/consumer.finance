/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月8日 下午2:27:46
 * @version V1.0
 */
package com.iqb.consumer.common.exception;

import java.io.Serializable;

/**
 * 接口请求异常
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class RequestException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final RequestException GET_PARAM_ERROR = new RequestException(30010001, "分期获取参数异常");
    public static final RequestException GET_DECRYPTION_EXCEPTION = new RequestException(30010001, "分期参数异常解密");
    public static final RequestException DECRYPTION_EXCEPTION = new RequestException(30010101, "参数异常解密");
    public static final RequestException SIGN_DECRYPTION_EXCEPTION = new RequestException(30010002, "分期参数验签失败");
    public static final RequestException COMMONSIGN_DECRYPTION_EXCEPTION = new RequestException(30010102, "参数验签失败");
    public static final RequestException INSTDATA_SAVE_EXCEPTION = new RequestException(30010003, "分期数据保存发生异常");

    public static final RequestException REPAYPARAMS_SIGN__EXCEPTION = new RequestException(60010001, "正常还款参数验签失败");
    public static final RequestException REPAYPARAMS_DECRYPTION_EXCEPTION =
            new RequestException(60010002, "正常还款参数异常解密");
    public static final RequestException REPAYPARAMS_CONVER2BEAN_EXCEPTION = new RequestException(60010003,
            "正常还款参数转Bean异常");

    public static final RequestException REFUND_SIGN_EXCEPTION = new RequestException(60010004, "平账数据验签失败");
    public static final RequestException REFUND_DECRYPTION_EXCEPTION = new RequestException(60010005, "平账数据异常解密");
    public static final RequestException REFUND_CONVER2BEAN_EXCEPTION = new RequestException(60010006, "平账数据转Bean异常");

    // 账单查询类异常
    public static final RequestException BILLQUERY_SIGN_EXCEPTION = new RequestException(60020004, "账单查询验签失败");
    public static final RequestException BILLQUERY_DECRYPTION_EXCEPTION = new RequestException(60020005, "账单查询异常解密");
    public static final RequestException BILLQUERY_CONVER2BEAN_EXCEPTION =
            new RequestException(60020006, "账单查询转Bean异常");
    public static final RequestException BILLQUERY_SUBLET_EXCEPTION =
            new RequestException(60020007, "转租记录查询异常");
    /**
     * 异常信息
     */
    protected String msg;

    /**
     * 具体异常码
     */
    protected int code;

    public RequestException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public RequestException() {
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
    public RequestException newInstance(String msgFormat, Object... args) {
        return new RequestException(this.code, msgFormat, args);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    public RequestException(String message) {
        super(message);
    }
}
