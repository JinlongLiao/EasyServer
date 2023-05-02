package io.github.jinlongliao.easy.server.boot.demo.core;

import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;
import io.github.jinlongliao.easy.server.utils.common.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.UUID;

/**
 * SSE Test
 *
 * @author: liaojinlong
 * @date: 2023/5/2 14:22
 */
@Component
public class SseServlet extends BaseHttpServlet<Object> {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String[] supportPath() {
        return new String[]{"/sse"};
    }

    @Override
    protected void todoLogic(HttpServletRequest req, HttpServletResponse response) throws IOException {

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        for (int i = 0; i < 20; i++) {
            if ((i & 1) == 1) {
                writer.write("event: diyEventType\n");
            }
            String id = "id: " + UUID.randomUUID().toString() + "\n";
            String retry = "retry: 100000\n";
            String data = "data: " + DateUtil.getStringDateTime() + "\n\n";
            writer.write(id);
            writer.write(retry);
            writer.write(data);
            writer.write(id);
            writer.write(retry);
            writer.write(data);
            writer.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        writer.close();
    }
}
