/**
 * Created on  13-09-19 20:56
 */
package io.github.jinlongliao.easy.server.cached.aop.spring;

import io.github.jinlongliao.easy.server.cached.CacheType;
import io.github.jinlongliao.easy.server.cached.annotation.DelCache;
import io.github.jinlongliao.easy.server.cached.annotation.EnableCache;
import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class CachePointcut extends StaticMethodMatcherPointcut implements ClassFilter {
    private static final Logger log = LoggerFactory.getLogger(CachePointcut.class);
    private final String[] basePackages;
    private final CacheConfig cacheConfig;

    public CachePointcut(String[] basePackages, CacheConfig cacheConfig) {
        setClassFilter(this);
        this.basePackages = basePackages;
        this.cacheConfig = cacheConfig;
    }


    private boolean exclude(String name) {
        if (name.startsWith("java")) {
            return true;
        }
        if (name.startsWith("org.springframework")) {
            return true;
        }
        return false;
    }


    @Override
    public boolean matches(Class<?> clazz) {
        EnableCache annotation = AnnotationUtils.getAnnotation(clazz, EnableCache.class);
        if (annotation == null) {
            return false;
        }
        String name = clazz.getName();
        if (name.contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
            return false;
        }
        if (exclude(name)) {
            return false;
        }
        for (String basePackage : basePackages) {
            boolean startsWith = name.startsWith(basePackage);
            if (startsWith) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Annotation annotation = null;
        if (annotation == null) {
            annotation = AnnotationUtils.getAnnotation(method, DelCache.class);
            casData(annotation, CacheType.DEL, method, targetClass);
        }
        if (annotation == null) {
            annotation = AnnotationUtils.getAnnotation(method, GetCache.class);
            casData(annotation, CacheType.GET, method, targetClass);
        }
        return annotation != null;
    }

    private void casData(Annotation annotation, CacheType cacheType, Method method, Class<?> targetClass) {
        if (annotation != null) {
            cacheConfig.addCachedMethod(method, cacheType, annotation, targetClass);
        }
    }
}
