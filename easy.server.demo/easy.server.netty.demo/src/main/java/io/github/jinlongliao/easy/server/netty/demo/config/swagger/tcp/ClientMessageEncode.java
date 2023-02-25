package io.github.jinlongliao.easy.server.netty.demo.config.swagger.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: liaojinlong
 * @date: 2022-08-09 14:53
 */
public class ClientMessageEncode extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        out.writeBytes(msg);
    }
}
