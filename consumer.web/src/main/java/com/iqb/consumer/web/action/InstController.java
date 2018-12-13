/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月18日 下午3:52:23
 * @version V1.0
 */
package com.iqb.consumer.web.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.web.config.ParamConfig;
import com.iqb.consumer.web.exception.BizException;
import com.iqb.consumer.web.util.Msg;
import com.iqb.consumer.web.util.RequestUtil;
import com.iqb.consumer.web.util.StringUtil;
import com.iqb.consumer.web.util.http.SimpleHttpUtils;
import com.iqb.consumer.web.util.sign.EncryptUtils;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@RestController
@RequestMapping("/inst")
public class InstController {

    @Resource
    private ParamConfig paramConfig;
    @Autowired
    private EncryptUtils encryptUtils;
    private static final Logger logger = LoggerFactory.getLogger(InstController.class);

    private ThreadPoolTaskExecutor executor;

    private InstCreateThreadTask task;

    @ResponseBody
    @RequestMapping(value = "/go2Inst.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object go2Inst(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("------");
        return null;
    }

    /**
     * 分期 { "regId": "手机号", "openId": "行业号", "orderId": "订单号", "merchantNo": "商户号", "orderDate":
     * "订单日期", "beginDate": "起息日", "installSumAmt": "分期总金额", "installAmt": "分期金额", "installTerms":
     * "分期期数", "planId": "计划ID" }
     * 
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/fenqi.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object fenqi(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("分期:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/install/paymentByInstll",
                            params);
            return JSONObject.parse(resultStr);
        } catch (BizException e) {
            logger.error("分期异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("分期异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 
     * Description: 开始启动线程 { "threadCount": "线程数量"}
     * 
     * @param
     * @return Object
     * @throws InterruptedException
     * @throws
     * @Author wangxinbang Create Date: 2016年11月24日 下午6:02:32
     */
    @ResponseBody
    @RequestMapping(value = "/startInstCreateThreadTask.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object startInstCreateThreadTask(HttpServletRequest request, HttpServletResponse response)
            throws InterruptedException {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("开始启动线程:" + JSONObject.toJSONString(objs));
        int threadCount = 40;
        if (!CollectionUtils.isEmpty(objs)) {
            if (StringUtil.isNotEmpty(objs.getString("threadCount"))) {
                threadCount = Integer.parseInt(objs.getString("threadCount"));
            }
        }

        if (this.executor == null) {
            this.executor = new ThreadPoolTaskExecutor();
        } else {
            this.task.setRunning(false);
        }
        executor.setCorePoolSize(40);
        executor.setMaxPoolSize(45);
        executor.setKeepAliveSeconds(3000);
        executor.initialize();
        for (int i = 0; i < threadCount; i++) {
            task = new InstCreateThreadTask(Integer.toString(i + 1));
            this.task.setRunning(true);
            executor.execute(task);
            Thread.sleep(2000);
        }
        return "has started";
    }

    /**
     * 
     * Description: 获取当前线程数
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年11月24日 下午6:02:17
     */
    @ResponseBody
    @RequestMapping(value = "/getExecutorActiveCount.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getExecutorActiveCount() {
        if (this.executor == null) {
            return "当前线程数量：" + 0;
        }
        return "当前线程数量：" + this.executor.getActiveCount();
    }

    /**
     * 
     * Description: 停止线程
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年11月24日 下午6:02:17
     */
    @ResponseBody
    @RequestMapping(value = "/destroyExecutor.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object destroyExecutor() {
        if (this.executor == null) {
            return "can't find!";
        }
        this.task.setRunning(false);
        return "has destroyed!";
    }

}
