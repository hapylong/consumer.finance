package com.iqb.consumer.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
 * Description: 测试
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月18日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@RestController
@RequestMapping("/test")
public class TestController {

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
     * Description: 测试 { "id": "测试id"}
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年11月18日 上午11:13:47
     */
    @ResponseBody
    @RequestMapping(value = "/tt.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Object tt(HttpServletRequest request, HttpServletResponse response) {
        JSONObject objs = RequestUtil.toJSON(request);
        logger.info("测试:" + JSONObject.toJSONString(objs));
        try {
            Map<String, Object> params = encryptUtils.encrypt(objs);
            String resultStr =
                    SimpleHttpUtils.httpPost(paramConfig.getRequestBaseUrl() + "/product/overDueInterest/deleteById",
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

    public static void main(String[] args) {
        // ApplicationContext ac = new
        // ClassPathXmlApplicationContext("classpath:spring-config.xml");
        // ParamConfig paramConfig = (ParamConfig) ac.getBean("paramConfig");
        // paramConfig.getCommonPublicKey();
        // System.out.println(paramConfig.getCommonPrivateKey());

    }

}
