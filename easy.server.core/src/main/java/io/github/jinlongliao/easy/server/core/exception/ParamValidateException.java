package io.github.jinlongliao.easy.server.core.exception;

/**
 * 参数校验异常
 *
 * @author liaojinlong
 * @since 2021/2/5 1:25
 */
public class ParamValidateException extends ValidateException {
    public ParamValidateException(String message) {
        super(message);
    }
}
