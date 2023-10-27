package io.github.jinlongliao.easy.server.kotlin.demo

import io.github.jinlongliao.easy.server.cached.annotation.EnableMethodCache
import io.github.jinlongliao.easy.server.core.annotation.LogicContextScan
import io.github.jinlongliao.easy.server.kotlin.demo.config.web.BeanConfiguration
import io.github.jinlongliao.easy.server.kotlin.demo.config.web.WebConfig
import io.github.jinlongliao.easy.server.swagger.config.ApiSpringAutoConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [DataSourceAutoConfiguration::class,
        WebSocketServletAutoConfiguration::class,
        TaskExecutionAutoConfiguration::class,
        TaskSchedulingAutoConfiguration::class],
    proxyBeanMethods = false,
    scanBasePackages = ["io.github.jinlongliao.easy.server.kotlin.demo"]
)
@EnableMethodCache(basePackages = ["io.github.jinlongliao.easy.server.kotlin.demo"])
@LogicContextScan("io.github.jinlongliao.easy.server.kotlin.demo")
@Configuration(proxyBeanMethods = false)
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Import(value = [WebConfig::class, BeanConfiguration::class, ApiSpringAutoConfig::class])
class KotlinMainApplication


fun main(args: Array<String>) {
    runApplication<KotlinMainApplication>(*args)
}
