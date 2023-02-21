package io.github.jinlongliao.easy.server.utils.logger.core.operate.inner;

import io.github.jinlongliao.easy.server.utils.logger.core.constant.LoggerLevel;
import io.github.jinlongliao.easy.server.utils.logger.core.operate.LoggerOperator;

/**
 * Log4j 系的日志操作类型
 *
 * @author liaojinlong
 * @since 2021/11/28 18:10
 */
public abstract class AbstractLog4jLoggerOperator implements LoggerOperator {
    public static LoggerLevel ROOT_LEVEL;
    public static String ROOT_NODE = "ROOT";

    /**
     * 判断是否同一 日志级别
     *
     * @param target 原
     * @param level  日志
     * @return /
     */
    protected boolean isSameLevel(LoggerLevel target, LoggerLevel level) {
        return target == level;
    }

    /**
     * 判断是否同一 日志级别
     *
     * @param path  路径
     * @param level 日志
     * @return /
     */
    protected boolean isSameLevel(String path, LoggerLevel level) {
        return getLoggerLevel(path) == level;
    }

    /**
     * 判断是否同一 日志级别
     *
     * @return /
     */
    protected boolean isSameLevel(String t, String n) {
        return t.equalsIgnoreCase(n);
    }
}
