/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月12日 下午4:49:30
 * @version V1.0
 */

package com.iqb.consumer.data.layer.mysql.dao.inst;

import com.iqb.consumer.common.basic.BaseDao;
import com.iqb.consumer.data.layer.mysql.bean.inst.InstallmentDetail;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface InstallmentDetailDao extends BaseDao<InstallmentDetail> {

    /**
     * 
     * Description: 终止分摊信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年11月16日 上午10:49:49
     */
    public long stopInstallmentDetail(InstallmentDetail mapToBean);

    /**
     * 转租-更新inst_detail
     * 
     * @param mapToBean
     * @return
     */
    public long updateInstallmentForOrderId(InstallmentDetail installmentDetail);

}
