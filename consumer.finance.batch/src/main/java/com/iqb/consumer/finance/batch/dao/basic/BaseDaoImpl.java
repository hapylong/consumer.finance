/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月26日 下午6:12:49
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.dao.basic;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.druid.pool.DruidDataSource;
import com.iqb.consumer.finance.batch.db.SubDbContextHolder;
import com.iqb.consumer.finance.batch.domain.BaseEntity;
import com.iqb.consumer.finance.batch.exception.BizException;
import com.iqb.consumer.finance.batch.page.PageBean;
import com.iqb.consumer.finance.batch.page.PageParam;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Resource
public abstract class BaseDaoImpl<T extends BaseEntity> extends SqlSessionDaoSupport implements BaseDao<T> {

    private final String MASTER = "mdb";// 主库标示
    private final String SLAVE = "sdb";// 从库标示

    /**
     * 数据源设置注入
     */
    @Autowired
    private SubDbContextHolder subDbContextHolder;

    /**
     * 设置db实例
     * 
     * @param idNum
     * @param dbType
     */
    public void setDb(int idNum, String dbType) {
        if (MASTER.equals(dbType)) {
            subDbContextHolder.setMaster(idNum);
        } else if (SLAVE.equals(dbType)) {
            subDbContextHolder.setSlave(idNum);
        }
    }

    protected static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);

    public static final String SQL_INSERT = "insert";
    public static final String SQL_BATCH_INSERT = "batchInsert";
    public static final String SQL_UPDATE = "update";
    public static final String SQL_GET_BY_ID = "getById";
    public static final String SQL_DELETE_BY_ID = "deleteById";
    public static final String SQL_LIST_PAGE = "listPage";
    public static final String SQL_LIST_PAGE_COUNT = "listPageCount";
    public static final String SQL_LIST_BY = "listBy";
    public static final String SQL_COUNT_BY_PAGE_PARAM = "countByPageParam"; // 根据当前分页参数进行统计
    private static final String SQL_SUFFIX = "Count";

    /**
     * 注入SqlSessionTemplate实例(要求Spring中进行SqlSessionTemplate的配置).<br/>
     * 可以调用sessionTemplate完成数据库操作.
     */

    @Autowired
    private SqlSessionTemplate sessionTemplate;

    @Autowired
    protected SqlSessionFactory sqlSessionFactory;

    @Autowired
    @Qualifier("common_platform_iqb_master001")
    private DruidDataSource druidDataSource;

    public SqlSessionTemplate getSessionTemplate() {
        return sessionTemplate;
    }

    public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {

        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public SqlSession getSqlSession() {
        return super.getSqlSession();
    }

    public long insert(T t) {

        if (t == null)
            throw new RuntimeException("T is null");

        this.setDb(0, MASTER);
        int result = sessionTemplate.insert(getStatement(SQL_INSERT), t);

        if (result <= 0)
            throw BizException.DB_INSERT_RESULT_0;

        if (t != null && t.getId() != null && result > 0)
            return t.getId();

        return result;
    }

    public long insert(List<T> list) {

        if (list == null || list.size() <= 0)
            return 0;

        this.setDb(0, MASTER);
        int result = sessionTemplate.insert(getStatement(SQL_BATCH_INSERT), list);

        if (result <= 0)
            throw BizException.DB_INSERT_RESULT_0;

        return result;
    }

    public long update(T t) {
        if (t == null)
            throw new RuntimeException("T is null");

        this.setDb(0, MASTER);
        int result = sessionTemplate.update(getStatement(SQL_UPDATE), t);

        if (result <= 0)
            throw BizException.DB_UPDATE_RESULT_0;

        return result;
    }

    public long update(List<T> list) {

        if (list == null || list.size() <= 0)
            return 0;

        int result = 0;

        for (int i = 0; i < list.size(); i++) {
            this.update(list.get(i));
            result += 1;
        }

        if (result <= 0)
            throw BizException.DB_UPDATE_RESULT_0;

        return result;
    }

    public T getById(long id) {
        this.setDb(1, SLAVE);
        return sessionTemplate.selectOne(getStatement(SQL_GET_BY_ID), id);
    }

    public long deleteById(long id) {
        this.setDb(0, MASTER);
        return (long) sessionTemplate.delete(getStatement(SQL_DELETE_BY_ID), id);
    }

    public PageBean listPage(PageParam pageParam, Map<String, Object> paramMap, String sqlId) {

        if (paramMap == null)
            paramMap = new HashMap<String, Object>();

        // 获取分页数据集 , 注切勿换成 sessionTemplate 对象
        List<Object> list = getSqlSession().selectList(getStatement(sqlId), paramMap,
                new RowBounds((pageParam.getPageNum() - 1) * pageParam.getNumPerPage(), pageParam.getNumPerPage()));

        // 统计总记录数
        // Object countObject =(Object) getSqlSession().selectOne(getStatement(sqlId), new
        // ExecutorInterceptor.CountParameter(paramMap));
        Object countObject = (Object) getSqlSession().selectOne(getStatement(sqlId + SQL_SUFFIX), paramMap);
        Long count = Long.valueOf(countObject.toString());

        return new PageBean(pageParam.getPageNum(), pageParam.getNumPerPage(), count.intValue(), list);
    }

    public PageBean listPage(PageParam pageParam, Map<String, Object> paramMap) {

        if (paramMap == null)
            paramMap = new HashMap<String, Object>();

        // 获取分页数据集 , 注切勿换成 sessionTemplate 对象
        List<Object> list = getSqlSession().selectList(getStatement(SQL_LIST_PAGE), paramMap,
                new RowBounds((pageParam.getPageNum() - 1) * pageParam.getNumPerPage(), pageParam.getNumPerPage()));

        // 统计总记录数
        // Object countObject = (Object) getSqlSession().selectOne(getStatement(SQL_LIST_PAGE),
        // new ExecutorInterceptor.CountParameter(paramMap));
        Object countObject = (Object) getSqlSession().selectOne(getStatement(SQL_LIST_PAGE_COUNT), paramMap);
        Long count = countObject == null ? 0 : Long.valueOf(countObject.toString());

        // 是否统计当前分页条件下的数据：1:是，其他为否
        Object isCount = paramMap.get("isCount");
        if (isCount != null && "1".equals(isCount.toString())) {
            this.setDb(1, SLAVE);
            Map<String, Object> countResultMap =
                    sessionTemplate.selectOne(getStatement(SQL_COUNT_BY_PAGE_PARAM), paramMap);
            return new PageBean(pageParam.getPageNum(), pageParam.getNumPerPage(), count.intValue(), list,
                    countResultMap);
        } else {
            return new PageBean(pageParam.getPageNum(), pageParam.getNumPerPage(), count.intValue(), list);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<T> listBy(Map<String, Object> paramMap) {
        return (List) this.listBy(paramMap, SQL_LIST_BY);
    }

    public List<Object> listBy(Map<String, Object> paramMap, String sqlId) {
        if (paramMap == null)
            paramMap = new HashMap<String, Object>();
        this.setDb(1, SLAVE);
        return sessionTemplate.selectList(getStatement(sqlId), paramMap);
    }

    @SuppressWarnings("unchecked")
    public T getBy(Map<String, Object> paramMap) {
        return (T) this.getBy(paramMap, SQL_LIST_BY);
    }

    public Object getBy(Map<String, Object> paramMap, String sqlId) {
        if (paramMap == null || paramMap.isEmpty())
            return null;
        this.setDb(1, SLAVE);
        return this.getSqlSession().selectOne(getStatement(sqlId), paramMap);
    }

    public String getStatement(String sqlId) {

        String name = this.getClass().getName();

        StringBuffer sb = new StringBuffer().append(name).append(".").append(sqlId);

        return sb.toString();
    }

    /**
     * 根据序列名称,获取序列值
     */
    public String getSeqNextValue(String seqName) {
        boolean isClosedConn = false;
        // 获取当前线程的连接
        Connection connection = this.sessionTemplate.getConnection();
        // 获取Mybatis的SQLRunner类
        SqlRunner sqlRunner = null;
        try {
            // 要执行的SQL
            String sql = "";
            // 数据库驱动类
            String driverClass = druidDataSource.getDriver().getClass().getName();
            // 不同的数据库,拼接SQL语句
            if (driverClass.equals("com.ibm.db2.jcc.DB2Driver")) {
                sql = "  VALUES " + seqName.toUpperCase() + ".NEXTVAL";
            }
            if (driverClass.equals("oracle.jdbc.OracleDriver")) {
                sql = "SELECT " + seqName.toUpperCase() + ".NEXTVAL FROM DUAL";
            }
            if (driverClass.equals("com.mysql.jdbc.Driver")) {
                sql = "SELECT  FUN_SEQ('" + seqName.toUpperCase() + "')";
            }
            // 如果状态为关闭,则需要从新打开一个连接
            if (connection.isClosed()) {
                connection = sqlSessionFactory.openSession().getConnection();
                isClosedConn = true;
            }
            sqlRunner = new SqlRunner(connection);
            Object[] args = {};
            // 执行SQL语句
            Map<String, Object> params = sqlRunner.selectOne(sql, args);
            for (Object o : params.values()) {
                return o.toString();
            }
            return null;
        } catch (Exception e) {
            throw BizException.DB_GET_SEQ_NEXT_VALUE_ERROR.newInstance("获取序列出现错误!序列名称:{%s}", seqName);
        } finally {
            if (isClosedConn) {
                sqlRunner.closeConnection();
            }
        }
    }
}
