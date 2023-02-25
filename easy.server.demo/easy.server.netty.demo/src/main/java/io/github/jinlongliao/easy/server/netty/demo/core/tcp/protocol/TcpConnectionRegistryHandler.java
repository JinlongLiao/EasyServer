package io.github.jinlongliao.easy.server.netty.demo.core.tcp.protocol;


import io.github.jinlongliao.easy.server.netty.demo.core.tcp.ExceptionHandler;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnection;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author: liaojinlong
 * @date: 2022-08-08 12:24
 */
public class TcpConnectionRegistryHandler extends ChannelInboundHandlerAdapter {
    private final TcpConnectionFactory tcpConnectionFactory;
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public TcpConnectionRegistryHandler(TcpConnectionFactory tcpConnectionFactory) {
        this.tcpConnectionFactory = tcpConnectionFactory;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        if (log.isDebugEnabled()) {
            InetSocketAddress inetSocketAddress = ((SocketChannel) ctx.channel()).remoteAddress();
            log.debug("register available channel to PushConnection host: {} ,port: {}", inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        }
        this.tcpConnectionFactory.softBindConnect(ctx);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.tcpConnectionFactory.flushUserState(ctx);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.tcpConnectionFactory.closeConnection(ctx, "channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                if (log.isDebugEnabled()) {
                    TcpConnection localTcpConnection = this.tcpConnectionFactory.getLocalTcpConnection(ctx);
                    if (Objects.nonNull(localTcpConnection)) {
                        log.debug("user{} Idle reader event triggered, make the channel clos", localTcpConnection.getUserId());
                    }
                }
                this.tcpConnectionFactory.closeConnection(ctx, "userEventTriggered");
            } else {
                super.userEventTriggered(ctx, evt);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ExceptionHandler.exceptionCaught(tcpConnectionFactory, ctx, cause, MethodHandles.lookup().lookupClass());
    }
}
