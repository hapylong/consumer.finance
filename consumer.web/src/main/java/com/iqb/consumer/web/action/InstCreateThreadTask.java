package com.iqb.consumer.web.action;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.web.config.ParamConfig;
import com.iqb.consumer.web.util.DateUtil;
import com.iqb.consumer.web.util.IdCardGenerator;
import com.iqb.consumer.web.util.RandomCreditCardNumberGenerator;
import com.iqb.consumer.web.util.RandomValue;
import com.iqb.consumer.web.util.SpringBeanUtil;
import com.iqb.consumer.web.util.StringUtil;
import com.iqb.consumer.web.util.http.SimpleHttpUtils;
import com.iqb.consumer.web.util.sign.EncryptUtils;

/**
 * 
 * Description: 分期线程任务
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月24日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class InstCreateThreadTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(InstCreateThreadTask.class);

    private ParamConfig paramConfig = SpringBeanUtil.getBean(ParamConfig.class);

    private EncryptUtils encryptUtils = SpringBeanUtil.getBean(EncryptUtils.class);

    private String name;

    private Random rand = new Random();

    private static boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        InstCreateThreadTask.running = running;
    }

    public InstCreateThreadTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (true && running) {
            logger.debug("线程" + name + "正在运行！");
            logger.debug("线程" + name + "运行结果：" + this.doInstCreate());
            try {
                Thread.sleep(150000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 
     * Description: 执行分期方法
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月24日 下午4:09:06
     */
    private String doInstCreate() {
        String regId = RandomValue.getTel();
        String pid = this.randomOpenId();
        JSONObject openAccObjs = new JSONObject();
        openAccObjs.put("regId", regId);
        openAccObjs.put("openId", pid);
        openAccObjs.put("idNo", IdCardGenerator.generate());
        openAccObjs.put("realName", RandomValue.getChineseName());
        openAccObjs.put("bankCardNo", RandomCreditCardNumberGenerator.generateMasterCardNumber());
        Map<String, Object> openAccparams = encryptUtils.encrypt(openAccObjs);
        String openAccResultStr =
                SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/account/openAccount", openAccparams);
        if (StringUtil.isEmpty(openAccResultStr)) {
            return "开户失败！返回信息：" + openAccResultStr;
        }
        try {
            if (!"success".equals(JSONObject.parseObject(openAccResultStr).getString("retCode"))) {
                return "开户失败！返回信息：" + openAccResultStr;
            }
        } catch (Exception e) {
            logger.error("开户异常！返回信息：" + openAccResultStr, e);
            return "开户异常！返回信息：" + openAccResultStr;
        }

        Date d = new Date();
        JSONObject objs = new JSONObject();
        objs.put("orderId", this.getOrderNo());
        objs.put("orderDate", DateUtil.getDateString(d, DateUtil.SHORT_DATE_FORMAT_NO_DASH));
        objs.put("regId", regId);
        objs.put("beginDate", DateUtil.getDateString(d, DateUtil.SHORT_DATE_FORMAT_NO_DASH));
        objs.put("openId", pid);
        objs.put("merchantNo", "111111");
        objs.put("installSumAmt", 100000);
        objs.put("installAmt", 100000);
        objs.put("installTerms", getInstallTerms());
        objs.put("planId", 10);
        Map<String, Object> params = encryptUtils.encrypt(objs);
        String resultStr =
                SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/install/paymentByInstll", params);
        return resultStr;
    }

    /**
     * 
     * Description: 获取订单号
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月24日 下午5:28:41
     */
    private String getOrderNo() {
        return Long.toString(System.currentTimeMillis()) + String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 
     * Description: 获取分期期数
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月24日 下午5:50:57
     */
    private String getInstallTerms() {
        return String.valueOf((int) ((Math.random() * 9 + 1) + 10));
    }

    /**
     * 
     * Description: 随机生成openId
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月25日 下午6:02:19
     */
    private String randomOpenId() {
        String[] ss = new String[] {"1", "101", "10101", "10102", "102", "103", "104", "2"};
        return ss[rand.nextInt(8)];
    }

}
