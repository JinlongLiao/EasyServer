package io.github.jinlongliao.easy.server.cached.field.simple;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;
import io.github.jinlongliao.easy.server.cached.CacheType;

import java.lang.annotation.Annotation;

/**
 * @author liaojinlong
 * @since 2022-02-14
 */
public class SimpleCacheNode {
    private final DirectMethod directMethod;
    private final Class<?> targetClass;
    private final CacheType cacheType;
    private final Annotation annotation;

    public SimpleCacheNode(DirectMethod directMethod, Annotation annotation, Class<?> targetClass, CacheType cacheType) {
        this.directMethod = directMethod;
        this.targetClass = targetClass;
        this.annotation = annotation;
        this.cacheType = cacheType;
    }


    public DirectMethod getDirectMethod() {
        return directMethod;
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
