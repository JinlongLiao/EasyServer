package io.github.jinlongliao.easy.server.kotlin.demo.logic

import io.github.jinlongliao.easy.server.cached.annotation.EnableCache
import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache
import io.github.jinlongliao.easy.server.core.annotation.*
import io.github.jinlongliao.easy.server.kotlin.demo.logic.annotation.UserId
import io.github.jinlongliao.easy.server.kotlin.demo.logic.response.TestResponse
import io.github.jinlongliao.easy.server.kotlin.demo.logic.service.TestAsyncService
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshClass
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.context.support.ApplicationObjectSupport
import org.springframework.util.Assert
import java.lang.invoke.MethodHandles

/**
 * @author liaojinlong
 * @since 2021/1/22 19:06
 */
@RefreshClass
@EnableCache
@LogicController(desc = "测试示例")
class LogicBean(
    private val testAsyncService: TestAsyncService
) : ApplicationObjectSupport() {
    @LogicMapping(value = ["100"], desc = "测试组合注解")
    fun testAnn(@UserId userId: Int, @LogicRequestIp clientIp: String?) {
        log.info("userId: {}\t clientIp: {}", userId, clientIp)
    }

    @LogicMapping(value = ["101"], desc = "Hex Response")
    @SimpleGetCache(keyValueEl = "newUserId")
    fun testHex(
        @LogicAlias("newUserId") @UserId(newV = "newUserId") userId: Int,
        @HttpRequest request: HttpServletRequest?,
        @HttpResponse response: HttpServletResponse?,
        @LogicRequestIp clientIp: String?
    ): TestResponse {
        log.info("userId: {}\t clientIp: {}", userId, clientIp)
        Assert.notNull(request, "NOT NULL")
        Assert.notNull(response, "NOT NULL")
        return TestResponse(userId, "ABCD", "ABCD")
    }

    @LogicMapping(value = ["500"], desc = "用于报错的接口")
    fun error(): Any {
        throw RuntimeException("error")
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
