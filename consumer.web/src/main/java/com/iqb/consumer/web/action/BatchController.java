package com.iqb.consumer.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.web.config.ParamConfig;
import com.iqb.consumer.web.exception.BizException;
import com.iqb.consumer.web.util.Msg;
import com.iqb.consumer.web.util.RequestUtil;
import com.iqb.consumer.web.util.http.SimpleHttpUtils;
import com.iqb.consumer.web.util.sign.EncryptUtils;

/**
 * 
 * Description:
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月21日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private ParamConfig paramConfig;

    @Autowired
    private EncryptUtils encryptUtils;

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    /**
     * 
     * Description: 批量逾期接口 { "orderId": "订单号", "instBillId": "账单号", "endDate":"日期"}
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年11月21日 上午11:01:29
     */
    @ResponseBody
    @RequestMapping(value = "/batchOverDue.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object batchOverDue(HttpServletRequest request, HttpServletResponse response) {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("批量逾期接口:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/bizSchedulerOverdue",
                            params);
            return Msg.info(resultStr);
        } catch (BizException e) {
            logger.error("测试异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("测试异常", e);
            return Msg.error(e);
        }
    }

    /**
     * 
     * Description: 复利逾期调度 { "orderId": "订单号", "instBillId": "账单号", "endDate":"日期"}
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年11月21日 下午3:32:38
     */
    @ResponseBody
    @RequestMapping(value = "/compoundBatchOverDue.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object compoundBatchOverDue(HttpServletRequest request, HttpServletResponse response) {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("测试:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/bill/calOverdueInterest4Test",
                            params);
            return Msg.info(resultStr);
        } catch (BizException e) {
            logger.error("测试异常", e);
            return Msg.error(e.getMsg());
        } catch (Exception e) {
            logger.error("测试异常", e);
            return Msg.error(e);
        }
    }

}
