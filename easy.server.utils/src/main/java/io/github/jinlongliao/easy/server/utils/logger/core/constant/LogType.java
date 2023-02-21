package io.github.jinlongliao.easy.server.utils.logger.core.constant;

/**
 * @author liaojinlong
 * @since 2021-11-25 10:43
 */
public enum LogType {

    /**
     * 未知或无实现
     */
    UN_KNOW(""),
    /**
     * log4j
     */
    LOG4J("org.slf4j.impl.Log4jLoggerFactory"),
    /**
     * logback
     */
    LOGBACK("ch.qos.logback.classic.util.ContextSelectorStaticBinder"),
    /**
     * log4j2
     */
    LOG4J2("org.apache.logging.slf4j.Log4jLoggerFactory"),

    ;
    public final String type;

    LogType(String type) {
        this.type = type;
    }

    private static LogType CU;

    /**
     * 获取当前slf4j 的实现类
     *
     * @param type
     * @return /
     */
    public static LogType valuesOf(String type) {
        if (CU == null) {
            CU = UN_KNOW;
            for (LogType logType : LogType.values()) {
                if (logType.type.equals(type)) {
                    CU = logType;
                    break;
                }
            }
        }
        return CU;
    }
}
