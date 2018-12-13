/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 下午3:28:14
 * @version V1.0
 */
package com.iqb.consumer.service.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iqb.consumer.data.layer.mysql.bean.sys.SysSmsConfig;
import com.iqb.consumer.data.layer.mysql.dao.sys.SysSmsConfigDao;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class SysSmsConfigServiceImpl implements SysSmsConfigService {

    @Resource
    private SysSmsConfigDao sysSmsConfigDao;

    @Override
    public SysSmsConfig getSmsChannelByOpenId(String openId) {
        return sysSmsConfigDao.getSmsChannelByOpenId(openId);
    }
}
