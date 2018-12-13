package com.iqb.consumer.web.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Description: request工具
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月18日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class RequestUtil {

    /**
     * 
     * Description: 转json
     * 
     * @param
     * @return JSONObject
     * @throws
     * @Author wangxinbang Create Date: 2016年11月18日 下午6:05:06
     */
    public static JSONObject toJSON(HttpServletRequest request) {
        Map<String, String> param = new HashMap<String, String>();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement();
            String para = request.getParameter(paraName);
            param.put(paraName, para.trim());
        }
        return JSONObject.parseObject(JSONObject.toJSONString(param));
    }

}
