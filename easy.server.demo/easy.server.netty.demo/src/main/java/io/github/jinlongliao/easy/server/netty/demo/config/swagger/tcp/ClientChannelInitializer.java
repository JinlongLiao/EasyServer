package io.github.jinlongliao.easy.server.netty.demo.config.swagger.tcp;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * push connection tcp channel initializer
 *
 * @author mahongxu
 * @date 2022-06-21
 */
@ChannelHandler.Sharable
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final TcpClient tcpClient;

    public ClientChannelInitializer(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ClientMessageDecoder());
        ch.pipeline().addLast(new ClientMessageEncode());
        ch.pipeline().addLast(new ClientMessageHandler(tcpClient));
    }
}
