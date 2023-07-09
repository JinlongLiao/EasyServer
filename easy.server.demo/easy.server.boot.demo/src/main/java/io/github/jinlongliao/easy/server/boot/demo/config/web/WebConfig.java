package io.github.jinlongliao.easy.server.boot.demo.config.web;

import io.github.jinlongliao.easy.server.boot.demo.config.swagger.DemoProxyAccessServlet;
import io.github.jinlongliao.easy.server.cached.aop.simple.SimplePointcutAndHandler;
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
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
    public DemoProxyAccessServlet demoProxyAccessServlet(ApiConfig apiConfig, JsonHelper jsonHelper) {
        return new DemoProxyAccessServlet(apiConfig, jsonHelper);
    }


}
