package com.iqb.consumer.common.db;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负载均衡设置数据库实例 - 分库
 * 
 * @author jack
 * 
 */
public class SubDbContextHolder {
    protected static final Logger logger = LoggerFactory.getLogger(SubDbContextHolder.class);
    /**
     * 主库最大分库数据量
     */
    public int SINGLE_DB_MAX_NUM;

    /**
     * 主库数量
     */
    public int MASTER_DB_NUM;

    /**
     * 从库数量
     */
    public int SLAVE_DB_NUM;

    /**
     * 主库str
     */
    public String MASTER_DB_DBSTR;

    /**
     * 从库str
     */
    public String SLAVE_DB_DBSTR;

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    private static Random random = new Random();

    /**
     * 获取数据源实例名称
     * 
     * @return
     */
    public String getCustomerType() {
        if (null == contextHolder.get()) {
            setMaster(1);
        }
        return contextHolder.get();
    }

    /**
     * 客服端 - 主库
     * 
     * @param id
     */
    public void setMaster(int idNum) {
        contextHolder.set(MASTER_DB_DBSTR + 1);
    }

    /**
     * 从库
     * 
     * @param id
     */
    public void setSlave(int idNum) {
        String db = "";
        boolean flag = true;
        // 根据从库数量和数据库分库最大数据量设置数据库实例
        for (int i = 1; i <= MASTER_DB_NUM; i++) {
            if (idNum > SINGLE_DB_MAX_NUM * (i - 1) &&
                    idNum <= SINGLE_DB_MAX_NUM * i) {
                contextHolder.set(MASTER_DB_DBSTR + i + SLAVE_DB_DBSTR + (random.nextInt(SLAVE_DB_NUM) + 1));
                db = MASTER_DB_DBSTR + i + SLAVE_DB_DBSTR + (random.nextInt(SLAVE_DB_NUM) + 1);
                flag = false;
                break;
            }
        }
        // 默认为第一个库
        if (flag) {
            contextHolder.set(MASTER_DB_DBSTR + 1 + SLAVE_DB_DBSTR + 1);
            db = MASTER_DB_DBSTR + 1 + SLAVE_DB_DBSTR + 1;
        }
        logger.info("----当前查询使用的库是-----{}", db);
    }

    public void clearType() {
        contextHolder.remove();
    }

    public void setSINGLE_DB_MAX_NUM(int sINGLE_DB_MAX_NUM) {
        SINGLE_DB_MAX_NUM = sINGLE_DB_MAX_NUM;
    }

    public void setMASTER_DB_NUM(int mASTER_DB_NUM) {
        MASTER_DB_NUM = mASTER_DB_NUM;
    }

    public void setSLAVE_DB_NUM(int sLAVE_DB_NUM) {
        SLAVE_DB_NUM = sLAVE_DB_NUM;
    }

    public void setMASTER_DB_DBSTR(String mASTER_DB_DBSTR) {
        MASTER_DB_DBSTR = mASTER_DB_DBSTR;
    }

    public void setSLAVE_DB_DBSTR(String sLAVE_DB_DBSTR) {
        SLAVE_DB_DBSTR = sLAVE_DB_DBSTR;
    }
}
