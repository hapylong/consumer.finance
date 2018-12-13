package com.iqb.consumer.data.layer.mysql.dao.acc;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.bean.acc.AccBaseInfo;
import com.iqb.consumer.data.layer.mysql.bean.acc.OpenInfo;
import com.iqb.consumer.data.layer.mysql.dao.acc.OpenInfoDao;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

public class OpenInfoDaoTest extends AbstractRepositoryTest {

    @Resource
    private OpenInfoDao openInfoDao;

    @Test
    public void testInsert() {
        OpenInfo openInfo = new OpenInfo();
        openInfo.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        openInfo.setBid(1);
        openInfo.setPid("101");
        openInfo.setBankCardNo("622848456321564564");
        openInfo.setRegId("18600005555");;
        openInfo.setOpenDay(new Date());
        long i = openInfoDao.insert(openInfo);
        System.out.println("结果：" + i);
    }

    @Test
    public void testUpdate() {
        OpenInfo openInfo = new OpenInfo();
        openInfo.setUuid("90902f68a44a42379ac0de69d1ace65d");
        openInfo.setBid(1);
        openInfo.setPid("101");
        openInfo.setBankCardNo("622848456321564565");
        openInfo.setRegId("18600005556");
        // openInfo.setOpenDay(new Date());
        long i = openInfoDao.update(openInfo);
        System.out.println("结果：" + i);
    }

    @Test
    public void testGetByIdNoAndPid() {
        OpenInfo openInfo = openInfoDao.getByCondition(null, "362204198503147412", null, 1);
        System.out.println(openInfo.getBankCardNo());
        System.out.println(openInfo.getUuid());
    }

    @Test
    public void testListByRegId() {
        List<OpenInfo> list = openInfoDao.listByCondition("15117923307", null, 0);
        System.out.println(list);
    }

}
