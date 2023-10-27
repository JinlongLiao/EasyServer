package io.github.jinlongliao.easy.server.core.annotation;

import java.lang.annotation.*;

/**
 * @date 2023-02-15 20:12
 * @author: liaojinlong
 * @description: /
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
public @interface LogicAlias {
    /**
     * 定义的别名
     */
    String value();
}
