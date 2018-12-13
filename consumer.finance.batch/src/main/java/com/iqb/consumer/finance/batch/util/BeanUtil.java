package com.iqb.consumer.finance.batch.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BeanUtil {

    private static Gson gson = new Gson();

    /**
     * 将实体类转换为map
     * 
     * @param object
     * @return
     */
    public static Map<String, String> entity2map(Object object) {
        String str = JSON.toJSONString(object);
        return JSONObject.parseObject(str, Map.class);
    }

    /**
     * 将多个实体组装成一个Map
     * 
     * @param objects
     * @return
     */
    public static Map<String, String> packEntityToMap(Object... objects) {
        Map<String, String> map = new HashMap<String, String>();
        for (Object o : objects) {
            map.putAll(entity2map(o));
        }
        return map;
    }

    /**
     * 将json字符转转换为java对象
     * 
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T toJavaObject(String str, Class<T> clazz) {
        Object obj = new Object();
        obj = JSON.parseObject(str, clazz);
        return (T) obj;
    }

    /**
     * 
     * Description: 字符串转list
     * 
     * @param
     * @return List<T>
     * @throws
     * @Author wangxinbang Create Date: 2016年8月29日 下午6:38:32
     */
    public static <T> List<T> toJavaList(String str, Class<T> clazz) {
        if (StringUtil.isEmpty(str))
            return new ArrayList<T>();
        List<T> list = (List<T>) JSONArray.parseArray(str, clazz);
        return list;

    }

    /**
     * 将map转换为JavaBean
     * 
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        return toJavaObject(JSON.toJSONString(map), clazz);
    }

    public static Object getValueOfField(String propertyName, Object obj) {
        try {
            Class clazz = obj.getClass();
            PropertyDescriptor pd = new PropertyDescriptor(propertyName, clazz);
            Method getMethod = pd.getReadMethod();
            return getMethod.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * Description: 字符串转list
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年8月31日 下午5:32:49
     */
    public static <T> List<T> toJavaListFromGson(String str, Class<T> clazz) {
        if (StringUtil.isEmpty(str))
            return new ArrayList<T>();
        return gson.fromJson(str, new TypeToken<List<T>>() {}.getType());
    }

}
