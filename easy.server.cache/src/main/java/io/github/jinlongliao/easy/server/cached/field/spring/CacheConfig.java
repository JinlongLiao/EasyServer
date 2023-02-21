package io.github.jinlongliao.easy.server.cached.field.spring;

import io.github.jinlongliao.easy.server.cached.aop.spring.handler.EmptyCacheHandler;
import io.github.jinlongliao.easy.server.cached.aop.spring.handler.ICacheHandler;
import io.github.jinlongliao.easy.server.cached.CacheType;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaojinlong
 * @since 2022-02-14 19:58
 */
public class CacheConfig {
    private Map<Method, CacheNode> cacheNodeMap;
    private Map<Class<? extends ICacheHandler>, ICacheHandler> handlerMap;
    private EmptyCacheHandler emptyCacheHandler = new EmptyCacheHandler();

    public CacheConfig() {
        this(64);
    }

    public CacheConfig(int cap) {
        this.cacheNodeMap = new HashMap<>(cap);
        this.handlerMap = new HashMap<>(16);
    }

    public boolean cacheMethod(Method method) {
        return cacheNodeMap.containsKey(method);
    }

    public CacheNode getCacheNode(Method method) {
        return this.cacheNodeMap.get(method);
    }

    public Map<Method, CacheNode> getCacheNodeMap() {
        return cacheNodeMap;
    }

    public void addCachedMethod(Method method, CacheType cacheType, Annotation annotation, Class<?> targetClass) {
        this.cacheNodeMap.put(method, new CacheNode(method, annotation, targetClass, cacheType));
    }

    public void addCacheHandler(ICacheHandler cacheHandler) {
        this.handlerMap.put((Class<? extends ICacheHandler>) ClassUtils.getUserClass(cacheHandler.getClass()), cacheHandler);
    }

    public ICacheHandler getCacheHandler(Class<? extends ICacheHandler> key) {
        ICacheHandler cacheHandler = this.handlerMap.get(key);
        return cacheHandler == null ? emptyCacheHandler : cacheHandler;
    }
}
