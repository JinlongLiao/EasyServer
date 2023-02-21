package io.github.jinlongliao.easy.server.cached.aop.spring.handler;

import io.github.jinlongliao.easy.server.cached.field.spring.CacheNode;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 无实现
 *
 * @author liaojinlong
 * @since 2022-02-15 10:37
 */
public class EmptyCacheHandler implements ICacheHandler {

    @Override
    public Object cacheHandler(CacheNode cacheNode, Method method, MethodInvocation invocation) throws Throwable {
        return invocation.proceed();
    }

}
