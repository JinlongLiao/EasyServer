package io.github.jinlongliao.easy.server.kotlin.demo.logic.response

import io.github.jinlongliao.easy.server.extend.response.ICommonResponse
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory
import io.github.jinlongliao.easy.server.kotlin.demo.logic.response.stream.ResponseStreamFactory
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field

open class RootResponse(val status: Int) : ICommonResponse {

    override fun buildResponseStreamFactory(): IResponseStreamFactory {
        return ResponseStreamFactory()
    }

    override fun headerAppender(): List<Field> {
        return header!!
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
        private var header: List<Field>? = null

        init {
            val header1: List<Field>
            header1 = try {
                listOf(RootResponse::class.java.getDeclaredField("status"))
            } catch (e: NoSuchFieldException) {
                log.error(e.message, e)
                emptyList()
            }
            header = header1
        }
    }
}
