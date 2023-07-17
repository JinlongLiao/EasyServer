package io.github.jinlongliao.easy.server.netty.demo.logic.request;


import io.github.jinlongliao.easy.server.core.parser.IMessageParserCallBack;
import io.github.jinlongliao.easy.server.core.parser.IRequestStreamFactory;
import io.github.jinlongliao.easy.server.core.parser.MeType;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnection;
import io.github.jinlongliao.easy.server.utils.common.IPAddressUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 消息解析
 *
 * @author: liaojinlong
 * @date: 2022-08-08 18:47
 */
public class TcpMsgParserCallBack implements IMessageParserCallBack {

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
        return this.parserCommonParam(meType, args);
    }

    @Override
    public Object parserCommonParam(HttpServletRequest request, MeType meType, Object... args) {
        return this.parserCommonParam(meType, args);
    }

    protected Object parserCommonParam(MeType meType, Object... args) {
        IRequest request = (IRequest) args[2];
        return request.getUserId();
    }

    @Override
    public Object parserInnerFiled(Object source, MeType meType, Object... args) {
        TcpConnection tcpConnection = ((TcpConnection) args[0]);
        if (meType.getParamName().equals("__CLIENT_IP__")) {
            return tcpConnection.getSocketChannel().remoteAddress().getHostString();
        } else {
            return tcpConnection;
        }
    }
}
