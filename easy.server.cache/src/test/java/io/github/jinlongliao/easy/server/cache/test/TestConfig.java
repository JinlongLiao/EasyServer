package io.github.jinlongliao.easy.server.cache.test;

import io.github.jinlongliao.easy.server.cached.annotation.EnableMethodCache;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableMethodCache(basePackages = "io.github.jinlongliao.easy.server.cached")
@Configuration
@EnableAspectJAutoProxy
public class TestConfig {

}
