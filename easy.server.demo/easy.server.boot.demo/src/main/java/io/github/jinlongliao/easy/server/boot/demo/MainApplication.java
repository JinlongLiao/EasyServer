package io.github.jinlongliao.easy.server.boot.demo;

import io.github.jinlongliao.easy.server.boot.demo.config.web.BeanConfiguration;
import io.github.jinlongliao.easy.server.boot.demo.config.web.WebConfig;
import io.github.jinlongliao.easy.server.cached.annotation.EnableMethodCache;
import io.github.jinlongliao.easy.server.core.annotation.LogicContextScan;
import io.github.jinlongliao.easy.server.script.groovy.annotation.EnableRefresh;
import io.github.jinlongliao.easy.server.script.groovy.config.DynamicRefreshBeanConfiguration;
import io.github.jinlongliao.easy.server.swagger.config.ApiSpringAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        WebSocketServletAutoConfiguration.class,
        TaskExecutionAutoConfiguration.class,
        TaskSchedulingAutoConfiguration.class,
},
        proxyBeanMethods = false,
        scanBasePackages = {
                "io.github.jinlongliao.easy.server.boot.demo",
        })
@EnableMethodCache(basePackages ={"io.github.jinlongliao.easy.server.boot.demo","groovy"} )
@LogicContextScan("io.github.jinlongliao.easy.server.boot.demo")
@Configuration(proxyBeanMethods = false)
@EnableRefresh(scriptPaths = {"classpath:/groovy/*.groovy"}, refreshDelay = 1)
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Import(value = {
        WebConfig.class,
        BeanConfiguration.class,
        DynamicRefreshBeanConfiguration.class,
        ApiSpringAutoConfig.class
})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
