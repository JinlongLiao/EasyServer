package io.github.jinlongliao.easy.server.netty.demo.config.swagger.tcp;

import io.github.jinlongliao.easy.server.netty.demo.logic.response.RootResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author: liaojinlong
 * @date: 2022-08-09 14:56
 */
public class ClientMessageHandler extends SimpleChannelInboundHandler<RootResponse> {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TcpClient tcpClient;

    public ClientMessageHandler(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RootResponse msg) throws Exception {
        log.info("msg:{}", msg);
        this.tcpClient.addNewResponse(msg);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("channelInactive");
        ctx.close();
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.warn("channelUnregistered");
        super.channelUnregistered(ctx);
    }
}
