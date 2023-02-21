package io.github.jinlongliao.easy.server.cached.aop.spring.handler;

import io.github.jinlongliao.easy.server.cached.field.spring.CacheNode;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 缓存处理接口
 *
 * @author liaojinlong
 * @since 2022-02-15 10:26
 */
public interface ICacheHandler {
    /**
     * 缓存处理业务
     *
     * @param cacheNode
     * @param method
     * @param invocation
     * @return /
     * @throws Throwable
     */
    Object cacheHandler(CacheNode cacheNode, Method method, MethodInvocation invocation) throws Throwable;
}
