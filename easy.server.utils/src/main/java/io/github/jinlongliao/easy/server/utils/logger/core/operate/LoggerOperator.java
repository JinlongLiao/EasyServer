package io.github.jinlongliao.easy.server.utils.logger.core.operate;

import io.github.jinlongliao.easy.server.utils.logger.core.constant.LogType;
import io.github.jinlongliao.easy.server.utils.logger.core.constant.LoggerLevel;

/**
 * 日志运行态 操作
 *
 * @author liaojinlong
 * @since 2021-11-25 15:15
 */
public interface LoggerOperator {
    /**
     * 获取指定路径的日志级别
     *
     * @param path 代码路径
     * @return /
     */
    LoggerLevel getLoggerLevel(String path);

    /**
     * 设置指定路径的日志级别
     *
     * @param path  代码路径
     * @param level 等级
     * @return /
     */
    boolean setLoggerLevel(String path, LoggerLevel level);

    /**
     * 对应日志的实现
     * return /
     */
    LogType supportLogType();
}
