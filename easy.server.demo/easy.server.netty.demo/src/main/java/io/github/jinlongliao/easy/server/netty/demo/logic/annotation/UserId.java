package io.github.jinlongliao.easy.server.netty.demo.logic.annotation;

import io.github.jinlongliao.easy.server.core.annotation.LogicAlias;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: liaojinlong
 * @date: 2022-09-16 10:00
 */
@LogicRequestParam(value = "userId", length = 4, defaultValue = "234")
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserId {
    /**
     * 参数名称
     *
     * @return 参数名称
     */
    @LogicAlias("value")
    String newV() default "userId";
}
