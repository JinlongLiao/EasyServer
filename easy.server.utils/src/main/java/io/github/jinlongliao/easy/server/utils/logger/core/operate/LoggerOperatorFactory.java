package io.github.jinlongliao.easy.server.utils.logger.core.operate;

import io.github.jinlongliao.easy.server.utils.logger.LoggerUtils;
import io.github.jinlongliao.easy.server.utils.logger.core.constant.LogType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author liaojinlong
 * @since 2021-11-25 15:31
 */
public class LoggerOperatorFactory {


    private static final Map<LogType, LoggerOperator> LOGGER_OPERATOR;

    static {
        LOGGER_OPERATOR = new HashMap<>(4);
        final ServiceLoader<LoggerOperator> operators = ServiceLoader.load(LoggerOperator.class);
        final Iterator<LoggerOperator> operatorIterator = operators.iterator();
        while (operatorIterator.hasNext()) {
            final LoggerOperator loggerOperator = operatorIterator.next();
            LOGGER_OPERATOR.put(loggerOperator.supportLogType(), loggerOperator);
        }
    }

    /**
     * 是否包含指定日志的操作类
     *
     * @param logType
     * @return LoggerOperator
     */
    public static boolean hasLoggerOperator(LogType logType) {
        return LOGGER_OPERATOR.containsKey(logType);
    }

    /**
     * 获取指定日志的操作类
     *
     * @param logType
     * @return LoggerOperator
     */
    public static LoggerOperator getLoggerOperator(LogType logType) {
        return LOGGER_OPERATOR.get(logType);
    }

    /**
     * 获取当前日志的操作类
     *
     * @return LoggerOperator
     */
    public static LoggerOperator getLoggerOperator() {
        return LoggerOperatorFactory.getLoggerOperator(LoggerUtils.getCurrentLogType());
    }
}
