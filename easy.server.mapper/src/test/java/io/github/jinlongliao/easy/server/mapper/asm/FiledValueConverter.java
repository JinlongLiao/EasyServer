package io.github.jinlongliao.easy.server.mapper.asm;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.ExtraFieldConverter;

public class FiledValueConverter implements ExtraFieldConverter {
    @Override
    public Object converter(Object value, int index, String filedName, String typeName, boolean isParent) {
        return value;
    }
}
