/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账户状态
 * @date 2016年7月18日 上午11:04:18
 * @version V1.0
 */

package com.iqb.consumer.common.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public enum StatusEnum {

    ACC_NORMAL(0, "正常"),
    ACC_FROZEN(1, "冷冻"),
    ACC_BLACK(2, "黑名单"),
    ACC_DISABLE(3, "禁用"),
    ACC_PROCESSING(4, "账户正在操作中"),
    BILL_OVERDUE(5, "过期账单"),
    BILL_USING(6, "账单期"),
    BILL_EXPECTED(7, "预期账单"),
    BILL_COMPLETE(8, "已还款账单"),
    DELAY_DELAY(9, "延期"),
    DELAY_REPAY(10, "支付"), ;
    private int value;
    private String desc;

    /**
     * @param value
     * @param desc
     */
    private StatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static StatusEnum getEnum(int value) {
        StatusEnum resultEnum = null;
        StatusEnum[] enumAry = StatusEnum.values();
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
        StatusEnum[] ary = StatusEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(ary[i].getValue()));
            map.put("desc", ary[i].getDesc());
            list.add(map);
        }
        return list;
    }

    public static Map<String, Map<String, Object>> toMap() {
        StatusEnum[] ary = StatusEnum.values();
        Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
        for (int num = 0; num < ary.length; num++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String key = String.valueOf(getEnum(ary[num].getValue()));
            map.put("value", String.valueOf(ary[num].getValue()));
            map.put("desc", ary[num].getDesc());
            enumMap.put(key, map);
        }
        return enumMap;
    }

    /**
     * 取枚举的json字符串
     * 
     * @return
     */
    public static String getJsonStr() {
        StatusEnum[] enums = StatusEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (StatusEnum senum : enums) {
            if (!"[".equals(jsonStr.toString())) {
                jsonStr.append(",");
            }
            jsonStr.append("{id:'").append(senum.toString()).append("',desc:'").append(senum.getDesc())
                    .append("',value:'").append(senum.getValue()).append("'}");
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }
}
