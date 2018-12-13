/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月13日 下午5:11:56
 * @version V1.0
 */

package com.iqb.consumer.virtual.account.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.iqb.consumer.common.utils.IPUtil;
import com.iqb.consumer.common.utils.StringUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class WorkerInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /** 不拦截校验的访问资源 **/
        String[] excludeRegex =
                new String[] {".*js/.*", ".*consumer.web/.*", ".*css/.*", ".*images/.*", ".*login$", ".*logout$",
                        ".*unIntcpt-.*", ".*captcha*", ".*cps/.*"};
        String[] bizcludeRegex =
                new String[] {".*install/.*", ".*bill/.*", ".*account/.*", ".*notice/.*", ".*product/.*"};
        String uri = request.getRequestURI();
        if (!StringUtil.matches(uri, excludeRegex) && !StringUtil.matches(uri, bizcludeRegex)) {
            logger.info("“{}”访问“{}”受限", IPUtil.getIpAddress(request), uri);
        }
        logger.info("“{}”访问“{}”成功", IPUtil.getIpAddress(request), uri);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {}

}
