package io.github.jinlongliao.easy.server.utils.logger.core.callback;


import java.util.*;

/**
 * @author: liaojinlong
 * @date: 2023/5/28 20:24
 */
public class LoggerCallbackFactory {
    public static final List<LoggerCallback> LOGGER_CALLBACKS = new ArrayList<>(2);

    static {

        final ServiceLoader<LoggerCallback> operators = ServiceLoader.load(LoggerCallback.class);
        for (LoggerCallback operator : operators) {
            LOGGER_CALLBACKS.add(operator);
        }
    }

}
