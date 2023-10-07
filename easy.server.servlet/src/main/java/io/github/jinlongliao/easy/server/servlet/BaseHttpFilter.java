package io.github.jinlongliao.easy.server.servlet;


import io.github.jinlongliao.easy.server.servlet.match.UrlMatcher;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 基础的Filter
 *
 * @author liaojinlong
 * @since 2021-12-26 12:48
 */
public abstract class BaseHttpFilter implements Filter, FilterConfig {
    protected Set<UrlMatcher> supportPath;

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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest req)) {
            chain.doFilter(request, response);
            return;
        }
        String contextPath = req.getContextPath();
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (!"/".equals(contextPath)) {
            uri = uri.replace(contextPath, "");
        }
        boolean cont = false;
        String finalUri = uri;
        if (getSupportPath().stream().filter(n -> !n.matcher(finalUri)).findAny().isEmpty()) {
            cont = this.doLogicFilter(req, res);
        }
        if (cont) {
            chain.doFilter(req, res);
        }
    }


    /**
     * @param req      /
     * @param response /
     * @return 是否进行下一个
     * @throws IOException      /
     * @throws ServletException /
     */
    public abstract boolean doLogicFilter(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException;

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

    public Set<UrlMatcher> getSupportPath() {
        if (Objects.isNull(supportPath)) {
            supportPath = Arrays.stream(supportPath()).map(UrlMatcher::new).collect(Collectors.toUnmodifiableSet());
        }
        return supportPath;
    }

}
