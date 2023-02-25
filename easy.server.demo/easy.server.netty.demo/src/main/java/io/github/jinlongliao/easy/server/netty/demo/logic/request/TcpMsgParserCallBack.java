package io.github.jinlongliao.easy.server.netty.demo.logic.request;


import io.github.jinlongliao.easy.server.core.parser.IMessageParserCallBack;
import io.github.jinlongliao.easy.server.core.parser.IRequestStreamFactory;
import io.github.jinlongliao.easy.server.core.parser.MeType;

import javax.servlet.http.HttpServletRequest;

/**
 * 消息解析
 *
 * @author: liaojinlong
 * @date: 2022-08-08 18:47
 */
public class TcpMsgParserCallBack implements IMessageParserCallBack {

    @Override
    public Object parserParamBody(IRequestStreamFactory request, MeType meType, Object arg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object parserParamBody(HttpServletRequest request, MeType meType, Object arg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object parserCommonParam(IRequestStreamFactory request, MeType meType, Object arg) {
        return this.parserCommonParam(meType, arg);
    }

    @Override
    public Object parserCommonParam(HttpServletRequest request, MeType meType, Object arg) {
        return this.parserCommonParam(meType, arg);
    }

    public Object parserCommonParam(MeType meType, Object arg) {
        IRequest request = (IRequest) arg;
        return request.getUserId();
    }

    @Override
    public Object parserInnerFiled(Object args, MeType meType, Object arg) {
        RequestStreamFactory factory = (RequestStreamFactory) args;
        return factory.getTcpConnection();
    }
}
