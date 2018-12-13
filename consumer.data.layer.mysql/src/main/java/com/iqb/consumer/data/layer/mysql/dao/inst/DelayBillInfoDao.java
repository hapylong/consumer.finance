/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月26日 下午5:49:33
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.domain.DelayBillInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface DelayBillInfoDao extends BaseDao<DelayBillInfo> {

    List<DelayBillInfo> listBy(Map<String, Object> paramMap);

    DelayBillInfo getBy(Map<String, Object> paramMap);
}
