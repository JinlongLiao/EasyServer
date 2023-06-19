package io.github.jinlongliao.easy.server.demo.spring;


import io.github.jinlongliao.easy.server.servlet.config.ServletBeanConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;


/**
 * @author liaojinlong
 * @since 2020/9/22 13:09
 */
public class MyWebApplicationInitializer extends ServletBeanConfig implements WebApplicationInitializer {
    private static ConfigurableWebApplicationContext applicationContext = null;

    public static ConfigurableWebApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        applicationContext = createWebContext(BeanConfiguration.class);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
        applicationContext.setServletContext(servletContext);
        applicationContext.refresh();
        this.servletInit(applicationContext, servletContext);
    }


    /**
     * 自定义配置类来实例化一个Web Application Context
     *
     * @param annotatedClasses
     * @return
     */
    private AnnotationConfigWebApplicationContext createWebContext(Class<?>... annotatedClasses) {
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(annotatedClasses);
        return webContext;
    }
}
