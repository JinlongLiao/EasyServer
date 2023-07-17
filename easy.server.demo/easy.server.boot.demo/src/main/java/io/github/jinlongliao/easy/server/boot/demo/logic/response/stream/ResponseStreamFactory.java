package io.github.jinlongliao.easy.server.boot.demo.logic.response.stream;

import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import java.util.Objects;
import io.github.jinlongliao.easy.server.utils.common.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

public class ResponseStreamFactory implements IResponseStreamFactory {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ByteBuffer byteBuff;
    /**
     * 快速模式
     */
    private final boolean fastMode;
    private static final byte[] EMPTY = new byte[0];

    public ResponseStreamFactory() {
        this(1024, false);
    }

    public ResponseStreamFactory(int size, boolean fastMode) {
        this.byteBuff = ByteBuffer.allocate(size);
        this.fastMode = fastMode;
    }

    /**
     * 写入Byte数值
     *
     * @param iByte
     */
    public void writeByte(byte iByte) {
        if (!fastMode) {
            this.ensureCapacityInternal(1);
        }
        try {
            this.byteBuff.put(iByte);
        } catch (BufferOverflowException e) {
            if (fastMode) {
                this.ensureCapacityInternal(1);
                this.byteBuff.put(iByte);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void writeChar(char c) {
        this.writeByte((byte) c);
    }

    @Override
    public void writeBool(boolean bool) {
        this.writeByte((byte) (bool ? 1 : 0));
    }

    @Override
    public void writeDate(Date date) {
        if (Objects.isNull(date)) {
            this.writeLong(0L);
        } else {
            this.writeLong(date.getTime());
        }

    }

    /**
     * 写入short数值
     *
     * @param iShort
     */
    public void writeShort(short iShort) {
        byte[] shortBytes = new byte[2];
        shortBytes[0] = (byte) iShort;
        shortBytes[1] = (byte) (iShort >>> 8);
        this.write(shortBytes);
    }

    /**
     * 写入整型数值
     *
     * @param iInt
     */
    public void writeInt(int iInt) {
        byte[] intBytes = new byte[4];
        for (int index = 0; index < 4; index++) {
            intBytes[index] = (byte) (iInt >>> (index * 8));
        }
        this.write(intBytes);
    }

    /**
     * 写入Long型数值
     *
     * @param iLong
     */
    public void writeLong(long iLong) {
        byte[] longBytes = new byte[8];
        for (int index = 0; index < 8; index++) {
            longBytes[index] = (byte) (iLong >>> (index * 8));
        }
        this.write(longBytes);
    }

    /**
     * 写入整型数组
     *
     * @param iIntBytes
     */
    public void write(int[] iIntBytes) {
        for (int iIntByte : iIntBytes) {
            this.writeInt(iIntByte);
        }
    }

    /**
     * 写入二进制数组
     *
     * @param bytes
     */
    private void write(byte[] bytes) {
        if (!fastMode) {
            this.ensureCapacityInternal(bytes.length);
        }
        try {
            this.byteBuff.put(bytes);
        } catch (BufferOverflowException e) {
            if (fastMode) {
                this.ensureCapacityInternal(bytes.length);
                this.byteBuff.put(bytes);
            } else {
                throw e;
            }
        }
    }

    private void ensureCapacityInternal(int length) {
        int limit = this.byteBuff.limit();
        int position = this.byteBuff.position();
        // 进行扩容
        int needCap = position + length;
        if (needCap > limit) {
            int newLimit = tableSizeFor(needCap);
            ByteBuffer newByteBuff = ByteBuffer.allocate(newLimit);
            newByteBuff.put(toBytes());
            this.byteBuff = newByteBuff;
        }

    }


    /**
     * 写字符串
     *
     * @param msg
     * @param iBytes
     */
    public void writeString(String msg, int iBytes) {
        if (Objects.nonNull(msg)) {
            byte[] infoBytes = msg.getBytes(StandardCharsets.UTF_8);
            int len = Math.min(infoBytes.length, iBytes);
            if (!fastMode) {
                this.ensureCapacityInternal(len);
            }
            try {
                this.byteBuff.put(infoBytes, 0, len);
            } catch (BufferOverflowException e) {
                if (fastMode) {
                    this.ensureCapacityInternal(infoBytes.length);
                    this.byteBuff.put(infoBytes);
                } else {
                    throw e;
                }
            }
        }
    }

    /**
     * 获取十六进制输出
     *
     * @return
     */
    public String getHexResponse() {
        byte[] bytes = toBytes();
        if (Objects.isNull(bytes) || bytes.length == 0) {

            return "";
        }
        return HexUtil.byte2Hex(bytes);
    }


    /**
     * 获取byte数组
     *
     * @return
     */
    public byte[] toBytes() {
        byteBuff.flip();
        if (byteBuff.limit() == 0) {
            return EMPTY;
        }
        byte[] bytes = new byte[byteBuff.limit()];
        byteBuff.get(bytes);
        return bytes;
    }

    /**
     * 清空
     */
    public void resetByte() {
        this.byteBuff.flip();
        this.byteBuff.clear();
    }

    @Override
    public void skip(int step) {
        this.byteBuff.position(this.byteBuff.position() + step);
    }

    public void writeBytes(byte[] bytes) {
        this.write(bytes);
    }

    @Override
    public void writeDynamicString(String str) {
        if (Objects.nonNull(str)) {
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            this.writeInt(bytes.length);
            this.write(bytes);
        } else {
            this.writeInt(0);
        }
    }

    @Override
    public void writeResponse(ICommonResponse response) {
        try {
            response.genResHex(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeResponses(Collection<Object> responses) {
        if (Objects.isNull(responses)) {
            this.writeInt(0);
        } else {
            this.writeInt(responses.size());
            Class<?> aClass = responses.toArray()[0].getClass();
            if (CLassUtils.isInteger(aClass)) {
                responses.stream().map(n -> ((Integer) n)).forEach(this::writeInt);
            } else if (ICommonResponse.class.isAssignableFrom(aClass)) {
                responses.stream().map(n -> ((ICommonResponse) n)).forEach(n -> n.genResHex(this));
            } else if (CLassUtils.isStringClass(aClass)) {
                responses.stream().map(n -> ((String) n)).forEach(this::writeDynamicString);
            } else if (CLassUtils.isBool(aClass)) {
                responses.stream().map(n -> ((Boolean) n)).forEach(this::writeBool);
            } else if (CLassUtils.isByte(aClass)) {
                responses.stream().map(n -> ((Byte) n)).forEach(this::writeByte);
            } else if (CLassUtils.isCharacter(aClass)) {
                responses.stream().map(n -> ((Character) n)).forEach(this::writeChar);
            } else if (CLassUtils.isShort(aClass)) {
                responses.stream().map(n -> ((Short) n)).forEach(this::writeShort);
            } else if (CLassUtils.isLong(aClass)) {
                responses.stream().map(n -> ((Long) n)).forEach(this::writeLong);
            }
        }
    }
}
