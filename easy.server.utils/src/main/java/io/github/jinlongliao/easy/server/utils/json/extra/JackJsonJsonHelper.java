package io.github.jinlongliao.easy.server.utils.json.extra;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.jinlongliao.easy.server.utils.json.DataType;
import io.github.jinlongliao.easy.server.utils.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimeZone;

/**
 * Fast JSON 序列化反序列 包装类
 *
 * @author liaojinlong
 * @since 2021/10/21 20:33
 */
public class JackJsonJsonHelper implements Json {

    private static final Logger log = LoggerFactory.getLogger(JackJsonJsonHelper.class);
    private final ObjectMapper objectMapper;

    public JackJsonJsonHelper() {
        this(null);
    }

    public JackJsonJsonHelper(ObjectMapper objectMapper) {
        if (objectMapper == null) {
            JsonMapper mapper = JsonMapper.builder().build();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setTimeZone(TimeZone.getTimeZone("CTT"));
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            this.objectMapper = mapper;
        } else {
            this.objectMapper = objectMapper;
        }
    }


    /**
     * Json  byte[]
     *
     * @param obj
     * @return /
     */
    @Override
    public byte[] toJsonByte(Object obj) {
        try {
            return this.objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;

    }

    /**
     * Json
     *
     * @param obj
     * @return /
     */
    @Override
    public String objectToJson(Object obj) {
        try {
            return this.objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
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
        return this.toJsonByte(obj);
    }


    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJson(String json, Class<T> tClass) {
        try {
            return this.objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> T fromJson(String json, DataType<T> dataType) {
        try {
            return (T) this.objectMapper.readValue(json, dataType.getRawType());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJsonByte(byte[] json, Class<T> tClass) {
        try {
            return this.objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T fromJsonByte(byte[] jsonBytes, DataType<T> tClass) {
        try {
            return this.objectMapper.readValue(jsonBytes, new TypeReference<T>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param json
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonArray(String json, Class<T> tClass) {
        try {
            return objectMapper.readerForListOf(tClass).readValue(json);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param jsonBytes
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, Class<T> tClass) {
        try {
            return objectMapper.readerForListOf(tClass).readValue(jsonBytes);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param json
     * @param dataType
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonArray(String json, DataType<T> dataType) {
        try {
            return objectMapper.readerForListOf(dataType.getRawType()).readValue(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param jsonBytes
     * @param dataType
     * @param <T>
     * @return /
     */
    @Override
    public <T> List<T> fromJsonByteArray(byte[] jsonBytes, DataType<T> dataType) {
        try {
            return objectMapper.readerForListOf(dataType.getRawType()).readValue(jsonBytes);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
