/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月14日 上午11:47:36
 * @version V1.0
 */

package com.iqb.consumer.common.utils.sign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class DigestUtils {
    public static String getSignData(String signName, Map<String, Object> params) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if (signName.equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            value = null == value ? "" : value;
            content.append("&" + key + "=" + value);
        }
        String str = content.toString();
        if (isEmpty(str)) {
            return str;
        }
        if (str.startsWith("&")) {
            return str.substring("&".length());
        }
        return str;
    }

    private static boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0);
    }
}
