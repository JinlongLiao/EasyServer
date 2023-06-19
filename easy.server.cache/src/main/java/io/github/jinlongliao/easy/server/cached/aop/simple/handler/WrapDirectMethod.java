package io.github.jinlongliao.easy.server.cached.aop.simple.handler;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;
import io.github.jinlongliao.easy.server.cached.field.simple.SimpleCacheNode;

import java.lang.reflect.Method;


/**
 * @author: liaojinlong
 * @date: 2022-11-24 17:`01
 */
public class WrapDirectMethod extends DirectMethod {
    private final SimpleCacheNode cacheNode;
    private final ISimpleCacheHandler simpleCacheHandler;


    public WrapDirectMethod(SimpleCacheNode cacheNode, ISimpleCacheHandler simpleCacheHandler) {
        super(null);
        this.cacheNode = cacheNode;
        this.simpleCacheHandler = simpleCacheHandler;
    }

    @Override
    public Object invoke(Object obj, Object... args) throws Exception {
        return simpleCacheHandler.cacheHandler(cacheNode, obj, args);
    }

    @Override
    public Method getMethod() {
        return this.cacheNode.getDirectMethod().getMethod();
    }

    @Override
    public String getName() {
        return this.cacheNode.getDirectMethod().getName();
    }
}
