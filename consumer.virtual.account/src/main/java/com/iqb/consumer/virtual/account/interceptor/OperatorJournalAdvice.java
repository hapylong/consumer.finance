/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月15日 上午11:54:47
 * @version V1.0
 */
package com.iqb.consumer.virtual.account.interceptor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.iqb.consumer.data.layer.mysql.bean.sys.SysOperateInfo;
import com.iqb.consumer.service.sys.JournalService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Aspect
public class OperatorJournalAdvice {
    private static final Logger logger = LoggerFactory.getLogger(OperatorJournalAdvice.class);
    private static final String JOURNAL_CREATE_BY = "OperatorJournalAdvice";
    private static final Set<String> SENSTIVE_METHODS;
    private static final boolean enable = true; // currently disable it
    static {
        final String METHOD_NAME_LOGIN = "login";
        final String METHOD_NAME_PASSWD_RESET = "passwdReset";
        final String METHOD_NAME_UPDATE_PASSWORD = "updatePassword";
        final String METHOD_NAME_UPDATE_PAYMENT_PASSWORD = "updatePaymentPassword";
        final Set<String> methodsContainSensitiveInfo = new HashSet<>();
        methodsContainSensitiveInfo.add(METHOD_NAME_LOGIN);
        methodsContainSensitiveInfo.add(METHOD_NAME_PASSWD_RESET);
        methodsContainSensitiveInfo.add(METHOD_NAME_UPDATE_PASSWORD);
        methodsContainSensitiveInfo.add(METHOD_NAME_UPDATE_PAYMENT_PASSWORD);

        SENSTIVE_METHODS = Collections.unmodifiableSet(methodsContainSensitiveInfo);
    }

    @Resource
    private JournalService journalService;

    @AfterReturning("@annotation(com.iqb.consumer.common.journal.InsertJournal)")
    public void afterReturning(JoinPoint joinPoint) throws Throwable {
        try {
            if (enable) {
                this.doJournal(joinPoint);
            }
        } catch (Exception e) {
            logger.warn("inserting Journal exception: {}", e);
            // swallow the error, as it is not vital
        }
    }

    @Async
    public void doJournal(JoinPoint jp) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("JoinPoint: this={}, target={}, args={}, signature={}",
                    new Object[] {jp.getThis(), jp.getTarget(), jp.getArgs(), jp.getSignature()});
        }
        SysOperateInfo journal = new SysOperateInfo();
        journal.setClassName(this.cleanupContent(jp.getSignature().getName(), jp.getArgs()));
        journal.setMethod(jp.getSignature().getName());
        journal.setReason("AUTO");
        journal.setCreateBy("OperatorJournalAdvice");
        this.journalService.insert(journal);
    }

    private String cleanupContent(String methodName, Object[] args) {
        Object[] argsCopy = args;
        if (SENSTIVE_METHODS.contains(methodName)) {
            if (args.length == 2) {
                argsCopy[1] = ""; // erase the password for security consideration
            }
        }

        String content = ArrayUtils.toString(argsCopy);
        String c = content.trim().replaceAll(".*\\[", "").replaceAll("\\].*", "").replaceAll(".*\\{", "")
                .replaceAll("\\}.*", "");
        if (c.length() > 255) {
            c = c.substring(0, 255);
        }
        return c;
    }

}
