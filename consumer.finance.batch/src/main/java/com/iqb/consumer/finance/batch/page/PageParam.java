/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 分页参数传递工具类
 * @date 2016年5月26日 下午6:02:23
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.page;

import java.io.Serializable;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class PageParam implements Serializable {

    private static final long serialVersionUID = 3117380337167325368L;
    private int pageNum; // 当前页数
    private int numPerPage; // 每页记录数

    public PageParam(int pageNum, int numPerPage) {
        super();
        this.pageNum = pageNum;
        this.numPerPage = numPerPage;
    }

    /** 当前页数 */
    public int getPageNum() {
        return pageNum;
    }

    /** 当前页数 */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /** 每页记录数 */
    public int getNumPerPage() {
        return numPerPage;
    }

    /** 每页记录数 */
    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

}
