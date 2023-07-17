package io.github.jinlongliao.easy.server.extend.response.proxy;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.FieldParserBody;

public class ExtraFieldParserBody extends FieldParserBody {
    private final boolean isStr;
    private final boolean isDynamic;
    private final int strLen;

    public ExtraFieldParserBody(String ownerName,
                                int index,
                                String sourceName,
                                String putMethod,
                                Class<?> filedType,
                                Class<?> convertClass,
                                String convertMethod,
                                boolean isParent,
                                boolean isStr,
                                boolean isDynamic,
                                int strLen) {
        super(ownerName, index, sourceName, putMethod, filedType, convertClass, convertMethod, isParent);
        this.isStr = isStr;
        this.isDynamic = isDynamic;
        this.strLen = strLen;
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    public int getStrLen() {
        return strLen;
    }

    public boolean isStr() {
        return isStr;
    }

    @Override
    public String toString() {
        return "ExtraFieldParserBody{" +
                "isStr=" + isStr +
                ", isDynamic=" + isDynamic +
                ", strLen=" + strLen +
                '}';
    }
}
