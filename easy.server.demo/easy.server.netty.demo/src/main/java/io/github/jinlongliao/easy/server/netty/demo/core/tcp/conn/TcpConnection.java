package io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn;


import io.github.jinlongliao.easy.server.netty.demo.logic.response.RootResponse;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Objects;

/**
 * @author: liaojinlong
 * @date: 2022-08-08 10:30
 */
public class TcpConnection {
    private final SocketChannel socketChannel;
    /**
     * 是否鉴权
     */
    private boolean authed = false;

    /**
     * 最后心跳时间
     */
    private long lastHeartBeatTs;
    private int userId;
    private ScheduledFuture<?> authedInThreeSeconds;

    public TcpConnection(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void writeResponse(RootResponse response) {
        if (this.socketChannel.isActive()) {
            this.socketChannel.writeAndFlush(response);
        }
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public int getUserId() {
        return userId;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    public boolean isAuthed() {
        return authed;
    }

    public long getLastHeartBeatTs() {
        return lastHeartBeatTs;
    }

    public void setLastHeartBeatTs(long lastHeartBeatTs) {
        this.lastHeartBeatTs = lastHeartBeatTs;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ScheduledFuture<?> getAuthedInThreeSeconds() {
        return authedInThreeSeconds;
    }

    public void setAuthedInThreeSeconds(ScheduledFuture<?> authedInThreeSeconds) {
        this.authedInThreeSeconds = authedInThreeSeconds;
    }

    void close() {
        this.authed = false;
        this.userId = 0;
        if (this.socketChannel.isActive()) {
            this.socketChannel.close();
        }
        if (Objects.nonNull(this.authedInThreeSeconds)) {
            this.authedInThreeSeconds.cancel(true);
        }
    }
}
