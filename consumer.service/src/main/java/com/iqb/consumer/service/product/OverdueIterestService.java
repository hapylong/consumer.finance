/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:29:42
 * @version V1.0
 */

package com.iqb.consumer.service.product;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * @author <a href="gongxiaoyu@aiqianbang.com">gxy</a>
 */
public interface OverdueIterestService {

    /**
     * @description 计算逾期费用
     * @param BigDecimal remainPrincipal 剩余本金
     * @param BigDecimal installAmt 实际还款金额
     * @param BigDecimal principal 本期账单
     * @param BigDecimal contractAmt 合同金额
     * @param BigDecimal remainPriandInterest 剩余本息
     * @param int overdueDays 逾期天数
     * @param String id 逾期计算id
     * @return Map<String, Object> result 逾期费用 固定金额
     */
    public Map<String, BigDecimal> calculateOverdueInterestWithFixed(BigDecimal remainPrincipal, BigDecimal installAmt,
            BigDecimal principal, BigDecimal contractAmt, BigDecimal remainPriandInterest, int overdueDays,
            String id);

    /**
     * 
     * Description: 根据id删除
     * 
     * @param
     * @return long
     * @throws @Author wangxinbang Create Date: 2016年11月17日 上午10:23:12
     */
    public long deleteById(JSONObject objs);

    /**
     * 
     * Description: update
     * 
     * @param
     * @return long
     * @throws @Author wangxinbang Create Date: 2016年11月17日 上午10:23:21
     */
    public long updateById(JSONObject objs);

    /**
     * 
     * Description: 插入
     * 
     * @param
     * @return long
     * @throws @Author wangxinbang Create Date: 2016年11月17日 上午10:23:37
     */
    public long insert(JSONObject objs);

}
