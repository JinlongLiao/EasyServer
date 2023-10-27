package io.github.jinlongliao.easy.server.kotlin.demo.logic.param

import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorCopy
import io.github.jinlongliao.easy.server.mapper.annotation.Mapping
import jakarta.validation.constraints.NotNull
import lombok.Data
import lombok.EqualsAndHashCode

/**
 * @author liaojinlong
 * @since 2021/2/22 23:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@GeneratorCopy
class UserModel {
    @LogicRequestParam("userId")
    private val userId: @NotNull String? = null

    @LogicRequestParam("age")
    private val age: Int? = null

    @LogicRequestParam("key")
    @Mapping(sourceName = "logicId")
    private val key: Int? = null
}
