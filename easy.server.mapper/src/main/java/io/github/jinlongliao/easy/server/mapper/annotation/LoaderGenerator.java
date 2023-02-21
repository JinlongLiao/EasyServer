package io.github.jinlongliao.easy.server.mapper.annotation;

import java.io.IOException;

/**
 * @date 2022-12-29 12:29
 * @author: liaojinlong
 * @description: 反向生成 时 执行接口 基于SPI
 **/

public interface LoaderGenerator {
    /**
     * 加载
     *
     * @throws IOException
     */
    void loader(Object... args) throws IOException;
}
