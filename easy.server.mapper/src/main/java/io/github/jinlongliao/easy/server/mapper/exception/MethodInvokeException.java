package io.github.jinlongliao.easy.server.mapper.exception;

/**
 * @author: liaojinlong
 * @date: 2022/8/16 22:53
 */

public class MethodInvokeException extends Exception {
    private final Throwable targetException;

    public MethodInvokeException() {
        super();
        this.targetException = this;
    }

    public MethodInvokeException(String message) {
        super(message);
        this.targetException = this;
    }

    public MethodInvokeException(String message, Throwable cause) {
        super(message, cause);
        this.targetException = cause;
    }

    public MethodInvokeException(Throwable cause) {
        super(cause);
        this.targetException = cause;
    }

    protected MethodInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.targetException = cause;

    }

    public Throwable getTargetException() {
        return targetException;
    }
}
