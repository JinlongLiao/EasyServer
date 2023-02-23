package io.github.jinlongliao.easy.server.netty.demo.core.tcp;


import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.MsgReflectHelper;
import io.github.jinlongliao.easy.server.netty.demo.utils.LocalAddressConstant;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.protocol.TcpChannelInitializer;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.lang.invoke.MethodHandles;

/**
 * Netty Tcp Server
 *
 * @author: liaojinlong
 * @date: 2022-08-03 18:24
 */
public class NettyTcpServer implements AutoCloseable, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    protected Channel channel;
    protected boolean stop;

    private final TcpConnectionFactory tcpConnectionFactory;
    private final MethodParse methodParse;
    private final MsgReflectHelper msgReflectHelper;
    private final JsonHelper jsonHelper;

    public NettyTcpServer(TcpConnectionFactory tcpConnectionFactory, LogicRegisterContext logicRegisterContext, MsgReflectHelper msgReflectHelper, JsonHelper jsonHelper) {
        this.tcpConnectionFactory = tcpConnectionFactory;
        this.methodParse = logicRegisterContext.getParse();
        this.msgReflectHelper = msgReflectHelper;
        this.jsonHelper = jsonHelper;

    }

    public void init() throws InterruptedException {

        this.bossGroup = this.getEventLoopGroup(1);
        this.workerGroup = this.getEventLoopGroup(0);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            this.configOption(serverBootstrap);
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(getServerSocketChannel())
                    .handler(new LoggingHandler(LogLevel.ERROR))
                    .childHandler(getChannelHandler());
            this.channel = serverBootstrap.bind(getPort())
                    .sync()
                    .channel();
            log.info(getLogT(), LocalAddressConstant.LOCAL_ADDRESS, getPort());
            this.channel.closeFuture().sync();
        } finally {
            this.close();
        }
    }

    protected String getLogT() {
        return "Tcp Init Success tcp://{}:{}";
    }

    protected void configOption(ServerBootstrap serverBootstrap) {
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024);
    }

    protected int getPort() {
        return 8100;
    }

    protected ChannelHandler getChannelHandler() {
        return new TcpChannelInitializer(tcpConnectionFactory, methodParse, msgReflectHelper, jsonHelper);
    }

    @Override
    public void close() {
        if (stop) {
            return;
        }
        synchronized (this) {
            if (stop) {
                return;
            }
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
            stop = true;
        }
    }

    protected EventLoopGroup getEventLoopGroup(int num) {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup(num);
        }
        return new NioEventLoopGroup(num);
    }

    protected Class<? extends ServerChannel> getServerSocketChannel() {
        if (Epoll.isAvailable()) {
            return EpollServerSocketChannel.class;
        }
        return NioServerSocketChannel.class;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                init();
            } catch (InterruptedException e) {
                NettyTcpServer.log.error(e.getMessage(), e);
            }
        }).start();
    }
}
