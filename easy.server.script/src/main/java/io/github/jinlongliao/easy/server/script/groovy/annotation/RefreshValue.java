package io.github.jinlongliao.easy.server.script.groovy.annotation;

import java.lang.annotation.*;

/**
 * 标识此字段支持热更新
 *
 * @author liaojinlong
 * @since 2022-02-18 15:54
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RefreshValue {
}
