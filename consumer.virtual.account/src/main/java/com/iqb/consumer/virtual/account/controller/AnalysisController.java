package com.iqb.consumer.virtual.account.controller;

import java.security.InvalidParameterException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.mysql.bean.pojo.GenerallyResponseMessage;
import com.iqb.consumer.data.layer.mysql.bean.pojo.GenerallyResponseMessage.StatusEnum;
import com.iqb.consumer.service.analysis.AnalysisService;
import com.iqb.consumer.virtual.account.config.AccountConifg;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    private static final Logger log = LoggerFactory.getLogger(BillController.class);

    @Autowired
    private AnalysisService analysisServiceImpl;

    @Autowired
    private AccountConifg accountConifg;

    /**
     * 
     * Description: FINANCE-2488 以租代购业务逾期指标内部统计
     * 
     * @param
     * @return GenerallyResponseMessage
     * @throws @Author adam Create Date: 2017年10月20日 上午11:36:03
     */
    @ResponseBody
    @RequestMapping(value = {"/getRoverDueList/{isPage}"}, method = {RequestMethod.POST})
    public GenerallyResponseMessage getRoverDueList(
            @RequestBody JSONObject requestMessage, @PathVariable Boolean isPage, HttpServletRequest request) {
        GenerallyResponseMessage responseMessage = new GenerallyResponseMessage();
        JSONObject jo = null;
        try {
            if (isPage == null || requestMessage == null) {
                throw new InvalidParameterException();
            }
            jo = (JSONObject) SignUtil.chatDecode(requestMessage,
                    JSONObject.class, accountConifg.getPublicKey());
        } catch (Exception e) {
            log.error("invalid request Msg.", e);
            responseMessage.setStatus(StatusEnum.FAIL);
            responseMessage.setErrorMsg("请求参数解密失败.");
            return responseMessage;
        }

        try {
            Object result = analysisServiceImpl.getRoverDueList(jo, isPage);
            responseMessage.setStatus(StatusEnum.OK);
            responseMessage.setResult(result);
            return responseMessage;
        } catch (Exception e) {
            log.error("getRoverDueList error:", e);
            responseMessage.setStatus(StatusEnum.FAIL);
            responseMessage.setErrorMsg("未知异常.");
            return responseMessage;
        }
    }

    /**
     * 
     * Description: FINANCE-2575 轮动风控对内逾期剩余本金报表
     * 
     * @param
     * @return GenerallyResponseMessage
     * @throws @Author chengzhen Create Date: 2017年12月5日 19:40:42
     */
    @ResponseBody
    @RequestMapping(value = {"/getNewRoverDueList/{isPage}"}, method = {RequestMethod.POST})
    public GenerallyResponseMessage getNewRoverDueList(
            @RequestBody JSONObject requestMessage, @PathVariable Boolean isPage, HttpServletRequest request) {
        GenerallyResponseMessage responseMessage = new GenerallyResponseMessage();
        JSONObject jo = null;
        try {
            if (isPage == null || requestMessage == null) {
                throw new InvalidParameterException();
            }
            jo = (JSONObject) SignUtil.chatDecode(requestMessage,
                    JSONObject.class, accountConifg.getPublicKey());
        } catch (Exception e) {
            log.error("invalid request Msg.", e);
            responseMessage.setStatus(StatusEnum.FAIL);
            responseMessage.setErrorMsg("请求参数解密失败.");
            return responseMessage;
        }

        try {
            Object result = analysisServiceImpl.getNewRoverDueList(jo, isPage);
            responseMessage.setStatus(StatusEnum.OK);
            responseMessage.setResult(result);
            return responseMessage;
        } catch (Exception e) {
            log.error("getNewRoverDueList error:", e);
            responseMessage.setStatus(StatusEnum.FAIL);
            responseMessage.setErrorMsg("未知异常.");
            return responseMessage;
        }
    }

    /**
     * 
     * Description: FINANCE-2575 轮动风控对内逾期剩余本金报表总计
     * 
     * @param
     * @return GenerallyResponseMessage
     * @throws @Author chengzhen Create Date: 2017年12月6日 16:32:14
     */
    @ResponseBody
    @RequestMapping(value = {"/getTotalNewRoverDueList/{isPage}"}, method = {RequestMethod.POST})
    public GenerallyResponseMessage getTotalNewRoverDueList(
            @RequestBody JSONObject requestMessage, @PathVariable Boolean isPage, HttpServletRequest request) {
        GenerallyResponseMessage responseMessage = new GenerallyResponseMessage();
        JSONObject jo = null;
        try {
            if (isPage == null || requestMessage == null) {
                throw new InvalidParameterException();
            }
            jo = (JSONObject) SignUtil.chatDecode(requestMessage,
                    JSONObject.class, accountConifg.getPublicKey());
        } catch (Exception e) {
            log.error("invalid request Msg.", e);
            responseMessage.setStatus(StatusEnum.FAIL);
            responseMessage.setErrorMsg("请求参数解密失败.");
            return responseMessage;
        }

        try {
            Object result = analysisServiceImpl.getTotalNewRoverDueList(jo, isPage);
            responseMessage.setStatus(StatusEnum.OK);
            responseMessage.setResult(result);
            return responseMessage;
        } catch (Exception e) {
            log.error("getNewRoverDueList error:", e);
            responseMessage.setStatus(StatusEnum.FAIL);
            responseMessage.setErrorMsg("未知异常.");
            return responseMessage;
        }
    }

    /**
     * 
     * Description: FINANCE-2180 轮动风控对外逾期剩余本金报表总计
     * 
     * @param
     * @return GenerallyResponseMessage
     * @throws @Author chengzhen Create Date: 2017年12月6日 16:32:14
     */
    @ResponseBody
    @RequestMapping(value = {"/getNoInsideTotalNewRoverDueList/{isPage}"}, method = {RequestMethod.POST})
    public GenerallyResponseMessage getNoInsideTotalNewRoverDueList(
            @RequestBody JSONObject requestMessage, @PathVariable Boolean isPage, HttpServletRequest request) {
        GenerallyResponseMessage responseMessage = new GenerallyResponseMessage();
        JSONObject jo = null;
        try {
            if (isPage == null || requestMessage == null) {
                throw new InvalidParameterException();
            }
            jo = (JSONObject) SignUtil.chatDecode(requestMessage,
                    JSONObject.class, accountConifg.getPublicKey());
        } catch (Exception e) {
            log.error("invalid request Msg.", e);
            responseMessage.setStatus(StatusEnum.FAIL);
            responseMessage.setErrorMsg("请求参数解密失败.");
            return responseMessage;
        }

        try {
            Object result = analysisServiceImpl.getNoInsideTotalNewRoverDueList(jo, isPage);
            responseMessage.setStatus(StatusEnum.OK);
            responseMessage.setResult(result);
            return responseMessage;
        } catch (Exception e) {
            log.error("getNewRoverDueList error:", e);
            responseMessage.setStatus(StatusEnum.FAIL);
            responseMessage.setErrorMsg("未知异常.");
            return responseMessage;
        }
    }

}
