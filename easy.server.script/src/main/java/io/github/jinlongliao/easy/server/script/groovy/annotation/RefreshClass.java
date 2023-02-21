package io.github.jinlongliao.easy.server.script.groovy.annotation;

import java.lang.annotation.*;

/**
 * 标明此类中存在需要热更新的属性
 *
 * @author liaojinlong
 * @since 2022-02-18 15:53
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RefreshClass {
}
