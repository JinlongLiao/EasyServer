package io.github.jinlongliao.easy.server.netty.demo.logic.response;

 import io.github.jinlongliao.easy.server.netty.demo.constant.ErrorCode;
 import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * 异常响应
 *
 * @author: liaojinlong
 * @date: 2022-08-08 16:46
 */
public class ErrorResponse extends RootResponse {
    protected String message;

    public ErrorResponse(int code, String message) {
        super(code);
         this.message = message;
    }

    public ErrorResponse(ErrorCode errorCode) {
        super(errorCode.getErrorCode());
         this.message = errorCode.getErrMsg();
    }

    @Override
    protected void writePrivateResp(ByteBuf byteBuf) {
        byte[] bytes = this.message.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeIntLE(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
