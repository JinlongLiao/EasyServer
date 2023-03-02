package io.github.jinlongliao.easy.server.core.parser;

/**
 * 二进制解析
 *
 * @author liaojinlong
 * @since 2021/1/22 20:36
 */

public interface IRequestStreamFactory {
    /**
     * 获取 byte
     *
     * @return /
     */
    boolean readBool();

    /**
     * 获取 byte
     *
     * @return /
     */
    byte readByte();

    /**
     * 获取 short
     *
     * @return /
     */
    short readShort();

    /**
     * 获取int
     *
     * @return /
     */
    int readInt();


    /**
     * 获取long
     *
     * @return /
     */
    long readLong();

    /**
     * 获取string
     *
     * @param iByteSize
     * @return /
     */
    String readString(int iByteSize);

    /**
     * 读取特定字节
     *
     * @param iByteSize
     * @return /
     */
    byte[] readBytes(int iByteSize);

    /**
     * @return ip
     */
    String getClientIp();
}
