package io.github.jinlongliao.easy.server.cached.annotation.process;


import io.github.jinlongliao.easy.server.cached.CacheType;
import io.github.jinlongliao.easy.server.cached.annotation.WeAsync;
import io.github.jinlongliao.easy.server.cached.aop.spring.handler.ICacheHandler;
import io.github.jinlongliao.easy.server.cached.exception.ExeTimeoutException;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheNode;
import io.github.jinlongliao.easy.server.cached.util.LocalMapCache;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.utils.common.DateUtil;
import io.github.jinlongliao.easy.server.utils.common.UUIDHelper;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 异步执行
 *
 * @author: liaojinlong
 * @date: 2023/8/7 17:31
 */
public class WeAsyncHandler implements ICacheHandler {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final int maxSize;
    private final LocalMapCache<Future<?>> futureLocalMapCache;

    public WeAsyncHandler(ThreadPoolTaskExecutor threadPoolTaskExecutor, int maxSize) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.maxSize = maxSize;
        this.futureLocalMapCache = new LocalMapCache<Future<?>>(maxSize) {
            @Override
            public void deleteCache(String cacheKey) {
                Future<?> future = getCache(cacheKey);
                if (Objects.nonNull(future)) {
                    if (future.isDone() || future.isCancelled()) {
                        future.cancel(true);
                    }
                }
                super.deleteCache(cacheKey);

            }
        };
    }

    @Override
    public Object cacheHandler(CacheNode cacheNode, Method method, MethodInvocation invocation) throws Throwable {
        if (!(cacheNode.getCacheType() == CacheType.ASYNC)) {
            log.warn("not weAsync:{}", cacheNode);
            return invocation.proceed();
        }
        WeAsync weAsync = (WeAsync) cacheNode.getAnnotation();
        try {
            return innerHandler(weAsync, method, invocation);
        } catch (ExeTimeoutException e) {
            Object defVal = this.defValue(weAsync.defaultVal(), method.getReturnType());
            if (Objects.nonNull(defVal)) {
                log.warn(e.getMessage(), e);
                return defVal;
            }
            throw e;
        }
    }

    private Object defValue(String defValStr, Class<?> returnType) {
        if (!StringUtils.hasText(defValStr)) {
            return null;
        }
        if (Objects.equals(Void.TYPE, returnType) || Objects.equals(Void.class, returnType)) {
            return null;
        }
        if (CLassUtils.isStringClass(returnType)) {
            return defValStr;
        }
        if (CLassUtils.isInteger(returnType)) {
            return Integer.parseInt(defValStr.trim());
        }
        if (CLassUtils.isBool(returnType)) {
            return Boolean.parseBoolean(defValStr.trim());
        }
        if (CLassUtils.isLong(returnType)) {
            return Long.parseLong(defValStr.trim());
        }
        if (Objects.equals(Date.class, returnType)) {
            return DateUtil.parseDateTime(defValStr);
        }

        return null;
    }

    private Object innerHandler(WeAsync weAsync, Method method, MethodInvocation invocation) throws Throwable {
        // task
        Callable<Object> task = () -> {
            try {
                return invocation.proceed();
            } catch (Throwable e) {
                throw new ExeTimeoutException(e);
            }
        };

        Class<?> returnType = method.getReturnType();
        if (CompletableFuture.class.isAssignableFrom(returnType)) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return invocation.proceed();
                } catch (Throwable e) {
                    throw new ExeTimeoutException(e);
                }
            }, this.threadPoolTaskExecutor);
        } else if (org.springframework.util.concurrent.ListenableFuture.class.isAssignableFrom(returnType)) {
            return threadPoolTaskExecutor.submitListenable(task);
        } else if (Future.class.isAssignableFrom(returnType)) {
            return this.threadPoolTaskExecutor.submit(task);
        }
        long maxTimeout = weAsync.maxTimeout();
        if (maxTimeout < 1) {
            maxTimeout = TimeUnit.MINUTES.toMillis(10);
        }
        Future<Object> submit = this.threadPoolTaskExecutor.submit(task);
        try {
            Object o;
            if (returnType.equals(Void.class) || returnType.equals(Void.TYPE) || this.futureLocalMapCache.getCurrentSize() > maxSize) {
                o = null;
                this.futureLocalMapCache.setCache(UUIDHelper.mongoId(), submit, maxTimeout);
            } else {
                o = submit.get(maxTimeout, TimeUnit.MILLISECONDS);
            }
            return o;
        } catch (TimeoutException e) {
            submit.cancel(true);
            throw new ExeTimeoutException(maxTimeout, method, e);
        } catch (Exception e) {
            throw new ExeTimeoutException(maxTimeout, method, e);
        }
    }

}
