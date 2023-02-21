package io.github.jinlongliao.easy.server.utils.json.extra;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import io.github.jinlongliao.easy.server.utils.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Fast JSON 序列化反序列 包装类
 *不建议使用，fastjson1 后期继续维护可能性极低
 * @author liaojinlong
 * @since 2021/10/21 20:33
 */
@Deprecated
public class FastJsonJsonHelper implements Json {

    private static final Logger log = LoggerFactory.getLogger(FastJsonJsonHelper.class);

    public FastJsonJsonHelper() {
        ParserConfig.getGlobalInstance().setSafeMode(true);
    }


    /**
     * Json  byte[]
     *
     * @param obj
     * @return /
     */
    @Override
    public byte[] toJsonByte(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    /**
     * Json
     *
     * @param obj
     * @return /
     */
    @Override
    public String objectToJson(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * Json
     *
     * @param obj
     * @return /
     */
    @Override
    public String objectToJsonArray(Object obj) {
        return JSONArray.toJSONString(obj);
    }

    /**
     * Json
     *
     * @param obj
     * @return /
     */
    @Override
    public byte[] objectToJsonArrayByte(Object obj) {
        return JSONArray.toJSONBytes(obj);
    }


    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJson(String json, Class<T> tClass) {
        return JSONObject.parseObject(json, tClass);
    }


    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJsonByte(byte[] json, Class<T> tClass) {
        return JSONObject.parseObject(json, tClass);
    }

    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonArray(String json, Class<T> tClass) {
        return JSONArray.parseArray(json, tClass);
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, Class<T> tClass) {
        return JSONArray.parseArray(new String(jsonBytes), tClass);
    }

}
