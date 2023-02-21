package io.github.jinlongliao.easy.server.swagger.servlet;

import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;

import java.util.Set;

/**
 * 代理访问类
 *
 * @author: liaojinlong
 * @date: 2022-06-20 14:47
 */
public abstract class AbstractProxyAccessServlet<T> extends BaseHttpServlet<T> {
    private final ApiConfig apiConfig;

    protected AbstractProxyAccessServlet(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public ApiConfig getApiConfig() {
        return apiConfig;
    }

    @Override
    public String[] supportPath() {
        String proxyAccessPath = apiConfig.getBasePath() + apiConfig.getProxyAccessPath();
        return new String[]{proxyAccessPath, proxyAccessPath + "/*"};
    }

    @Override
    public Set<String> supportMethod() {
        return this.supportMethod;
    }

}
