package io.github.jinlongliao.easy.server.kotlin.demo.logic.response

import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam
import io.github.jinlongliao.easy.server.extend.annotation.GeneratorResponse

@GeneratorResponse
class TestResponse @JvmOverloads constructor(
    status: Int = 0, @field:LogicRequestParam(length = 20) val name: String = "", @field:LogicRequestParam(
        dynamicLength = true
    ) val age: String = ""
) : RootResponse(status)
