package io.github.jinlongliao.easy.server.cached.aop.spring.handler;


import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import io.github.jinlongliao.easy.server.cached.aop.CasIfAbsent;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheNode;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author: liaojinlong
 * @date: 2023/7/5 11:43
 */
public class PerAccessFilterHandler implements ICacheHandler {
    private final CasIfAbsent casIfAbsent;

    public PerAccessFilterHandler(CasIfAbsent casIfAbsent) {
        this.casIfAbsent = casIfAbsent;
    }

    @Override
    public Object cacheHandler(CacheNode cacheNode, Method method, MethodInvocation invocation) throws Throwable {
         if (cacheNode.getAnnotation() instanceof GetCache getCache) {
            long milliSecond = getCache.milliSecond();
            int i = getCache.argsIndex();
            Object argument = invocation.getArguments()[i];
            StringBuilder keyBuffer = new StringBuilder(cacheNode.getMethodFullName());
            this.casIfAbsent.setKeyIfAbsentDuration(keyBuffer.toString(), Duration.ofMillis(milliSecond));

        }
//        method.
        return invocation.proceed();
    }
}
