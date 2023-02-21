package io.github.jinlongliao.easy.server.core.exception;

/**
 * 校验失败异常
 *
 * @author liaojinlong
 * @since 2021/2/5 1:19
 */
public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }
}
