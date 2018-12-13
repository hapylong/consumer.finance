package com.iqb.consumer.service.product;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Description: 违约金比例服务接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public interface IBreachContractService {

    /**
     * 
     * Description: 获取违约金
     * 
     * @param 剩余本金
     * @param 合同金额
     * @param 总应还金额
     * @param 本期还款金额
     * @param 违约金id
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年11月9日 上午11:00:05
     */
    public BigDecimal getBreachContract(BigDecimal remainPrincipal, BigDecimal contractAmt, BigDecimal repaymentAmt,
            BigDecimal currentAmt, int bid);

    /**
     * 
     * Description: 根据id删除
     * 
     * @param
     * @return long
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:23:12
     */
    public long deleteById(JSONObject objs);

    /**
     * 
     * Description: update
     * 
     * @param
     * @return long
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:23:21
     */
    public long updateById(JSONObject objs);

    /**
     * 
     * Description: 插入
     * 
     * @param
     * @return long
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:23:37
     */
    public long insert(JSONObject objs);

}
