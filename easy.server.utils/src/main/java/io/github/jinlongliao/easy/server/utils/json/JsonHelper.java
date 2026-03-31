package io.github.jinlongliao.easy.server.utils.json;


import io.github.jinlongliao.easy.server.utils.json.extra.FastJson2JsonHelper;
import io.github.jinlongliao.easy.server.utils.json.extra.FastJsonJsonHelper;
import io.github.jinlongliao.easy.server.utils.json.extra.JackJsonJsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * JSON 序列化反序列 包装类
 *
 * @author liaojinlong
 * @since 2021/10/21 20:33
 */
public class JsonHelper implements Json {
    public static JsonHelper jsonHelper;

    private static final Logger log = LoggerFactory.getLogger(JsonHelper.class);
    private final JsonType jsonType;
    private final FastJsonJsonHelper fastJson;
    private final FastJson2JsonHelper fastJson2;
    private final JackJsonJsonHelper jackJson;
    private final Json extraJson;

    public JsonHelper() {
        this(JsonType.FAST_JSON2, null, new FastJson2JsonHelper(), null, new FastJson2JsonHelper());
    }

    public JsonHelper(Json json) {
        this(JsonType.EXTRA_JSON, null, null, null, json);
    }

    public JsonHelper(JsonType jsonType,
                      FastJsonJsonHelper fastJson,
                      FastJson2JsonHelper fastJson2,
                      JackJsonJsonHelper jackJson,
                      Json extraJson) {
        this.jsonType = jsonType;
        this.fastJson = fastJson;
        this.fastJson2 = fastJson2;
        this.jackJson = jackJson;
        this.extraJson = extraJson;
        JsonHelper.jsonHelper = this;

    }

    /**
     * JsonArray
     *
     * @param obj
     * @return /
     */
    @Override
    public String objectToJsonArray(Object obj) {
        return this.objectToJsonArray(obj, jsonType);
    }

    /**
     * JsonArray
     *
     * @param obj
     * @return /
     */
    @Override
    public byte[] objectToJsonArrayByte(Object obj) {
        return this.objectToJsonArrayByte(obj, jsonType);
    }


    /**
     * Json  byte[]
     *
     * @param obj
     * @return /
     */
    @Override
    public byte[] toJsonByte(Object obj) {
        return this.toJsonByte(obj, jsonType);
    }

    /**
     * Json
     *
     * @param obj
     * @return /
     */
    @Override
    public String objectToJson(Object obj) {
        return this.objectToJson(obj, jsonType);
    }


    @Override
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, DataType<T> dataType) {
        return getJson(jsonType).fromJsonByteArray(jsonBytes, dataType);
    }

    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJson(String json, Class<T> tClass) {
        return getJson(jsonType).fromJson(json, tClass);
    }

    @Override
    public <T> T fromJson(String json, DataType<T> dataType) {
        return getJson(jsonType).fromJson(json, dataType);
    }

    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJsonByte(byte[] json, Class<T> tClass) {
        return getJson(jsonType).fromJsonByte(json, tClass);
    }

    @Override
    public <T> T fromJsonByte(byte[] json, DataType<T> dataType) {
        return getJson(jsonType).fromJsonByte(json, dataType);
    }

    /**
     * Json  byte[]
     *
     * @param obj
     * @return /
     */
    public byte[] toJsonByte(Object obj, JsonType jsonType) {
        return getJson(jsonType).toJsonByte(obj);
    }

    /**
     * Json
     *
     * @param obj
     * @param jsonType
     * @return /
     */
    public String objectToJson(Object obj, JsonType jsonType) {
        return getJson(jsonType).objectToJson(obj);
    }

    /**
     * Json
     *
     * @param obj
     * @param jsonType
     * @return /
     */
    public String objectToJsonArray(Object obj, JsonType jsonType) {
        return getJson(jsonType).objectToJsonArray(obj);

    }

    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonArray(String json, Class<T> tClass) {
        return this.fromJsonArray(json, tClass, jsonType);
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, Class<T> tClass) {
        return this.fromJsonByteArray(jsonBytes, tClass, jsonType);
    }

    public <T> List<T> fromJsonArray(byte[] jsonBytes, DataType<T> dataType, JsonType jsonType) {
        return getJson(jsonType).fromJsonByteArray(jsonBytes, dataType);
    }

    public <T> List<T> fromJsonArray(String json, DataType<T> dataType, JsonType jsonType) {
        return getJson(jsonType).fromJsonArray(json, dataType);
    }

    @Override
    public <T> List<T> fromJsonArray(String json, DataType<T> dataType) {
        return this.getJson(jsonType).fromJsonArray(json, dataType);
    }


    /**
     * Json
     *
     * @param obj
     * @param jsonType
     * @return /
     */
    public byte[] objectToJsonArrayByte(Object obj, JsonType jsonType) {
        return getJson(jsonType).objectToJsonArrayByte(obj);
    }

    /**
     * @param json
     * @param tClass
     * @param jsonType
     * @param <T>
     * @return /
     */
    public <T> T fromJson(String json, Class<T> tClass, JsonType jsonType) {
        return getJson(jsonType).fromJson(json, tClass);
    }

    public <T> T fromJson(String json, DataType<T> dataType, JsonType jsonType) {
        return getJson(jsonType).fromJson(json, dataType);
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param jsonType
     * @param <T>
     * @return /
     */
    public <T> T fromJsonByte(byte[] jsonBytes, Class<T> tClass, JsonType jsonType) {
        return getJson(jsonType).fromJsonByte(jsonBytes, tClass);
    }

    public <T> T fromJsonByte(byte[] jsonBytes, DataType<T> dataType, JsonType jsonType) {
        return getJson(jsonType).fromJsonByte(jsonBytes, dataType);
    }

    /**
     * @param json
     * @param tClass
     * @param jsonType
     * @param <T>
     * @return /
     */
    public <T> List<T> fromJsonArray(String json, Class<T> tClass, JsonType jsonType) {
        return getJson(jsonType).fromJsonArray(json, tClass);
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param jsonType
     * @param <T>
     * @return /
     */
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, Class<T> tClass, JsonType jsonType) {
        return getJson(jsonType).fromJsonByteArray(jsonBytes, tClass);
    }

    private Json getJson(JsonType jsonType) {
        Json json;
        switch (jsonType) {
            case FAST_JSON:
                json = fastJson;
                break;
            case FAST_JSON2:
                json = fastJson2;
                break;
            case JACK_JSON:
                json = jackJson;
                break;
            default:
                json = extraJson;
        }
        return json;
    }

}
