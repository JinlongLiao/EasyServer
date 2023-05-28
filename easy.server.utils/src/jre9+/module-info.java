module easy.server.utils {
    exports io.github.jinlongliao.easy.server.utils.json;
    exports io.github.jinlongliao.easy.server.utils.json.extra;
    exports io.github.jinlongliao.easy.server.utils.common;
    exports io.github.jinlongliao.easy.server.utils.logger.core.operate;
    exports io.github.jinlongliao.easy.server.utils.logger.core.operate.inner;
    exports io.github.jinlongliao.easy.server.utils.logger.core.constant;
    exports io.github.jinlongliao.easy.server.utils.logger.core.callback;
    exports io.github.jinlongliao.easy.server.utils.logger.core.log4j2;
    exports io.github.jinlongliao.easy.server.utils.logger.core.logback;

    requires org.slf4j;

    requires static com.alibaba.fastjson2;
    requires static com.fasterxml.jackson.core;
    requires static com.fasterxml.jackson.databind;
    requires static ch.qos.logback.classic;
    requires static org.apache.logging.log4j;
    requires static org.apache.logging.log4j.core;
    requires static javax.servlet.api;

    uses io.github.jinlongliao.easy.server.utils.logger.core.operate.LoggerOperator;
    uses io.github.jinlongliao.easy.server.utils.logger.core.callback.LoggerCallback;

    provides io.github.jinlongliao.easy.server.utils.logger.core.operate.LoggerOperator with
            io.github.jinlongliao.easy.server.utils.logger.core.operate.inner.LogBackLoggerOperator,
            io.github.jinlongliao.easy.server.utils.logger.core.operate.inner.Log4j2LoggerOperator;

}
