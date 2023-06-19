package io.github.jinlongliao.easy.server.mapper.core.mapstruct;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.wrap.ICoreData2Object2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet.IServletData2Object;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet.ServletExtraClassMethodGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: liaojinlong
 * @date: 2022/5/22 20:58
 */

public class BeanCopierUtils {
    /**
     * 实现类缓存
     */
    private final static Map<Class, ICoreData2Object2> DATA_2_OBJECT_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, ICoreData2Object2> FULL_DATA_2_OBJECT_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, IServletData2Object> DATA_2_OBJECT_WEB_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, IServletData2Object> FULL_DATA_2_OBJECT_WEB_CACHE = new ConcurrentHashMap<>(1 << 8);

    private static final AsmProxyCodeGenerator GENERATOR = new AsmProxyCodeGenerator();
    private static final ServletExtraClassMethodGenerator CLASS_METHOD_GENERATOR = new ServletExtraClassMethodGenerator();

    /**
     * 获取数据转换类
     *
     * @param tClass
     * @return IData2Object
     */
    public static <T> ICoreData2Object2<T> getData2Object(Class<T> tClass) {
        if (DATA_2_OBJECT_CACHE.containsKey(tClass)) {
            return DATA_2_OBJECT_CACHE.get(tClass);
        }
        synchronized (tClass) {
            if (DATA_2_OBJECT_CACHE.containsKey(tClass)) {
                return DATA_2_OBJECT_CACHE.get(tClass);
            } else {

                ICoreData2Object2<T> proxyObject = GENERATOR.getProxyObject(tClass);
                DATA_2_OBJECT_CACHE.put(tClass, proxyObject);
                return proxyObject;
            }
        }
    }

    /**
     * 获取数据转换类,包涵父类属性
     *
     * @param tClass
     * @return IData2Object
     */
    public static <T> ICoreData2Object2 getFullData2Object(Class<T> tClass) {
        if (FULL_DATA_2_OBJECT_CACHE.containsKey(tClass)) {
            return FULL_DATA_2_OBJECT_CACHE.get(tClass);
        }
        synchronized (tClass) {
            if (FULL_DATA_2_OBJECT_CACHE.containsKey(tClass)) {
                return FULL_DATA_2_OBJECT_CACHE.get(tClass);
            } else {
                ICoreData2Object2<T> proxyObject = GENERATOR.getProxyObject(tClass, true);
                FULL_DATA_2_OBJECT_CACHE.put(tClass, proxyObject);
                return proxyObject;
            }
        }
    }

    /**
     * 获取数据转换类
     *
     * @param tClass
     * @return IData2Object
     */
    public static <T> IServletData2Object<T> getData2WebObject(Class<T> tClass) {
        if (DATA_2_OBJECT_WEB_CACHE.containsKey(tClass)) {
            return DATA_2_OBJECT_WEB_CACHE.get(tClass);
        }
        synchronized (tClass) {
            if (DATA_2_OBJECT_WEB_CACHE.containsKey(tClass)) {
                return DATA_2_OBJECT_WEB_CACHE.get(tClass);
            } else {
                IServletData2Object<T> proxyObject = GENERATOR.getProxyObject(
                        CLASS_METHOD_GENERATOR,
                        tClass,
                        false,
                        AsmProxyCodeGenerator.JAVA_DEF_VERSION,
                        null);
                DATA_2_OBJECT_WEB_CACHE.put(tClass, proxyObject);
                return proxyObject;
            }
        }
    }

    /**
     * 获取数据转换类,包涵父类属性
     *
     * @param tClass
     * @return IData2Object
     */
    public static <T> IServletData2Object getFullData2WebObject(Class<T> tClass) {
        if (FULL_DATA_2_OBJECT_WEB_CACHE.containsKey(tClass)) {
            return FULL_DATA_2_OBJECT_WEB_CACHE.get(tClass);
        }
        synchronized (tClass) {
            if (FULL_DATA_2_OBJECT_WEB_CACHE.containsKey(tClass)) {
                return FULL_DATA_2_OBJECT_WEB_CACHE.get(tClass);
            } else {
                IServletData2Object<T> proxyObject = GENERATOR.getProxyObject(
                        CLASS_METHOD_GENERATOR,
                        tClass,
                        true,
                        AsmProxyCodeGenerator.JAVA_DEF_VERSION,
                        null);
                FULL_DATA_2_OBJECT_WEB_CACHE.put(tClass, proxyObject);
                return proxyObject;
            }
        }
    }
}
