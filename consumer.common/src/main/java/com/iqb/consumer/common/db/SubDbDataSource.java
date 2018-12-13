package com.iqb.consumer.common.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源实现 - 分库配置
 */
public class SubDbDataSource extends AbstractRoutingDataSource {

    private SubDbContextHolder subDbContextHolder;

    @Override
    protected Object determineCurrentLookupKey() {
        return subDbContextHolder.getCustomerType();
    }

    public void setSubDbContextHolder(SubDbContextHolder subDbContextHolder) {
        this.subDbContextHolder = subDbContextHolder;
    }
}
