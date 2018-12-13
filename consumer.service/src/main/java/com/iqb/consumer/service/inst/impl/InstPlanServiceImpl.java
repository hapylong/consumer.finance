/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月9日 下午2:29:33
 * @version V1.0
 */
package com.iqb.consumer.service.inst.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.utils.BeanUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentPlan;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentPlanDao;
import com.iqb.consumer.service.inst.IInstPlanService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class InstPlanServiceImpl implements IInstPlanService {

    @Resource
    private InstallmentPlanDao installmentPlanDaoImpl;

    @Override
    public InstallmentPlan getByPlanId(long planId) {
        return installmentPlanDaoImpl.getByPlanId(planId);
    }

    @Override
    public long deleteById(JSONObject objs) {
        this.checkEmpty(objs);
        return installmentPlanDaoImpl.deleteById(Integer.parseInt(objs.getString("id")));
    }

    @Override
    public long updateById(JSONObject objs) {
        this.checkEmpty(objs);
        InstallmentPlan installmentPlan = BeanUtil.mapToBean(objs, InstallmentPlan.class);
        return installmentPlanDaoImpl.update(installmentPlan);
    }

    @Override
    public long insert(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        InstallmentPlan installmentPlan = BeanUtil.mapToBean(objs, InstallmentPlan.class);
        return installmentPlanDaoImpl.insert(installmentPlan);
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
