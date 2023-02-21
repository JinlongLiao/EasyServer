package io.github.jinlongliao.easy.server.cached.field.spring;

import io.github.jinlongliao.easy.server.cached.aop.spring.handler.ICacheHandler;
import io.github.jinlongliao.easy.server.cached.CacheType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author liaojinlong
 * @since 2022-02-14
 */
public class CacheNode {
    private final Method method;
    private final Class<?> targetClass;
    private final CacheType cacheType;
    private final Annotation annotation;
    private ICacheHandler cacheHandler;
    public CacheNode(Method method, Annotation annotation, Class<?> targetClass, CacheType cacheType) {
        this.method = method;
        this.targetClass = targetClass;
        this.annotation = annotation;
        this.cacheType = cacheType;
    }

    public ICacheHandler getCacheHandler() {
        return cacheHandler;
    }

    public void setCacheHandler(ICacheHandler cacheHandler) {
        this.cacheHandler = cacheHandler;
    }

    public Method getMethod() {
        return method;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }


    public CacheType getCacheType() {
        return cacheType;
    }

}
