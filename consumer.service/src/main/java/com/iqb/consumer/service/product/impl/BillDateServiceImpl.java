package com.iqb.consumer.service.product.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.AccountConstant.BillDateConstant;
import com.iqb.consumer.common.exception.BizException;
import com.iqb.consumer.common.utils.BeanUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.NumberUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.mysql.bean.product.RepayDate;
import com.iqb.consumer.data.layer.mysql.dao.product.BillDateDao;
import com.iqb.consumer.service.product.IBillDateService;

/**
 * 
 * Description: 账单日相关服务
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月7日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Service
public class BillDateServiceImpl implements IBillDateService {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(BillDateServiceImpl.class);

    /**
     * 转入参数日期格式
     */
    private static final String DEFAULT_OUTPUT_BILLDATE = DateUtil.SHORT_DATE_FORMAT_NO_DASH;

    /**
     * 对外输入日期格式
     */
    private static final String OUTPUT_BILLDATE = DateUtil.SHORT_DATE_FORMAT_NO_DASH;

    @Resource
    private BillDateDao billDateDao;

    @Override
    public String getBillDate(String date, long bid) {
        try {
            logger.info("获取账单日传入信息:date->{},bid->{}", date, bid);
            return this.getBillDate(null, date, bid, DEFAULT_OUTPUT_BILLDATE, null, null);
        } catch (Exception e) {
            logger.error("获取账单日异常：", e);
            return null;
        }
    }

    @Override
    public String getBillDate(String curBillDate, String date, long bid, String format, String days, Integer cycle) {
        logger.info("获取账单日传入信息:date->{},bid->{},format->{}", date, bid, format);
        if (StringUtil.isEmpty(date)) {
            logger.error("获取账单日:传入账单日期信息空值");
            return null;
        }
        if (StringUtil.isEmpty(format)) {
            logger.error("获取账单日:传入格式化信息空值");
            return null;
        }
        RepayDate repayDateBean = billDateDao.getById(bid);
        if (repayDateBean == null) {
            logger.error("获取账单日:从数据查询账单计划为空");
            return null;
        }
        Calendar cal = DateUtil.parseCalendar(date, format);
        switch (repayDateBean.getType()) {
            case BillDateConstant.BILLDATE_TYPE_STATIC:
                return this.calStaticBillDate(curBillDate, cal, repayDateBean, cycle);

            case BillDateConstant.BILLDATE_TYPE_MONTHLY_STATIC:
                return this.calMonthlyStaticBillDate(cal, repayDateBean, cycle);

            case BillDateConstant.BILLDATE_TYPE_MONTHLY_END:
                return this.calMonthlyEndBillDate(cal, repayDateBean, cycle);

            case BillDateConstant.BILLDATE_TYPE_MONTHLY_STATIC_PRE_DAY:
                return this.calMonthlyStaticPreDayBillDate(cal, repayDateBean, cycle);

            case BillDateConstant.BILLDATE_TYPE_MONTHLY_END_PRE_DAY:
                return this.calMonthlyEndPreDayBillDate(cal, repayDateBean, cycle);

            case BillDateConstant.BILLDATE_TYPE_DYNAMIC:
                return this.calDynamicBillDate(cal, days);

            default:
                logger.error("获取账单日:没有找到账单日对应类型");
                return null;
        }
    }

    /**
     * 
     * Description: 处理固定账单日
     * 
     * @param 指定日志格式 1-2,0,5 几号到几号，(0,下月，1当月)，5号，即 1-2号分期则第一期账单日为下月5号
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午7:16:04
     * @author <href>zhuyaoming@aliyun.com</href>
     */
    private String calStaticBillDate(String curBillDate, Calendar cal, RepayDate repayDateBean, Integer cycle) {
        if (StringUtils.isNoneEmpty(curBillDate)) {
            Calendar billDateCal = DateUtil.parseCalendar(curBillDate, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            billDateCal.add(Calendar.MONTH, 1);
            return DateUtil.format(billDateCal, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        } else {
            /** 指定日期 **/
            String specifiedDates = repayDateBean.getSpecifiedDate();
            List<String> specifiedDateList = StringUtil.strToSortList(specifiedDates, "|");
            for (String specifiedDate : specifiedDateList) {
                logger.info("时间区间为:{}", specifiedDate);
                String speDate = specifiedDate.split(",")[0];// 时间区间 1-4
                String dayFlag = specifiedDate.split(",")[1]; // 当月还是下月
                String billDay = specifiedDate.split(",")[2]; // 固定账单日
                List<String> dayList = StringUtil.strToSortList(speDate, "-");
                Integer minDay =
                        Integer.parseInt(dayList.get(0)) > Integer.parseInt(dayList.get(1)) ? Integer.parseInt(dayList
                                .get(1)) : Integer.parseInt(dayList.get(0)); // 最小日期包含
                Integer maxDay =
                        Integer.parseInt(dayList.get(0)) > Integer.parseInt(dayList.get(1)) ? Integer.parseInt(dayList
                                .get(0)) : Integer.parseInt(dayList.get(1)); // 最小日期包含// 最大日期包含
                if (cal.get(Calendar.DAY_OF_MONTH) >= minDay && cal.get(Calendar.DAY_OF_MONTH) <= maxDay) {
                    // 时间被命中
                    if ("0".equals(dayFlag)) { // 下月账单
                        cal.add(Calendar.MONTH, 1);
                    }
                    cal.set(Calendar.DAY_OF_MONTH, NumberUtil.toInt(billDay));
                    return DateUtil.format(cal, OUTPUT_BILLDATE);
                }
            }
        }
        return null;
    }

    /**
     * 
     * Description: 处理月对月固定日期
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午8:16:41
     */
    private String calMonthlyStaticBillDate(Calendar cal, RepayDate repayDateBean, Integer cycle) {
        /** 指定日期 **/
        String specifiedDates = repayDateBean.getSpecifiedDate();
        /** 固定账单日 **/
        String billDate = repayDateBean.getBillDate();
        if (StringUtil.isEmpty(billDate)) {
            logger.error("获取账单日传入信息:处理月对月固定日期异常:固定账单日为空.");
            return null;
        }
        List<String> specifiedDateList = StringUtil.strToList(specifiedDates, ",");
        Calendar tCal = DateUtil.copyCal(cal);
        tCal.add(Calendar.MONTH, cycle);
        if (!CollectionUtils.contains(specifiedDateList.iterator(), Integer.toString(cal.get(Calendar.DAY_OF_MONTH)))) {
            return DateUtil.format(tCal, OUTPUT_BILLDATE);
        }
        tCal.set(Calendar.DAY_OF_MONTH, NumberUtil.toInt(billDate));
        return DateUtil.format(tCal, OUTPUT_BILLDATE);
    }

    /**
     * 
     * Description: 处理月对月月末
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午8:42:10
     */
    private String calMonthlyEndBillDate(Calendar cal, RepayDate repayDateBean, Integer cycle) {
        Calendar tCal = DateUtil.copyCal(cal);
        tCal.add(Calendar.MONTH, cycle == null ? 0 : cycle);
        int nextMonthMaxDayOfMonth = tCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (cal.get(Calendar.DAY_OF_MONTH) < nextMonthMaxDayOfMonth) {
            return DateUtil.format(tCal, OUTPUT_BILLDATE);
        }
        tCal.set(Calendar.DAY_OF_MONTH, nextMonthMaxDayOfMonth);
        return DateUtil.format(tCal, OUTPUT_BILLDATE);
    }

    /**
     * 
     * Description: 月对月前一日固定日期
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午9:08:30
     */
    private String calMonthlyStaticPreDayBillDate(Calendar cal, RepayDate repayDateBean, Integer cycle) {
        // 先根据月对月计算天数
        String months = this.calMonthlyEndPreDayBillDate(cal, repayDateBean, cycle);
        /** 指定日期 **/
        String specifiedDates = repayDateBean.getSpecifiedDate();
        /** 固定账单日 **/
        String billDate = repayDateBean.getBillDate();
        List<String> specifiedDateList = StringUtil.strToList(specifiedDates, ",");
        Calendar tCal = DateUtil.parseCalendar(months, OUTPUT_BILLDATE);
        if (!CollectionUtils.contains(specifiedDateList.iterator(), Integer.toString(cal.get(Calendar.DAY_OF_MONTH)))) {
            return DateUtil.format(tCal, OUTPUT_BILLDATE);
        }
        tCal.set(Calendar.DAY_OF_MONTH, NumberUtil.toInt(billDate));
        return DateUtil.format(tCal, OUTPUT_BILLDATE);
    }

    /**
     * 
     * Description: 月对月前一日月末
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午9:18:15
     */
    private String calMonthlyEndPreDayBillDate(Calendar cal, RepayDate repayDateBean, Integer cycle) {
        String months = this.calMonthlyEndBillDate(cal, repayDateBean, cycle);
        Calendar tCal = DateUtil.parseCalendar(months, OUTPUT_BILLDATE);
        // 如果分期日>2月的最大天数则二月不需要减一天
        int nextMonthMaxDayOfMonth = tCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (cal.get(Calendar.DAY_OF_MONTH) > nextMonthMaxDayOfMonth) {
            // 分期日大于2月份最大日期,则无需减一天
        } else {
            tCal.add(Calendar.DAY_OF_MONTH, -1);
        }
        return DateUtil.format(tCal, OUTPUT_BILLDATE);
    }

    /**
     * 根据传入的时间计算账单日
     * 
     * @param cal
     * @param days
     * @return
     */
    private String calDynamicBillDate(Calendar cal, String days) {
        int intDay = 0;
        try {
            intDay = days == null ? 0 : Integer.parseInt(days);
        } catch (Exception e) {
            logger.error("传入的时间间隔:{}无法转为整数", days);
            intDay = 0;
        }
        cal.add(Calendar.DAY_OF_MONTH, intDay);
        return DateUtil.format(cal, OUTPUT_BILLDATE);
    }

    // public static void main(String[] args) {
    // Calendar cal = DateUtil.parseCalendar("20170728", "yyyyMMdd");
    // System.out.println(calDynamicBillDate(cal, 5));
    // }

    @Override
    public String getBillDate(String date, long bid, String sDate, long Sbid) {
        /** 获取第一次分期账单日 **/
        String fBillDate = this.getBillDate(date, bid);
        /** 获取第二次分期账单日 **/
        String sBillDate = this.getBillDate(sDate, Sbid);

        Calendar fCal = DateUtil.parseCalendar(fBillDate, OUTPUT_BILLDATE);
        Calendar sCal = DateUtil.parseCalendar(sBillDate, OUTPUT_BILLDATE);

        sCal.set(Calendar.DAY_OF_MONTH, fCal.get(Calendar.DAY_OF_MONTH));

        return DateUtil.format(sCal, OUTPUT_BILLDATE);
    }

    @Override
    public long deleteById(JSONObject objs) {
        this.checkEmpty(objs);
        return billDateDao.deleteById(Integer.parseInt(objs.getString("id")));
    }

    @Override
    public long updateById(JSONObject objs) {
        this.checkEmpty(objs);
        RepayDate repayDate = BeanUtil.mapToBean(objs, RepayDate.class);
        return billDateDao.update(repayDate);
    }

    @Override
    public long insert(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        RepayDate repayDate = BeanUtil.mapToBean(objs, RepayDate.class);
        return billDateDao.insert(repayDate);
    }

    /**
     * 
     * Description: 校验非空
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:24:53
     */
    private void checkEmpty(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
        if (StringUtil.isEmpty(objs.getString("id"))) {
            throw BizException.COMMON_PARAM_NULL_ERROR;
        }
    }

}
