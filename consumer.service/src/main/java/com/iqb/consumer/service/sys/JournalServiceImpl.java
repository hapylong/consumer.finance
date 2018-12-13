/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 上午10:09:51
 * @version V1.0
 */
package com.iqb.consumer.service.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iqb.consumer.data.layer.mysql.bean.sys.SysOperateInfo;
import com.iqb.consumer.data.layer.mysql.dao.sys.SysOperateInfoDao;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class JournalServiceImpl implements JournalService {

    @Resource
    private SysOperateInfoDao sysOperateInfoDao;

    @Override
    public long insert(SysOperateInfo sysOperateInfo) {
        return sysOperateInfoDao.insert(sysOperateInfo);
    }

}
