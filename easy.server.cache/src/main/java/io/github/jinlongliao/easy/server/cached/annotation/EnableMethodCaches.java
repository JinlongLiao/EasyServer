package io.github.jinlongliao.easy.server.cached.annotation;

import io.github.jinlongliao.easy.server.cached.config.RepeatingCacheProxyConfiguration;
import org.springframework.context.annotation.Import;

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
@Import(RepeatingCacheProxyConfiguration.class)
public @interface EnableMethodCaches {
    EnableMethodCache[] value();
}
