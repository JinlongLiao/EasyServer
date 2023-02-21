package io.github.jinlongliao.easy.server.utils.logger.core.operate.inner;

import io.github.jinlongliao.easy.server.utils.logger.core.constant.LogType;
import io.github.jinlongliao.easy.server.utils.logger.core.constant.LoggerLevel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.StackLocatorUtil;

import java.util.Map;


/**
 * log4j2 日志操作实现
 *
 * @author liaojinlong
 * @since 2021-11-25 15:39
 */
public class Log4j2LoggerOperator extends AbstractLog4jLoggerOperator {
    private static final String FQCN = "org.apache.logging.slf4j.Log4jLoggerFactory";
    private static final String PACKAGE = "org.slf4j";
    private static LoggerContext LOGGER_CONTEXT;
    private static LoggerLevel ROOT_LEVEL;


    /**
     * 获取指定路径的日志级别
     *
     * @param path 代码路径
     * @return /
     */
    @Override
    public LoggerLevel getLoggerLevel(String path) {
        final LoggerContext context = getContext();
        ExtendedLogger logger = context.getLogger(path);
        if (logger == null) {
            return getRootLevel(context);
        }
        return LoggerLevel.getLogLevel(logger.getLevel().name());
    }

    private LoggerLevel getRootLevel(LoggerContext context) {
        if (ROOT_LEVEL == null) {
            ExtendedLogger logger = context.getLogger(ROOT_NODE);
            return ROOT_LEVEL = LoggerLevel.getLogLevel(logger.getLevel().name());
        }
        return ROOT_LEVEL;

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
        if (isSameLevel(path, level)) {
            return false;
        }
        LoggerContext context = getContext();
        if (context instanceof org.apache.logging.log4j.core.LoggerContext) {
            org.apache.logging.log4j.core.LoggerContext loggerContext = (org.apache.logging.log4j.core.LoggerContext) context;
            Configuration configuration = loggerContext.getConfiguration();
            LoggerConfig loggerConfig = configuration.getLoggerConfig(path);
            final Level newLevel = Level.getLevel(level.level);
            if (!loggerConfig.getName().equals(path)) {
                final LogEventFactory logEventFactory = loggerConfig.getLogEventFactory();
                final Map<String, Appender> appenders = loggerConfig.getAppenders();
                final Filter filter = loggerConfig.getFilter();
                final LoggerConfig parent = loggerConfig.getParent();
                loggerConfig = new LoggerConfig(path, newLevel, false);
                loggerConfig.setParent(parent);
                loggerConfig.setLogEventFactory(logEventFactory);
                for (Map.Entry<String, Appender> appenderEntry : appenders.entrySet()) {
                    loggerConfig.addAppender(appenderEntry.getValue(), newLevel, filter);
                }
                configuration.addLogger(path, loggerConfig);
            }
            loggerConfig.setLevel(newLevel);
            loggerContext.updateLoggers(configuration);
            return true;
        }
        return false;
    }

    /**
     * 对应日志的实现
     * return /
     */
    @Override
    public LogType supportLogType() {
        return LogType.LOG4J2;
    }

    protected LoggerContext getContext() {
        if (LOGGER_CONTEXT == null) {
            final Class<?> anchor = StackLocatorUtil.getCallerClass(FQCN, PACKAGE);
            LOGGER_CONTEXT = anchor == null ? LogManager.getContext() : getContext(StackLocatorUtil.getCallerClass(anchor));
        }
        return LOGGER_CONTEXT;
    }

    protected LoggerContext getContext(final Class<?> callerClass) {
        ClassLoader cl = null;
        if (callerClass != null) {
            cl = callerClass.getClassLoader();
        }
        if (cl == null) {
            cl = LoaderUtil.getThreadContextClassLoader();
        }
        return LogManager.getContext(cl, false);
    }
}
