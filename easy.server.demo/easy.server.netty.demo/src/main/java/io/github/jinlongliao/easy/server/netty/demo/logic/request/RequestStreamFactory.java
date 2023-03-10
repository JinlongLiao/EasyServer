package io.github.jinlongliao.easy.server.netty.demo.logic.request;

import io.github.jinlongliao.easy.server.core.parser.IRequestStreamFactory;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnection;
import io.netty.buffer.ByteBuf;


import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author: liaojinlong
 * @date: 2022-08-07 10:19
 */
public class RequestStreamFactory implements IRequestStreamFactory, Closeable {
    private final ByteBuf byteBuf;

    public RequestStreamFactory(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public int readInt() {
        return this.byteBuf.readIntLE();
    }

    public short readShort() {
        return this.byteBuf.readShortLE();
    }

    @Override
    public long readLong() {
        return this.byteBuf.readLongLE();
    }

    @Override
    public String readString(int iByteSize) {
        CharSequence charSequence = this.byteBuf.readCharSequence(iByteSize, StandardCharsets.UTF_8);
        return charSequence.toString().trim();
    }

    @Override
    public byte[] readBytes(int iByteSize) {
        return new byte[0];
    }

    @Override
    public boolean readBool() {
        return false;
    }

    public byte readByte() {
        return this.byteBuf.readByte();
    }

    public byte[] readByte(int iByteSize) {
        return this.byteBuf.readBytes(iByteSize).readBytes(iByteSize).array();
    }

    @Override
    public void close() throws IOException {
        this.byteBuf.release();
    }

    @Override
    public String getClientIp() {
        return null;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }
}
