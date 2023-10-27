package io.github.jinlongliao.easy.server.kotlin.demo.config.web

import io.github.jinlongliao.easy.server.cached.annotation.process.WeAsyncHandler
import io.github.jinlongliao.easy.server.cached.aop.CasIfAbsent
import io.github.jinlongliao.easy.server.cached.aop.simple.handler.SimpleLimitPerAccessFilterHandler
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext
import io.github.jinlongliao.easy.server.kotlin.demo.config.swagger.DemoDefaultApiGenerator
import io.github.jinlongliao.easy.server.mapper.spring.BeanMapperFactoryBean
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig
import io.github.jinlongliao.easy.server.swagger.model.ApiDocInfo
import io.github.jinlongliao.easy.server.swagger.model.License
import io.github.jinlongliao.easy.server.utils.json.JsonHelper
import io.github.jinlongliao.easy.server.utils.json.extra.JackJsonJsonHelper
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.util.ConcurrentReferenceHashMap
import java.time.Duration
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor

/**
 * bean 配置
 *
 * @author : liaojinlong
 * @since : 2020/11/10 16:55
 */
class BeanConfiguration {
    @Bean
    fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        //最大线程数
        executor.maxPoolSize = 8
        //核心线程数
        executor.corePoolSize = 2
        //任务队列的大小
        executor.queueCapacity = 500
        //线程前缀名
        executor.setThreadNamePrefix("spring-async-")
        //线程存活时间
        executor.keepAliveSeconds = 60
        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.DiscardPolicy())
        //线程初始化
        executor.initialize()
        return executor
    }

    @Bean
    fun scheduledThreadPoolExecutor(threadPoolTaskExecutor: ThreadPoolTaskExecutor?): ScheduledThreadPoolExecutor {
        return ScheduledThreadPoolExecutor(1, threadPoolTaskExecutor)
    }

    @Bean
    fun weAsyncHandler(threadPoolTaskExecutor: ThreadPoolTaskExecutor?): WeAsyncHandler {
        return WeAsyncHandler(threadPoolTaskExecutor, 1024)
    }

    @Bean
    fun simpleLimitPerAccessFilterHandler(): SimpleLimitPerAccessFilterHandler {
        return SimpleLimitPerAccessFilterHandler(object : CasIfAbsent {
            private val cache: MutableMap<String, Long> = ConcurrentReferenceHashMap()
            override fun setKeyIfAbsentDuration(key: String, duration: Duration): Boolean {
                val orDefault = cache.getOrDefault(key, 0L)
                val millis = System.currentTimeMillis()
                if (orDefault > millis) {
                    return false
                }
                cache[key] = millis + duration.toMillis()
                return true
            }
        })
    }

    @Bean
    fun jsonHelper(): JsonHelper {
        val jackJsonJsonHelper = JackJsonJsonHelper()
        return JsonHelper(jackJsonJsonHelper)
    }

    @Bean
    fun apiConfig(): ApiConfig {
        val apiDocInfo = ApiDocInfo()
        val name = "SpringBoot APiDemo"
        apiDocInfo.title = name
        apiDocInfo.version = "v1"
        apiDocInfo.termsOfService = "https://www.boke.com/"
        apiDocInfo.description = name
        val contact: MutableMap<String, Any> = HashMap(4, 1.5f)
        contact["name"] = "廖金龙"
        contact["email"] = "jinlongliao@foxmail.com"
        apiDocInfo.contact = contact
        apiDocInfo.license = License(
            "Apache License",
            "https://www.apache.org/licenses/LICENSE-2.0.txt"
        )
        val apiConfig = ApiConfig(
            "127.0.0.1",
            "/api/",
            "/api",
            "/api", arrayOf("http", "https"),
            apiDocInfo,
            null,
            "proxy"
        )
        apiConfig.isEnableBasicAuth = true
        apiConfig.password = "123"
        apiConfig.userName = "admin"
        return apiConfig
    }

    @Bean
    fun beanMapperFactoryBean(): BeanMapperFactoryBean {
        return BeanMapperFactoryBean(false)
    }

    @Bean
    fun demoDefaultApiGenerator(
        logicRegisterContext: LogicRegisterContext?,
        apiConfig: ApiConfig?
    ): DemoDefaultApiGenerator {
        return DemoDefaultApiGenerator(logicRegisterContext, apiConfig, emptyList())
    }
}
