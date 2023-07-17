package io.github.jinlongliao.easy.server.cached.aop.simple.handler;


import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache;
import io.github.jinlongliao.easy.server.cached.aop.CasIfAbsent;
import io.github.jinlongliao.easy.server.cached.aop.el.ParamElParserBuilder;
import io.github.jinlongliao.easy.server.cached.exception.AccessLimitException;
import io.github.jinlongliao.easy.server.cached.field.simple.SimpleCacheNode;

import java.time.Duration;

/**
 * @author: liaojinlong
 * @date: 2023/7/5 11:43
 */
public class SimpleLimitPerAccessFilterHandler implements ISimpleCacheHandler {
    private final CasIfAbsent casIfAbsent;

    public SimpleLimitPerAccessFilterHandler(CasIfAbsent casIfAbsent) {
        this.casIfAbsent = casIfAbsent;
    }

    @Override
    public Object cacheHandler(SimpleCacheNode cacheNode, Object target, Object... params) throws Exception {
        if (cacheNode.getAnnotation() instanceof SimpleGetCache ) {
            SimpleGetCache getCache = (SimpleGetCache) cacheNode.getAnnotation();
            long milliSecond = getCache.milliSecond();
            int i = getCache.argsIndex();
            StringBuilder keyBuffer = new StringBuilder(cacheNode.getMethodFullName());
            String key;
            if (params.length >= i) {
                Object argument = params[i];
                key = ParamElParserBuilder.putElValue(keyBuffer, argument, i, cacheNode.getDirectMethod().getMethod(), getCache.keyValueEl());
            } else {
                key = keyBuffer.toString();
            }
            boolean absentDuration = this.casIfAbsent.setKeyIfAbsentDuration(key, Duration.ofMillis(milliSecond));
            if (!absentDuration) {
                throw new AccessLimitException();
            }
        }
        return cacheNode.getDirectMethod().invoke(target, params);
    }
}
