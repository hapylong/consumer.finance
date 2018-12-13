/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月17日 下午7:02:37
 * @version V1.0
 */
package com.iqb.consumer.data.layer.mysql.dao.acc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.bean.acc.AccRepayHistory;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class AccRepayHistoryDaoTest extends AbstractRepositoryTest {

    @Resource
    private AccRepayHistoryDao accRepayHistoryDao;

    @Test
    public void testBatchInsert() {
        List<AccRepayHistory> list = new ArrayList<AccRepayHistory>();
        AccRepayHistory ac = new AccRepayHistory();
        ac.setBankCardNo("11");
        ac.setOpenId("100100");
        list.add(ac);
        accRepayHistoryDao.insert(list);
    }

    @Test
    public void testListPage() {
        PageParam pageParam = new PageParam(0, 8);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("openId", "10101");
        paramMap.put("regId", "15117923307");
        PageBean pageBean = accRepayHistoryDao.listPage(pageParam, paramMap);
        System.out.println(pageBean.getRecordList());
    }

    @Test
    public void testGetRepayAmtByOrderId() {
        System.out.println(accRepayHistoryDao.getRepayAmtByOrderId("20161129008"));
    }
}
