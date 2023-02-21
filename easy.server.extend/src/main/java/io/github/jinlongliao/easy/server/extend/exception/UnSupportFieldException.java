package io.github.jinlongliao.easy.server.extend.exception;
/**
 * @date 2022-12-15 14:33
 * @author: liaojinlong
 * @description: /
**/

public class UnSupportFieldException extends RuntimeException{
    public UnSupportFieldException() {
        super();
    }

    public UnSupportFieldException(String message) {
        super(message);
    }

    public UnSupportFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnSupportFieldException(Throwable cause) {
        super(cause);
    }

    protected UnSupportFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
