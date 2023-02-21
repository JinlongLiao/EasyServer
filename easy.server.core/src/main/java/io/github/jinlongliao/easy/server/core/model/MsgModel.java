package io.github.jinlongliao.easy.server.core.model;

import java.lang.annotation.Annotation;

/**
 * 消息体定义缓存
 *
 * @author liaojinlong
 * @since 2021/1/22 16:58
 */
public class MsgModel {
    /**
     * 参数名称
     */
    private final String paramName;
    /**
     * 参数类型
     */
    private final Class<?> type;
    /**
     * 参数长度
     */
    private final int length;

    /**
     * 动态长度
     * 只支持Hex String
     */
    private final boolean dynamicLength;
    /**
     * 额外扩展
     */
    private final Annotation[] extAnnotation;
    /**
     * 此次参数是不是 RequestBody，</br>
     */
    private final boolean isRequestBody;
    /**
     * 对应消息的ID
     */
    private final int msgType;
    /**
     * 参数默认值
     */
    private final String defaultValue;
    /**
     * 内部属性
     */
    private final boolean isCommon;
    /**
     * 内部属性
     */
    private final boolean innerField;


    public MsgModel(String paramName, Class<?> type,
                    int length,
                    boolean dynamicLength,
                    Annotation[] extAnnotation,
                    boolean isRequestBody,
                    int msgType,
                    String defaultValue,
                    boolean isCommon,
                    boolean innerField) {
        this.paramName = paramName;
        this.type = type;
        this.length = length;
        this.dynamicLength = dynamicLength;
        this.extAnnotation = extAnnotation;
        this.isRequestBody = isRequestBody;
        this.msgType = msgType;
        this.defaultValue = defaultValue;
        this.isCommon = isCommon;
        this.innerField = innerField;
    }

    public boolean isInnerField() {
        return innerField;
    }

    public String getParamName() {
        return paramName;
    }

    public Class<?> getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public boolean isDynamicLength() {
        return dynamicLength;
    }

    public Annotation[] getExtAnnotation() {
        return extAnnotation;
    }

    public boolean isRequestBody() {
        return isRequestBody;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isCommon() {
        return isCommon;
    }
}
