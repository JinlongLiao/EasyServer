package io.github.jinlongliao.easy.server.kotlin.demo.logic.annotation

import io.github.jinlongliao.easy.server.core.core.MethodInfo
import io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodAnnotationProcess
import io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodDesc
import io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodDesc.MethodDesc
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method
import java.util.*
import java.util.stream.Collectors

/**
 * 额外解析
 *
 * @author: liaojinlong
 * @date: 2023/1/31 22:23
 */
class LogicExtraMethodAnnotationProcess : ExtraMethodAnnotationProcess {
    override fun extraProcessMethod(data: Map<String, MethodInfo>, method: Method): ExtraMethodDesc? {
        val logic = AnnotationUtils.getAnnotation(method, Logic::class.java)
        if (Objects.isNull(logic)) {
            return null
        }
        val desc = Arrays.stream(
            logic!!.value
        )
            .map { node: MsgId -> MethodDesc(node.logicId, node.desc) }
            .collect(Collectors.toList())
        return ExtraMethodDesc(desc)
    }
}
