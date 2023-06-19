package io.github.jinlongliao.easy.server.swagger.servlet.help;

import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 清理 api Cache
 *
 * @author: liaojinlong
 * @date: 2022-06-22 11:46
 */
public class CleanupApiCache extends BaseHttpServlet<Object> {
    private final ApiCleanHelper apiCleanHelper;
    private final ApiConfig apiConfig;

    public CleanupApiCache(ApiCleanHelper apiCleanHelper, ApiConfig apiConfig) {
        this.apiCleanHelper = apiCleanHelper;
        this.apiConfig = apiConfig;
    }

    @Override
    public String[] supportPath() {
        return new String[]{apiConfig.getBasePath() + "clean"};
    }

    @Override
    protected void todoLogic(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.apiCleanHelper.reset();
        this.seJsonMsgContext(resp);
        resp.getWriter().println(SUCCESS);
    }

    @Override
    public String getDescription() {
        return "清理 api Cache";
    }

}
