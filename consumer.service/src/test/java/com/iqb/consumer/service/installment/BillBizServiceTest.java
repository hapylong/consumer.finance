package com.iqb.consumer.service.installment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant.DeratePenaltyConstant;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.QueryBillInfo;
import com.iqb.consumer.data.layer.mysql.dao.inst.InstallmentInfoDao;
import com.iqb.consumer.service.AbstractServiceTest;
import com.iqb.consumer.service.account.BillBizService;

public class BillBizServiceTest extends AbstractServiceTest {

    @Resource
    private BillBizService billBizService;
    @Resource
    private InstallmentInfoDao installmentInfoDao;

    @Test
    public void testCreateBill() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", "111111");
        paramMap.put("openId", "10101");
        InstallmentInfo installmentInfo = installmentInfoDao.getBy(paramMap);

        Map<String, Object> result = billBizService.createBill(installmentInfo);
        System.out.println("创建账单结果：" + result);
    }

    @Test
    public void testSelectCurrBills() {
        Object list = billBizService.selectCurrBills("18911908439", "10101");
        System.out.println(list);
    }

    @Test
    public void testSelectCost4Advance() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", "111112");
        paramMap.put("regId", "15117923307");
        Map<String, Object> result = billBizService.selectCost4Advance(paramMap);
        System.out.println("金额：" + result);
    }

    @Test
    public void testDeratePenalty() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(DeratePenaltyConstant.CUTINTEREST_KEY, new BigDecimal(20));
        paramMap.put(DeratePenaltyConstant.CUTOVERDUEINTEREST_KEY, new BigDecimal(30));
        paramMap.put(DeratePenaltyConstant.CUTFIXEDOVERDUEAMT_KEY, new BigDecimal(40));
        paramMap.put(DeratePenaltyConstant.INSTALLDETAILID_KEY, "2679932");

        Map<String, Object> result =
                billBizService.deratePenalty(JSONObject.parseObject(JSONObject.toJSONString(paramMap)));
        System.out.println("返回结果：" + JSONObject.toJSONString(result));
    }
}
