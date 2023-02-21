/**
 * Created on  13-09-18 20:33
 */
package io.github.jinlongliao.easy.server.cached.aop.spring;

import io.github.jinlongliao.easy.server.cached.CacheType;
import io.github.jinlongliao.easy.server.cached.annotation.DelCache;
import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import io.github.jinlongliao.easy.server.cached.aop.spring.handler.ICacheHandler;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheConfig;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheNode;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author liaojinlong
 * @since 2022-02-14 16:48
 */
public class CacheInterceptor implements MethodInterceptor, ApplicationContextAware {
    private final CacheConfig cacheConfig;

    public CacheInterceptor(CacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        CacheNode cacheNode = cacheConfig.getCacheNode(method);
        Object proceed;
        if (cacheNode == null) {
            proceed = invocation.proceed();
        } else {
            proceed = cacheHandler(cacheNode, method, invocation);
        }
        return proceed;
    }

    private Object cacheHandler(CacheNode cacheNode, Method method, MethodInvocation invocation) throws Throwable {
        ICacheHandler handler = cacheNode.getCacheHandler();
        if (handler == null) {
            Class<? extends ICacheHandler> handlerClass;
            if (cacheNode.getCacheType() == CacheType.DEL) {
                handlerClass = ((DelCache) cacheNode.getAnnotation()).handler();
            } else {
                handlerClass = ((GetCache) cacheNode.getAnnotation()).handler();
            }
            handler = cacheConfig.getCacheHandler(handlerClass);
            cacheNode.setCacheHandler(handler);
        }
        return handler.cacheHandler(cacheNode, method, invocation);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ICacheHandler> beansOfType = applicationContext.getBeansOfType(ICacheHandler.class);
        for (ICacheHandler cacheHandler : beansOfType.values()) {
            cacheConfig.addCacheHandler(cacheHandler);
        }
    }
}
