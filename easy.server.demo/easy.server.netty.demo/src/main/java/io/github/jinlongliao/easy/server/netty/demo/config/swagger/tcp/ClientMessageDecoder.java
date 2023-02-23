package io.github.jinlongliao.easy.server.netty.demo.config.swagger.tcp;

import io.github.jinlongliao.easy.server.netty.demo.logic.response.StringResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * @author: liaojinlong
 * @date: 2022-08-07 09:59
 */
public class ClientMessageDecoder extends LengthFieldBasedFrameDecoder {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ClientMessageDecoder() {
        super(ByteOrder.BIG_ENDIAN, 4 * 1024 * 1024, 2, 2, -4, 4, true);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decode = super.decode(ctx, in);

        if (Objects.isNull(decode)) {
            return null;
        }
        ByteBuf byteBuf = (ByteBuf) decode;
        // 获取签名密钥

        int code = byteBuf.readIntLE();
        boolean readable = byteBuf.isReadable();
        String json = "";
        if (readable) {
            int bodySize = byteBuf.readIntLE();
            json = byteBuf.readCharSequence(bodySize, StandardCharsets.UTF_8).toString();
        }
        byteBuf.release();

        return new StringResponse(code, json);
    }

    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //        log.info("客户端读取数据完毕");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
