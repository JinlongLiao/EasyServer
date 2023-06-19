package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator;

/**
 * @author: liaojinlong
 * @date: 2022/5/21 21:27
 */
public class FieldParserBody {
    private final String ownerName;
    private final int index;
    private final String sourceName;
    private final String putMethod;
    private final Class<?> filedType;
    private final Class<?> convertClass;
    private final String convertMethod;
    private final boolean isParent;

    public FieldParserBody(String ownerName,
                           int index,
                           String sourceName,
                           String putMethod,
                           Class<?> filedType, Class<?> convertClass,
                           String convertMethod,
                           boolean isParent) {
        this.ownerName = ownerName;
        this.index = index;
        this.sourceName = sourceName;
        this.putMethod = putMethod;
        this.filedType = filedType;
        this.convertClass = convertClass;
        this.convertMethod = convertMethod;
        this.isParent = isParent;
    }

    public int getIndex() {
        return index;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getPutMethod() {
        return putMethod;
    }

    public Class<?> getFiledType() {
        return filedType;
    }

    public Class<?> getConvertClass() {
        return convertClass;
    }

    public String getConvertMethod() {
        return convertMethod;
    }

    public boolean isParent() {
        return isParent;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return "FieldParserBody{" +
                "ownerName='" + ownerName + '\'' +
                ", index=" + index +
                ", sourceName='" + sourceName + '\'' +
                ", putMethod='" + putMethod + '\'' +
                ", filedType=" + filedType +
                ", convertClass=" + convertClass +
                ", convertMethod='" + convertMethod + '\'' +
                ", isParent=" + isParent +
                '}';
    }
}
