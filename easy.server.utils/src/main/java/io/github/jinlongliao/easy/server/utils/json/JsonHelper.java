package io.github.jinlongliao.easy.server.utils.json;

import io.github.jinlongliao.easy.server.utils.json.extra.FastJsonJsonHelper;
import io.github.jinlongliao.easy.server.utils.json.extra.JackJsonJsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.jinlongliao.easy.server.utils.json.extra.FastJson2JsonHelper;

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

    public JsonHelper(JsonType jsonType, FastJsonJsonHelper fastJson, FastJson2JsonHelper fastJson2, JackJsonJsonHelper jackJson, Json extraJson) {
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

    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJson(String json, Class<T> tClass) {
        return this.fromJson(json, tClass, jsonType);
    }

    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJsonByte(byte[] json, Class<T> tClass) {
        return this.fromJsonByte(json, tClass, jsonType);
    }

    /**
     * Json  byte[]
     *
     * @param obj
     * @return /
     */
    public byte[] toJsonByte(Object obj, JsonType jsonType) {
        byte[] data;
        switch (jsonType) {
            case FAST_JSON:
                data = fastJson.toJsonByte(obj);
                break;
            case FAST_JSON2:
                data = fastJson2.toJsonByte(obj);
                break;
            case JACK_JSON:
                data = jackJson.toJsonByte(obj);
                break;
            default:
                data = extraJson.toJsonByte(obj);
        }
        return data;
    }

    /**
     * Json
     *
     * @param obj
     * @param jsonType
     * @return /
     */
    public String objectToJson(Object obj, JsonType jsonType) {
        String data;
        switch (jsonType) {
            case FAST_JSON2:
                data = fastJson2.objectToJson(obj);
                break;
            case FAST_JSON:
                data = fastJson.objectToJson(obj);
                break;
            case JACK_JSON:
                data = jackJson.objectToJson(obj);
                break;
            default:
                data = extraJson.objectToJson(obj);
        }
        return data;
    }

    /**
     * Json
     *
     * @param obj
     * @param jsonType
     * @return /
     */
    public String objectToJsonArray(Object obj, JsonType jsonType) {
        String data;
        switch (jsonType) {
            case FAST_JSON:
                data = fastJson.objectToJsonArray(obj);
                break;
            case FAST_JSON2:
                data = fastJson2.objectToJsonArray(obj);
                break;
            case JACK_JSON:
                data = jackJson.objectToJsonArray(obj);
                break;
            default:
                data = extraJson.objectToJsonArray(obj);
        }
        return data;
    }

    /**
     * Json
     *
     * @param obj
     * @param jsonType
     * @return /
     */
    public byte[] objectToJsonArrayByte(Object obj, JsonType jsonType) {
        byte[] data;
        switch (jsonType) {
            case FAST_JSON:
                data = fastJson.objectToJsonArrayByte(obj);
                break;
            case FAST_JSON2:
                data = fastJson2.objectToJsonArrayByte(obj);
                break;
            case JACK_JSON:
                data = jackJson.objectToJsonArrayByte(obj);
                break;
            default:
                data = extraJson.objectToJsonArrayByte(obj);
        }
        return data;
    }

    /**
     * @param json
     * @param tClass
     * @param jsonType
     * @param <T>
     * @return /
     */
    public <T> T fromJson(String json, Class<T> tClass, JsonType jsonType) {
        T data;
        switch (jsonType) {
            case FAST_JSON:
                data = fastJson.fromJson(json, tClass);
                break;
            case FAST_JSON2:
                data = fastJson2.fromJson(json, tClass);
                break;
            case JACK_JSON:
                data = jackJson.fromJson(json, tClass);
                break;
            default:
                data = extraJson.fromJson(json, tClass);
        }
        return data;
    }


    /**
     * @param jsonBytes
     * @param tClass
     * @param jsonType
     * @param <T>
     * @return /
     */
    public <T> T fromJsonByte(byte[] jsonBytes, Class<T> tClass, JsonType jsonType) {
        T data;
        switch (jsonType) {
            case FAST_JSON:
                data = fastJson.fromJsonByte(jsonBytes, tClass);
                break;
            case FAST_JSON2:
                data = fastJson2.fromJsonByte(jsonBytes, tClass);
                break;
            case JACK_JSON:
                data = jackJson.fromJsonByte(jsonBytes, tClass);
                break;
            default:
                data = extraJson.fromJsonByte(jsonBytes, tClass);
        }
        return data;
    }

    /**
     * @param json
     * @param tClass
     * @param jsonType
     * @param <T>
     * @return /
     */
    public <T> List<T> fromJsonArray(String json, Class<T> tClass, JsonType jsonType) {
        List<T> data;
        switch (jsonType) {
            case FAST_JSON:
                data = fastJson.fromJsonArray(json, tClass);
                break;
            case FAST_JSON2:
                data = fastJson2.fromJsonArray(json, tClass);
                break;
            case JACK_JSON:
                data = jackJson.fromJsonArray(json, tClass);
                break;
            default:
                data = extraJson.fromJsonArray(json, tClass);
        }
        return data;
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param jsonType
     * @param <T>
     * @return /
     */
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, Class<T> tClass, JsonType jsonType) {
        List<T> data;
        switch (jsonType) {
            case FAST_JSON:
                data = fastJson.fromJsonByteArray(jsonBytes, tClass);
                break;
            case FAST_JSON2:
                data = fastJson2.fromJsonByteArray(jsonBytes, tClass);
                break;
            case JACK_JSON:
                data = jackJson.fromJsonByteArray(jsonBytes, tClass);
                break;
            default:
                data = extraJson.fromJsonByteArray(jsonBytes, tClass);
        }
        return data;
    }
}
