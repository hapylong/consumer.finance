/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月17日 上午10:14:01
 * @version V1.0
 */
package com.iqb.consumer.virtual.account.https;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.iqb.consumer.common.utils.httpclient.SimpleHttpUtils;
import com.iqb.consumer.virtual.account.utils.SendHttpsUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class SendHttpsServiceTest {

    //
    @Test
    public void testHttps() {
        String url = "https://112.126.81.154/consumer.manage.front/nr/noticeQuickResult";
        Map<String, String> params = new HashMap<String, String>();
        params.put("tradeNo", "201611161002241031610008372928");
        // SendHttpsUtil.postMsg4GetMap(url, params);
        String str = SimpleHttpUtils.httpPost(url, params);
        System.out.println(str);
    }
}
