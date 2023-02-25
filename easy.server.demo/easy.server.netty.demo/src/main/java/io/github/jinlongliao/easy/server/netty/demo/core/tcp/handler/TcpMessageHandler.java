package io.github.jinlongliao.easy.server.netty.demo.core.tcp.handler;


import io.github.jinlongliao.easy.server.netty.demo.constant.ErrorCode;
import io.github.jinlongliao.easy.server.netty.demo.constant.LogicId;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnection;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.LogicRequest;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.ExceptionHandler;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: liaojinlong
 * @date: 2022-08-08 09:58
 */

public class TcpMessageHandler extends SimpleChannelInboundHandler<LogicRequest> {
    private static final Map<String, TcpLogicDispatcher> LOGIC_DISPATCHER_MAP = new ConcurrentHashMap<>(128);
    private final TcpConnectionFactory tcpConnectionFactory;
    private final MethodParse methodParse;
    private final JsonHelper jsonHelper;

    public TcpMessageHandler(TcpConnectionFactory tcpConnectionFactory, MethodParse methodParse, JsonHelper jsonHelper) {
        this.tcpConnectionFactory = tcpConnectionFactory;
        this.methodParse = methodParse;
        this.jsonHelper = jsonHelper;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogicRequest msg) throws Exception {
        if (Objects.isNull(msg)) {
            return;
        }
        String logicId = msg.getLogicId();
        LogicModel logicModel = methodParse.getLogicDefineCache().get(logicId);
        TcpLogicDispatcher tcpLogicDispatcher = LOGIC_DISPATCHER_MAP.computeIfAbsent(logicId,
                key -> new TcpLogicDispatcher(key,
                        logicModel,
                        new TcpLogicResultHandler(this.jsonHelper)));

        TcpConnection localTcpConnection = this.tcpConnectionFactory.getLocalTcpConnection(ctx);
        if (Objects.isNull(localTcpConnection)) {
            throw new RuntimeException(String.valueOf(ErrorCode.CONN_ERROR));
        }
        boolean authed = localTcpConnection.isAuthed();
        if (authed) {
            int userId = msg.getUserId();
            if (userId != localTcpConnection.getUserId() && userId != 0) {
                throw new RuntimeException(String.valueOf(ErrorCode.NEED_AUTHED));
            }
        }
        if (!authed && !LogicId.USER_AUTH.equals(logicId)) {
            throw new RuntimeException(String.valueOf(ErrorCode.NEED_AUTHED));
        }
        tcpLogicDispatcher.dispatcher(logicId, msg.getArgs(), localTcpConnection);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ExceptionHandler.exceptionCaught(tcpConnectionFactory, ctx, cause, MethodHandles.lookup().lookupClass());
    }
}
