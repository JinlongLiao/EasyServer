package io.github.jinlongliao.easy.server.boot.demo.config.web;


import io.github.jinlongliao.easy.server.boot.demo.config.swagger.DemoDefaultApiGenerator;
import io.github.jinlongliao.easy.server.cached.annotation.process.WeAsyncHandler;
import io.github.jinlongliao.easy.server.cached.aop.CasIfAbsent;
import io.github.jinlongliao.easy.server.cached.aop.simple.handler.SimpleLimitPerAccessFilterHandler;
import io.github.jinlongliao.easy.server.mapper.spring.BeanMapperFactoryBean;
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.ExtraApiDocGenerator;
import io.github.jinlongliao.easy.server.swagger.model.ApiDocInfo;
import io.github.jinlongliao.easy.server.swagger.model.License;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.github.jinlongliao.easy.server.utils.json.extra.JackJsonJsonHelper;
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * bean 配置
 *
 * @author : liaojinlong
 * @since : 2020/11/10 16:55
 */

public class BeanConfiguration {
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最大线程数
        executor.setMaxPoolSize(8);
        //核心线程数
        executor.setCorePoolSize(2);
        //任务队列的大小
        executor.setQueueCapacity(500);
        //线程前缀名
        executor.setThreadNamePrefix("spring-async-");
        //线程存活时间
        executor.setKeepAliveSeconds(60);

        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        //线程初始化
        executor.initialize();
        return executor;
    }

    @Bean()
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return new ScheduledThreadPoolExecutor(1, threadPoolTaskExecutor);
    }

    @Bean()
    public WeAsyncHandler weAsyncHandler(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return new WeAsyncHandler(threadPoolTaskExecutor, 1024);
    }

    @Bean
    public SimpleLimitPerAccessFilterHandler simpleLimitPerAccessFilterHandler() {
        return new SimpleLimitPerAccessFilterHandler(new CasIfAbsent() {
            private final Map<String, Long> cache = new ConcurrentReferenceHashMap<>();

            @Override
            public boolean setKeyIfAbsentDuration(String key, Duration duration) {
                Long orDefault = cache.getOrDefault(key, 0L);
                long millis = System.currentTimeMillis();
                if (orDefault > millis) {
                    return false;
                }
                cache.put(key, millis + duration.toMillis());
                return true;
            }
        });
    }

    @Bean
    public JsonHelper jsonHelper() {
        JackJsonJsonHelper jackJsonJsonHelper = new JackJsonJsonHelper();
        return new JsonHelper(jackJsonJsonHelper);

    }

    @Bean
    public ApiConfig apiConfig() {
        ApiDocInfo apiDocInfo = new ApiDocInfo();
        String name = "SpringBoot APiDemo";
        apiDocInfo.setTitle(name);
        apiDocInfo.setVersion("v1");
        apiDocInfo.setTermsOfService("https://www.boke.com/");
        apiDocInfo.setDescription(name);
        Map<String, Object> contact = new HashMap<>(4, 1.5f);
        contact.put("name", "廖金龙");
        contact.put("email", "jinlongliao@foxmail.com");
        apiDocInfo.setContact(contact);
        apiDocInfo.setLicense(new License("Apache License", "https://www.apache.org/licenses/LICENSE-2.0.txt"));
        ApiConfig apiConfig = new ApiConfig("127.0.0.1",
                "/api/",
                "/api",
                "/api",
                new String[]{"http", "https"},
                apiDocInfo,
                null,
                "proxy");
        apiConfig.setEnableBasicAuth(true);

        apiConfig.setPassword("123");
        apiConfig.setUserName("admin");
        return apiConfig;
    }

    @Bean
    public BeanMapperFactoryBean beanMapperFactoryBean() {
        return new BeanMapperFactoryBean(false);
    }

    @Bean
    public DemoDefaultApiGenerator demoDefaultApiGenerator(LogicRegisterContext logicRegisterContext, ApiConfig apiConfig) {
        return new DemoDefaultApiGenerator(logicRegisterContext, apiConfig, Collections.<ExtraApiDocGenerator>emptyList());
    }

}
