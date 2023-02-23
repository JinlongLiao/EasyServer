package io.github.jinlongliao.easy.server.netty.demo.config.swagger.tcp;


import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.NettyTcpServer;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.MsgReflectHelper;
import io.github.jinlongliao.easy.server.netty.demo.logic.response.RootResponse;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author: liaojinlong
 * @date: 2022-08-09 14:32
 */
@Component
public class TcpClient extends NettyTcpServer implements DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private EventLoopGroup loopGroup;



    private final Map<Integer, List<RootResponse>> responseMap = new HashMap<>(8);
    private final Map<Integer, Channel> channelMap = new ConcurrentHashMap<>(32, 1L);

    public TcpClient(TcpConnectionFactory tcpConnectionFactory, MethodParse methodParse, MsgReflectHelper msgReflectHelper, JsonHelper jsonHelper) {
        super(tcpConnectionFactory, methodParse, msgReflectHelper, jsonHelper);
    }

    @Override
    public void init() throws InterruptedException {
        log.info("new Socket");
        this.loopGroup = this.getEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup).option(ChannelOption.SO_KEEPALIVE, true).channel(getSocketChannel()).handler(new LoggingHandler(LogLevel.INFO)).handler(new ClientChannelInitializer(this));
        // Start the connection attempt.
        this.channel = bootstrap.connect("10.161.2.151", 1118).sync().channel();
        //        this.channel = bootstrap.connect("127.0.0.1", 8100).sync().channel();
        //        this.channel = bootstrap.connect("127.0.0.1", 8200).sync().channel();
    }

    @Override
    protected EventLoopGroup getEventLoopGroup(int num) {
        return super.getEventLoopGroup(num);
    }

    private Class<? extends SocketChannel> getSocketChannel() {
        if (Epoll.isAvailable()) {
            return EpollSocketChannel.class;
        }
        if (KQueue.isAvailable()) {
            return KQueueSocketChannel.class;
        }
        return NioSocketChannel.class;
    }

    public Channel checkChannelActive(int userId) {
        Channel chan = this.channelMap.computeIfAbsent(userId, key -> {
            this.init0();
            return this.channel;
        });
        if (!chan.isActive()) {
            this.init0();
            this.channelMap.put(userId, chan = this.channel);
        }
        return chan;
    }

    private void init0() {
        try {
            this.init();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(this.loopGroup)) {
            this.loopGroup.shutdownGracefully();
            for (Channel channel : channelMap.values()) {
                channel.closeFuture().sync();
            }
        }
    }

    public synchronized void addNewResponse(RootResponse response) {
         List<RootResponse> responses = this.responseMap.computeIfAbsent(1, k -> new ArrayList<>(8));
        responses.add(response);
    }

    public synchronized List<RootResponse> popAllResponse(int userId) {
        List<RootResponse> responses = this.responseMap.computeIfAbsent(userId, k -> new ArrayList<>(8));
        List<RootResponse> list = this.responseMap.getOrDefault(0, Collections.emptyList());
        List<RootResponse> res = new ArrayList<>(responses);
        res.addAll(list);
        list.clear();
        responses.clear();
        return res;
    }

    public Map<Integer, Channel> getChannelMap() {
        return channelMap;
    }
}
