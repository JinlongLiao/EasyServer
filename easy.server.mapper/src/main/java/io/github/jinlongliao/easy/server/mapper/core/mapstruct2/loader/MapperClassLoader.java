package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.loader;

import io.github.jinlongliao.easy.server.mapper.exception.ConverterNotFountException;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义classLoader
 *
 * @author: liaojinlong
 * @date: 2022/5/21 21:17
 */
public class MapperClassLoader extends ClassLoader {
    private static final ProtectionDomain PROTECTION_DOMAIN = AccessController.doPrivileged((PrivilegedAction<ProtectionDomain>) MapperClassLoader.class::getProtectionDomain);
    private final Map<String, Class<?>> loadClasses = new ConcurrentHashMap<>(32);


    static {
        ClassLoader.registerAsParallelCapable();
    }

    public MapperClassLoader() {
        super(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }

    /**
     * 使用父class Loader 加载
     *
     * @param classes
     * @param <T>
     * @return /
     */
    public <T> Class<T> reLoadClassBySupeClassloader(String className, byte[] classes) {

        Class<?> aClass;
        try {
            aClass = this.loadClass(className);
        } catch (ClassNotFoundException e) {
            aClass = this.defineClass(className, classes, 0, classes.length, PROTECTION_DOMAIN);
        }
        return (Class<T>) aClass;
    }

    public <T> T reLoadInstance(String name, byte[] classes, Object... args) {
        String className = name.replace('/', '.');
        try {
            Class<Object> objectClass = this.reLoadClassBySupeClassloader(className, classes);


            Constructor<?> constructor = findClass(args.length, objectClass);
            if (args.length == 0) {
                args = null;
            }
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> findClass(int length, Class<Object> objectClass) {
        Optional<Constructor<?>> first = Arrays.stream(objectClass.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterTypes().length == length)
                .findFirst();
        return first.orElseThrow(() -> new ConverterNotFountException("not found Constructor length: " + length + " classes： " + objectClass.getName()));
    }
}
