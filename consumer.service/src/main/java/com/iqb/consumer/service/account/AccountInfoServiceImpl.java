/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月28日 下午6:46:10
 * @version V1.0
 */

package com.iqb.consumer.service.account;

import java.math.BigDecimal;
import java.util.HashMap;
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
import org.springframework.transaction.annotation.Transactional;

import com.iqb.consumer.common.constant.AccountConstant;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.data.layer.mysql.dao.AccountInfoDao;
import com.iqb.consumer.data.layer.mysql.domain.AccountInfo;
import com.iqb.consumer.service.utils.NotifyUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class AccountInfoServiceImpl implements AccountInfoService {

    protected static final Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @Resource
    private AccountInfoDao accountInfoDao;
    @Resource
    private JmsTemplate notifyJmsTemplate;

    @Override
    public Integer getByIDCard(String idNo, String industryType) {
        return accountInfoDao.getByIDCard(idNo, industryType);
    }

    @Transactional
    @Override
    public Map<String, Object> insertAccount(final AccountInfo accountInfo, final String url) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 开户额度扩大100
        BigDecimal creditLimit = BigDecimalUtil.mul(accountInfo.getCreditLimit(), new BigDecimal(100));
        accountInfo.setCreditLimit(creditLimit);
        accountInfo.setAvailLimit(creditLimit);
        accountInfo.setAccStatus(0);
        // 开户添加记录
        accountInfoDao.insert(accountInfo);
        // 通知消息队列 发送短信(开户内部操作,不发送短信)
        // notifyJmsTemplate.send(new MessageCreator() {
        // public Message createMessage(Session session) throws JMSException {
        // return session.createTextMessage(NotifyUtil.formatSMS(accountInfo, url));
        // }
        // });
        result.put("retCode", AccountConstant.SUCCESS);
        result.put("msg", "开户成功");
        return result;
    }

    @Override
    public AccountInfo getByConUUID(String conUUID) {
        AccountInfo accountInfo = null;
        try {
            accountInfo = accountInfoDao.getByConUUID(conUUID);
        } catch (Exception e) {
            logger.error("用户:{}查询账户信息异常", conUUID, e);
        }
        return accountInfo;
    }

    @Override
    public long updateAccount(AccountInfo accountInfo) {
        long result = 0;
        try {
            result = accountInfoDao.update(accountInfo);
        } catch (Exception e) {
            logger.error("用户ID:{}查询账户信息异常", accountInfo.getId(), e);
        }
        return result;
    }

}
