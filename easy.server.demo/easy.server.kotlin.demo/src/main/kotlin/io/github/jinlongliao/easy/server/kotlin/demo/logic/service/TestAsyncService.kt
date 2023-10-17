package io.github.jinlongliao.easy.server.kotlin.demo.logic.service

import io.github.jinlongliao.easy.server.cached.annotation.EnableCache
import io.github.jinlongliao.easy.server.cached.annotation.WeAsync
import io.github.jinlongliao.easy.server.utils.common.UUIDHelper
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.lang.invoke.MethodHandles

/**
 * 测试异步
 *
 * @author: liaojinlong
 * @date: 2023/9/18 21:29
 */
@Service
@EnableCache
class TestAsyncService {
    @Async
    fun testAsync() {
        log.info("threadId:{} name:{}", Thread.currentThread().id, Thread.currentThread().name)
    }

    @WeAsync(maxTimeout = 1000L)
    fun testWeAsync(block: Boolean): Any? {
        if (block) {
            try {
                Thread.sleep(20000.toLong())
            } catch (e: InterruptedException) {
                log.error(e.message, e)
                return e.message
            }
        }
        log.info("threadId:{} name:{}", Thread.currentThread().id, Thread.currentThread().name)
        return UUIDHelper.mongoId()
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
