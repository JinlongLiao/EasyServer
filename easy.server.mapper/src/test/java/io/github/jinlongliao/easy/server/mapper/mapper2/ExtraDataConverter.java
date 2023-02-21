package io.github.jinlongliao.easy.server.mapper.mapper2;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.DefaultDataConverter;

/**
 * @date 2023-01-16 17:33
 * @author: liaojinlong
 * @description: 扩展使用
 **/

public class ExtraDataConverter extends DefaultDataConverter {
    @Override
    public <T> T getT(Class<T> tClass, Object extra, Object data) {
        return (T) data;
    }
}
