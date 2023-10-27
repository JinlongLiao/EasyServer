package io.github.jinlongliao.easy.server.kotlin.demo.logic.annotation

import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorMethod

/**
 * 自定义
 *
 * @author: liaojinlong
 * @date: 2023/1/31 22:19
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
)
@GeneratorMethod
@MustBeDocumented
annotation class Logic(
    /**
     * 标记
     *
     * @return /
     */
    vararg val value: MsgId
)
