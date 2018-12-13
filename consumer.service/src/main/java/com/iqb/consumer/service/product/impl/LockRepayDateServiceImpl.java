package com.iqb.consumer.service.product.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.utils.BeanUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.mysql.bean.product.LockRepayDate;
import com.iqb.consumer.data.layer.mysql.dao.product.LockRepayDateDao;
import com.iqb.consumer.service.product.LockRepayDateService;

/**
 * 锁定日期相关服务
 */
@Service
public class LockRepayDateServiceImpl implements LockRepayDateService {

    private static final Logger logger = LoggerFactory.getLogger(LockRepayDateServiceImpl.class);

    @Resource
    private LockRepayDateDao lockRepayDateDao;

    @Override
    public String getLockDays(String date, long id) {
        String result = null;
        logger.info("获取账单日传入信息:date->{},id->{}", date, id);
        if (StringUtil.isEmpty(date)) {
            logger.error("获取锁定还款日期:传入账单日期信息空值");
            return date;
        }
        LockRepayDate lockRepayDate = lockRepayDateDao.getById(id);
        if (lockRepayDate == null) {
            logger.error("获取锁定还款日期:查询锁定还款日期为空");
            return date;
        }
        Calendar cal = DateUtil.parseCalendar(date, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        switch (lockRepayDate.getLockrepaytype()) {
            case 1:// 月
                cal.add(Calendar.MONTH, lockRepayDate.getLockDays());
                break;
            case 2:// 日
                cal.add(Calendar.DATE, lockRepayDate.getLockDays());
                break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        result = sdf.format(cal.getTime());
        return result;
    }

    @Override
    public long deleteById(JSONObject objs) {
        this.checkEmpty(objs);
        return lockRepayDateDao.deleteById(Integer.parseInt(objs.getString("id")));
    }

    @Override
    public long updateById(JSONObject objs) {
        this.checkEmpty(objs);
        LockRepayDate lockRepayDate = BeanUtil.mapToBean(objs, LockRepayDate.class);
        return lockRepayDateDao.update(lockRepayDate);
    }

    @Override
    public long insert(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        LockRepayDate lockRepayDate = BeanUtil.mapToBean(objs, LockRepayDate.class);
        return lockRepayDateDao.insert(lockRepayDate);
    }

    /**
     * 
     * Description: 校验非空
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:24:53
     */
    private void checkEmpty(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        if (StringUtil.isEmpty(objs.getString("id"))) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
    }

}
