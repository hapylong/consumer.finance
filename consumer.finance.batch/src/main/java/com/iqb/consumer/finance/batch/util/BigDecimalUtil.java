/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月19日 上午11:20:17
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class BigDecimalUtil {

    public static BigDecimal add(BigDecimal... v) {
        BigDecimal initBigDecimal = new BigDecimal(0);
        for (int i = 0; i < v.length; i++) {
            initBigDecimal = initBigDecimal.add(v[i]);
        }
        return format(initBigDecimal);
    }

    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return format(v1.subtract(v2));
    }

    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return format(v1.multiply(v2));
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return v1.divide(v2, 4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 保留2位有效数字除法
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal divKeep(BigDecimal v1, BigDecimal v2) {
        return v1.divide(v2, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal format(BigDecimal v1) {
        DecimalFormat df = new DecimalFormat("#.00");
        return new BigDecimal(df.format(v1));
    }

    /**
     * 缩小100倍
     * 
     * @param v1
     * @return
     */
    public static BigDecimal narrow(BigDecimal v1) {
        return div(v1, new BigDecimal(100));
    }

    /**
     * 扩大100倍
     * 
     * @return
     */
    public static BigDecimal expand(BigDecimal v1) {
        return mul(v1, new BigDecimal(100));
    }

    /**
     * 
     * Description: 格式校验非负数
     * 
     * @param
     * @return BigDecimal
     * @throws @Author wangxinbang Create Date: 2016年11月9日 上午11:25:24
     */
    public static BigDecimal getFmtValResNonNegative(BigDecimal d) {
        if (d == null) {
            return BigDecimal.ZERO;
        }
        if (d.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return d;
    }

    /**
     * 
     * Description: 利率除法（高精度 15位小数位）
     * 
     * @param
     * @return BigDecimal
     * @throws @Author gxy Create Date: 2016年11月9日 上午11:25:24
     */
    public static BigDecimal divForRate(BigDecimal v1, BigDecimal v2) {
        return v1.divide(v2, 15, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 
     * Description: 利率乘法（高精度 15位小数位）
     * 
     * @param
     * @return BigDecimal
     * @throws @Author gxy Create Date: 2016年11月9日 上午11:25:24
     */
    public static BigDecimal mulForRate(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    public static void main(String[] args) {
        System.out.println(format(new BigDecimal(2.79999999)));
        System.out.println(div(new BigDecimal(507.5), new BigDecimal(100)));
        System.out.println(add(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3), new BigDecimal(4)));
    }
}
