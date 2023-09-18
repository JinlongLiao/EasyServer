package io.github.jinlongliao.easy.server.boot.demo.logic.service;

import io.github.jinlongliao.easy.server.cached.annotation.EnableCache;
import io.github.jinlongliao.easy.server.cached.annotation.WeAsync;
import io.github.jinlongliao.easy.server.utils.common.UUIDHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

/**
 * 测试异步
 *
 * @author: liaojinlong
 * @date: 2023/9/18 21:29
 */
@Service
@EnableCache
public class TestAsyncService {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Async
    public void testAsync() {
        log.info("threadId:{} name:{}", Thread.currentThread().getId(), Thread.currentThread().getName());
    }

    @WeAsync(maxTimeout = 1000L)
    public Object testWeAsync(boolean block) {
        if (block) {
            try {
                Thread.sleep((20000));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return e.getMessage();
            }
        }
        log.info("threadId:{} name:{}", Thread.currentThread().getId(), Thread.currentThread().getName());
        return UUIDHelper.mongoId();
    }
}
