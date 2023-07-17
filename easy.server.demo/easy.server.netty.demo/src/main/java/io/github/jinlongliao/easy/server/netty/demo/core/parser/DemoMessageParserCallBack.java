package io.github.jinlongliao.easy.server.netty.demo.core.parser;

import io.github.jinlongliao.easy.server.core.parser.IMessageParserCallBack;
import io.github.jinlongliao.easy.server.core.parser.IRequestStreamFactory;
import io.github.jinlongliao.easy.server.core.parser.MeType;
import io.github.jinlongliao.easy.server.mapper.spring.IBeanMapper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: liaojinlong
 * @date: 2022-09-16 10:34
 */
public class DemoMessageParserCallBack implements IMessageParserCallBack {
    private final IBeanMapper beanMapper;

    public DemoMessageParserCallBack(IBeanMapper beanMapper) {
        this.beanMapper = beanMapper;
    }

    @Override
    public Object parserParamBody(IRequestStreamFactory request, MeType meType, Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object parserParamBody(HttpServletRequest request, MeType meType, Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object parserCommonParam(IRequestStreamFactory request, MeType meType, Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object parserCommonParam(HttpServletRequest request, MeType meType, Object... args) {
        throw new UnsupportedOperationException();
    }


}
