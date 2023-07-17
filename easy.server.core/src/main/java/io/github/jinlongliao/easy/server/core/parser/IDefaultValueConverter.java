package io.github.jinlongliao.easy.server.core.parser;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter.InnerConverter;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.core.model.MsgModel;

/**
 * @author liaojinlong
 * @since 11/4/2021 8:37 PM
 */
public interface IDefaultValueConverter {
    IDefaultValueConverter DEFAULT = new DefaultValueConverter();

    /**
     * 转换默认值
     *
     * @param msgModel
     * @return /
     */
    Object toConverterDefaultValue(MsgModel msgModel);

}

class DefaultValueConverter implements IDefaultValueConverter {

    /**
     * 转换默认值
     *
     * @param msgModel
     * @return /
     */
    @Override
    public Object toConverterDefaultValue(MsgModel msgModel) {
        final boolean requestBody = msgModel.isRequestBody();
        if (requestBody) {
            return null;
        }
        final String defaultValue = msgModel.getDefaultValue();
        if (defaultValue.isEmpty()) {
            return null;
        }
        final Class<?> fieldClazz = msgModel.getType();
        Object def;
        if (fieldClazz == String.class) {
            // 当变量为字符串类型时，获取该字符长度
            def = defaultValue;
        } else if (CLassUtils.isInteger(fieldClazz)) {
            def = InnerConverter.getInt(defaultValue);
        } else if (CLassUtils.isLong(fieldClazz)) {
            def = InnerConverter.getLong(defaultValue);
        } else if (CLassUtils.isBool(fieldClazz)) {
            def = InnerConverter.getBoolean(defaultValue);
        } else if (CLassUtils.isByte(fieldClazz)) {
            def = InnerConverter.getByte(defaultValue);
        } else if (CLassUtils.isShort(fieldClazz)) {
            def = InnerConverter.getShort(defaultValue);
        } else if (CLassUtils.isFloat(fieldClazz)) {
            def = InnerConverter.getFloat(defaultValue);
        } else if (CLassUtils.isDouble(fieldClazz)) {
            def = InnerConverter.getDouble(defaultValue);
        } else if (CLassUtils.isCharacter(fieldClazz)) {
            def = InnerConverter.getChar(defaultValue);
        } else {
            def = null;
        }
        return def;
    }
}

