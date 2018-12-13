/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月18日 下午7:06:02
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
public enum InstallTypeEnum {

    ACTUALTIME(1, "实时分期"),
    EATCHMONTH(2, "月对月"),
    FIXEDDAY(3, "固定日期");

    private int value;
    private String desc;

    /**
     * @param value
     * @param desc
     */
    private InstallTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static InstallTypeEnum getEnum(int value) {
        InstallTypeEnum resultEnum = null;
        InstallTypeEnum[] enumAry = InstallTypeEnum.values();
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
        InstallTypeEnum[] ary = InstallTypeEnum.values();
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
        InstallTypeEnum[] ary = InstallTypeEnum.values();
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
        InstallTypeEnum[] enums = InstallTypeEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (InstallTypeEnum senum : enums) {
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
