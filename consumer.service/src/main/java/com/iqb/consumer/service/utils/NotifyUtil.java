/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月27日 下午7:17:50
 * @version V1.0
 */

package com.iqb.consumer.service.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.enums.NotifyTypeEnum;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.domain.NotifyRecord;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class NotifyUtil {
    protected static final Logger logger = LoggerFactory.getLogger(NotifyUtil.class);
    private static final String UTF_8 = "utf-8";

    public static String formatInstInfo(InstallmentInfo installmentInfo, String NOTITY_CREATEBILL_URL) {
        String msg = JSONObject.toJSONString(installmentInfo);
        try {
            msg = URLEncoder.encode(Base64.encodeBase64String(msg.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("==>UnsupportedEncodingException:", e);
        }
        final NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNotifyType(NotifyTypeEnum.CREATEINSTDETAIL.getValue());
        notifyRecord.setMerchantNo(installmentInfo.getMerchantNo());// 商户号
        notifyRecord.setMerchantOrderNo(installmentInfo.getOrderId());// 订单号
        notifyRecord.setUrl(NOTITY_CREATEBILL_URL + "/install/generateInstDetails?msg=" + msg);
        notifyRecord.setMsg(msg);
        return JSONObject.toJSONString(notifyRecord);
    }

    public static String formatLSInstInfo(InstallmentInfo installmentInfo, String NOTITY_CREATEBILL_URL) {
        String msg = JSONObject.toJSONString(installmentInfo);
        try {
            msg = URLEncoder.encode(Base64.encodeBase64String(msg.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("==>UnsupportedEncodingException:", e);
        }
        final NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNotifyType(NotifyTypeEnum.CREATEINSTDETAIL.getValue());
        notifyRecord.setMerchantNo(installmentInfo.getMerchantNo());// 商户号
        notifyRecord.setMerchantOrderNo(installmentInfo.getOrderId());// 订单号
        notifyRecord.setUrl(NOTITY_CREATEBILL_URL + "/install/genLSInstDetails?msg=" + msg);
        notifyRecord.setMsg(msg);
        return JSONObject.toJSONString(notifyRecord);
    }

    public static String formatMortgageInfo(InstallmentInfo installmentInfo, String NOTITY_CREATEBILL_URL) {
        String msg = JSONObject.toJSONString(installmentInfo);
        try {
            msg = URLEncoder.encode(Base64.encodeBase64String(msg.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("==>UnsupportedEncodingException:", e);
        }
        final NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNotifyType(NotifyTypeEnum.CREATEINSTDETAIL.getValue());
        notifyRecord.setMerchantNo(installmentInfo.getMerchantNo());// 商户号
        notifyRecord.setMerchantOrderNo(installmentInfo.getOrderId());// 订单号
        notifyRecord.setUrl(NOTITY_CREATEBILL_URL + "/install/genMortgageDetails?msg=" + msg);
        notifyRecord.setMsg(msg);
        return JSONObject.toJSONString(notifyRecord);
    }

    public static String formatBill(InstallmentInfo installmentInfo, String NOTITY_CREATEBILL_URL) {
        String msg = JSONObject.toJSONString(installmentInfo);
        try {
            msg = URLEncoder.encode(Base64.encodeBase64String(msg.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("==>UnsupportedEncodingException:", e);
        }
        final NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNotifyType(NotifyTypeEnum.CREATEBILL.getValue());
        notifyRecord.setMerchantNo(installmentInfo.getMerchantNo());// 商户号
        notifyRecord.setMerchantOrderNo(installmentInfo.getOrderId());// 订单号
        notifyRecord.setUrl(NOTITY_CREATEBILL_URL + "/bill/generatedBills?msg=" + msg);
        notifyRecord.setMsg(msg);
        return JSONObject.toJSONString(notifyRecord);
    }

    public static String formatLSBill(InstallmentInfo installmentInfo, String NOTITY_CREATEBILL_URL) {
        String msg = JSONObject.toJSONString(installmentInfo);
        try {
            msg = URLEncoder.encode(Base64.encodeBase64String(msg.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("==>UnsupportedEncodingException:", e);
        }
        final NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNotifyType(NotifyTypeEnum.CREATEBILL.getValue());
        notifyRecord.setMerchantNo(installmentInfo.getMerchantNo());// 商户号
        notifyRecord.setMerchantOrderNo(installmentInfo.getOrderId());// 订单号
        notifyRecord.setUrl(NOTITY_CREATEBILL_URL + "/bill/genLSBills?msg=" + msg);
        notifyRecord.setMsg(msg);
        return JSONObject.toJSONString(notifyRecord);
    }

    public static String formatMortgageBill(InstallmentInfo installmentInfo, String NOTITY_CREATEBILL_URL) {
        String msg = JSONObject.toJSONString(installmentInfo);
        try {
            msg = URLEncoder.encode(Base64.encodeBase64String(msg.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("==>UnsupportedEncodingException:", e);
        }
        final NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNotifyType(NotifyTypeEnum.CREATEBILL.getValue());
        notifyRecord.setMerchantNo(installmentInfo.getMerchantNo());// 商户号
        notifyRecord.setMerchantOrderNo(installmentInfo.getOrderId());// 订单号
        notifyRecord.setUrl(NOTITY_CREATEBILL_URL + "/bill/genMortgageBills?msg=" + msg);
        notifyRecord.setMsg(msg);
        return JSONObject.toJSONString(notifyRecord);
    }

    public static String formatSMS(Object object, String NOTITY_CREATEBILL_URL) {
        String msg = JSONObject.toJSONString(object);
        try {
            msg = URLEncoder.encode(Base64.encodeBase64String(msg.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("==>UnsupportedEncodingException:", e);
        }
        final NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNotifyType(NotifyTypeEnum.SMS.getValue());
        notifyRecord.setMerchantNo(null);// 商户号
        notifyRecord.setMerchantOrderNo(null);// 订单号
        notifyRecord.setUrl(NOTITY_CREATEBILL_URL + "/notice/noticeOpenAccount?msg=" + msg);
        notifyRecord.setMsg(msg);
        return JSONObject.toJSONString(notifyRecord);
    }

    public static String fromatRepaySMS(Object object, String NOTITY_CREATEBILL_URL) {
        String msg = JSONObject.toJSONString(object);
        try {
            msg = URLEncoder.encode(Base64.encodeBase64String(msg.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("==>UnsupportedEncodingException:", e);
        }
        final NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setNotifyType(NotifyTypeEnum.REPAYSMS.getValue());
        notifyRecord.setMerchantNo(null);// 商户号
        notifyRecord.setMerchantOrderNo(null);// 订单号
        notifyRecord.setUrl(NOTITY_CREATEBILL_URL + "/notice/noticeRepay?msg=" + msg);
        notifyRecord.setMsg(msg);
        return JSONObject.toJSONString(notifyRecord);
    }

}
