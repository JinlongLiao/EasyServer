package io.github.jinlongliao.easy.server.cached.exception;

import java.lang.reflect.Method;

/**
 * @author: liaojinlong
 * @date: 2023/8/7 17:53
 */
public class ExeTimeoutException extends AccessLimitException {

    public ExeTimeoutException(long timeout, Method method, Throwable throwable) {
        super(method.getName() + " exe: " + timeout + "mills timeout", throwable);
    }

    public ExeTimeoutException(long timeout, Method method) {
        super(method.getName() + " exe: " + timeout + "mills timeout");
    }

    public ExeTimeoutException(Throwable throwable) {
        super(throwable);
    }
}
