package io.github.jinlongliao.easy.server.cached.aop.spring.handler;

import io.github.jinlongliao.easy.server.cached.util.LocalMapCache;
import io.github.jinlongliao.easy.server.cached.CacheType;
import io.github.jinlongliao.easy.server.cached.annotation.DelCache;
import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheNode;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 默认实现
 *
 * @author liaojinlong
 * @since 2022-02-15 10:37
 */
public class DefaultCacheHandler implements ICacheHandler {
    private LocalMapCache<Object> cache = new LocalMapCache<>(2 << 12);

    @Override
    public Object cacheHandler(CacheNode cacheNode, Method method, MethodInvocation invocation) throws Throwable {
        final CacheType cacheType = cacheNode.getCacheType();
        if (cacheType == CacheType.NONE) {
            return invocation.proceed();
        }
        Object[] arguments = invocation.getArguments();
        String cacheKey = buildKey(cacheType, cacheNode.getAnnotation(), method, arguments);
        if (cacheType == CacheType.DEL) {
            cache.deleteCache(cacheKey);
            return null;
        }
        GetCache getCache = (GetCache) cacheNode.getAnnotation();
        Object obj = cache.getCache(cacheKey);
        if (obj == null) {
            obj = invocation.proceed();
            cache.setCache(cacheKey, obj, getCache.second() * 1000);
        }
        return obj;
    }

    private String buildKey(CacheType cacheType, Annotation annotation, Method method, Object[] arguments) {
        StringBuilder builder = new StringBuilder();
        if (cacheType == CacheType.DEL) {
            builder.append(((DelCache) annotation).value());
        } else if (cacheType == CacheType.GET) {
            builder.append(((GetCache) annotation).value());
        }
        builder.append(":");
        builder.append(method.getName());
        for (Object argument : arguments) {
            builder.append(":");
            builder.append(argument);
        }
        return builder.toString();
    }
}
