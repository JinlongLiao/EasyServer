package io.github.jinlongliao.easy.server.boot.demo.config.web;

import io.github.jinlongliao.easy.server.servlet.config.ServletBeanConfig;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * 脱离 标准Servlet 注册，基于Spring 托管
 *
 * @author liaojinlong
 * @since 2021-11-24 14:40
 */
public class SpringServletBeanConfig extends ServletBeanConfig implements ServletContextInitializer, ApplicationContextAware {
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * Configure the given {@link ServletContext} with any servlets, filters, listeners
     * context-params and attributes necessary for initialization.
     *
     * @param servletContext the {@code ServletContext} to initialize
     * @throws ServletException if any call against the given {@code ServletContext}
     *                          throws a {@code ServletException}
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.servletInit(applicationContext, servletContext);
    }

}
