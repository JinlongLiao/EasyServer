package io.github.jinlongliao.easy.server.cached.exception;

/**
 * 访问频繁
 *
 * @author: liaojinlong
 * @date: 2023/7/9 22:09
 */
public class AccessLimitException extends RuntimeException {
    public AccessLimitException() {
        super("access Limit");
    }

    public AccessLimitException(String message) {
        super(message);
    }

    public AccessLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessLimitException(Throwable cause) {
        super(cause);
    }

    protected AccessLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
