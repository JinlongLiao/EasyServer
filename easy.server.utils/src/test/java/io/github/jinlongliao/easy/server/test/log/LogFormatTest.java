package io.github.jinlongliao.easy.server.test.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: liaojinlong
 * @date: 2022-06-10 09:59
 */

public class LogFormatTest {
    private static final Logger log = LoggerFactory.getLogger(LogFormatTest.class);
    private Exception e = new RuntimeException("*****");

    @Test
    public void loggerEx() {
        log.error(e.getMessage(), e);
    }
}
