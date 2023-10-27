package io.github.jinlongliao.easy.server.servlet;

import io.github.jinlongliao.easy.server.mapper.spring.BeanMapperFactoryBean;
import io.github.jinlongliao.easy.server.mapper.spring.IBeanMapper;
import io.github.jinlongliao.easy.server.ServletInstantCache;
import io.github.jinlongliao.easy.server.servlet.match.UrlMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础Servlet
 *
 * @author liaojinlong
 * @since 2021-11-24 14:50
 */
public abstract class BaseHttpServlet<T> extends HttpServlet implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(BaseHttpServlet.class);
    public static final String SUCCESS = "{\"result:\":200}";
    public static final String NOT_PASS = "{\"result:\":210}";
    public static final String NOT_SUPPORT = "{\"result:\":300}";
    public static final String RESULT_KEY = "result";

    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_TRACE = "TRACE";
    protected final Class<T> genericClass;
    protected final IBeanMapper beanMapper;
    protected Set<String> supportMethod;
    protected Set<UrlMatcher> supportPath;

    public BaseHttpServlet() {
        this(new BeanMapperFactoryBean(false).getObject());
    }

    public BaseHttpServlet(IBeanMapper beanMapper) {
        this.beanMapper = beanMapper;
        genericClass = getGenericClass0();
        supportMethod = new HashSet<>(8);
        supportMethod.add(METHOD_DELETE);
        supportMethod.add(METHOD_TRACE);
        supportMethod.add(METHOD_HEAD);
        supportMethod.add(METHOD_PUT);
        supportMethod.add(METHOD_GET);
        supportMethod.add(METHOD_POST);
        supportMethod.add(METHOD_OPTIONS);
    }

    /**
     * Receives standard HTTP requests from the public
     * <code>service</code> method and dispatches
     * them to the <code>do</code><i>XXX</i> methods defined in
     * this class. This method is an HTTP-specific version of the
     * {@link Servlet#service} method. There's no
     * need to override this method.
     *
     * @param req  the {@link HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @throws IOException      if an input or output error occurs
     *                          while the servlet is handling the
     *                          HTTP request
     * @throws ServletException if the HTTP request
     *                          cannot be handled
     * @see Servlet#service
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        if (!"/".equals(contextPath)) {
            uri = uri.replace(contextPath, "");
        }
        final String method = req.getMethod();
        if (supportMethod().contains(method)) {
            String finalUri = uri;
            if (getSupportPath().stream().anyMatch(n -> !n.matcher(finalUri))) {
                this.todoLogic(req, resp);
            } else {
                this.notSupportMethodResponse(resp, 404);
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Not Support = {}", method);
            }
            this.notSupportMethodResponse(resp, 502);
        }
    }

    protected void notSupportMethodResponse(HttpServletResponse resp, int code) throws IOException {
        resp.setStatus(code);
        resp.getWriter().write(NOT_SUPPORT);
    }

    /**
     * 业务处理
     *
     * @param req
     * @param resp
     */
    protected void todoLogic(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write(SUCCESS);
    }

    /**
     * 支持的URL
     *
     * @return /
     */
    public abstract String[] supportPath();

    public Set<UrlMatcher> getSupportPath() {
        if (Objects.isNull(supportPath)) {
            supportPath = Arrays.stream(supportPath()).map(UrlMatcher::new).collect(Collectors.toSet());
        }
        return supportPath;
    }

    /**
     * 支持的请求方法
     *
     * @return /
     */
    public Set<String> supportMethod() {
        return supportMethod;
    }

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

    /**
     * Servlet 初始化其他配置
     * 普通Servlet 不建议配置
     *
     * @param dynamic
     * @param servletContext
     */
    public void extraConfig(final ServletRegistration.Dynamic dynamic, ServletContext servletContext) {

    }

    protected void seJsonMsgContext(HttpServletResponse response) {
        this.setMsgContext("application/json", response);
    }

    protected void setMsgContext(String msgContext, HttpServletResponse response) {
        response.setContentType(msgContext);
    }

    /**
     * 输出返回值
     *
     * @param response
     * @param msg
     */
    public void flushResponse(HttpServletResponse response, Object msg) throws IOException {
        this.flushResponse(response, msg, "text/xml;charset=utf-8");
    }

    /**
     * 输出返回值
     *
     * @param response
     * @param response
     * @param contentType
     */
    public void flushResponse(HttpServletResponse response, Object msg, String contentType) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(contentType);
        final PrintWriter out = response.getWriter();
        out.print(msg);
        out.flush();
        out.close();
    }

    public T getRequestBody(HttpServletRequest request) {
        Class<T> genericClass = this.getGenericClass();
        if (genericClass != null) {
            return beanMapper.servletBeanMapper(genericClass, this.handleRequest(request));
        }
        return null;
    }

    protected HttpServletRequest handleRequest(HttpServletRequest request) {
        return request;
    }

    /**
     * 返回此类的泛型
     *
     * @return /
     */
    public Class<T> getGenericClass() {
        return genericClass;
    }

    private Class<T> getGenericClass0() {
        Class<? extends BaseHttpServlet> aClass = this.getClass();
        Type genericSuperclass = aClass.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * @return 获取描述信息
     */
    public String getDescription() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServletInstantCache.addJavaxServlet(this);
    }
}
