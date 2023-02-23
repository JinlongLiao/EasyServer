package io.github.jinlongliao.easy.server.netty.demo.config;

import io.github.jinlongliao.easy.server.cached.aop.simple.SimplePointcutAndHandler;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author: liaojinlong
 * @date: 2022-10-12 11:12
 */
public class WebConfig {
    @Bean
    public SpringServletBeanConfig servletBeanConfig() {
        return new SpringServletBeanConfig();
    }

    @Bean
    public SimplePointcutAndHandler simplePointcutAndHandler(ListableBeanFactory listableBeanFactory) {
        return new SimplePointcutAndHandler(listableBeanFactory, new String[]{"io.github.jinlongliao.easy.server.demo.logic"});
    }
}
