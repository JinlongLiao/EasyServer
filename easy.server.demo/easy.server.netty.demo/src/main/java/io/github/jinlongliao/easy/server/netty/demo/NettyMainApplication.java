package io.github.jinlongliao.easy.server.netty.demo;

import io.github.jinlongliao.easy.server.netty.demo.config.swagger.config.DeveloperConfig;
import io.github.jinlongliao.easy.server.netty.demo.config.BeanConfiguration;
import io.github.jinlongliao.easy.server.netty.demo.config.WebConfig;
import io.github.jinlongliao.easy.server.cached.annotation.EnableMethodCache;
import io.github.jinlongliao.easy.server.core.annotation.LogicContextScan;
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
                "io.github.jinlongliao.easy.server.netty.demo",
        })
@LogicContextScan("io.github.jinlongliao.easy.server.netty.demo")
@Configuration(proxyBeanMethods = false)
@EnableMethodCache(basePackages = "io.github.jinlongliao.easy.netty.demo")
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Import(value = {
        WebConfig.class,
        DeveloperConfig.class,
        BeanConfiguration.class,
        ApiSpringAutoConfig.class
})
public class NettyMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyMainApplication.class, args);
    }

}
