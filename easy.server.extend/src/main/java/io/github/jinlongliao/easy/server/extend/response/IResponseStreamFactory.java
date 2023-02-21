package io.github.jinlongliao.easy.server.extend.response;


import java.util.Collection;
import java.util.Date;

/**
 * @author: liaojinlong
 * @date: 2022-06-09 14:56
 */
public interface IResponseStreamFactory {

    /**
     * 写入Byte数值
     *
     * @param b
     */
    void writeByte(byte b);

    void writeChar(char c);

    void writeBool(boolean bool);

    void writeDate(Date date);

    /**
     * 写入short数值
     *
     * @param iShort
     */
    void writeShort(short iShort);

    /**
     * 写入整型数值
     *
     * @param iInt
     */
    void writeInt(int iInt);

    /**
     * 写入Long型数值
     *
     * @param iLong
     */
    void writeLong(long iLong);

    /**
     * 写入整型数组
     *
     * @param iIntBytes
     */
    void write(int[] iIntBytes);


    default int tableSizeFor(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : (n == Integer.MAX_VALUE) ? Integer.MAX_VALUE : (n + 1);
    }

    /**
     * 写字符串
     *
     * @param msg
     * @param len
     */
    void writeString(String msg, int len);

    /**
     * 获取十六进制输出
     *
     * @return
     */
    String getHexResponse();

    /**
     * 获取byte数组
     *
     * @return
     */
    byte[] toBytes();

    /**
     * 清空
     */
    void resetByte();

    void skip(int step);

    void writeBytes(byte[] bytes);

    void writeDynamicString(String str);

    void writeResponse(ICommonResponse response);

    void writeResponses(Collection<Object> responses);
}
