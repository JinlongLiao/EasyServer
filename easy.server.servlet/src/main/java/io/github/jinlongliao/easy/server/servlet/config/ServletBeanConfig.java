package io.github.jinlongliao.easy.server.servlet.config;

import io.github.jinlongliao.easy.server.servlet.BaseHttpFilter;
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;
import org.springframework.context.ApplicationContext;


import jakarta.servlet.*;

import java.util.EnumSet;
import java.util.Map;

/**
 * 脱离 标准Servlet 注册，基于Spring 托管
 *
 * @author liaojinlong
 * @since 2021-11-24 14:40
 */
public abstract class ServletBeanConfig {


    /**
     * Configure the given {@link ServletContext} with any servlets, filters, listeners
     * context-params and attributes necessary for initialization.
     *
     * @param servletContext the {@code ServletContext} to initialize
     * @throws ServletException if any call against the given {@code ServletContext}
     *                          throws a {@code ServletException}
     */
    public void servletInit(ApplicationContext applicationContext, ServletContext servletContext) throws ServletException {
        this.initServlet(applicationContext, servletContext);
        this.initFilter(applicationContext, servletContext);
    }

    private void initFilter(ApplicationContext applicationContext, ServletContext servletContext) {
        final Map<String, BaseHttpFilter> beans = applicationContext.getBeansOfType(BaseHttpFilter.class);
        for (Map.Entry<String, BaseHttpFilter> filterEntry : beans.entrySet()) {
            final BaseHttpFilter httpFilter = filterEntry.getValue();
            final String filterName = filterEntry.getKey();
            final FilterRegistration.Dynamic dynamic = servletContext.addFilter("_inner_" + filterName, httpFilter);
            dynamic.setAsyncSupported(httpFilter.isAsync());
            dynamic.setInitParameters(httpFilter.initParam());
            dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, httpFilter.supportPath());

        }
    }

    private void initServlet(ApplicationContext applicationContext, ServletContext servletContext) {
        final Map<String, BaseHttpServlet> beans = applicationContext.getBeansOfType(BaseHttpServlet.class);
        for (Map.Entry<String, BaseHttpServlet> servletEntry : beans.entrySet()) {
            final BaseHttpServlet servlet = servletEntry.getValue();
            String servletName = servletEntry.getKey();
            final ServletRegistration.Dynamic dynamic = servletContext.addServlet("_inner_" + servletName, servlet);
            dynamic.setLoadOnStartup(1);
            dynamic.addMapping(servlet.supportPath());
            dynamic.setAsyncSupported(servlet.isAsync());
            dynamic.setInitParameters(servlet.initParam());
            // 非常用配置
            servlet.extraConfig(dynamic, servletContext);
        }
    }
}
