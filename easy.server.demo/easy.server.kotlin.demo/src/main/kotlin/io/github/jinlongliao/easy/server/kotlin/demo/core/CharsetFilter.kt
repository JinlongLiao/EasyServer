package io.github.jinlongliao.easy.server.kotlin.demo.core

import io.github.jinlongliao.easy.server.servlet.BaseHttpFilter
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.io.IOException

/**
 * @author: liaojinlong
 * @date: 2022-06-28 16:33
 */
@Component
class CharsetFilter : BaseHttpFilter() {
    override fun supportPath(): Array<String> {
        return arrayOf("/*")
    }

    @Throws(IOException::class, ServletException::class)
    override fun doLogicFilter(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        request.characterEncoding = "UTF-8"
        return true
    }

    override fun isAsync(): Boolean {
        return true
    }
}
