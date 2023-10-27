package io.github.jinlongliao.easy.server.kotlin.demo.core

import io.github.jinlongliao.easy.server.kotlin.demo.logic.param.UserModel
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet
import jakarta.servlet.MultipartConfigElement
import jakarta.servlet.ServletContext
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRegistration
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest
import java.io.IOException

/**
 * @author: liaojinlong
 * @date: 2022/5/28 19:46
 */
@Component
class ServletDemo : BaseHttpServlet<UserModel?>() {
    override fun supportPath(): Array<String> {
        return arrayOf("/test")
    }

    @Throws(ServletException::class, IOException::class)
    override fun todoLogic(req: HttpServletRequest, resp: HttpServletResponse) {
        val requestBody = getRequestBody(req)
        resp.writer.write(requestBody.toString())
    }

    override fun handleRequest(request: HttpServletRequest): HttpServletRequest {
        return if (StringUtils.startsWithIgnoreCase(request.contentType, "multipart/form-data")) {
            StandardMultipartHttpServletRequest(request)
        } else request
    }

    override fun extraConfig(dynamic: ServletRegistration.Dynamic, servletContext: ServletContext) {
        super.extraConfig(dynamic, servletContext)
        dynamic.setMultipartConfig(MultipartConfigElement(""))
    }

    override fun getDescription(): String {
        return "测试Servlet"
    }
}
