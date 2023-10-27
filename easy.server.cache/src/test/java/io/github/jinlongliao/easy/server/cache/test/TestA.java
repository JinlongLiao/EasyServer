package io.github.jinlongliao.easy.server.cache.test;

import io.github.jinlongliao.easy.server.cached.annotation.EnableCache;
import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import org.springframework.stereotype.Component;

@EnableCache
@Component
public class TestA {
    @GetCache(milliSecond = 4)
    public String say() {
        return "before:" + System.currentTimeMillis();
    }
}
