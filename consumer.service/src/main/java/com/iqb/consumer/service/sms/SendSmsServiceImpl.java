/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月1日 下午1:42:44
 * @version V1.0
 */

package com.iqb.consumer.service.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant.RedisLockConstant;
import com.iqb.consumer.common.constant.AccountConstant.RemindSmsConstant;
import com.iqb.consumer.common.constant.AccountConstant.RemindSmsTypeConstant;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.HttpsClientUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.sys.SysSmsConfig;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentBillInfoDao;
import com.iqb.consumer.finance.cache.kv.KVCache;
import com.iqb.consumer.service.conf.MsgConfig;
import com.iqb.consumer.service.utils.NotifyUtil;

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
        logger.info("---发送地址:{}---发送信息--{}--给手机号--{}", sysSmsConfig.getSmsUrl(), msg, regId);
        URL url = null;
        String postContent = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("account", sysSmsConfig.getSmsName());
            map.put("password", sysSmsConfig.getSmsPswd());
            map.put("msg", msg);
            map.put("phone", regId);
            map.put("report", "true");
            postContent = JSONObject.toJSONString(map);
            url = new URL(sysSmsConfig.getSmsUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            httpURLConnection.setConnectTimeout(10000);// 连接超时 单位毫秒
            httpURLConnection.setReadTimeout(10000);// 读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            // PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // printWriter.write(postContent);
            // printWriter.flush();

            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();

            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                // 开始获取数据
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            remindSmsBean = new RemindSmsBean();
            remindSmsBean.setOpenId(installmentBillInfo.getOpenId());
            remindSmsBean.setOverDueAmount(installmentBillInfo.getCurRepayOverdueInterest());
            remindSmsBean.setRegId(installmentBillInfo.getRegId());
            remindSmsBean.setRepayAmount(installmentBillInfo.getCurRepayAmt());
            remindSmsBean.setRepayDay(DateUtil.getDay(installmentBillInfo.getLastRepayDate()));
            remindSmsBean.setRepayMonth(DateUtil.getMonth(installmentBillInfo.getLastRepayDate()));
            remindSmsBean.setType(Integer.parseInt(RemindSmsConstant.OverdueRemindSms));
            remindSmsList.add(remindSmsBean);
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

        /** 封装队列短信list **/
        for (InstallmentBillInfo installmentBillInfo : billList) {
            remindSmsBean = new RemindSmsBean();
            remindSmsBean.setOpenId(installmentBillInfo.getOpenId());
            remindSmsBean.setOverDueAmount(installmentBillInfo.getCurRepayOverdueInterest());
            remindSmsBean.setRegId(installmentBillInfo.getRegId());
            remindSmsBean.setRepayAmount(installmentBillInfo.getCurRepayAmt());
            remindSmsBean.setRepayDay(DateUtil.getDay(installmentBillInfo.getLastRepayDate()));
            remindSmsBean.setRepayMonth(DateUtil.getMonth(installmentBillInfo.getLastRepayDate()));
            remindSmsBean.setType(Integer.parseInt(RemindSmsConstant.RepayRemindSms));
            remindSmsList.add(remindSmsBean);
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
        notifyJmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(NotifyUtil.fromatRepaySMS(remindSmsList, msgConfig.getBaseUrl()));
            }
        });
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
