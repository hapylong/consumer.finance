/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月9日 下午1:51:02
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
public enum RetCodeEnum {

    INSTSUCCESS(10000, "操作成功"),
    PLANNOTEXIST(10004, "分期方案不存在"),
    LOCKINST(10005, "该时间咱不允许分期"), // 系统正在计算逾期
    OPENACCNOTEXIST(10003, "用户不存在对应的行业户"),
    REPEATINST(10001, "重复分期"),
    INSTEXCEPTION(10002, "分期数据保存异常");
    private int value;
    private String desc;

    /**
     * @param value
     * @param desc
     */
    private RetCodeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static RetCodeEnum getEnum(int value) {
        RetCodeEnum resultEnum = null;
        RetCodeEnum[] enumAry = RetCodeEnum.values();
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
        RetCodeEnum[] ary = RetCodeEnum.values();
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
        RetCodeEnum[] ary = RetCodeEnum.values();
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
        RetCodeEnum[] enums = RetCodeEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (RetCodeEnum senum : enums) {
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
