package io.github.jinlongliao.easy.server;

import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: liaojinlong
 * @date: 2022-06-20 20:36
 */
public final class ServletInstantCache {
    private final static Map<String, BaseHttpServlet> javaxServletCache = new ConcurrentHashMap<>(32);

    public static void addJavaxServlet(BaseHttpServlet javaServlet) {
        String[] supportPath = javaServlet.supportPath();
        for (String path : supportPath) {
            ServletInstantCache.javaxServletCache.put(path, javaServlet);
        }
    }

    public static BaseHttpServlet getJavaxServlet(String path) {
        return ServletInstantCache.javaxServletCache.get(path);
    }
}
