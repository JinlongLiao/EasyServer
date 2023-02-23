package io.github.jinlongliao.easy.server.netty.demo.logic.response;

import io.netty.buffer.ByteBuf;


/**
 * 返回ＪＳＯＮ
 *
 * @author: liaojinlong
 * @date: 2022-08-08 16:37
 */

public class StringResponse extends RootResponse {
    private final String json;

    public StringResponse(int status, String json) {
        super(status);
        this.json = json;
    }

    public StringResponse(String json) {
        super(0);
        this.json = json;
    }

    @Override
    protected void writePrivateResp(ByteBuf byteBuf) {
        byte[] bytes = this.json.getBytes();
        byteBuf.writeIntLE(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public String getJson() {
        return json;
    }

    @Override
    public String toString() {
        return "StringResponse{" +
                "json='" + json + '\'' +
                '}';
    }
}
