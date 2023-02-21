package io.github.jinlongliao.easy.server.cached.annotation;

import java.lang.annotation.*;

/**
 * 是否启用缓存
 *
 * @author liaojinlong
 * @since 2022-02-14 16:23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableCache {
    boolean value() default true;
}
