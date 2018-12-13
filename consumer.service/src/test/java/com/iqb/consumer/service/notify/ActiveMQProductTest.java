/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月28日 上午10:25:36
 * @version V1.0
 */

package com.iqb.consumer.service.notify;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.iqb.consumer.service.AbstractServiceTest;
import com.iqb.consumer.service.utils.NotifyUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class ActiveMQProductTest extends AbstractServiceTest {
    @Resource
    private JmsTemplate notifyJmsTemplate;

    @Test
    public void testSendMQ() {
        // final OrderInfo orderInfo = new OrderInfo();
        // orderInfo.set_id("12312313212321");
        // orderInfo.setOrderId("20160708111111000001");
        // orderInfo.setSumAmount(new BigDecimal("12000.00"));
        // orderInfo.setSumShareAmount(new BigDecimal("11000.00"));
        // orderInfo.setSumPeriods(12);
        // orderInfo.setRealPeriods(11);
        // orderInfo.setInstallNo(1);// 分期计划1
        // orderInfo.setEachAmount(new BigDecimal("1000.00"));// 每期金额
        // orderInfo.setEachShareAmount(new BigDecimal("1000.00"));
        // orderInfo.setMerchantId("test001");
        // orderInfo.setNeedShare(1);
        // orderInfo.setInstallType(2);
        // orderInfo.setPrePayment(new BigDecimal("0.00"));
        // orderInfo.setMargin(new BigDecimal("0.00"));
        // orderInfo.setServiceFee(new BigDecimal("0.00"));
        // orderInfo.setDownPayment(new BigDecimal("0.00"));
        // orderInfo.setFinalPayment(new BigDecimal("0.00"));
        // orderInfo.setRefundAmount(new BigDecimal("0.00"));
        // orderInfo.setRefundStatus(1);
        // orderInfo.setFeeType(2);
        // orderInfo.setOrderStatus(1);
        // orderInfo.setUseFreeInterest(false);
        // orderInfo.setFreeInterestCount(0);
        // orderInfo.setMerchantRemark("纳美测试");
        // orderInfo.setThirdInfos(null);
        // orderInfo.setOrderInfo(null);
        // orderInfo.setConUuid("12312313212321");
        // orderInfo.setConRemark("测试");
        // // private BigDecimal prePayment;//首付金额
        // // private BigDecimal margin;// 保证金
        // // private BigDecimal serviceFee;// 服务费
        // // private BigDecimal downPayment;// 定金
        // // private BigDecimal finalPayment;// 尾款
        // // private BigDecimal refundAmount;// 退款金额
        // // private int refundStatus;// 退款状态
        // // private int feeType;// 费率类型(年费率,月费率,日费率)
        // // private int installType;//分期类型(固定日期账单,月对月账单,隔日账单,实时账单)
        // // private int orderStatus;// 订单状态
        // // private boolean useFreeInterest = false;//使用免息卷
        // // private int freeInterestCount;//免息卷张数(必须小于<=分期期数)
        // // private int installNo;//分期计划
        // // private String merchantId;// 商户ID
        // // private String merchantRemark;// 商户备注
        // // private ThirdInfo thirdInfos;// 第三方信息
        // // private String orderInfo;// 订单详情
        // // private String conUuid;// 用户
        // // private String conRemark;// 用户备注
        //
        // notifyJmsTemplate.send(new MessageCreator() {
        // public Message createMessage(Session session) throws JMSException {
        // return session.createTextMessage(NotifyUtil.formatInstall(orderInfo));
        // }
        // });
    }
}
