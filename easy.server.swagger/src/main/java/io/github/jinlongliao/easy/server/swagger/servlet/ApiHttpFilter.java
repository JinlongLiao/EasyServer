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
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        if (securityBasicAuthFilter.doFilter(request, response)) {
            boolean needDispatcher = true;
            if (apiMapping.supportRequest(servletRequest, servletResponse)) {
                needDispatcher = this.apiMapping.dispatcher(servletRequest, servletResponse);
            }
            if (needDispatcher) {
                chain.doFilter(request, response);
            }
        }
    }
}
