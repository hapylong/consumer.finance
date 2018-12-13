package com.iqb.consumer.data.layer.mysql.dao.inst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentDetail;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

public class InstallmentDetailTest extends AbstractRepositoryTest {

    @Resource
    private InstallmentDetailDao installmentDetailDao;

    @Test
    public void testListBy() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("installInfoId", "49");
        List<InstallmentDetail> list = installmentDetailDao.listBy(param);
        System.out.println(list);
    }

}
