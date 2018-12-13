/*
 * 软件著作权：北京爱钱帮财富科技有限公司 项目名称：
 * 
 * NAME : FinanceUtil.java
 * 
 * PURPOSE :
 * 
 * AUTHOR : crw
 * 
 * 
 * 创建日期: 2017年5月9日 HISTORY： 变更日期
 */
package com.iqb.consumer.common.constant;

import org.springframework.stereotype.Component;

/**
 * @author crw
 * 
 */
@Component
public class FinanceUtil extends AccountConstant {
    /** 账户号(10102 以租代售新车 ,10102以租代售二手车, 10103 抵押车, 10101 质押车, 10201 易安家, 10202 医美, 10203 旅游) **/
    public static final String OPENID_YZDS_NEW = "10102";// 以租代售新车
    public static final String OPENID_YZDS_OLD = "10102";// 以租代售二手车
    public static final String OPENID_ZY = "10101";// 质押车
    public static final String OPENID_DY = "10103";// 抵押车
    public static final String OPENID_CMD = "10104";// 车秒贷
    public static final String OPENID_YAJ = "10201";// 易安家
    public static final String OPENID_YM = "10202";// 医美
    public static final String OPENID_LY = "10203";// 旅游
}
