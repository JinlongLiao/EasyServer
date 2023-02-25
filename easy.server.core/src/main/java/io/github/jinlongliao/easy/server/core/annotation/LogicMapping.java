package io.github.jinlongliao.easy.server.core.annotation;


import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorMethod;

import java.lang.annotation.*;

/**
 * 消息处理标识
 *
 * @author liaojinlong
 * @since 2021/1/22 16:27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@GeneratorMethod
@Documented
public @interface LogicMapping {
    /**
     * 消息类型
     *
     * @return 处理的消息类型
     */
    String[] value();

    /**
     * 消息描述
     *
     * @return 消息描述
     */
    String desc() default "消息描述";
}
