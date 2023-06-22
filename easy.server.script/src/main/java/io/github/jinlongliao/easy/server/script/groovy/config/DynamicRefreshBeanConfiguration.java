package io.github.jinlongliao.easy.server.script.groovy.config;

import io.github.jinlongliao.easy.server.script.groovy.GroovyBeanScriptLoader;
import io.github.jinlongliao.easy.server.script.groovy.config.dynamic.DynamicRefreshBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author liaojinlong
 * @since 2022-02-18 16:33
 */
@Import(GroovyBeanScriptLoader.class)
public class DynamicRefreshBeanConfiguration {
    @Bean
    public DynamicRefreshBean dynamicRefreshBean(ScriptConfig scriptConfig, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        return new DynamicRefreshBean(scriptConfig, scheduledThreadPoolExecutor);
    }
}
