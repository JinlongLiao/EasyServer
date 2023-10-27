package io.github.jinlongliao.easy.server.kotlin.demo.logic

import io.github.jinlongliao.easy.server.cached.annotation.EnableCache
import io.github.jinlongliao.easy.server.core.annotation.LogicController
import io.github.jinlongliao.easy.server.core.annotation.LogicMapping
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshClass
import io.github.jinlongliao.easy.server.swagger.servlet.help.ApiCleanHelper
import org.slf4j.LoggerFactory
import org.springframework.context.support.ApplicationObjectSupport

/**
 * @author liaojinlong
 * @since 2021/1/22 19:06
 */
@RefreshClass
@EnableCache
@LogicController(desc = "测试示例2", value = "Logic-Test-2")
class LogicBean2(private val apiCleanHelper: ApiCleanHelper) : ApplicationObjectSupport() {
    @LogicMapping(value = ["112"], desc = "刷新ApiUi缓存")
    fun refreshApi() {
        apiCleanHelper.reset()
    }

    companion object {
        private val log = LoggerFactory.getLogger(LogicBean2::class.java)
    }
}
