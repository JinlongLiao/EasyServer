package io.github.jinlongliao.easy.server.utils.json;


import java.util.List;

/**
 * JSON 序列化反序列 包装类
 *
 * @author liaojinlong
 * @since 2021/10/21 20:33
 */
public interface Json {
    /**
     * JsonArray
     *
     * @param obj
     * @return /
     */
    String objectToJsonArray(Object obj);

    /**
     * JsonArray
     *
     * @param obj
     * @return /
     */
    byte[] objectToJsonArrayByte(Object obj);


    /**
     * Json  byte[]
     *
     * @param obj
     * @return /
     */
    byte[] toJsonByte(Object obj);


    /**
     * Json
     *
     * @param obj
     * @return /
     */
    String objectToJson(Object obj);

    /**
     * List
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    <T> List<T> fromJsonArray(String json, Class<T> tClass);

    /**
     * Byte List
     *
     * @param jsonBytes
     * @param tClass
     * @param <T>
     * @return /
     */
    <T> List<T> fromJsonByteArray(byte[] jsonBytes, Class<T> tClass);

    /**
     * Object Str
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    <T> T fromJson(String json, Class<T> tClass);


    /**
     * byte Json Object
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    <T> T fromJsonByte(byte[] json, Class<T> tClass);

}
