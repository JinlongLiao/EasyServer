package io.github.jinlongliao.easy.server.mapper.core.mapstruct.loader;

import io.github.jinlongliao.easy.server.mapper.exception.ConverterNotFountException;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;

import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 自定义classLoader
 *
 * @author: liaojinlong
 * @date: 2022/5/21 21:17
 */
public class MapperClassLoader extends ClassLoader {
    private static final ProtectionDomain PROTECTION_DOMAIN = MethodHandles.lookup().lookupClass().getProtectionDomain();
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public MapperClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
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
            return newInstance(objectClass, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T newInstance(Class<?> objectClass, Object... args) {
        try {
            Constructor<?> constructor = findClass(args.length, objectClass);
            if (args.length == 0) {
                args = null;
            }
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T newInstanceWithEmpty(Class<?> objectClass) {
        Object defaultValue = CLassUtils.getDefaultValue(objectClass);
        if (Objects.isNull(defaultValue)) {
            Optional<Constructor<?>> first = Arrays.stream(objectClass.getDeclaredConstructors())
                    .findFirst();
            if (first.isEmpty()) {
                return null;
            }
            Constructor<?> constructor = first.get();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            try {
                if (parameterTypes.length == 0) {
                    defaultValue = constructor.newInstance();
                } else {
                    Object[] array = Arrays.stream(parameterTypes).map(CLassUtils::getDefaultValue).toList().toArray(new Object[0]);
                    defaultValue = constructor.newInstance(array);
                }
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }

        return (T) defaultValue;


    }

    private Constructor<?> findClass(int length, Class<?> objectClass) {
        Optional<Constructor<?>> first = Arrays.stream(objectClass.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterTypes().length == length)
                .findFirst();
        return first.orElseThrow(() -> new ConverterNotFountException("not found Constructor length: " + length + " classes： " + objectClass.getName()));
    }
}
