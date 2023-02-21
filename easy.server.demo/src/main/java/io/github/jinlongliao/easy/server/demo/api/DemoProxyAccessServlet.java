package io.github.jinlongliao.easy.server.demo.api;

import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.servlet.AbstractProxyAccessServlet;
import io.github.jinlongliao.easy.server.demo.logic.param.UserModel;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: liaojinlong
 * @date: 2022-06-20 14:52
 */
public class DemoProxyAccessServlet extends AbstractProxyAccessServlet<UserModel> {
    private final JsonHelper jsonHelper;
    private static final Logger log = LoggerFactory.getLogger(DemoProxyAccessServlet.class);

    public DemoProxyAccessServlet(ApiConfig apiConfig, JsonHelper jsonHelper) {
        super(apiConfig);
        this.jsonHelper = jsonHelper;
    }

    @Override
    protected void todoLogic(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.seJsonMsgContext(resp);
        String servlet = req.getParameter("servlet");
        if (servlet == null || servlet.isEmpty()) {
            req.getRequestDispatcher("/handlerMsg").forward(req, resp);
        } else {
            ApiConfig apiConfig = getApiConfig();
            String proxyAccessPath = apiConfig.getBasePath() + apiConfig.getProxyAccessPath();
            String replace = req.getRequestURI().replace(proxyAccessPath, "/");
            req.getRequestDispatcher(replace).forward(req, resp);
        }
    }

    @Override
    public String getDescription() {
        return "代理转发访问Servlet";
    }
}
