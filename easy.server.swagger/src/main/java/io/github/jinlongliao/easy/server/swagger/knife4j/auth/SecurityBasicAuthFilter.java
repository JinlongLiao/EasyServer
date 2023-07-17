package io.github.jinlongliao.easy.server.swagger.knife4j.auth;

import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author: liaojinlong
 * @date: 2022/6/26 21:52
 */
public class SecurityBasicAuthFilter {
    private final ApiConfig apiConfig;

    public SecurityBasicAuthFilter(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }


    /***
     * basic auth验证
     */
    public static final String BASIC_AUTH_FILTER = "SecurityBasicAuthFilter";


    public boolean doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //针对swagger资源请求过滤
        if (apiConfig.isEnableBasicAuth()) {
            //判断Session中是否存在
            Object swaggerSessionValue = servletRequest.getSession().getAttribute(BASIC_AUTH_FILTER);
            if (swaggerSessionValue != null) {
                return true;
            } else {
                //匹配到,判断auth
                //获取请求头Authorization
                String auth = servletRequest.getHeader("Authorization");
                if (auth == null || "".equals(auth)) {
                    writeForbiddenCode(httpServletResponse);
                    return false;
                }
                String userAndPass = new String(Base64.getDecoder().decode(auth.substring(6)), StandardCharsets.UTF_8);
                String[] upArr = userAndPass.split(":");
                if (upArr.length != 2) {
                    writeForbiddenCode(httpServletResponse);
                } else {
                    String iptUser = upArr[0];
                    String iptPass = upArr[1];
                    //匹配服务端用户名及密码
                    String userName = apiConfig.getUserName();
                    if (iptUser.equals(userName) && iptPass.equals(apiConfig.getPassword())) {
                        servletRequest.getSession().setAttribute(BASIC_AUTH_FILTER, userName);
                        return true;
                    } else {
                        writeForbiddenCode(httpServletResponse);
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private void writeForbiddenCode(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(401);
        httpServletResponse.setHeader("WWW-Authenticate", "Basic realm=\"input Swagger Basic userName & password \"");
        httpServletResponse.getWriter().write("You do not have permission to access this resource");
    }

}
