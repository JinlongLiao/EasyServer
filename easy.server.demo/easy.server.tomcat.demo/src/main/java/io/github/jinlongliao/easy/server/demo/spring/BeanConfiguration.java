package io.github.jinlongliao.easy.server.demo.spring;


import io.github.jinlongliao.easy.server.mapper.spring.BeanMapperFactoryBean;
import io.github.jinlongliao.easy.server.cached.annotation.EnableMethodCache;
import io.github.jinlongliao.easy.server.cached.aop.simple.SimplePointcutAndHandler;
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.config.ApiSpringAutoConfig;
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.ExtraApiDocGenerator;
import io.github.jinlongliao.easy.server.swagger.model.ApiDocInfo;
import io.github.jinlongliao.easy.server.swagger.model.License;
import io.github.jinlongliao.easy.server.demo.api.DemoDefaultApiGenerator;
import io.github.jinlongliao.easy.server.demo.api.DemoProxyAccessServlet;
import io.github.jinlongliao.easy.server.script.groovy.annotation.EnableRefresh;
import io.github.jinlongliao.easy.server.script.groovy.config.DynamicRefreshBeanConfiguration;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.github.jinlongliao.easy.server.utils.json.extra.JackJsonJsonHelper;
import io.github.jinlongliao.easy.server.core.annotation.LogicContextScan;
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
@EnableScheduling
@Configuration(proxyBeanMethods = false)
@ComponentScan("io.github.jinlongliao.easy.server.demo")
@Import({DynamicRefreshBeanConfiguration.class, ApiSpringAutoConfig.class})
@LogicContextScan("io.github.jinlongliao.easy.server.demo.logic")
@EnableAspectJAutoProxy
@EnableMethodCache(basePackages = "io.github.jinlongliao.easy.server.demo")
@EnableRefresh(scriptPaths = {"classpath:/groovy/*.groovy"}, refreshDelay = 1)
@EnableAsync
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

    @Bean
    public JsonHelper jsonHelper() {
        JackJsonJsonHelper jackJsonJsonHelper = new JackJsonJsonHelper();
        return new JsonHelper(jackJsonJsonHelper);

    }

    @Bean
    public ApiConfig apiConfig() {
        ApiDocInfo apiDocInfo = new ApiDocInfo();
        apiDocInfo.setTitle("ApiDemo");
        apiDocInfo.setVersion("v1");
        apiDocInfo.setTermsOfService("Easy Server");
        apiDocInfo.setDescription("测试展示");
        Map<String, Object> contact = new HashMap<>(4, 1.5f);
        contact.put("name", "jinlongliao@foxmail.com");
        apiDocInfo.setContact(contact);
        apiDocInfo.setLicense(new License("Apache License", "https://www.apache.org/licenses/LICENSE-2.0.txt"));
        ApiConfig config = new ApiConfig("127.0.0.1",
                "/api",
                "/api",
                "/api",
                new String[]{"http"},
                apiDocInfo,
                null,
                "proxy");
        config.setEnableBasicAuth(true);
        config.setPassword("123");
        config.setUserName("admin");
        return config;
    }

    @Bean
    public BeanMapperFactoryBean beanMapperFactoryBean() {
        return new BeanMapperFactoryBean(false);
    }

    @Bean
    public DemoDefaultApiGenerator demoDefaultApiGenerator(LogicRegisterContext logicRegisterContext, ApiConfig apiConfig) {
        return new DemoDefaultApiGenerator(logicRegisterContext, apiConfig, Collections.<ExtraApiDocGenerator>emptyList());
    }

    @Bean
    public DemoProxyAccessServlet demoProxyAccessServlet(ApiConfig apiConfig, JsonHelper jsonHelper) {
        return new DemoProxyAccessServlet(apiConfig, jsonHelper);
    }

    @Bean
    public SimplePointcutAndHandler simplePointcutAndHandler(ListableBeanFactory listableBeanFactory) {
        return new SimplePointcutAndHandler(listableBeanFactory, new String[]{"io.github.jinlongliao.easy.server.demo.logic"});
    }
}
