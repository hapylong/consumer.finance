/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月15日 下午6:43:39
 * @version V1.0
 */

package com.iqb.consumer.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public abstract class BaseDomain extends AbstractStringableEntity implements Serializable {
    private Date createTime = new Date(); // 注意，这个字段未按照命名规范: createTime
    private Date updateTime = new Date(); // 注意，这个字段未按照命名规范: updateTime
    private String createdBy;
    private String updatedBy;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
