package com.iqb.consumer.service.account;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iqb.consumer.common.constant.AccountConstant;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccBaseInfo;
import com.iqb.consumer.data.layer.mysql.bean.acc.OpenInfo;
import com.iqb.consumer.data.layer.mysql.dao.acc.AccBaseInfoDao;
import com.iqb.consumer.data.layer.mysql.dao.acc.OpenInfoDao;
import com.iqb.consumer.service.conf.MsgConfig;

@Service
public class AcctInfoServiceImpl implements AcctInfoService {

    protected static final Logger logger = LoggerFactory.getLogger(AcctInfoServiceImpl.class);
    @Resource
    private AccBaseInfoDao accBaseInfoDao;
    @Resource
    private OpenInfoDao openInfoDao;
    @Resource
    private JmsTemplate notifyJmsTemplate;
    @Resource
    private MsgConfig msgConfig;

    @Transactional
    @Override
    public Map<String, String> openAccount(Map<String, Object> param) {
        Map<String, String> result = new HashMap<String, String>();
        // 获取参数信息
        String regId = (String) param.get("regId");
        String pid = (String) param.get("openId");
        String idNo = (String) param.get("idNo");
        String realName = (String) param.get("realName");
        String bankCardNo = (String) param.get("bankCardNo");

        // 1.查询是否开户过
        List<OpenInfo> openInfoList = openInfoDao.listByCondition(null, idNo, 0);// 查询用户所有开户行业信息(按照身份证号查询)
        if (openInfoList == null || openInfoList.size() == 0) {// 未开户
            logger.debug("手机号为{}的用户，从未开户任何行业，开始进行开户", regId);
            // 1.插入基础信息表
            AccBaseInfo baseInfo = new AccBaseInfo();
            baseInfo.setIdNo(idNo);
            baseInfo.setRealName(realName);
            baseInfo.setStatus(AccountConstant.STATUS_OK);
            long bid = accBaseInfoDao.insert(baseInfo);
            // 2.插入开户表
            OpenInfo openInfo = new OpenInfo();
            openInfo.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
            openInfo.setBid(bid);
            openInfo.setPid(pid);
            openInfo.setRealName(realName);
            openInfo.setIdNo(idNo);
            openInfo.setBankCardNo(bankCardNo);
            openInfo.setRegId(regId);
            openInfo.setOpenDay(new Date());
            openInfo.setStatus(AccountConstant.STATUS_OK);
            openInfoDao.insert(openInfo);
        } else {
            // OpenInfo openInfo = openInfoDao.getByRegIdAndPid(regId, pid, 0);
            logger.debug("手机号为{}的用户，开户过某些行业", regId);
            OpenInfo openInfo = null;
            long bid = openInfoList.get(0).getBid();
            for (OpenInfo oi : openInfoList) {
                if (oi.getPid().equals(pid)) {
                    openInfo = oi;
                    break;
                }
            }

            if (openInfo == null) {// 未开户此行业则直接开户
                logger.debug("手机号为{}的用户，未开户次行业,开始进行开户", regId);
                // 开户子账户
                openInfo = new OpenInfo();
                openInfo.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
                openInfo.setBid(bid);
                openInfo.setPid(pid);
                openInfo.setRealName(realName);
                openInfo.setIdNo(idNo);
                openInfo.setBankCardNo(bankCardNo);
                openInfo.setRegId(regId);
                openInfo.setOpenDay(new Date());
                openInfo.setStatus(AccountConstant.STATUS_OK);
                openInfoDao.insert(openInfo);
            } else {
                switch (openInfo.getStatus()) {
                    case 1:// 已开过户
                        logger.debug("手机号为{}的用户，开户过次行业", regId);
                        result.put("retCode", AccountConstant.ERROR);
                        result.put("msg", "该账号已开户");
                        return result;
                    case 2:// 销户过，仅需update
                        logger.debug("手机号为{}的用户，销户过次行业，进行重新开户", regId);
                        openInfo.setBankCardNo(bankCardNo);
                        openInfo.setRealName(realName);
                        openInfo.setIdNo(idNo);
                        openInfo.setRegId(regId);
                        openInfo.setOpenDay(new Date());
                        openInfo.setStatus(AccountConstant.STATUS_OK);
                        openInfoDao.update(openInfo);
                        break;
                    default:// 数据有问题
                        logger.debug("手机号为{}的用户已存在开户信息并且status状态异常", regId);
                        result.put("retCode", AccountConstant.ERROR);
                        result.put("msg", "已存在开户信息并且信息异常");
                        return result;
                }
            }

        }
        /*
         * try { final OpenInfo openInfo = new OpenInfo(); openInfo.setRegId(regId);
         * openInfo.setPid(pid); // 通知消息队列 发送短信 notifyJmsTemplate.send(new MessageCreator() { public
         * Message createMessage(Session session) throws JMSException { return
         * session.createTextMessage(NotifyUtil.formatSMS(openInfo, msgConfig.getBaseUrl())); } });
         * } catch (Exception e) { logger.error("开户成功发送短信失败", e); }
         */

        result.put("retCode", AccountConstant.SUCCESS);
        result.put("msg", "开户成功");
        logger.debug("手机号为{}的用户开户成功", regId);
        return result;
    }

    @Override
    public Map<String, String> queryAccount(Map<String, Object> param) {
        Map<String, String> result = new HashMap<String, String>();
        String regId = (String) param.get("regId");
        String pid = (String) param.get("openId");
        if (!this.isOpenIdEnabled(pid)) {// 行业号不存在
            result.put("retCode", "01");
            result.put("msg", "该行业号不存在");
            return result;
        }
        OpenInfo openInfo = openInfoDao.getByCondition(regId, null, pid, AccountConstant.STATUS_OK);
        if (openInfo != null) {// 未开户
            result.put("retCode", "00");
            result.put("msg", "已开户");
        } else {
            result.put("retCode", "01");
            result.put("msg", "未开户");
        }

        return result;
    }

    @Transactional
    @Override
    public Map<String, String> destroyAccount(Map<String, Object> param) {
        Map<String, String> result = new HashMap<String, String>();
        String regId = (String) param.get("regId");
        String flag = (String) param.get("flag");
        String idNo = (String) param.get("idNo");
        String pid = (String) param.get("openId");
        if ("1".equals(flag)) {// 消除主账号
            AccBaseInfo abi = accBaseInfoDao.getByIDCard(idNo, AccountConstant.STATUS_OK);
            if (abi == null) {
                result.put("retCode", "01");
                result.put("msg", "不存在该用户");
                return result;
            }
            // 1.消除主账户
            AccBaseInfo baseInfo = new AccBaseInfo();
            baseInfo.setStatus(AccountConstant.STATUS_NO);
            baseInfo.setIdNo(idNo);
            accBaseInfoDao.update(baseInfo);

            // 2.消除所有字账户
            OpenInfo openInfo = new OpenInfo();
            // openInfo.setRegId(regId);
            openInfo.setIdNo(idNo);
            openInfo.setStatus(AccountConstant.STATUS_NO);
            openInfoDao.updateStatus(openInfo);
        } else if ("2".equals(flag)) {// 消除行业账号
            OpenInfo oi = openInfoDao.getByCondition(null, idNo, pid, AccountConstant.STATUS_OK);
            if (oi == null) {
                result.put("retCode", "01");
                result.put("msg", "不存在该用户");
                return result;
            }
            OpenInfo openInfo = new OpenInfo();
            openInfo.setPid(pid);
            // openInfo.setRegId(regId);
            openInfo.setIdNo(idNo);
            openInfo.setStatus(AccountConstant.STATUS_NO);
            openInfoDao.updateStatus(openInfo);
        } else {
            result.put("retCode", "01");
            result.put("msg", "传入销户类别有误");
            return result;
        }

        result.put("retCode", "00");
        result.put("retMsg", "成功销户");
        logger.debug("手机号为:{}的用户成功销户", regId);
        return result;
    }

    @Override
    public OpenInfo queryAccByParams(Map<String, Object> param) {
        String regId = (String) param.get("regId");
        String pid = (String) param.get("openId");
        return openInfoDao.getByCondition(regId, null, pid, AccountConstant.STATUS_OK);
    }

    // 查询是否存在此行业号
    @Override
    public boolean isOpenIdEnabled(String openId) {
        boolean flag = false;
        List<OpenInfo> list = openInfoDao.listOpenInfo4Base();// 行业基础信息列表
        for (OpenInfo openInfo : list) {
            if (openId.equals(openInfo.getUuid())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}
