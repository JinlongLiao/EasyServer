package io.github.jinlongliao.easy.server.cached.annotation;

import io.github.jinlongliao.easy.server.cached.aop.spring.handler.DefaultCacheHandler;
import io.github.jinlongliao.easy.server.cached.aop.spring.handler.ICacheHandler;

import java.lang.annotation.*;

/**
 * 获取缓存，不存在返回空
 *
 * @author liaojinlong
 * @since 2022-02-14 16:23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DelCache {
    /**
     * ID 前缀
     *
     * @return/
     */
    String value() default "delCache:";

    /**
     * 业务处理类
     *
     * @return /
     */
    Class<? extends ICacheHandler> handler() default DefaultCacheHandler.class;
}
