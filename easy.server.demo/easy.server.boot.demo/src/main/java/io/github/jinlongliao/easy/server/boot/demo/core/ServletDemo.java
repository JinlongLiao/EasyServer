package io.github.jinlongliao.easy.server.boot.demo.core;

import io.github.jinlongliao.easy.server.boot.demo.logic.param.UserModel;
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: liaojinlong
 * @date: 2022/5/28 19:46
 */
@Component
public class ServletDemo extends BaseHttpServlet<UserModel> {
    @Override
    public String[] supportPath() {
        return new String[]{"/test"};
    }

    @Override
    protected void todoLogic(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel requestBody = getRequestBody(req);
        resp.getWriter().write(requestBody.toString());
    }

    @Override
    protected HttpServletRequest handleRequest(HttpServletRequest request) {
        if (StringUtils.startsWithIgnoreCase(request.getContentType(), ("multipart/form-data"))) {
            return new StandardMultipartHttpServletRequest(request);
        }
        return request;
    }

    @Override
    public void extraConfig(ServletRegistration.Dynamic dynamic, ServletContext servletContext) {
        super.extraConfig(dynamic, servletContext);
        dynamic.setMultipartConfig(new MultipartConfigElement(""));
    }

    @Override
    public String getDescription() {
        return "测试Servlet";
    }
}
