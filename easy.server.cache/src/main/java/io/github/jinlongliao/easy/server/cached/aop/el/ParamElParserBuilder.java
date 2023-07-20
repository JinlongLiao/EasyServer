package io.github.jinlongliao.easy.server.cached.aop.el;


import io.github.jinlongliao.easy.server.mapper.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.jinlongliao.easy.server.cached.aop.el.ParamElParserGenerator.build0;


/**
 * param el 解析
 *
 * @author: liaojinlong
 * @date: 2023/7/5 17:01
 */
public class ParamElParserBuilder {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Map<Method, ParamElParser> cache = new ConcurrentHashMap<>(16);

    public static ParamElParser build(String el, int argIndex, Method method, Class<?> paramClass) {
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Map<Type, Type[]> generic;
        if (genericParameterTypes[argIndex] instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            generic = Collections.singletonMap(parameterizedType.getRawType(), actualTypeArguments);
        } else {
            generic = Collections.emptyMap();
        }
        String[] ands = el.trim().split("and");
        List<String[]> elList = new ArrayList<>(4);
        for (String and : ands) {
            String[] split = and.trim().split("\\.");
//            if (split.length < 2 || (!split[0].equals("base") && !split[0].equals("param"))) {
//                throw new IllegalArgumentException("illegal el [" + el);
//            }
            elList.add(split);
        }

        try {
            return build0(el, elList, method, generic, paramClass);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return (stringBuilder, param) -> stringBuilder.toString();
        }
    }


    public static String putElValue(StringBuilder stringBuilder, Object param, int argIndex, Method method, String keyValueEl) {
        if (StringUtil.isEmpty(keyValueEl)) {
            return stringBuilder.toString();
        }

        return Objects.requireNonNull(cache.computeIfAbsent(method, k -> build(keyValueEl, argIndex, method, param.getClass()))).parseValue(stringBuilder, param);
    }


}
