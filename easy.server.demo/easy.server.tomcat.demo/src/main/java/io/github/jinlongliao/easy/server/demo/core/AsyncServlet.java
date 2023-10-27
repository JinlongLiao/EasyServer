package io.github.jinlongliao.easy.server.demo.core;

import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author: liaojinlong
 * @date: 2022-06-28 16:28
 */
@Component
public class AsyncServlet extends BaseHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AsyncServlet.class);

    @Override
    protected void todoLogic(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean asyncSupported = req.isAsyncSupported();
        log.info("current Thread {}", Thread.currentThread().getId());
        if (asyncSupported) {
            AsyncContext asyncContext = req.startAsync(req, resp);
            asyncContext.setTimeout(1000);
            asyncContext.addListener(new AsyncListener() {
                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                    log.info("onComplete");
                }

                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    log.info("onTimeout");
                    ServletResponse response = event.getSuppliedResponse();
                    response.getWriter().println("{\"code\":500}");
                }

                @Override
                public void onError(AsyncEvent event) throws IOException {
                    log.info("onError");
                }

                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                    log.info("onStartAsync");
                }
            });
            asyncContext.start(() -> {
                HttpSession session = req.getSession();
                Assert.notNull(session, "Not Null");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                log.info("current Thread {}", Thread.currentThread().getId());
                try {
                    ServletResponse response = asyncContext.getResponse();
                    response.getWriter().println("{\"code\":200}");
                    asyncContext.complete();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    @Override
    public String[] supportPath() {
        return new String[]{"*.async"};
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
