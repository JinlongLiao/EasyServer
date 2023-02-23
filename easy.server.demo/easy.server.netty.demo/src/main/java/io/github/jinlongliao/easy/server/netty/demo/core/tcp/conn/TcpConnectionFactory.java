package io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn;


import io.github.jinlongliao.easy.server.netty.demo.utils.LocalAddressConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author: liaojinlong
 * @date: 2022-08-08 10:35
 */

public class TcpConnectionFactory {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<SocketChannel, TcpConnection> connectionMap = new ConcurrentHashMap<>(1024);
    private final Map<Integer, TcpConnection> authedMap = new ConcurrentHashMap<>(1024);


    private String localHttpUrl;
    private String localDomain;


    public TcpConnection getLocalTcpConnection(int userId) {
        return this.authedMap.get(userId);
    }


    public TcpConnection getLocalTcpConnection(SocketChannel socketChannel) {
        return this.connectionMap.get(socketChannel);
    }

    public TcpConnection getLocalTcpConnection(ChannelHandlerContext channelHandlerContext) {
        return this.getLocalTcpConnection((SocketChannel) channelHandlerContext.channel());
    }

    /**
     * 弱绑定绑定关系,3s 未鉴权 自动断连
     *
     * @param channelHandlerContext
     */
    public void softBindConnect(ChannelHandlerContext channelHandlerContext) {
        SocketChannel socketChannel = (SocketChannel) channelHandlerContext.channel();
        TcpConnection tcpConnection = new TcpConnection(socketChannel);
        this.connectionMap.put(socketChannel, tcpConnection);
        ScheduledFuture<?> authedInThreeSeconds = socketChannel.eventLoop().schedule(() -> {
            if (tcpConnection.isAuthed()) {
                return;
            }
            this.connectionMap.remove(socketChannel);
            log.warn("not authed in three seconds");
            tcpConnection.close();
        }, 3, TimeUnit.SECONDS);
        tcpConnection.setAuthedInThreeSeconds(authedInThreeSeconds);
    }

    public void bindConnect(int userId, TcpConnection tcpConn) {
        tcpConn.setAuthed(true);
        tcpConn.setUserId(userId);
        this.authedMap.put(userId, tcpConn);
    }

    /**
     * 刷新用户在线状态
     *
     * @param ctx
     */
    public void flushUserState(ChannelHandlerContext ctx) {
        TcpConnection localTcpConnection = this.getLocalTcpConnection(ctx);
        if (Objects.isNull(localTcpConnection)) {
            return;
        }
        localTcpConnection.setLastHeartBeatTs(System.currentTimeMillis());
    }

    /**
     * 关闭Socket 连接
     *
     * @param ctx
     */
    public void closeConnection(ChannelHandlerContext ctx, String msg) {
        TcpConnection localTcpConnection = this.connectionMap.remove(ctx.channel());
        if (Objects.isNull(localTcpConnection)) {
            return;
        }
        int userId = localTcpConnection.getUserId();
        if (log.isDebugEnabled()) {
            log.debug("msg:{} userId：{} ", msg, userId);
        }
        if (userId > 0) {
            this.authedMap.remove(userId);
        }
        localTcpConnection.close();
    }

    /**
     * 关闭Socket 连接
     *
     * @param tcpConnection
     */
    public void closeConnection(TcpConnection tcpConnection) {
        if (Objects.isNull(tcpConnection)) {
            return;
        }
        this.connectionMap.remove(tcpConnection.getSocketChannel());
        int userId = tcpConnection.getUserId();
        if (userId > 0) {
            this.authedMap.remove(userId);
        }
        tcpConnection.close();
    }

    /**
     * 关闭全部用户的连接
     */
    public void close() {
        for (Map.Entry<Integer, TcpConnection> e : authedMap.entrySet()) {
            TcpConnection tcpConnection = e.getValue();
            tcpConnection.close();
        }
        this.authedMap.clear();
        this.connectionMap.clear();
    }


    public String getLocalHttpServerUrl() {
        if (Objects.nonNull(this.localHttpUrl)) {
            return this.localHttpUrl;
        }
        return this.localHttpUrl = ("http://" + getLocalDomain() + "/");
    }

    public String getLocalDomain() {
        if (Objects.nonNull(this.localDomain)) {
            return this.localDomain;
        }
        return this.localDomain = (LocalAddressConstant.LOCAL_ADDRESS + ":" + 9999);
    }

    public Map<Integer, TcpConnection> getAuthedMap() {
        return authedMap;
    }

}
