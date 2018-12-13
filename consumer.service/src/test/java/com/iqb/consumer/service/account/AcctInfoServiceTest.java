package com.iqb.consumer.service.account;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.service.AbstractServiceTest;

public class AcctInfoServiceTest extends AbstractServiceTest {

    @Resource
    private AcctInfoService acctInfoService;

    @Test
    public void testOpenAcct() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("regId", "15117923307");
        param.put("idNo", "340304199355621452");
        param.put("realName", "瑞文");
        param.put("bankCardNo", "622848562166632544");
        param.put("openId", "10101");
        Map<String, String> result = acctInfoService.openAccount(param);
        System.out.println("开户结果:" + result);
    }

    @Test
    public void testQueryAcct() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("regId", "15117923307");
        param.put("openId", "10101");
        Map<String, String> result = acctInfoService.queryAccount(param);
        System.out.println("查询结果:" + result);
    }

    @Test
    public void tesDestroyAcct() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("regId", "13311113333");
        param.put("openId", "101");
        param.put("idNo", "110121199101020131");
        param.put("flag", "2");
        Map<String, String> result = acctInfoService.destroyAccount(param);
        System.out.println("查询结果:" + result);
    }

    @Test
    public void isOpenIdEnabled() {
        System.out.println("查询结果:" + acctInfoService.isOpenIdEnabled("2"));
    }
}
