package io.github.jinlongliao.easy.server.cached;

import io.github.jinlongliao.easy.server.cached.annotation.EnableCache;
import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import org.springframework.stereotype.Component;

@EnableCache
@Component
public class TestA {
    @GetCache(second = 4)
    public String say() {
        return "before:" + System.currentTimeMillis();
    }
}
