package io.github.jinlongliao.easy.server.kotlin.demo.core.parser

import io.github.jinlongliao.easy.server.core.parser.IMessageParserCallBack
import io.github.jinlongliao.easy.server.core.parser.IRequestStreamFactory
import io.github.jinlongliao.easy.server.core.parser.MeType
import io.github.jinlongliao.easy.server.mapper.spring.IBeanMapper
import jakarta.servlet.http.HttpServletRequest

/**
 * @author: liaojinlong
 * @date: 2022-09-16 10:34
 */
class DemoMessageParserCallBack(private val beanMapper: IBeanMapper) : IMessageParserCallBack {
    override fun parserParamBody(request: IRequestStreamFactory, meType: MeType, vararg args: Any): Any {
        throw UnsupportedOperationException()
    }

    override fun parserParamBody(request: HttpServletRequest, meType: MeType, vararg args: Any): Any {
        return beanMapper.servletBeanMapper(meType.type, request)
    }

    override fun parserCommonParam(request: IRequestStreamFactory, meType: MeType, vararg args: Any): Any {
        throw UnsupportedOperationException()
    }

    override fun parserCommonParam(request: HttpServletRequest, meType: MeType, vararg args: Any): Any {
        throw UnsupportedOperationException()
    }
}
