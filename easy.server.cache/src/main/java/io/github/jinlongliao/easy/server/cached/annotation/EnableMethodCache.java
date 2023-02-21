package io.github.jinlongliao.easy.server.cached.annotation;

import io.github.jinlongliao.easy.server.cached.config.CacheProxyConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 配置切面的包信息
 *
 * @author liaojinlong
 * @since 2022-02-14 16:40
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CacheProxyConfiguration.class)
public @interface EnableMethodCache {
    /**
     * Indicate the ordering of the execution of the caching advisor
     * when multiple advices are applied at a specific joinpoint.
     * The default is {@link Ordered#LOWEST_PRECEDENCE}.
     */
    int order() default Ordered.LOWEST_PRECEDENCE;

    String[] basePackages();
}
