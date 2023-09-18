package io.github.jinlongliao.easy.server.cached.annotation;


import io.github.jinlongliao.easy.server.cached.annotation.process.WeAsyncHandler;
import io.github.jinlongliao.easy.server.cached.aop.spring.handler.ICacheHandler;

import java.lang.annotation.*;

/**
 * 自定义超时时间
 *
 * @author: liaojinlong
 * @date: 2023/8/7 17:18
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WeAsync {
    /**
     * 默认超时时间 毫秒
     *
     * @return /
     */
    long maxTimeout() default -1;

    /**
     * 业务处理类
     *
     * @return /
     */
    Class<? extends ICacheHandler> handler() default WeAsyncHandler.class;

    /**
     * 失败默认值
     */
    String defaultVal() default "";
}
