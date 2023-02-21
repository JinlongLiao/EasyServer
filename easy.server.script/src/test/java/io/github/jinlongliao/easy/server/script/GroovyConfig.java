package io.github.jinlongliao.easy.server.script;

import io.github.jinlongliao.easy.server.script.groovy.annotation.EnableRefresh;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@EnableRefresh(scriptPaths = {"classpath:/groovy/*.groovy"}, refreshDelay = 1)
public class GroovyConfig {
    @Bean()
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        ThreadPoolTaskExecutor tf = new ThreadPoolTaskExecutor();
        tf.setCorePoolSize(1);
        tf.setMaxPoolSize(2);
        tf.setThreadNamePrefix("GroovyConfig thread");
        tf.initialize();
        return new ScheduledThreadPoolExecutor(1, tf);
    }
}
