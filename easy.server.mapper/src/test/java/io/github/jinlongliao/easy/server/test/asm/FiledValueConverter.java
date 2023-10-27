package io.github.jinlongliao.easy.server.test.asm;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.ExtraFieldConverter;

public class FiledValueConverter implements ExtraFieldConverter {
    @Override
    public Object converter(Object value, int index, String filedName, String typeName, boolean isParent) {
        return value;
    }
}
