package io.github.jinlongliao.easy.server.cached.aop.simple;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;
import io.github.jinlongliao.easy.server.cached.CacheType;
import io.github.jinlongliao.easy.server.cached.annotation.EnableCache;
import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleDelCache;
import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache;
import io.github.jinlongliao.easy.server.cached.aop.simple.handler.DefaultSimpleCacheHandler;
import io.github.jinlongliao.easy.server.cached.aop.simple.handler.ISimpleCacheHandler;
import io.github.jinlongliao.easy.server.cached.aop.simple.handler.WrapDirectMethod;
import io.github.jinlongliao.easy.server.cached.field.simple.SimpleCacheNode;
import io.github.jinlongliao.easy.server.core.core.spring.register.MethodPostProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * 简易版 切面处理
 *
 * @author: liaojinlong
 * @date: 2022-11-24 14:41
 */
public class SimplePointcutAndHandler implements MethodPostProcess {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ListableBeanFactory listableBeanFactory;
    private final String[] basePackages;

    public SimplePointcutAndHandler(ListableBeanFactory listableBeanFactory, String[] basePackages) {
        this.listableBeanFactory = listableBeanFactory;
        this.basePackages = basePackages;
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


    public boolean matchesClass(Class<?> clazz) {
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

    public Annotation matches(Method method, Class<?> targetClass) {
        if (!matchesClass(targetClass)) {
            return null;
        }
        Annotation annotation = AnnotationUtils.getAnnotation(method, SimpleDelCache.class);
        if (annotation == null) {
            annotation = AnnotationUtils.getAnnotation(method, SimpleGetCache.class);
        }
        return annotation;
    }


    @Override
    public DirectMethod process(Class<?> userClass, DirectMethod directMethod) {
        Annotation matches = this.matches(directMethod.getMethod(), userClass);
        if (matches == null) {
            return directMethod;
        }
        boolean pass = false;
        String userClassName = userClass.getName();
        for (String basePackage : basePackages) {
            if (userClassName.startsWith(basePackage)) {
                pass = true;
                break;
            }
        }
        if (!pass) {
            log.warn("ignore class {}", userClassName);
            return directMethod;
        }
        CacheType cacheType;
        Class<? extends ISimpleCacheHandler> handler;
        if (matches instanceof SimpleDelCache) {
            cacheType = CacheType.DEL;
            handler = ((SimpleDelCache) matches).handler();
        } else if (matches instanceof SimpleGetCache) {
            cacheType = CacheType.GET;
            handler = ((SimpleGetCache) matches).handler();
        } else {
            return directMethod;
        }
        ISimpleCacheHandler contextBean;
        if (handler == DefaultSimpleCacheHandler.class) {
            contextBean = DefaultSimpleCacheHandler.getInstance();
        } else {
            try {
                contextBean = this.listableBeanFactory.getBean(handler);
            } catch (RuntimeException e) {
                log.warn("find class {} error ", handler.getName());
                return directMethod;
            }
        }
        SimpleCacheNode simpleCacheNode = new SimpleCacheNode(directMethod, matches, userClass, cacheType);
        return new WrapDirectMethod(simpleCacheNode, contextBean);
    }

}
