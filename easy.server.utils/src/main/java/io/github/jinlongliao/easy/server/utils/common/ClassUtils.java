package io.github.jinlongliao.easy.server.utils.common;


import java.lang.invoke.MethodHandles;

/**
 * @author: liaojinlong
 * @date: 2022-06-17 11:22
 */
public final class ClassUtils {
    private static final ClassLoader LOADER = MethodHandles.lookup().lookupClass().getClassLoader();

    public static boolean existClass(String className) {
        try {
            Class.forName(className, true, LOADER);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
