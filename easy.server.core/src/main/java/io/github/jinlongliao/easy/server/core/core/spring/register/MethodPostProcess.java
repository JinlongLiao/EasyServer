package io.github.jinlongliao.easy.server.core.core.spring.register;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;

/**
 * 执行方法 实力化后处理类
 *
 * @author: liaojinlong
 * @date: 2022-11-24 15:24
 */
public interface MethodPostProcess {
    /**
     * 是否生产新的处理方法
     *
     * @param userClass    实例
     * @param directMethod 执行方法
     * @return 新执行方法
     */
    default DirectMethod process(Class<?> userClass, DirectMethod directMethod) {
        return directMethod;
    }

    /**
     * 权重
     *
     * @return /
     */
    default int order() {
        return 0;
    }
}
