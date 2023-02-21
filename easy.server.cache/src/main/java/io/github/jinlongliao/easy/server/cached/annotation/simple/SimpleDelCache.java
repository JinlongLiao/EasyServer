package io.github.jinlongliao.easy.server.cached.annotation.simple;

import io.github.jinlongliao.easy.server.cached.aop.simple.handler.DefaultSimpleCacheHandler;
import io.github.jinlongliao.easy.server.cached.aop.simple.handler.ISimpleCacheHandler;

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
public @interface SimpleDelCache {
    /**
     * ID 前缀
     *
     * @return/
     */
    String value() default "simpleDelCache:";

    /**
     * 业务处理类
     *
     * @return /
     */
    Class<? extends ISimpleCacheHandler> handler() default DefaultSimpleCacheHandler.class;
}
