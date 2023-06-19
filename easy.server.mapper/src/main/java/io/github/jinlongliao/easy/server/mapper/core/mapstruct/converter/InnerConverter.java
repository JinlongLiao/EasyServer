package io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter;

import io.github.jinlongliao.easy.server.mapper.annotation.Ignore;
import io.github.jinlongliao.easy.server.mapper.exception.ConverterNotFountException;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内部转换
 *
 * @author: liaojinlong
 * @date: 2022/5/21 19:12
 */
public class InnerConverter {
    private static final IDataConverter CONVERTER;

    /**
     * 数据值转换 函数名
     */
    private static final Map<Type, Method> VALUE_CONVERTER_CACHE;

    static {
        VALUE_CONVERTER_CACHE = new ConcurrentHashMap<>(1 << 8);
        final Class<InnerConverter> innerCoreDataConverterClass = InnerConverter.class;
        final Method[] methods = innerCoreDataConverterClass.getDeclaredMethods();
        for (Method method : methods) {
            Ignore annotation = method.getAnnotation(Ignore.class);
            if (annotation != null && annotation.value()) {
                continue;
            }
            final Class<?> type = method.getReturnType();
            VALUE_CONVERTER_CACHE.put(type, method);
        }
        List<IDataConverter> dataConverters = new ArrayList<>();
        CONVERTER = AccessController.doPrivileged((PrivilegedAction<IDataConverter>) () -> {
            Iterator<IDataConverter> iterator = ServiceLoader.load(IDataConverter.class).iterator();
            while (iterator.hasNext()) {
                dataConverters.add(iterator.next());
            }
            Collections.sort(dataConverters);
            return dataConverters.stream().findFirst().orElse(IDataConverter.getDefault());
        });
    }

    @Ignore
    public static void addGlobalConverter(Type type, Method method) {
        VALUE_CONVERTER_CACHE.put(type, method);
    }

    @Ignore
    public static Method getGlobalConverter(Type type) {
        return VALUE_CONVERTER_CACHE.computeIfAbsent(type, (k) -> {
            throw new ConverterNotFountException("not found type converter:" + type.getTypeName());
        });
    }

    @Ignore
    public static boolean containDateConverter(Type type) {
        return VALUE_CONVERTER_CACHE.containsKey(type);
    }

    /**
     * 数据转换为byte
     *
     * @param data
     * @return byte
     */
    public static byte getByte(Object data) {
        return CONVERTER.getByte(data);
    }

    /**
     * 数据转换为byte
     *
     * @param data
     * @return byte
     */
    public static Byte getByte2(Object data) {
        return CONVERTER.getByte(data);
    }

    /**
     * 数据转换为 Boolean
     *
     * @param data
     * @return Boolean
     */
    public static boolean getBoolean(Object data) {
        return getBoolean2(data);
    }

    /**
     * 数据转换为Boolean
     *
     * @param data
     * @return Boolean
     */
    public static Boolean getBoolean2(Object data) {
        return CONVERTER.getBoolean(data);
    }

    /**
     * 数据转换为Short
     *
     * @param data
     * @return Short
     */
    public static short getShort(Object data) {
        return CONVERTER.getShort(data);
    }

    /**
     * 数据转换为Short
     *
     * @param data
     * @return Short
     */
    public static Short getShort2(Object data) {
        return CONVERTER.getShort(data);
    }

    /**
     * 数据转换为float
     *
     * @param data
     * @return float
     */
    public static float getFloat(Object data) {
        return CONVERTER.getFloat(data);
    }

    /**
     * 数据转换为float
     *
     * @param data
     * @return float
     */
    public static Float getFloat2(Object data) {
        return CONVERTER.getFloat(data);
    }

    /**
     * 数据转换为double
     *
     * @param data
     * @return double
     */
    public static double getDouble(Object data) {
        return CONVERTER.getDouble(data);
    }

    /**
     * 数据转换为double
     *
     * @param data
     * @return double
     */
    public static Double getDouble2(Object data) {
        return CONVERTER.getDouble(data);
    }

    /**
     * 数据转换为long
     *
     * @param data
     * @return long
     */
    public static long getLong(Object data) {
        return CONVERTER.getLong(data);
    }

    /**
     * 数据转换为long
     *
     * @param data
     * @return long
     */
    public static Long getLong2(Object data) {
        return CONVERTER.getLong(data);
    }

    /**
     * 数据转换为int
     *
     * @param data
     * @return int
     */
    public static int getInt(Object data) {
        return CONVERTER.getInt(data);
    }

    /**
     * 数据转换为int
     *
     * @param data
     * @return int
     */
    public static Integer getInt2(Object data) {
        return CONVERTER.getInt(data);
    }

    /**
     * 数据转换为String
     *
     * @param data
     * @return String
     */
    public static String getStr(Object data) {
        return CONVERTER.getStr(data);
    }

    /**
     * 数据转换为 char
     *
     * @param data
     * @return char
     */
    public static char getChar(Object data) {
        return CONVERTER.getChar(data);
    }

    /**
     * 数据转换为 char
     *
     * @param data
     * @return char
     */
    public static Character getChar2(Object data) {
        return CONVERTER.getChar(data);
    }


    /**
     * 泛型转换
     *
     * @param data
     * @param tClass
     * @param <T>
     * @return /
     */
    public static <T> T getT(Class<T> tClass, Object extra, Object data) {
        return CONVERTER.getT(tClass, extra, data);
    }
}
