package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.method;

import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;

/**
 * @author: liaojinlong
 * @date: 2022/8/16 22:45
 */
public interface MethodInvoke {

    Object invoke(Object obj, Object... args) throws MethodInvokeException;
}
