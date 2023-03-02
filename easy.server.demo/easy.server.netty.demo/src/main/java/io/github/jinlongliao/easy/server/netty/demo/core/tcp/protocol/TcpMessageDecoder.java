package io.github.jinlongliao.easy.server.netty.demo.core.tcp.protocol;


import io.github.jinlongliao.easy.server.netty.demo.core.tcp.ExceptionHandler;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnection;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.MsgReflectHelper;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.RequestStreamFactory;
import io.github.jinlongliao.easy.server.utils.common.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.ByteOrder;
import java.util.Objects;


/**
 * @author: liaojinlong
 * @date: 2022-08-07 09:59
 */
public class TcpMessageDecoder extends LengthFieldBasedFrameDecoder {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TcpConnectionFactory tcpConnectionFactory;
    private final MsgReflectHelper msgReflectHelper;

    public TcpMessageDecoder(TcpConnectionFactory tcpConnectionFactory, MsgReflectHelper msgReflectHelper) {
        super(ByteOrder.BIG_ENDIAN, 4 * 1024 * 1024, 2, 2, -4, 4, true);
        this.tcpConnectionFactory = tcpConnectionFactory;
        this.msgReflectHelper = msgReflectHelper;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decode = super.decode(ctx, in);
        if (Objects.isNull(decode)) {
            if (log.isDebugEnabled()) {
                int len = in.readableBytes();
                byte[] dst = new byte[len];
                in.readBytes(dst);
                log.warn("error Msg {}", HexUtil.byte2Hex(dst));
            }
            return null;
        }

        ByteBuf decodedByteBuf = (ByteBuf) decode;
        try (RequestStreamFactory requestStreamFactory = new RequestStreamFactory(decodedByteBuf)) {
            return this.msgReflectHelper.transferMsgInfo(requestStreamFactory, tcpConnectionFactory.getLocalTcpConnection(ctx));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ExceptionHandler.exceptionCaught(tcpConnectionFactory, ctx, cause, MethodHandles.lookup().lookupClass());
    }
}
