package io.github.jinlongliao.easy.server.cached.aop.simple.handler;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.method.DirectMethod;
import io.github.jinlongliao.easy.server.cached.CacheType;
import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleDelCache;
import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache;
import io.github.jinlongliao.easy.server.cached.field.simple.SimpleCacheNode;
import io.github.jinlongliao.easy.server.cached.util.LocalMapCache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 默认实现
 *
 * @author liaojinlong
 * @since 2022-02-15 10:37
 */
public class DefaultSimpleCacheHandler implements ISimpleCacheHandler {
    private final LocalMapCache<Object> cache = new LocalMapCache<>(2 << 12);


    private String buildKey(CacheType cacheType, Annotation annotation, Method method, Object[] arguments) {
        StringBuilder builder = new StringBuilder();
        if (cacheType == CacheType.DEL) {
            builder.append(((SimpleDelCache) annotation).value());
        } else if (cacheType == CacheType.GET) {
            builder.append(((SimpleGetCache) annotation).value());
        }
        builder.append(":");
        builder.append(method.getName());
        for (Object argument : arguments) {
            builder.append(":");
            builder.append(argument);
        }
        return builder.toString();
    }

    @Override
    public Object cacheHandler(SimpleCacheNode cacheNode, Object target, Object... params) throws Exception {
        final CacheType cacheType = cacheNode.getCacheType();
        DirectMethod directMethod = cacheNode.getDirectMethod();
        if (cacheType == CacheType.NONE) {
            return directMethod.invoke(target, params);
        }
        String cacheKey = buildKey(cacheType, cacheNode.getAnnotation(), directMethod.getMethod(), params);
        if (cacheType == CacheType.DEL) {
            cache.deleteCache(cacheKey);
            return null;
        }
        SimpleGetCache getCache = (SimpleGetCache) cacheNode.getAnnotation();
        Object obj = cache.getCache(cacheKey);
        if (obj == null) {
            obj = directMethod.invoke(target, params);
            cache.setCache(cacheKey, obj, getCache.second() * 1000);
        }
        return obj;
    }

    /**
     * 本实例
     */
    private static DefaultSimpleCacheHandler instance;

    /**
     * 私有的构造方法
     */
    private DefaultSimpleCacheHandler() {
    }

    /**
     * 单例的实例
     *
     * @return this
     */
    public static DefaultSimpleCacheHandler getInstance() {
        if (instance == null) {
            synchronized (DefaultSimpleCacheHandler.class) {
                if (instance == null) {
                    instance = new DefaultSimpleCacheHandler();
                }
            }
        }
        return instance;
    }
}
