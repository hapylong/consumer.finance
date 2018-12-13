/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 拦截器基类
 * @date 2016年5月26日 下午8:27:17
 * @version V1.0
 */

package com.iqb.consumer.common.aop;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public abstract class AbstractInterceptor implements Interceptor {

    protected MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource, boolean isCount) {
        Builder builder =
                new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        String[] s = ms.getKeyProperties();
        if (s == null) {
            builder.keyProperty(null);
        } else {
            builder.keyProperty(s[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        if (isCount) {
            List<ResultMap> resultMaps = new ArrayList<ResultMap>();
            resultMaps.add(new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Integer.class,
                    new ArrayList<ResultMapping>()).build());
            builder.resultMaps(resultMaps);
        } else {
            builder.resultMaps(ms.getResultMaps());
        }
        builder.cache(ms.getCache());
        MappedStatement newMs = builder.build();
        return newMs;
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
