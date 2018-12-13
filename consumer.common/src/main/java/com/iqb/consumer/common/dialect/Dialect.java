/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月26日 下午9:05:41
 * @version V1.0
 */

package com.iqb.consumer.common.dialect;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public abstract class Dialect {
    public boolean supportsLimit() {
        return false;
    }

    public boolean supportsLimitOffset() {
        return supportsLimit();
    }

    /**
     * 将sql变成分页sql语句,直接使用offset,limit的值作为占位符.</br> 源代码为: getLimitString(sql,offset
     * ,String.valueOf(offset),limit,String.valueOf(limit))
     */
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
    }

    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符(placeholder)替换.
     * 
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     * 
     * @return 包含占位符的分页sql
     */
    public abstract String getLimitString(String sql, int offset, String offsetPlaceholder, int limit,
            String limitPlaceholder);
}
