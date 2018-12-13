package com.iqb.consumer.data.layer.mysql.dao.product;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.data.layer.mysql.bean.product.LockRepayDate;
import com.iqb.consumer.data.layer.mysql.dao.product.LockRepayDateDao;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

public class LockRepayDateDaoTest extends AbstractRepositoryTest {

    @Resource
    private LockRepayDateDao lockRepayDateDao;

    @Test
    public void testGet() {
        LockRepayDate lockRepayDate = lockRepayDateDao.getById(1);
        System.out.println(lockRepayDate.getLockDays());
    }

}
