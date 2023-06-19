package io.github.jinlongliao.easy.server.servlet;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * 基础的Filter
 *
 * @author liaojinlong
 * @since 2021-12-26 12:48
 */
public abstract class BaseHttpFilter implements Filter, FilterConfig {

    /**
     * The filter configuration.
     */
    private volatile FilterConfig filterConfig;


    @Override
    public String getInitParameter(String name) {
        return getFilterConfig().getInitParameter(name);
    }


    @Override
    public Enumeration<String> getInitParameterNames() {
        return getFilterConfig().getInitParameterNames();
    }


    /**
     * Obtain the FilterConfig used to initialise this Filter instance.
     *
     * @return The config previously passed to the {@link #init(FilterConfig)}
     * method
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }


    @Override
    public ServletContext getServletContext() {
        return getFilterConfig().getServletContext();
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        init();
    }

    @Override
    public void destroy() {

    }

    /**
     * Convenience method for sub-classes to save them having to call
     * <code>super.init(config)</code>. This is a NO-OP by default.
     *
     * @throws ServletException If an exception occurs that interrupts the
     *                          Filter's normal operation
     */
    public void init() throws ServletException {
        // NO-OP
    }

    @Override
    public String getFilterName() {
        return getFilterConfig().getFilterName();
    }

    /**
     * 支持的URL
     *
     * @return /
     */
    public abstract String[] supportPath();


    /**
     * 初始化参数
     *
     * @return /
     */
    public Map<String, String> initParam() {
        return Collections.emptyMap();
    }

    /**
     * 是否支持异步
     *
     * @return
     */
    public boolean isAsync() {
        return false;
    }


}
