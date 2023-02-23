package io.github.jinlongliao.easy.server.boot.demo.logic.annotation;

import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorMethod;

import java.lang.annotation.*;

/**
 * 自定义
 *
 * @author: liaojinlong
 * @date: 2023/1/31 22:19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@GeneratorMethod
@Documented
public @interface Logic {
    /**
     * 标记
     *
     * @return /
     */
    MsgId[] value();
}
