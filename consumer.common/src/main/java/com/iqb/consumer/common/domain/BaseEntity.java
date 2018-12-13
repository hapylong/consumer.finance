/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月26日 下午6:11:43
 * @version V1.0
 */

package com.iqb.consumer.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class BaseEntity implements Serializable {

    private Long id;
    private Integer version = 0;
    // 创建时间
    protected Date createTime = new Date();
    private Date updateTime = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

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

}
