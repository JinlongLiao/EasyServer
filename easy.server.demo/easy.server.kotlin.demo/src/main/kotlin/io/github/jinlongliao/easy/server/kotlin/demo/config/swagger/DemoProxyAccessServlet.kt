package io.github.jinlongliao.easy.server.kotlin.demo.config.swagger

import io.github.jinlongliao.easy.server.kotlin.demo.logic.param.UserModel
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig
import io.github.jinlongliao.easy.server.swagger.servlet.AbstractProxyAccessServlet
import io.github.jinlongliao.easy.server.utils.json.JsonHelper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * @author: liaojinlong
 * @date: 2022-06-20 14:52
 */
class DemoProxyAccessServlet(apiConfig: ApiConfig?, private val jsonHelper: JsonHelper) :
    AbstractProxyAccessServlet<UserModel?>(apiConfig) {
    @Throws(ServletException::class, IOException::class)
    override fun todoLogic(req: HttpServletRequest, resp: HttpServletResponse) {
        seJsonMsgContext(resp)
        val servlet = req.getParameter("servlet")
        if (servlet == null || servlet.isEmpty()) {
            req.getRequestDispatcher("/handlerMsg").forward(req, resp)
        } else {
            val apiConfig = apiConfig
            val proxyAccessPath = apiConfig.basePath + apiConfig.proxyAccessPath
            val replace = req.requestURI.replace(proxyAccessPath, "/")
            req.getRequestDispatcher(replace).forward(req, resp)
        }
    }

    override fun getDescription(): String {
        return "代理转发访问Servlet"
    }

    companion object {
        private val log = LoggerFactory.getLogger(DemoProxyAccessServlet::class.java)
    }
}
