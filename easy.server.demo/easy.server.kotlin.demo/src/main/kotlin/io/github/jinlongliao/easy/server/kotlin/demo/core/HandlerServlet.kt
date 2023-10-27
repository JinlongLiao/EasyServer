package io.github.jinlongliao.easy.server.kotlin.demo.core

import io.github.jinlongliao.easy.server.core.ILogicResultHandler
import io.github.jinlongliao.easy.server.core.LogicDispatcher
import io.github.jinlongliao.easy.server.core.core.MethodParse
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext
import io.github.jinlongliao.easy.server.core.model.LogicModel
import io.github.jinlongliao.easy.server.core.parser.ParseAdapter
import io.github.jinlongliao.easy.server.extend.parser.StaticRequestParseRule
import io.github.jinlongliao.easy.server.extend.response.ICommonResponse
import io.github.jinlongliao.easy.server.kotlin.demo.core.parser.DemoMessageParserCallBack
import io.github.jinlongliao.easy.server.kotlin.demo.logic.param.UserModel
import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException
import io.github.jinlongliao.easy.server.mapper.spring.IBeanMapper
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet
import io.github.jinlongliao.easy.server.utils.common.HexUtil
import io.github.jinlongliao.easy.server.utils.json.JsonHelper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author LiaoJL
 * @description 业务转发核心Servlet
 * @email jinlongliao@foxmail.com
 * @date 2021/2/18 22:36
 */
@Component
class HandlerServlet(
    logicRegisterContext: LogicRegisterContext,
    private val jsonHelper: JsonHelper,
    beanMapper: IBeanMapper?
) : BaseHttpServlet<UserModel?>(beanMapper) {
    private val methodParse: MethodParse

    init {
        methodParse = logicRegisterContext.parse
    }

    @Throws(ServletException::class, IOException::class)
    override fun todoLogic(req: HttpServletRequest, resp: HttpServletResponse) {
        val logicId: String
        logicId = try {
            req.getParameter(MSG_TYPE)
        } catch (ignore: NumberFormatException) {
            req.getParameter(KEY)
        }
        val logicModel = methodParse.logicDefineCache[logicId]
        resp.characterEncoding = "UTF-8"
        resp.setHeader("Content-Type", "application/json")
        resp.setHeader("Access-Control-Allow-Credentials", "true")
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST")
        resp.setHeader("Access-Control-Allow-Origin", "*")
        resp.setHeader("Access-Control-Max-Age", "3600")
        resp.status = HttpServletResponse.SC_OK
        resp.contentType = "application/json;charset=UTF-8"
        try {
            val args = getArgs(req, resp, logicModel)
            val logicDispatcher = getLogicDispatcher(logicId)
            val result = logicDispatcher.dispatcher(logicId, args)
            resp.writer.write(result)
        } catch (e: Throwable) {
            log.error(e.message, e)
        }
    }

    override fun supportPath(): Array<String> {
        return arrayOf("/handlerMsg")
    }

    private fun getLogicDispatcher(logicId: String): LogicDispatcher {
        val logicDispatcher: LogicDispatcher = LOGIC_DISPATCHER_CACHE.computeIfAbsent(logicId) { key: String? ->
            LogicDispatcher(
                methodParse.logicDefineCache[key], object : ILogicResultHandler {
                    @Throws(Exception::class)
                    override fun logicResultHandler(logicId: String, obj: Any?): String {
                        val data: MutableMap<String, Any?> = HashMap(2, 1.5f)
                        data["status"] = 0
                        if (Objects.nonNull(obj)) {
                            if (obj is ICommonResponse) {
                                data["hex"] = HexUtil.byte2Hex(obj.genResHex())
                            } else {
                                data["msg"] = obj
                            }
                        }
                        return jsonHelper.objectToJson(data)
                    }

                    @Throws(Exception::class)
                    override fun logicExceptionHandler(logicId: String, exception: Exception): String {
                        var exception = exception
                        if (exception is MethodInvokeException) {
                            exception = exception.targetException as Exception
                        }
                        log.error(exception.localizedMessage, exception)
                        val data: MutableMap<String, Any?> = HashMap(2, 1.5f)
                        data["status"] = 1
                        data["error"] = exception.message
                        return data.toString()
                    }
                })
        }
        return logicDispatcher
    }

    private fun getArgs(req: HttpServletRequest, resp: HttpServletResponse, logicModel: LogicModel?): Array<Any> {
        val parseAdapter = PARSE_ADAPTER_CACHE.computeIfAbsent(logicModel) { key: LogicModel? ->
            ParseAdapter(
                StaticRequestParseRule(logicModel), DemoMessageParserCallBack(beanMapper)
            )
        }
        return parseAdapter.parseMsg(req, req, resp)
    }

    override fun getDescription(): String {
        return "Logic 入口Servlet"
    }

    companion object {
        private val log = LoggerFactory.getLogger(HandlerServlet::class.java)
        private val LOGIC_DISPATCHER_CACHE: MutableMap<String, LogicDispatcher> = ConcurrentHashMap(16)
        private val PARSE_ADAPTER_CACHE: MutableMap<LogicModel?, ParseAdapter> = ConcurrentHashMap(16)

        /**
         * 业务处理id
         */
        private const val MSG_TYPE = "logicId"
        private const val KEY = "key"
    }
}
