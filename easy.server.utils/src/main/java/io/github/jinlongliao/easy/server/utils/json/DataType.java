package io.github.jinlongliao.easy.server.utils.json;


import io.github.jinlongliao.easy.server.utils.common.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class DataType<T> {
    protected final Type type;
    protected final Class<? super T> rawType;

    public DataType() {
        Type superClass = getClass().getGenericSuperclass();
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        rawType = (Class<? super T>) ClassUtils.getRawType(type);
    }

    public Type getType() {
        return type;
    }

    public Class<? super T> getRawType() {
        return rawType;
    }
}
