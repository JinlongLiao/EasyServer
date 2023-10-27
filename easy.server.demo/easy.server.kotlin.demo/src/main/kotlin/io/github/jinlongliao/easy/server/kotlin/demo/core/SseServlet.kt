package io.github.jinlongliao.easy.server.kotlin.demo.core

import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet
import io.github.jinlongliao.easy.server.utils.common.DateUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException
import java.lang.invoke.MethodHandles
import java.util.*

/**
 * SSE Test
 *
 * @author: liaojinlong
 * @date: 2023/5/2 14:22
 */
@Component
class SseServlet : BaseHttpServlet<Any?>() {
    override fun supportPath(): Array<String> {
        return arrayOf("/sse")
    }

    @Throws(IOException::class)
    override fun todoLogic(req: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/event-stream"
        response.characterEncoding = "UTF-8"
        val writer = response.writer
        for (i in 0..19) {
            if (i and 1 == 1) {
                writer.write("event: diyEventType\n")
            }
            val id = "id: " + UUID.randomUUID().toString() + "\n"
            val retry = "retry: 100000\n"
            val data = "data: " + DateUtil.getStringDateTime() + "\n\n"
            writer.write(id)
            writer.write(retry)
            writer.write(data)
            writer.write(id)
            writer.write(retry)
            writer.write(data)
            writer.flush()
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                log.error(e.message, e)
            }
        }
        writer.close()
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
