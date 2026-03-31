package io.github.jinlongliao.easy.server.utils.json.extra;


import com.alibaba.fastjson2.JSON;
import io.github.jinlongliao.easy.server.utils.json.DataType;
import io.github.jinlongliao.easy.server.utils.json.Json;

import java.util.List;

/**
 * Fast JSON 序列化反序列 包装类
 *
 * @author liaojinlong
 * @since 2021/10/21 20:33
 */
public class FastJson2JsonHelper implements Json {


    /**
     * Json  byte[]
     *
     * @param obj
     * @return /
     */
    @Override
    public byte[] toJsonByte(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    /**
     * Json
     *
     * @param obj
     * @return /
     */
    @Override
    public String objectToJson(Object obj) {
        return JSON.toJSONString(obj, "millis");
    }

    /**
     * Json
     *
     * @param obj
     * @return /
     */
    @Override
    public String objectToJsonArray(Object obj) {
        return this.objectToJson(obj);
    }

    /**
     * Json
     *
     * @param obj
     * @return /
     */
    @Override
    public byte[] objectToJsonArrayByte(Object obj) {
        return JSON.toJSONBytes(obj);
    }


    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJson(String json, Class<T> tClass) {
        return JSON.parseObject(json, tClass, "millis");
    }

    @Override
    public <T> T fromJson(String json, DataType<T> dataType) {
        return JSON.parseObject(json, dataType.getType());
    }


    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJsonByte(byte[] json, Class<T> tClass) {
        return JSON.parseObject(json, tClass);
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJsonByte(byte[] jsonBytes, DataType<T> tClass) {
        return (T) JSON.parseObject(jsonBytes, tClass.getRawType());
    }

    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonArray(String json, Class<T> tClass) {
        return JSON.parseArray(json, tClass);
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, Class<T> tClass) {
        return JSON.parseArray(jsonBytes, tClass);
    }

    /**
     * @param json
     * @param dataType
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonArray(String json, DataType<T> dataType) {
        return JSON.parseArray(json, dataType.getType());
    }

    /**
     * @param jsonBytes
     * @param dataType
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, DataType<T> dataType) {
        return (List<T>) JSON.parseArray(jsonBytes,  dataType.getRawType());
    }

}
