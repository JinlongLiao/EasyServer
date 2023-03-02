package io.github.jinlongliao.easy.server.netty.demo.core.tcp;

import io.github.jinlongliao.easy.server.netty.demo.constant.ErrorCode;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnection;
import io.github.jinlongliao.easy.server.netty.demo.logic.response.ErrorResponse;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.unix.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.SocketException;
import java.util.Objects;

/**
 * Netty 异常处理
 *
 * @author: liaojinlong
 * @date: 2022-08-10 11:46
 */
public final class ExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void exceptionCaught(TcpConnectionFactory tcpConnectionFactory, ChannelHandlerContext ctx, Throwable cause, Class<?> aClass) throws Exception {
        if (cause instanceof Errors.NativeIoException || cause instanceof SocketException) {
            if (log.isDebugEnabled()) {
                log.warn("client Error Close");
            }
            tcpConnectionFactory.closeConnection(ctx, "client Error Close");
            return;
        }


        if (cause instanceof MethodInvokeException) {
            cause = ((MethodInvokeException) cause).getTargetException();
        }
        if (log.isDebugEnabled()) {
            log.debug("from class {} ex{}", aClass.getName(), cause.getClass().getName());
        }
        log.warn(cause.getMessage(), cause);
        TcpConnection localTcpConnection = tcpConnectionFactory.getLocalTcpConnection(ctx);
        int userId = 0;
        Channel channel = ctx.channel();
        if (Objects.nonNull(localTcpConnection)) {
            userId = localTcpConnection.getUserId();
        }
        ErrorResponse response = new ErrorResponse(ErrorCode.SYS_ERROR.getErrorCode(), cause.getMessage());
        if (channel.isActive()) {
            channel.writeAndFlush(response);
        }
        tcpConnectionFactory.closeConnection(ctx, "exceptionCaught");

    }
}
