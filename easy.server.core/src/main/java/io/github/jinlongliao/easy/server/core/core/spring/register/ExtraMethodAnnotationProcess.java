package io.github.jinlongliao.easy.server.core.core.spring.register;

import io.github.jinlongliao.easy.server.core.core.MethodInfo;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @date 2023-01-29 12:00
 * @author: liaojinlong
 * @description: 用于扩展的声明注解处理
 **/

public interface ExtraMethodAnnotationProcess {
    /**
     * 返回支持的ID
     *
     * @param data
     * @param method
     * @return /
     */
    ExtraMethodDesc extraProcessMethod(Map<Integer, MethodInfo> data, Method method);

    /**
     * 权重
     *
     * @return /
     */
    default int order() {
        return 0;
    }
}

