package io.github.jinlongliao.easy.server.cached.aop.spring.handler;


import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import io.github.jinlongliao.easy.server.cached.aop.CasIfAbsent;
import io.github.jinlongliao.easy.server.cached.aop.el.ParamElParserBuilder;
import io.github.jinlongliao.easy.server.cached.exception.AccessLimitException;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheNode;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author: liaojinlong
 * @date: 2023/7/5 11:43
 */
public class PerAccessLimitFilterHandler implements ICacheHandler {
    private final CasIfAbsent casIfAbsent;

    public PerAccessLimitFilterHandler(CasIfAbsent casIfAbsent) {
        this.casIfAbsent = casIfAbsent;
    }

    @Override
    public Object cacheHandler(CacheNode cacheNode, Method method, MethodInvocation invocation) throws Throwable {
        if (cacheNode.getAnnotation() instanceof GetCache getCache) {
            long milliSecond = getCache.milliSecond();
            int i = getCache.argsIndex();
            Object[] params = invocation.getArguments();
            StringBuilder keyBuffer = new StringBuilder(cacheNode.getMethodFullName());
            String key;
            if (params.length >= i) {
                Object argument = params[i];
                key = ParamElParserBuilder.putElValue(keyBuffer, argument, i, method, getCache.keyValueEl());
            } else {
                key = keyBuffer.toString();
            }
            boolean absentDuration = this.casIfAbsent.setKeyIfAbsentDuration(key, Duration.ofMillis(milliSecond));
            if (!absentDuration) {
                throw new AccessLimitException();
            }
        }
//        method.
        return invocation.proceed();
    }
}
