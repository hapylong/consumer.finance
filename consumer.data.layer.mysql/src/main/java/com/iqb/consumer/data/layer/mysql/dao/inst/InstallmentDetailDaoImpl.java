/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:49:40
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.common.basic.BaseDaoImpl;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentDetail;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class InstallmentDetailDaoImpl extends BaseDaoImpl<InstallmentDetail> implements InstallmentDetailDao {

    @Override
    public long stopInstallmentDetail(InstallmentDetail installmentDetail) {
        return super.getSqlSession().update(getStatement("stopInstallmentDetail"), installmentDetail);
    }

    /**
     * 转租-更新inst_detail
     * 
     * @param mapToBean
     * @return
     */
    @Override
    public long updateInstallmentForOrderId(InstallmentDetail installmentDetail) {
        return super.getSqlSession().update(getStatement("updateInstallmentForOrderId"), installmentDetail);
    }

}
