/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 数据访问层基础支撑接口
 * @date 2016年5月26日 下午5:57:55
 * @version V1.0
 */

package com.iqb.consumer.common.basic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import com.iqb.consumer.common.page.PageBean;
import com.iqb.consumer.common.page.PageParam;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface BaseDao<T> {

    /**
     * 根据实体对象新增记录.
     * 
     * @param entity .
     * @return id .
     */
    long insert(T entity);

    /**
     * 批量保存对象.
     * 
     * @param entity .
     * @return id .
     */
    long insert(List<T> list);

    /**
     * 更新实体对应的记录.
     * 
     * @param entity .
     * @return
     */
    long update(T entity);

    /**
     * 批量更新对象.
     * 
     * @param entity .
     * @return int .
     */
    long update(List<T> list);

    /**
     * 根据ID查找记录.
     * 
     * @param id .
     * @return entity .
     */
    T getById(long id);

    /**
     * 根据ID删除记录.
     * 
     * @param id .
     * @return
     */
    long deleteById(long id);

    /**
     * 分页查询 .
     * 
     * @param pageParam 分页参数.
     * @param paramMap 业务条件查询参数.
     * @return
     */
    PageBean listPage(PageParam pageParam, Map<String, Object> paramMap);

    PageBean listPage(PageParam pageParam, Map<String, Object> paramMap, String sqlId);

    /**
     * 根据条件查询 listBy: <br/>
     * 
     * @param paramMap
     * @return 返回集合
     */
    List<T> listBy(Map<String, Object> paramMap);

    List<Object> listBy(Map<String, Object> paramMap, String sqlId);

    /**
     * 根据条件查询 listBy: <br/>
     * 
     * @param paramMap
     * @return 返回实体
     */
    T getBy(Map<String, Object> paramMap);

    Object getBy(Map<String, Object> paramMap, String sqlId);

    /**
     * 根据序列名称获取下一个值
     * 
     * @return
     */
    String getSeqNextValue(String seqName);

    SqlSessionTemplate getSessionTemplate();

    SqlSession getSqlSession();

}
