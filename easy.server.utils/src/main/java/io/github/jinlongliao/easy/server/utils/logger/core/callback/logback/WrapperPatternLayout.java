package io.github.jinlongliao.easy.server.utils.logger.core.callback.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.github.jinlongliao.easy.server.utils.logger.core.callback.LoggerCallback;
import io.github.jinlongliao.easy.server.utils.logger.core.callback.LoggerCallbackFactory;

/**
 * @author: liaojinlong
 * @date: 2023/5/28 20:20
 */
public class WrapperPatternLayout extends PatternLayout {
    @Override
    public String doLayout(ILoggingEvent event) {
        String doLayout = super.doLayout(event);
        for (LoggerCallback loggerCallback : LoggerCallbackFactory.LOGGER_CALLBACKS) {
            doLayout = loggerCallback.loggerCall(doLayout);
        }
        return doLayout;
    }
}
