/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月9日 上午10:16:30
 * @version V1.0
 */

package com.iqb.consumer.virtual.account.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.data.layer.mysql.bean.acc.OpenInfo;
import com.iqb.consumer.data.layer.mysql.bean.sys.SysSmsConfig;
import com.iqb.consumer.service.account.dto.NoticeBeanDto;
import com.iqb.consumer.service.sms.RemindSmsBean;
import com.iqb.consumer.service.sms.SendSmsService;
import com.iqb.consumer.service.sys.SysSmsConfigService;
import com.iqb.consumer.virtual.account.config.AccountConifg;
import com.iqb.consumer.virtual.account.constant.Constant;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    protected static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
    private static final String UTF_8 = "utf-8";

    @Resource
    private SendSmsService sendSmsService;
    @Resource
    private AccountConifg accountConifg;
    @Resource
    private SysSmsConfigService sysSmsConfigService;

    /**
     * 账单短信通知
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeBill", method = RequestMethod.GET)
    public Object noticeBill(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        return result;
    }

    /**
     * 通知用户还款(7,3,1)
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeRepay", method = RequestMethod.GET)
    public Object noticeRepay(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String msgBase64 = null;
        try {
            msgBase64 = URLDecoder.decode(request.getParameter("msg"), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("账单生成参数[msg]编码异常");
        }
        String json = new String(Base64.decodeBase64(msgBase64));
        List<RemindSmsBean> list = JSONObject.parseArray(json, RemindSmsBean.class);
        SysSmsConfig sysSmsConfig = null;
        String msg = null;
        for (RemindSmsBean remindSmsBean : list) {
            try {
                if (1 == remindSmsBean.getType()) {// 提醒还款
                    msg = accountConifg.getSmsRepayInfo();
                    msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                    msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                    msg =
                            msg.replaceAll("repayAmount",
                                    BigDecimalUtil.divKeep(remindSmsBean.getRepayAmount(), new BigDecimal(100)) + "")
                                    .replaceAll("regId", remindSmsBean.getRegId())
                                    .replaceAll("repayMonth", remindSmsBean.getRepayMonth())
                                    .replaceAll("repayDay", remindSmsBean.getRepayDay());
                } else {// 逾期催收
                    msg = accountConifg.getSmsOverDueInfo();
                    msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                    msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                    msg =
                            msg.replaceAll("repayAmount",
                                    BigDecimalUtil.divKeep(remindSmsBean.getRepayAmount(), new BigDecimal(100)) + "")
                                    .replaceAll(
                                            "overDueAmount",
                                            BigDecimalUtil.divKeep(remindSmsBean.getOverDueAmount(),
                                                    new BigDecimal(100))
                                                    + "")
                                    .replaceAll("regId", remindSmsBean.getRegId());
                }
                sysSmsConfig = sysSmsConfigService.getSmsChannelByOpenId(remindSmsBean.getOpenId());
                sendSmsService.sendSms(sysSmsConfig, remindSmsBean.getRegId(), msg);
            } catch (Exception e) {
                logger.error("用户:{},发送短信发送异常", remindSmsBean.getRegId(), e);
            }
        }
        result.put("retCode", Constant.SUCCESS);
        result.put("msg", "短信通知完毕");
        return result;
    }

    /**
     * 分期短信通知
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeInstall", method = RequestMethod.GET)
    public Object noticeInstall(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String msgBase64 = null;
        try {
            msgBase64 = URLDecoder.decode(request.getParameter("msg"), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("账单生成参数[msg]编码异常");
        }
        String json = new String(Base64.decodeBase64(msgBase64));
        // installInfo=账户regId于sysTime请求分期付款￥amount元。您的分期期数为instalmentTerms期，每期分摊金额￥termsAmount元，最后一期分摊金额￥lastTermAmount元。当前欠款为currBal元。
        NoticeBeanDto noticeBeanDto = JSONObject.parseObject(json, NoticeBeanDto.class);
        try {
            String msg = accountConifg.getSmsInstallInfo();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分ss秒");
                msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                msg =
                        msg.replaceAll("amount", BigDecimalUtil.narrow(noticeBeanDto.getSumShareAmount()) + "")
                                .replaceAll("regId", noticeBeanDto.getRegID()).replaceAll("sysTime",
                                        sdf.format(new Date()));
                msg =
                        msg.replaceAll("instalmentTerms", noticeBeanDto.getRealPeriods() + "")
                                .replaceAll(
                                        "termsAmount", BigDecimalUtil.narrow(noticeBeanDto.getFirstShareAmount()) + "")
                                .replaceAll(
                                        "lastTermAmount",
                                        BigDecimalUtil.narrow(noticeBeanDto.getLastShareAmount()) + "");
                msg = msg.replaceAll("currBal", "0");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // if (1 == noticeBeanDto.getPublicNo()) {// 花的花世界
            // sendSmsService.sendSmsFHH(noticeBeanDto.getRegID(), msg);
            // } else {// 其他公众号
            // sendSmsService.sendSms(noticeBeanDto.getRegID(), msg);
            // }
            result.put("retCode", Constant.SUCCESS);
            result.put("msg", "短信通知完毕");
        } catch (Exception e) {
            logger.error("用户:{}开户短信发送出现异常", noticeBeanDto.getRegID(), e);
            result.put("retCode", Constant.ERROR);
            result.put("msg", "短信通知异常");
        }
        return result;
    }

    /**
     * 开户短信通知
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeOpenAccount", method = RequestMethod.GET)
    public Object noticeOpenAccount(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String msgBase64 = null;
        try {
            msgBase64 = URLDecoder.decode(request.getParameter("msg"), UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("账单生成参数[msg]编码异常");
        }
        String json = new String(Base64.decodeBase64(msgBase64));
        OpenInfo openInfo = JSONObject.parseObject(json, OpenInfo.class);
        try {
            String msg = accountConifg.getSmsOpenAcc();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                msg = new String(msg.getBytes("ISO-8859-1"), "UTF-8");
                msg =
                        msg.replaceAll("regId", openInfo.getRegId()).replaceAll("openDateTime",
                                sdf.format(new Date()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            logger.info("开户短信为:{}", msg);
            SysSmsConfig sysSmsConfig = sysSmsConfigService.getSmsChannelByOpenId(openInfo.getPid() + "");
            // sendSmsService.sendSms(sysSmsConfig, openInfo.getRegId(), msg);
            result.put("retCode", Constant.SUCCESS);
            result.put("msg", "短信通知完毕");
        } catch (Exception e) {
            logger.error("用户:{}开户短信发送出现异常", openInfo.getRegId(), e);
            result.put("retCode", Constant.ERROR);
            result.put("msg", "短信通知异常");
        }
        return result;
    }
}
