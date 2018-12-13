/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月17日 下午7:06:57
 * @version V1.0
 */
package com.iqb.consumer.service.account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccRepayHistory;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface AccRepayHistoryService {

    long batchInsertAccRepayHistory(List<AccRepayHistory> list);

    PageBean selectRepayHistoryByPages(Map<String, Object> params);

    BigDecimal getRepayAmtByOrderId(String orderId);
}
