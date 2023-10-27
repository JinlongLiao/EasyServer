package io.github.jinlongliao.easy.server.kotlin.demo.config.web

import io.github.jinlongliao.easy.server.kotlin.demo.config.swagger.DemoProxyAccessServlet
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig
import io.github.jinlongliao.easy.server.utils.json.JsonHelper
import org.springframework.context.annotation.Bean

/**
 * @author: liaojinlong
 * @date: 2022-10-12 11:12
 */
class WebConfig {
    @Bean
    fun servletBeanConfig(): SpringServletBeanConfig {
        return SpringServletBeanConfig()
    }

    @Bean
    fun demoProxyAccessServlet(apiConfig: ApiConfig?, jsonHelper: JsonHelper): DemoProxyAccessServlet {
        return DemoProxyAccessServlet(apiConfig, jsonHelper)
    }
}
