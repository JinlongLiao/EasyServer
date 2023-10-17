package io.github.jinlongliao.easy.server.kotlin.demo.logic.annotation

import io.github.jinlongliao.easy.server.core.annotation.LogicAlias
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam

/**
 * @author: liaojinlong
 * @date: 2022-09-16 10:00
 */
@LogicRequestParam(value = "userId", length = 4, defaultValue = "234")
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class UserId(
    /**
     * 参数名称
     *
     * @return 参数名称
     */
    @get:LogicAlias("value") val newV: String = "userId"
)
