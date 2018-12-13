/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月17日 下午7:08:27
 * @version V1.0
 */
package com.iqb.consumer.service.account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccRepayHistory;
import com.iqb.consumer.data.layer.mysql.dao.acc.AccRepayHistoryDao;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class AccRepayHistoryServiceImpl implements AccRepayHistoryService {

    @Resource
    private AccRepayHistoryDao accRepayHistoryDao;

    @Override
    public long batchInsertAccRepayHistory(List<AccRepayHistory> list) {
        return accRepayHistoryDao.insert(list);
    }

    @Override
    public PageBean selectRepayHistoryByPages(Map<String, Object> params) {
        int pageNum = params.get("pageNum") == null ? 0 : Integer.parseInt(params.get("pageNum").toString());
        int numPerPage = params.get("numPerPage") == null ? 0 : Integer.parseInt(params.get("numPerPage").toString());
        PageParam pageParam = new PageParam(pageNum, numPerPage);
        return accRepayHistoryDao.listPage(pageParam, params);
    }

    @Override
    public BigDecimal getRepayAmtByOrderId(String orderId) {
        double d = accRepayHistoryDao.getRepayAmtByOrderId(orderId);
        BigDecimal result = BigDecimalUtil.div(new BigDecimal(d), new BigDecimal(100));
        return result;
    }
}
