package io.github.jinlongliao.easy.server.utils.logger.core.operate.inner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.github.jinlongliao.easy.server.utils.logger.LoggerUtils;
import io.github.jinlongliao.easy.server.utils.logger.core.constant.LogType;
import io.github.jinlongliao.easy.server.utils.logger.core.constant.LoggerLevel;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * LogBack 日志操作实现
 *
 * @author liaojinlong
 * @since 2021-11-25 15:39
 */
public class LogBackLoggerOperator extends AbstractLog4jLoggerOperator {

    private static LoggerContext LOGGERCONTEXT;
    private static LoggerLevel ROOT_LEVEL;

    static {
        if (LoggerUtils.getCurrentLogType() == LogType.LOGBACK) {
            LOGGERCONTEXT = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
        }
    }

    /**
     * 获取指定路径的日志级别
     *
     * @param path 代码路径
     * @return /
     */
    @Override
    public LoggerLevel getLoggerLevel(String path) {
        return getLoggerLevel(getLogger(path));
    }

    public LoggerLevel getLoggerLevel(Logger logger) {
        if (logger == null || logger.getLevel() == null) {
            return ROOT_LEVEL != null ?
                    ROOT_LEVEL :
                    (ROOT_LEVEL = LoggerLevel.getLogLevel(LOGGERCONTEXT
                            .getLogger(ROOT_NODE)
                            .getLevel().levelStr));
        }
        return LoggerLevel.getLogLevel(logger
                .getLevel()
                .levelStr);
    }

    private Logger getLogger(String path) {
        return LOGGERCONTEXT.getLogger(path);
    }

    /**
     * 设置指定路径的日志级别
     *
     * @param path  /
     * @param level /
     * @return /
     */
    @Override
    public boolean setLoggerLevel(String path, LoggerLevel level) {
        Logger logger = getLogger(path);
        if (logger == null) {
            return false;
        }
        if (isSameLevel(getLoggerLevel(logger), level)) {
            return false;
        }
        logger.setLevel(Level.toLevel(level.level));
        logger.setAdditive(true);
        return true;
    }

    /**
     * 对应日志的实现
     * return /
     */
    @Override
    public LogType supportLogType() {
        return LogType.LOGBACK;
    }
}
