package io.github.jinlongliao.easy.server.kotlin.demo.config.web

import io.github.jinlongliao.easy.server.servlet.config.ServletBeanConfig
import jakarta.servlet.ServletContext
import jakarta.servlet.ServletException
import org.springframework.beans.BeansException
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

/**
 * 脱离 标准Servlet 注册，基于Spring 托管
 *
 * @author liaojinlong
 * @since 2021-11-24 14:40
 */
class SpringServletBeanConfig : ServletBeanConfig(), ServletContextInitializer, ApplicationContextAware {
    private var applicationContext: ApplicationContext? = null
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    /**
     * Configure the given [ServletContext] with any servlets, filters, listeners
     * context-params and attributes necessary for initialization.
     *
     * @param servletContext the `ServletContext` to initialize
     * @throws ServletException if any call against the given `ServletContext`
     * throws a `ServletException`
     */
    @Throws(ServletException::class)
    override fun onStartup(servletContext: ServletContext) {
        servletInit(applicationContext, servletContext)
    }
}
