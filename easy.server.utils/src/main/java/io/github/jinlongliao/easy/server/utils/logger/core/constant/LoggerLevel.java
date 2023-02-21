package io.github.jinlongliao.easy.server.utils.logger.core.constant;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 日志级别
 *
 * @author liaojinlong
 * @since 2021-11-25 15:18
 */
public enum LoggerLevel {
    UN_KNOW(""),
    OFF("OFF"),
    FATAL("FATAL"),
    ERROR("ERROR"),
    WARN("WARN"),
    INFO("INFO"),
    DEBUG("DEBUG"),
    TRACE("TRACE"),
    ALL("ALL"),
    ;
    public final String level;

    LoggerLevel(String level) {
        this.level = level;
    }

    private static final Map<String, LoggerLevel> LEVEL_MAP;

    static {
        final LoggerLevel[] loggerLevels = LoggerLevel.values();
        LEVEL_MAP = new HashMap<>(loggerLevels.length);
        for (LoggerLevel loggerLevel : loggerLevels) {
            LEVEL_MAP.put(loggerLevel.level, loggerLevel);
        }
    }

    public static final LoggerLevel getLogLevel(String level) {
        if (level == null) {
            return LoggerLevel.UN_KNOW;
        }
        final LoggerLevel loggerLevel = LEVEL_MAP.get(level.toUpperCase(Locale.ROOT));
        return loggerLevel == null ? UN_KNOW : loggerLevel;
    }
}
