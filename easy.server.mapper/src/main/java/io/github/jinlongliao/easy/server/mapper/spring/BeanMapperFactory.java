package io.github.jinlongliao.easy.server.mapper.spring;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.ClassMethodCoreGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.IData2Object2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.array.ArrayClassMethodCoreGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.array.IArrayData2Object2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.map.IMapData2Object2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.map.MapClassMethodCoreGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet.IServletData2Object;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet.ServletExtraClassMethodGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: liaojinlong
 * @date: 2022-06-16 17:56
 */
public class BeanMapperFactory implements InvocationHandler {
    /**
     * 实现类缓存
     */
    private final static Map<Class, IArrayData2Object2> DATA_2_ARRAY_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, IArrayData2Object2> FULL_DATA_2_ARRAY_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, IMapData2Object2> DATA_2_MAP_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, IMapData2Object2> FULL_DATA_2_MAP_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, IServletData2Object> DATA_2_OBJECT_WEB_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, IServletData2Object> FULL_DATA_2_OBJECT_WEB_CACHE = new ConcurrentHashMap<>(1 << 8);
    /**
     * <p>生成器</p>
     */
    public static final ServletExtraClassMethodGenerator SERVLET_EXTRA_CLASS_METHOD_GENERATOR = new ServletExtraClassMethodGenerator();
    public static final ArrayClassMethodCoreGenerator ARRAY_CLASS_METHOD_CORE_GENERATOR = new ArrayClassMethodCoreGenerator();
    public static final MapClassMethodCoreGenerator MAP_CLASS_METHOD_CORE_GENERATOR = new MapClassMethodCoreGenerator();
    /**
     * 构造器
     */
    private static final AsmProxyCodeGenerator CODE_GENERATOR = new AsmProxyCodeGenerator();

    private final boolean searchParent;

    public BeanMapperFactory(boolean searchParent) {
        this.searchParent = searchParent;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        Object result;
        switch (name) {
            case "servletBeanMapper":
                result = getResultByServlet(args);
                break;
            case "mapBeanMapper":
                result = getResultByMap(args);
                break;
            case "arrayBeanMapper":
                result = getResultByArray(args);
                break;
            default:
                result = method.invoke(proxy, args);
        }
        return result;
    }

    private Object getResultByArray(Object[] args) {
        Class tClass = (Class) args[0];
        Object[] params = (Object[]) args[1];
        return ((IArrayData2Object2) this.common(tClass,
                ARRAY_CLASS_METHOD_CORE_GENERATOR,
                searchParent ? FULL_DATA_2_ARRAY_CACHE : DATA_2_ARRAY_CACHE,
                searchParent))
                .toArrayConverter(params);
    }

    private Object getResultByMap(Object[] args) {
        Class tClass = (Class) args[0];
        Map<String, Object> params = (Map<String, Object>) args[1];
        return ((IMapData2Object2) this.common(tClass,
                MAP_CLASS_METHOD_CORE_GENERATOR,
                searchParent ? FULL_DATA_2_MAP_CACHE : DATA_2_MAP_CACHE,
                searchParent))
                .toMapConverter(params);
    }

    private Object getResultByServlet(Object[] args) {
        Class tClass = (Class) args[0];
        jakarta.servlet.http.HttpServletRequest params = (jakarta.servlet.http.HttpServletRequest) args[1];
        return ((IServletData2Object) this.common(tClass,
                SERVLET_EXTRA_CLASS_METHOD_GENERATOR,
                searchParent ? FULL_DATA_2_OBJECT_WEB_CACHE : DATA_2_OBJECT_WEB_CACHE,
                searchParent))
                .toHttpServletRequestConverter(params);
    }

    public <T> IData2Object2<T> common(Class<T> tClass,
                                       ClassMethodCoreGenerator classMethodCoreGenerator,
                                       Map cache,
                                       boolean searchParentField) {
        if (cache.containsKey(tClass)) {
            return (IData2Object2<T>) cache.get(tClass);
        }
        synchronized (tClass) {
            if (cache.containsKey(tClass)) {
                return (IData2Object2<T>) cache.get(tClass);
            } else {
                IData2Object2<T> proxyObject = CODE_GENERATOR.getProxyObject(
                        classMethodCoreGenerator,
                        tClass,
                        searchParentField,
                        AsmProxyCodeGenerator.JAVA_DEF_VERSION,
                        null);
                cache.put(tClass, proxyObject);
                return proxyObject;
            }
        }
    }


}
