package com.iqb.consumer.data.layer.mysql.dao.inst;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentBillInfo;
import com.iqb.consumer.data.layer.mysql.bean.inst.ShouldDebtDetail;
import com.iqb.consumer.data.layer.mysql.bean.inst.ToRiskBillInfo;
import com.iqb.consumer.data.layer.mysql.repository.AbstractRepositoryTest;

public class InstallmentBillInfoTest extends AbstractRepositoryTest {

    @Resource
    private InstallmentBillInfoDao instBillInfoDao;

    @Test
    public void testGet() {
        InstallmentBillInfo installmentBillInfo = instBillInfoDao.getById(1);
        System.out.println(installmentBillInfo.getRegId());
    }

    @Test
    public void testGetSumAmt() {
        BigDecimal a1 = new BigDecimal("1009000.00");
        BigDecimal a2 = new BigDecimal("1009000");
        System.out.println(a1.compareTo(a2));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", "90000001");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        params.put("list", list);
        System.out.println(instBillInfoDao.getSAmtByOrderIdAndRepayNo(params));
    }

    @Test
    public void testBatchInsert() {
        List<InstallmentBillInfo> bills = new ArrayList<InstallmentBillInfo>();
        for (int i = 0; i < 2; i++) {
            InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo();
            installmentBillInfo.setCurRealRepayamt(new BigDecimal(0));// 本期实际还款金额
            installmentBillInfo.setCurRepayAmt(new BigDecimal(1200000));// 本期应还总额
            installmentBillInfo.setCurRepayInterest(new BigDecimal(200));// 本期应还利息
            installmentBillInfo.setCurRepayOverdueInterest(new BigDecimal(0));// 本期应还逾期金额
            installmentBillInfo.setCurRepayPrincipal(new BigDecimal(0));// 本期应还本金
            installmentBillInfo.setDelayBeginDate(new Date());// 滞纳金计算时间
            installmentBillInfo.setInstallAmt(new BigDecimal(1000));// 分期金额
            installmentBillInfo.setInstallDetailId(1);// 分期明细ID
            installmentBillInfo.setInstallInfoId(1);// 分期总账号ID
            installmentBillInfo.setLastRepayDate(new Date());// 最后还款日
            installmentBillInfo.setOpenId("test0000000000001test");// 账户号
            installmentBillInfo.setOverdueDays(0);// 逾期天数
            installmentBillInfo.setPartPayment(1);// 部分还款
            installmentBillInfo.setPreInterest(new BigDecimal(20));// 每天的利息
            installmentBillInfo.setPreOverdueInterest(new BigDecimal(30));// 每日滞纳金
            installmentBillInfo.setPrePayment(1);// 提前还款
            installmentBillInfo.setPrincipal(new BigDecimal(1200000));// 本金
            installmentBillInfo.setRegId("13800001111");// 注册号
            installmentBillInfo.setRemainPrincipal(new BigDecimal(1200000));// 剩余本金
            installmentBillInfo.setRepayNo(i + 1);// 还款序号
            bills.add(installmentBillInfo);
        }
        instBillInfoDao.insert(bills);
    }

    @Test
    public void testListPage() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("status", 1);
        param.put("regId", "15117923307");
        PageParam pageParam = new PageParam(0, 3);
        PageBean pageBean = instBillInfoDao.listPage(pageParam, param);
        List<Object> recordList = pageBean.getRecordList();
        for (Object object : recordList) {
            InstallmentBillInfo ibi = (InstallmentBillInfo) object;
            System.out
                    .println("regId:" + ibi.getRegId() + ",status:" + ibi.getStatus() + ",repayNo" + ibi.getRepayNo());
        }
        System.out.println("结果:" + recordList);
    }

    @Test
    public void testListCurrBill() {
        long start = System.currentTimeMillis();
        Map<String, List<InstallmentBillInfo>> mapList = instBillInfoDao.listCurrBill("18911908439", "10101");
        System.out.println("查询时间：" + (System.currentTimeMillis() - start) / 1000 + "秒");
        // Map<String, List<InstallmentBillInfo>> mapList =
        // instBillInfoDao.listCurrBill("15117923307", "10101");
        for (Map.Entry<String, List<InstallmentBillInfo>> m : mapList.entrySet()) {
            List<InstallmentBillInfo> list = m.getValue();
            for (InstallmentBillInfo ibi : list) {
                System.out
                        .println("orderId:" + ibi.getOrderId() + ",regId:" + ibi.getRegId() + ",status:"
                                + ibi.getStatus()
                                + ",repayNo:" + ibi.getRepayNo() + "当期应还:" + ibi.getCurRepayAmt());
            }
        }
        System.out.println("查询和处理时间：" + (System.currentTimeMillis() - start) / 1000 + "秒");
    }

    @Test
    public void testGetRepayNoByOrderId() {
        String orderId = "20161128001";
        String today = "20170116";
        String nextDay = "20170216";
        System.out.println(instBillInfoDao.getRepayNoByOrderId(orderId, today, nextDay));
    }

    @Test
    public void testUpdateForNormalRepayment() {
        List<InstallmentBillInfo> list = new ArrayList<InstallmentBillInfo>();
        for (int i = 0; i < 2; i++) {
            InstallmentBillInfo installmentBillInfo = new InstallmentBillInfo(); // 提前还款只支持一个订单
            installmentBillInfo.setOrderId("111111");// 订单号
            installmentBillInfo.setRepayNo(i + 1);
            installmentBillInfo.setCurRepayDate(new Date());// 还款时间
            installmentBillInfo.setStatus(3);// 账单状态 1，未还款，2,部分还款 3，全部还款，4，失效账单
            list.add(installmentBillInfo);
        }
        instBillInfoDao.updateForNormalRepayment(list);
    }

    @Test
    public void testGetToRiskBillInfo() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("status", 3);
        param.put("regId", "15998631206");
        List<ToRiskBillInfo> list = instBillInfoDao.getToRiskBillInfo(param);
        for (ToRiskBillInfo toRiskBillInfo : list) {
            System.out.println(toRiskBillInfo.getRealName() + ", " + toRiskBillInfo.getRepayNo() + ", "
                    + toRiskBillInfo.getCurRealRepayAmt());
        }
    }

    @Test
    public void testSelectShouldDebtDetail() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("regId", "15998631206");
        PageParam pageParam = new PageParam(1, 5);
        PageBean pageBean = instBillInfoDao.selectShouldDebtDetail(pageParam, params);
        List<Object> list = pageBean.getRecordList();
        for (Object obj : list) {
            ShouldDebtDetail shouldDebtDetail = (ShouldDebtDetail) obj;
            System.out.println(shouldDebtDetail.getRealName() + ", " + shouldDebtDetail.getRepayNo() + ", "
                    + shouldDebtDetail.getCurRealRepayAmt());
        }
    }

    @Test
    public void testGetTotalRepayAmt() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("regId", "15998631206");
        BigDecimal totalRepayAmt = instBillInfoDao.getTotalRepayAmt(params);
        System.out.println(totalRepayAmt);

    }
}
