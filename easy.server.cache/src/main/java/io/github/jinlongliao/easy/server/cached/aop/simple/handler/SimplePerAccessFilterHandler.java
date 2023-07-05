package io.github.jinlongliao.easy.server.cached.aop.simple.handler;


import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache;
import io.github.jinlongliao.easy.server.cached.aop.CasIfAbsent;
import io.github.jinlongliao.easy.server.cached.field.simple.SimpleCacheNode;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheNode;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author: liaojinlong
 * @date: 2023/7/5 11:43
 */
public class SimplePerAccessFilterHandler implements ISimpleCacheHandler {
    private final CasIfAbsent casIfAbsent;

    public SimplePerAccessFilterHandler(CasIfAbsent casIfAbsent) {
        this.casIfAbsent = casIfAbsent;
    }

    @Override
    public Object cacheHandler(SimpleCacheNode cacheNode, Object target, Object... params) throws Exception {
        if (cacheNode.getAnnotation() instanceof SimpleGetCache getCache) {
            long milliSecond = getCache.milliSecond();
            int i = getCache.argsIndex();
            Object argument = params[i];
            StringBuilder keyBuffer = new StringBuilder(cacheNode.getMethodFullName());
            this.casIfAbsent.setKeyIfAbsentDuration(keyBuffer.toString(), Duration.ofMillis(milliSecond));

        }
//        method.
        return cacheNode.getDirectMethod().invoke(target, params);
    }
}
