package io.github.jinlongliao.easy.server.swagger.servlet;

import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.knife4j.auth.SecurityBasicAuthFilter;
import io.github.jinlongliao.easy.server.swagger.servlet.help.ApiMapping;
import io.github.jinlongliao.easy.server.servlet.BaseHttpFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author: liaojinlong
 * @date: 2022/6/19 19:43
 */
public class ApiHttpFilter extends BaseHttpFilter {
    private final ApiConfig apiConfig;
    private final ApiMapping apiMapping;
    private final SecurityBasicAuthFilter securityBasicAuthFilter;

    public ApiHttpFilter(ApiMapping apiMapping, SecurityBasicAuthFilter securityBasicAuthFilter) {
        this.apiConfig = apiMapping.getApiConfig();
        this.apiMapping = apiMapping;
        this.securityBasicAuthFilter = securityBasicAuthFilter;
    }


    @Override
    public String[] supportPath() {
        return new String[]{apiConfig.getBasePath() + "*"};
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean logicFilter = this.doLogicFilter((HttpServletRequest) request, (HttpServletResponse) response);
        if (logicFilter) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public boolean doLogicFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean needDispatcher = false;
        if (securityBasicAuthFilter.doFilter(request, response)) {
            if (apiMapping.supportRequest(request, response)) {
                needDispatcher = this.apiMapping.dispatcher(request, response);
            } else {
                needDispatcher = true;
            }
        }
        return needDispatcher;
    }
}
