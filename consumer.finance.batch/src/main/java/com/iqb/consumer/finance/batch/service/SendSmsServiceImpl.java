/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 下午1:42:44
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.finance.batch.bean.InstallmentBillInfo;
import com.iqb.consumer.finance.batch.bean.RemindSmsBean;
import com.iqb.consumer.finance.batch.cache.KVCache;
import com.iqb.consumer.finance.batch.config.MsgConfig;
import com.iqb.consumer.finance.batch.config.SysSmsConfig;
import com.iqb.consumer.finance.batch.constant.AccountConstant.RedisLockConstant;
import com.iqb.consumer.finance.batch.constant.AccountConstant.RemindSmsConstant;
import com.iqb.consumer.finance.batch.constant.AccountConstant.RemindSmsTypeConstant;
import com.iqb.consumer.finance.batch.dao.InstallmentBillInfoDao;
import com.iqb.consumer.finance.batch.util.DateUtil;
import com.iqb.consumer.finance.batch.util.HttpsClientUtil;
import com.iqb.consumer.finance.batch.util.NotifyUtil;
import com.iqb.consumer.finance.batch.util.StringUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class SendSmsServiceImpl implements SendSmsService {

    /** 日志 **/
    private static final Logger logger = LoggerFactory.getLogger(SendSmsServiceImpl.class);

    @Resource
    private InstallmentBillInfoDao installmentBillInfoDao;
    @Resource
    private JmsTemplate notifyJmsTemplate;
    @Resource
    private MsgConfig msgConfig;
    @Resource
    private KVCache<Integer> lockLockedCache;

    @Override
    public String sendSms(SysSmsConfig sysSmsConfig, String regId, String msg) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        try {
            URI base = new URI(sysSmsConfig.getSmsUrl(), false);
            method.setURI(new URI(base, "HttpBatchSendSM", false));
            method.setQueryString(new NameValuePair[] {
                    new NameValuePair("account", sysSmsConfig.getSmsName()),
                    new NameValuePair("pswd", sysSmsConfig.getSmsPswd()),
                    new NameValuePair("mobile", regId),
                    new NameValuePair("needstatus", sysSmsConfig.getSmsNeedStatus() == 1 ? "true" : "false"),
                    new NameValuePair("msg", msg),
                    new NameValuePair("product", null),
                    new NameValuePair("extno", null),
            });
            int result = client.executeMethod(method);
            if (result == HttpStatus.SC_OK) {
                InputStream in = method.getResponseBodyAsStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return URLDecoder.decode(baos.toString(), "UTF-8");
            } else {
                throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
            }
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public Object smsRemind(JSONObject objs, Integer i) {
        if (RedisLockConstant.BIZ_LOCKED_REPAY_SMS_COMPLETE.equals(lockLockedCache
                .get(RedisLockConstant.BIZ_LOCKED_REPAY_SMS_KEY))) {
            return -1;
        }
        lockLockedCache.put(RedisLockConstant.BIZ_LOCKED_REPAY_SMS_KEY,
                RedisLockConstant.BIZ_LOCKED_REPAY_SMS_COMPLETE,
                Integer.parseInt(RedisLockConstant.BIZ_LOCKED_REPAY_SMS_TIMEOUT));
        if (CollectionUtils.isEmpty(objs)) {
            objs = new JSONObject();
        }
        switch (i) {
        /** 提醒还款短信 **/
            case 1:
                return this.repayRemindSms(objs.getString("wechatNo"), objs.getString("merchantNo"),
                        objs.getString("dealDate"), objs.get("dayAdv"));

            case 2:
                return this.repayRemindSms(objs.getString("wechatNo"), objs.getString("merchantNo"),
                        objs.getString("dealDate"), objs.get("dayAdv"));
        }
        return 0;
    }

    /**
     * 逾期账单还款短信提醒
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年6月12日
     */
    @Override
    public Integer billOverdueMSGRemind(Integer type) {
        if (type == null
                || type != RemindSmsTypeConstant.COM_IQIANBANG_SMS_TYPE_03) {
            return -1;
        }
        List<InstallmentBillInfo> billList = installmentBillInfoDao
                .getOverdueBillListByDay(RemindSmsTypeConstant.COM_IQIANBANG_SMS_TYPE_03_OVERDUE_DAY);
        if (billList == null || billList.size() == 0) {
            return 0;
        }

        List<RemindSmsBean> remindSmsList = new ArrayList<RemindSmsBean>();
        RemindSmsBean remindSmsBean;

        /** 封装队列短信list **/
        for (InstallmentBillInfo installmentBillInfo : billList) {
            // 校验车辆状态是否已入库，已入库则不发送短信提醒
            if (!validateCarStatusStoraged(installmentBillInfo.getOrderId())) {
                remindSmsBean = new RemindSmsBean();
                remindSmsBean.setOpenId(installmentBillInfo.getOpenId());
                remindSmsBean.setOverDueAmount(installmentBillInfo.getCurRepayOverdueInterest());
                remindSmsBean.setRegId(installmentBillInfo.getSmsMobile() != null
                        ? installmentBillInfo.getSmsMobile()
                        : installmentBillInfo.getRegId());
                remindSmsBean.setRepayAmount(installmentBillInfo.getCurRepayAmt());
                remindSmsBean.setRepayDay(DateUtil.getDay(installmentBillInfo.getLastRepayDate()));
                remindSmsBean.setRepayMonth(DateUtil.getMonth(installmentBillInfo.getLastRepayDate()));
                remindSmsBean.setType(Integer.parseInt(RemindSmsConstant.OverdueRemindSms));
                remindSmsList.add(remindSmsBean);
            }

        }
        this.pushRemindSmsToQ(remindSmsList);
        return remindSmsList.size();
    }

    /**
     * 
     * Description: 处理发短息逻辑
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年1月11日 下午2:05:33
     */
    private Object repayRemindSms(String wechatNo, String merchantNo, String dealDateStr, Object dayAdv) {
        int i = 0;
        try {
            Long startT = System.currentTimeMillis();
            if (StringUtil.isEmpty(dealDateStr)) {
                /** 每日调度 **/
                dealDateStr = DateUtil.getDateString(new Date(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            }
            if (dayAdv == null) {
                logger.error("传入间隔日期为空！");
                return 0;
            }
            logger.info("还款短信调度服务开始：" + startT);
            String[] dayAdvs = (String[]) dayAdv;
            Date dealDate = DateUtil.parseDate(dealDateStr, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            for (int j = 0; j < dayAdvs.length; j++) {
                Date repayDate = DateUtil.addDayFromDate(dealDate, Integer.parseInt(dayAdvs[j]));
                String redisK = RedisLockConstant.BIZ_LOCKED_REPAY_SMS_KEY + j;
                if (RedisLockConstant.BIZ_LOCKED_REPAY_SMS_COMPLETE.equals(lockLockedCache.get(redisK))) {
                    return -1;
                }
                lockLockedCache.put(redisK, RedisLockConstant.BIZ_LOCKED_REPAY_SMS_COMPLETE,
                        Integer.parseInt(RedisLockConstant.BIZ_LOCKED_REPAY_SMS_TIMEOUT));
                i = i + this.repayRemindSmsWithDate(wechatNo, merchantNo, repayDate);
            }
            Long endT = System.currentTimeMillis();
            logger.info("还款短信调度服务完成：" + endT);
            logger.info("还款短信调度服务完成所花时间：" + (endT - startT) + "毫秒");
        } catch (Exception e) {
            logger.error("还款短信调度服务异常：", e);
        }
        return i;
    }

    /**
     * 
     * Description: 提醒还款短信
     * 
     * @param
     * @return int
     * @throws
     * @Author wangxinbang Create Date: 2017年1月11日 下午2:44:08
     */
    private Integer repayRemindSmsWithDate(String wechatNo, String merchantNo, Date repayDate) {
        /** 获取需要发送短信的账单列表 **/
        List<InstallmentBillInfo> billList =
                installmentBillInfoDao.getRepayRemindBillList(wechatNo, merchantNo, repayDate);
        List<RemindSmsBean> remindSmsList = new ArrayList<RemindSmsBean>();
        RemindSmsBean remindSmsBean;
        if (billList == null || billList.size() == 0) {
            return 0;
        }
        logger.info("--待还款短信提醒列表---{}",JSONObject.toJSONString(remindSmsList));
        /** 封装队列短信list **/
        for (InstallmentBillInfo installmentBillInfo : billList) {
            // 校验车辆状态是否已入库，已入库则不发送短信提醒
            if (!validateCarStatusStoraged(installmentBillInfo.getOrderId())) {
                remindSmsBean = new RemindSmsBean();
                remindSmsBean.setOpenId(installmentBillInfo.getOpenId());
                remindSmsBean.setOverDueAmount(installmentBillInfo.getCurRepayOverdueInterest());
                remindSmsBean.setRegId(installmentBillInfo.getSmsMobile() != null
                        ? installmentBillInfo.getSmsMobile()
                        : installmentBillInfo.getRegId());
                remindSmsBean.setRepayAmount(installmentBillInfo.getCurRepayAmt());
                remindSmsBean.setRepayDay(DateUtil.getDay(installmentBillInfo.getLastRepayDate()));
                remindSmsBean.setRepayMonth(DateUtil.getMonth(installmentBillInfo.getLastRepayDate()));
                remindSmsBean.setType(Integer.parseInt(RemindSmsConstant.RepayRemindSms));
                remindSmsList.add(remindSmsBean);
            }
        }

        this.pushRemindSmsToQ(remindSmsList);
        return billList.size();
    }

    /**
     * 
     * Description: 推送短信列表
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年1月11日 下午3:49:33
     */
    private void pushRemindSmsToQ(final List<RemindSmsBean> remindSmsList) {
        /** 短信列表过长则截取并分多次发送  **/
        Integer startNum = 0;
        Integer endNum = 15;
        Integer perNum = 15;
        Integer smsListSize = remindSmsList.size();
        while(startNum < smsListSize){
            endNum = endNum > smsListSize ? smsListSize : endNum;
            final List<RemindSmsBean> sendList =  remindSmsList.subList(startNum, endNum);
            startNum = endNum;
            endNum = startNum + perNum;
            notifyJmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(NotifyUtil.fromatRepaySMS(sendList, msgConfig.getBaseUrl()));
                }
            });
        }
    }

    /**
     * 
     * Description:校验车辆状态是否已入库(status==30)
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月29日
     */
    @SuppressWarnings("static-access")
    public boolean validateCarStatusStoraged(String orderId) {
        int status = 0;
        JSONObject sendReqJson = new JSONObject();
        sendReqJson.put("orderId", orderId);
        String resultStr =
                HttpsClientUtil.getInstance().doPost(msgConfig.getQueryManagecarInfoUrl(),
                        sendReqJson.toJSONString(), "UTF-8");
        JSONObject json = JSONObject.parseObject(resultStr);
        String retCode = json.getString("retCode");
        if (retCode.equals("00000000")) {
            String iqbResult = json.getString("iqbResult");
            JSONObject resultJson = JSONObject.parseObject(iqbResult);
            String retValue = resultJson.getString("result");
            String code = resultJson.getString("code");
            if (code != null && code.equals("000000")) {
                if (!StringUtil.isNull(retValue)) {
                    JSONObject mangerCarInfo = JSONObject.parseObject(retValue);
                    status = mangerCarInfo.getIntValue("status");
                }
            }
        }
        if (status == 30) {
            return true;
        }
        return false;
    }
}
