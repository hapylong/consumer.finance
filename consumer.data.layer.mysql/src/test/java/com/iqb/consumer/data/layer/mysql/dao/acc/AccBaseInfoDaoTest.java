package com.iqb.consumer.data.layer.mysql.dao.acc;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.bean.acc.AccBaseInfo;
import com.iqb.consumer.data.layer.mysql.dao.acc.AccBaseInfoDao;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

public class AccBaseInfoDaoTest extends AbstractRepositoryTest {

    @Resource
    private AccBaseInfoDao baseInfoDao;

    @Test
    public void testInsert() {
        AccBaseInfo baseInfo = new AccBaseInfo();
        baseInfo.setRealName("刘姥姥2");
        baseInfo.setIdNo("454545485511218");
        baseInfo.setStatus(1);
        long i = baseInfoDao.insert(baseInfo);
        // long i = baseInfoDao.insert(baseInfo);
        System.out.println("结果：" + i);
    }

    @Test
    public void testUpdate() {
        AccBaseInfo baseInfo = new AccBaseInfo();
        baseInfo.setRealName("刘姥姥2222");
        baseInfo.setIdNo("22112454855145648");
        baseInfo.setStatus(1);
        baseInfo.setId(3L);
        baseInfoDao.update(baseInfo);
    }

    @Test
    public void testGetByIDCard() {
        AccBaseInfo baseInfo = baseInfoDao.getByIDCard("3335664465484646", 1);
        System.out.println(baseInfo.getRealName());
        System.out.println(baseInfo.getId());
    }

}
