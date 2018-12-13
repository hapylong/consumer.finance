package com.iqb.consumer.finance.batch.service;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.finance.batch.bean.InstBillInfoBean;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年10月18日下午2:28:57 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstBillInfoService {
    /**
     * MD5：逾期5天以内（含5天）的账单，即逾期天数<=5；当期未还总额本息\当期应还的的总金额 Description:获取资产逾期MD5数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月18日
     */
    public List<InstBillInfoBean> listOverdueForMD5(Map<String, Object> paramMap);

    /**
     * MOB1：逾期30天以内（含30天）的账单，不包含MD5，即5<逾期天数<=30；当月剩余未还款本金\总的贷款余额（首逾期：上月新增客户，次月出现逾期。首逾率—首次逾期本金\
     * 上月新增放款总额） Description:获取资产逾期MOB1数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月18日
     */
    public List<InstBillInfoBean> listOverdueForMOB1(Map<String, Object> paramMap);

    /**
     * MOB2：逾期60天以内（含60天）的账单，不含M1，即30<逾期天数<=60；当期剩余未还款本金\总的贷款余额 Description:获取资产逾期MOB2数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月18日
     */
    public List<InstBillInfoBean> listOverdueForMOB2(Map<String, Object> paramMap);

    /**
     * MOB3：60<呆账天数≤90，剩余未还本金\总的贷款余额（包含在库车辆的情况） Description:获取资产逾期MOB3数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月18日
     */
    public List<InstBillInfoBean> listOverdueForMOB3(Map<String, Object> paramMap);

    /**
     * MOB4:90<坏账天数，剩余未还本金\总的贷款余额（90天＞天没有清账，统一定位坏账，无论什么情况） Description:获取资产逾期MOB4数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月18日
     */
    public List<InstBillInfoBean> listOverdueForMOB4(Map<String, Object> paramMap);

    /**
     * 
     * Description:合并MD5 MOB1-MOB4数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月19日
     */
    public long mergeList(Map<String, Object> paramMap);
}
