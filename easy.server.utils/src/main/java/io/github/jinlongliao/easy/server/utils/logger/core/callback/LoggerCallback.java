package io.github.jinlongliao.easy.server.utils.logger.core.callback;

/**
 * @author: liaojinlong
 * @date: 2023/5/28 20:24
 */
public interface LoggerCallback {
    default <T extends CharSequence> T loggerCall(T logger) {
        return logger;
    }
}
