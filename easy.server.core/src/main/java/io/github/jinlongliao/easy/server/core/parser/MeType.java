package io.github.jinlongliao.easy.server.core.parser;

/**
 * 字段类型
 *
 * @author liaojinlong
 * @since 2021/1/25 10:11
 */
public class MeType {
    /**
     * 类型
     */
    private Class<?> type;
    /**
     * 数据长度
     */
    private int len;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 是否公共参数
     */
    private boolean isCommon;
    /**
     * 是否为包装参数
     */
    private boolean isBody;

    private Object defaultValue;
    private boolean innerField;

    public MeType(Class<?> type, int len, String paramName, Object defValue) {
        this(type, len, paramName, defValue, false);
    }

    public MeType(Class<?> type, int len, String paramName, Object obj, boolean isCommon) {
        this(type, len, paramName, obj, isCommon, false);
    }

    public MeType(Class<?> type, int len, String paramName, Object obj, boolean isCommon, boolean isBody) {
        this.type = type;
        this.len = len;
        this.defaultValue = obj;
        this.paramName = paramName;
        this.isCommon = isCommon;
        this.isBody = isBody;
    }

    public boolean isInnerField() {
        return innerField;
    }

    public void setInnerField(boolean innerField) {
        this.innerField = innerField;
    }

    public boolean isBody() {
        return isBody;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public boolean isCommon() {
        return isCommon;
    }

    public void setCommon(boolean common) {
        isCommon = common;
    }

    public void setBody(boolean body) {
        isBody = body;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean hasDef() {
        return defaultValue != null;
    }

    @Override
    public String toString() {
        return "MeType{" +
                "type=" + type +
                ", len=" + len +
                ", paramName='" + paramName + '\'' +
                ", isCommon=" + isCommon +
                ", isBody=" + isBody +
                ", defaultValue=" + defaultValue +
                ", innerField=" + innerField +
                '}';
    }
}
