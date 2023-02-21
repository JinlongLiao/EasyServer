package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter;


/**
 * @date 2023-01-16 15:23
 * @author: liaojinlong
 * @description: 数据转换  自定义实现类型
 **/

public interface IDataConverter extends Comparable<IDataConverter> {

    /**
     * 数据转换为byte
     *
     * @param data
     * @return byte
     */
    byte getByte(Object data);


    /**
     * 数据转换为 Boolean
     *
     * @param data
     * @return Boolean
     */
    boolean getBoolean(Object data);


    /**
     * 数据转换为Short
     *
     * @param data
     * @return Short
     */
    short getShort(Object data);


    /**
     * 数据转换为float
     *
     * @param data
     * @return float
     */
    float getFloat(Object data);


    /**
     * 数据转换为double
     *
     * @param data
     * @return double
     */
    double getDouble(Object data);


    /**
     * 数据转换为long
     *
     * @param data
     * @return long
     */
    long getLong(Object data);


    /**
     * 数据转换为int
     *
     * @param data
     * @return int
     */
    int getInt(Object data);

    /**
     * 数据转换为String
     *
     * @param data
     * @return String
     */
    String getStr(Object data);

    /**
     * 数据转换为 char
     *
     * @param data
     * @return char
     */
    char getChar(Object data);

    /**
     * 泛型转换
     *
     * @param data
     * @param extra
     * @param tClass
     * @param <T>
     * @return /
     */
    <T> T getT(Class<T> tClass, Object extra, Object data);

    static IDataConverter getDefault() {
        return new DefaultDataConverter();
    }

    /**
     * @return 使用优先级
     */
    default int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    default int compareTo(IDataConverter dataConverter) {
        return this.order() > dataConverter.order() ? 1 : 0;
    }
}

