package io.github.jinlongliao.easy.server.swagger.servlet.help;

import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author: liaojinlong
 * @date: 2022-06-20 10:37
 */
public class ApiMapping {
    private final ApiConfig apiConfig;
    private final ApiResourceHelper apiResourceHelper;
    private final ResourcePatternResolver resourcePatternResolver;
    private final String basePath;
    private final String basePath2;

    public ApiMapping(ApiConfig apiConfig,
                      ApiResourceHelper apiResourceHelper,
                      ResourcePatternResolver resourcePatternResolver) {
        this.apiConfig = apiConfig;
        this.apiResourceHelper = apiResourceHelper;
        this.resourcePatternResolver = resourcePatternResolver;
        this.basePath = apiConfig.getBasePath();
        this.basePath2 = apiConfig.getBasePath().substring(0, this.basePath.length() - 1);
    }

    public ApiConfig getApiConfig() {
        return apiConfig;
    }

    /**
     * 是否支持访问
     *
     * @param request
     * @param response
     * @return /
     */
    public boolean supportRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        boolean needFilter = false;
        String requestURI = request.getServletPath();
        if (requestURI.startsWith(basePath) || requestURI.endsWith(basePath2)) {
            Pattern excludePath = apiConfig.getExcludePath();
            if (excludePath != null) {
                if (!excludePath.matcher(requestURI).matches()) {
                    needFilter = true;
                }
            } else {
                needFilter = true;
            }
        }
        return needFilter;
    }

    /**
     * 转发请求
     *
     * @param request
     * @param response
     * @return
     */
    public boolean dispatcher(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = request.getServletPath().toLowerCase();
        String resource = servletPath.replaceFirst(basePath2, "");
        String replacement = "/";
        resource = resource.replaceFirst("//", replacement);
        if (resource.isEmpty() || replacement.equals(resource)) {
            response.sendRedirect(request.getRequestURI() + "/doc.html");
            return false;
        }
        boolean dispatcher = false;
        if (servletPath.endsWith("doc.html")) {
            this.jsDispatcher(request, response);
        } else if (servletPath.endsWith("js")) {
            response.setHeader("content-type", "text/javascript");
            this.jsDispatcher(request, response);
        } else if (servletPath.endsWith("svg")
                || servletPath.endsWith("gif")) {
            this.imgDispatcher(request, response);
        } else if (servletPath.endsWith("css")) {
            response.setHeader("content-type", "text/css;charset=UTF-8");
            this.cssDispatcher(request, response);
        } else if (servletPath.endsWith("woff2") || servletPath.endsWith("tff") || servletPath.endsWith("woff") || servletPath.endsWith("eot")) {
            this.frontDispatcher(request, response);
        } else if (servletPath.endsWith("/ui")) {
            response.setHeader("content-type", "application/json");
            response.getWriter().println("{\"deepLinking\":true,\"displayOperationId\":true,\"defaultModelsExpandDepth\":1,\"defaultModelExpandDepth\":1,\"defaultModelRendering\":\"example\",\"displayRequestDuration\":false,\"docExpansion\":\"none\",\"filter\":false,\"operationsSorter\":\"alpha\",\"showExtensions\":false,\"tagsSorter\":\"alpha\",\"validatorUrl\":\"\",\"apisSorter\":\"alpha\",\"jsonEditor\":false,\"showRequestHeaders\":false,\"supportedSubmitMethods\":[\"get\",\"put\",\"post\",\"delete\",\"options\",\"head\",\"patch\",\"trace\"]}");
        } else if (servletPath.endsWith("/swagger-resources")) {
            response.setHeader("content-type", "application/json");
            String apiResource = this.apiResourceHelper.apiResource();
            response.getWriter().println(apiResource);
        } else if (servletPath.endsWith(apiConfig.getApiResourceUrl())) {
            response.setHeader("content-type", "application/json");
            String group = request.getParameter("group");
            response.getWriter().println(this.apiResourceHelper.aipDoc(group));
        } else {
            dispatcher = true;
        }
        return dispatcher;
    }

    protected void frontDispatcher(HttpServletRequest request, HttpServletResponse response) {
        this.staticFileDispatcher(request, response);
    }

    protected void cssDispatcher(HttpServletRequest request, HttpServletResponse response) {
        this.staticFileDispatcher(request, response);
    }

    protected void imgDispatcher(HttpServletRequest request, HttpServletResponse response) {
        this.staticFileDispatcher(request, response);
    }

    protected void jsDispatcher(HttpServletRequest request, HttpServletResponse response) {
        this.staticFileDispatcher(request, response);
    }

    protected void staticFileDispatcher(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getServletPath();
        requestURI = requestURI.replace(apiConfig.getBasePath(), "");

        requestURI = requestURI.replaceAll("/{2,}", "/");
        Resource resource = this.resourcePatternResolver.getResource("classpath:/io/github/jinlongliao/easy/server/swagger/resource/knife/" + requestURI);
          if (resource.exists()) {
            try (InputStream resourceAsStream = resource.getInputStream()) {
                this.copy(resourceAsStream, response.getOutputStream());
            } catch (Exception ignored) {
            }
        }
    }

    private void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buf = new byte[8192];
        int n;
        while ((n = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, n);
        }
    }

}
