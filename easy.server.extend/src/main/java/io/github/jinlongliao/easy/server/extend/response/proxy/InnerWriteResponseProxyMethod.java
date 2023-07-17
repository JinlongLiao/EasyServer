package io.github.jinlongliao.easy.server.extend.response.proxy;

import io.github.jinlongliao.easy.server.extend.exception.UnSupportFieldException;
import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory;
import io.github.jinlongliao.easy.server.mapper.annotation.Ignore;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date 2022-12-21 16:20
 * @author: liaojinlong
 * @description: /
 **/

public final class InnerWriteResponseProxyMethod {
    /**
     * 数据值转换 函数名
     */
    public static final Map<Type, Method> MAP;
    public static final Method dynamicStrMethod;
    public static final Method strMethod;

    static {
        MAP = new ConcurrentHashMap<>(1 << 8);
        final Class<InnerWriteResponseProxyMethod> innerCoreDataConverterClass = InnerWriteResponseProxyMethod.class;
        final Method[] methods = innerCoreDataConverterClass.getDeclaredMethods();
        for (Method method : methods) {
            Ignore annotation = method.getAnnotation(Ignore.class);
            if (annotation != null && annotation.value()) {
                continue;
            }
            final Class<?> type = method.getParameterTypes()[1];
            if (type != String.class) {
                MAP.put(type, method);
            }
        }
        try {
            strMethod = innerCoreDataConverterClass.getDeclaredMethod("writeString", IResponseStreamFactory.class, String.class, Integer.TYPE);
            dynamicStrMethod = innerCoreDataConverterClass.getDeclaredMethod("writeDynamicString", IResponseStreamFactory.class, String.class);
        } catch (NoSuchMethodException e) {
            throw new UnSupportFieldException(e);
        }
    }

    @Ignore
    public static void addGlobalConverter(Type type, Method method) {
        MAP.put(type, method);
    }

    @Ignore
    public static Method getGlobalConverter(Type type) {
        return MAP.get(type);
    }

    public static void writeResponse(IResponseStreamFactory factory, ICommonResponse response) {
        factory.writeResponse(response);
    }

    public static void writeResponseCollection(IResponseStreamFactory factory, Collection<Object> responses) {
        factory.writeResponses(responses);
    }

    public static void writeResponseList(IResponseStreamFactory factory, List<Object> responses) {
        factory.writeResponses(responses);
    }

    public static void writeResponseSet(IResponseStreamFactory factory, Set<Object> responses) {
        factory.writeResponses(responses);
    }

    public static void writeByte(IResponseStreamFactory factory, byte bt) {
        factory.writeByte(bt);
    }

    public static void writeByte2(IResponseStreamFactory factory, Byte bt) {
        if (Objects.isNull(bt)) {
            bt = 0;
        }
        factory.writeByte(bt);
    }

    public static void writeChar(IResponseStreamFactory factory, char c) {
        factory.writeChar(c);
    }

    public static void writeCharacter(IResponseStreamFactory factory, Character c) {
        if (Objects.isNull(c)) {
            c = 32;
        }
        factory.writeChar(c);
    }

    public static void writeBool(IResponseStreamFactory factory, boolean bool) {
        factory.writeBool(bool);
    }

    public static void writeBoolean(IResponseStreamFactory factory, Boolean bool) {
        if (Objects.isNull(bool)) {
            bool = false;
        }
        factory.writeBool(bool);
    }

    public static void writeDate(IResponseStreamFactory factory, Date date) {
        if (Objects.isNull(date)) {
            date = new Date(0);
        }
        factory.writeDate(date);
    }

    public static void writeShort(IResponseStreamFactory factory, short _short) {
        factory.writeShort(_short);
    }

    public static void writeShort2(IResponseStreamFactory factory, Short _short) {
        if (Objects.isNull(_short)) {
            _short = 0;
        }
        factory.writeShort(_short);
    }

    public static void writeInt(IResponseStreamFactory factory, int _int) {
        factory.writeInt(_int);
    }

    public static void writeInteger(IResponseStreamFactory factory, Integer _int) {
        if (Objects.isNull(_int)) {
            _int = 0;
        }
        factory.writeInt(_int);
    }

    public static void writeLong(IResponseStreamFactory factory, long _long) {
        factory.writeLong(_long);
    }

    public static void writeLong2(IResponseStreamFactory factory, Long _long) {
        if (Objects.isNull(_long)) {
            _long = 0L;
        }
        factory.writeLong(_long);
    }

    public static void writeIntArr(IResponseStreamFactory factory, int[] ints) {
        factory.write(ints);
    }

    public static void writeIntArr2(IResponseStreamFactory factory, Integer[] ints) {
        if (Objects.isNull(ints)) {
            writeIntArr(factory, new int[0]);
        } else {
            writeIntArr(factory, Arrays.stream(ints).mapToInt(Integer::valueOf).toArray());
        }
    }

    public static void writeByteArr(IResponseStreamFactory factory, byte[] bytes) {
        factory.writeBytes(bytes);
    }

    public static void writeByteArr2(IResponseStreamFactory factory, Byte[] bytes) {
        if (Objects.isNull(bytes)) {
            writeByteArr(factory, new byte[0]);
        } else {
            byte[] bts = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                bts[i] = bytes[i];
            }
            writeByteArr(factory, bts);
        }
    }

    public static void writeString(IResponseStreamFactory factory, String str, int len) {
        factory.writeString(str, len);
    }

    public static void writeDynamicString(IResponseStreamFactory factory, String str) {
        factory.writeDynamicString(str);
    }

}
