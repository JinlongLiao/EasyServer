package io.github.jinlongliao.easy.server.netty.demo.core.tcp.protocol;


import io.github.jinlongliao.easy.server.netty.demo.core.tcp.ExceptionHandler;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.handler.TcpMessageHandler;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.MsgReflectHelper;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.lang.invoke.MethodHandles;

/**
 * push connection tcp channel initializer
 *
 * @author mahongxu
 * @date 2022-06-21
 */
@ChannelHandler.Sharable
public class TcpChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final TcpConnectionFactory tcpConnectionFactory;
    private final MethodParse methodParse;
    private final MsgReflectHelper msgReflectHelper;
    private final JsonHelper jsonHelper;

    public TcpChannelInitializer(TcpConnectionFactory tcpConnectionFactory,
                                 MethodParse methodParse,
                                 MsgReflectHelper msgReflectHelper,
                                 JsonHelper jsonHelper) {
        this.tcpConnectionFactory = tcpConnectionFactory;
        this.methodParse = methodParse;
        this.msgReflectHelper = msgReflectHelper;
        this.jsonHelper = jsonHelper;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(120, 0, 0));
        ch.pipeline().addLast(new TcpConnectionRegistryHandler(this.tcpConnectionFactory));
        ch.pipeline().addLast(new TcpMessageDecoder(this.tcpConnectionFactory, msgReflectHelper));
        ch.pipeline().addLast(new TcpMessageEncoder());
        ch.pipeline().addLast(new TcpMessageHandler(this.tcpConnectionFactory, methodParse, jsonHelper));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ExceptionHandler.exceptionCaught(tcpConnectionFactory, ctx, cause, MethodHandles.lookup().lookupClass());
    }
}
