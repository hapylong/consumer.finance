/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月27日 下午7:16:18
 * @version V1.0
 */

package com.iqb.consumer.finance.batch.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public enum NotifyTypeEnum {

    INSTALLMENT("分期交易", 111),
    CREATEBILL("账单生成", 222),
    CREATEINSTDETAIL("生成分期分摊详情", 333),
    SMS("短信", 777),
    REPAYSMS("催款短信", 700),
    MAIL("邮件", 888), ;

    /** 描述 */
    private String desc;
    /** 枚举值 */
    private int value;

    /** 构造函数 */
    private NotifyTypeEnum(String desc, int value) {
        this.desc = desc;
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NotifyTypeEnum getEnum(int value) {
        NotifyTypeEnum resultEnum = null;
        NotifyTypeEnum[] enumAry = NotifyTypeEnum.values();
        for (int i = 0; i < enumAry.length; i++) {
            if (enumAry[i].getValue() == value) {
                resultEnum = enumAry[i];
                break;
            }
        }
        return resultEnum;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List toList() {
        NotifyTypeEnum[] ary = NotifyTypeEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(ary[i].getValue()));
            map.put("desc", ary[i].getDesc());
            list.add(map);
        }
        return list;
    }

}
