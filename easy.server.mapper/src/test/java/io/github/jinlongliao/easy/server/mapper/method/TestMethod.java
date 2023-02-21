package io.github.jinlongliao.easy.server.mapper.method;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.Proxy;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.method.MethodInvoke;
import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;

public final class TestMethod implements MethodInvoke {
    @Override
    public Object invoke(Object obj, Object... args) throws MethodInvokeException {
        try {
            Proxy stream = (Proxy) obj;
            stream.notify();
            return null;
        } catch (Exception e) {
            throw new MethodInvokeException(e);
        }
    }
}
