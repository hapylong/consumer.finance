package com.iqb.consumer.common.utils;

import java.math.BigDecimal;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * Description: 数字相关工具类
 * 
 * @author iqb
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年8月9日    wangxinbang     1.0        1.0 Version 
 * </pre>
 */
public class NumberUtil extends NumberUtils {

    /**
     * 
     * Description:obj 转 int
     * 
     * @param
     * @return int
     * @throws
     * @Author wangxinbang Create Date: 2016年8月9日 下午5:14:11
     */
    public static int toInt(Object obj) {
        return toInt(ObjectUtils.toString(obj));
    }

    /**
     * 
     * Description: obj 转 int， 带默认值
     * 
     * @param
     * @return int
     * @throws
     * @Author wangxinbang Create Date: 2016年8月9日 下午5:17:45
     */
    public static int toInt(Object obj, int defaultValue) {
        try {
            String value = ObjectUtils.toString(obj, defaultValue + "");
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 
     * Description: obj 转 BigDecimal，带默认值
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年8月9日 下午5:20:40
     */
    public static BigDecimal toDecimal(Object obj, BigDecimal defaltValue) {
        String value = ObjectUtils.toString(obj);
        if (StringUtils.isEmpty(value)) {
            return defaltValue;
        }
        return new BigDecimal(value);
    }

    /**
     * 
     * Description: 两个decimal相加
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年8月9日 下午5:24:41
     */
    public static BigDecimal addDecimal(BigDecimal obj1, BigDecimal obj2) {
        BigDecimal value1 = toDecimal(obj1, BigDecimal.ZERO);
        BigDecimal value2 = toDecimal(obj2, BigDecimal.ZERO);
        return value1.add(value2);
    }

    /**
     * 
     * Description: 多个decimal相加
     * 
     * @param
     * @return BigDecimal
     * @throws
     * @Author wangxinbang Create Date: 2016年8月9日 下午5:21:58
     */
    public static BigDecimal addDecimal(Object... obj) {
        if (obj != null && obj.length > 0) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < obj.length; i++) {
                sum = sum.add(toDecimal(obj[i], BigDecimal.ZERO));
            }
            return sum;
        }
        return BigDecimal.ZERO;
    }

}
