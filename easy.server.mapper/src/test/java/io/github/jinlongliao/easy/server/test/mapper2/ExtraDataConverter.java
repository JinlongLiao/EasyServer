package io.github.jinlongliao.easy.server.test.mapper2;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter.DefaultDataConverter;

/**
 * @date 2023-01-16 17:33
 * @author: liaojinlong
 * @description: 扩展使用
 **/

public class ExtraDataConverter extends DefaultDataConverter {
    @Override
    public <T> T getT(Class<T> tClass, Object extra, Object data) {
        return super.getT(tClass, extra, data);
    }
}
