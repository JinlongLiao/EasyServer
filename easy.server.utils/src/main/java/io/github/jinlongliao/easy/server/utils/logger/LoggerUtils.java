package io.github.jinlongliao.easy.server.utils.logger;

import io.github.jinlongliao.easy.server.utils.logger.core.constant.LogType;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * @author liaojinlong
 * @since 2021-11-25 15:08
 */
public class LoggerUtils {
    /**
     * 获取当前日志实现类
     *
     * @return /
     */
    public static LogType getCurrentLogType() {
        try {
            final StaticLoggerBinder staticLoggerBinder = StaticLoggerBinder.getSingleton();
            return LogType.valuesOf(staticLoggerBinder.getLoggerFactoryClassStr());
        } catch (NoClassDefFoundError e) {
        }
        return LogType.UN_KNOW;
    }
}
