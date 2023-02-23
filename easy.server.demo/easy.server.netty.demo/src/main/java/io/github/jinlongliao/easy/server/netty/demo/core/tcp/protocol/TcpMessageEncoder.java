package io.github.jinlongliao.easy.server.netty.demo.core.tcp.protocol;


import io.github.jinlongliao.easy.server.netty.demo.logic.response.RootResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


/**
 * @author: liaojinlong
 * @date: 2022-08-07 09:59
 */
public class TcpMessageEncoder extends MessageToByteEncoder<RootResponse> {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    protected void encode(ChannelHandlerContext ctx, RootResponse response, ByteBuf byteBuf) throws Exception {
        response.writeResponse(byteBuf);
    }
}
