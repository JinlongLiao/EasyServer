package io.github.jinlongliao.easy.server.netty.demo.config;


import io.github.jinlongliao.easy.server.mapper.spring.BeanMapperFactoryBean;
import io.github.jinlongliao.easy.server.netty.demo.config.swagger.tcp.TcpClient;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.NettyTcpServer;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.MsgReflectHelper;

import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.github.jinlongliao.easy.server.utils.json.extra.JackJsonJsonHelper;
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

    @Bean
    public JsonHelper jsonHelper() {
        JackJsonJsonHelper jackJsonJsonHelper = new JackJsonJsonHelper();
        return new JsonHelper(jackJsonJsonHelper);

    }

    @Bean
    public BeanMapperFactoryBean beanMapperFactoryBean() {
        return new BeanMapperFactoryBean(false);
    }

    @Bean
    public TcpConnectionFactory tcpConnectionFactory() {
        return new TcpConnectionFactory();
    }

    @Bean
    public MsgReflectHelper msgReflectHelper(TcpConnectionFactory tcpConnectionFactory, LogicRegisterContext logicRegisterContext) {
        return new MsgReflectHelper(tcpConnectionFactory, logicRegisterContext.getParse());
    }

    @Bean
    public NettyTcpServer nettyTcpServer(LogicRegisterContext logicRegisterContext, TcpConnectionFactory tcpConnectionFactory, MsgReflectHelper msgReflectHelper, JsonHelper jsonHelper) {
        return new NettyTcpServer(tcpConnectionFactory, logicRegisterContext, msgReflectHelper, jsonHelper);
    }

    @Bean
    public TcpClient tcpClient(LogicRegisterContext logicRegisterContext, TcpConnectionFactory tcpConnectionFactory, MsgReflectHelper msgReflectHelper, JsonHelper jsonHelper) {
        return new TcpClient(tcpConnectionFactory, logicRegisterContext, msgReflectHelper, jsonHelper);
    }


}
