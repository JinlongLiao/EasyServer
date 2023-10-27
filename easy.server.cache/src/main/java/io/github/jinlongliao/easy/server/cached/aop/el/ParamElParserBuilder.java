package io.github.jinlongliao.easy.server.cached.aop.el;


import io.github.jinlongliao.easy.server.core.annotation.LogicAlias;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestBody;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.mapper.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * param el 解析
 *
 * @author: liaojinlong
 * @date: 2023/7/5 17:01
 */
public class ParamElParserBuilder {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Map<Method, ParamElParser> cache = new ConcurrentHashMap<>(16);

    public static ParamElParser build(String el, Method method) {
        Map<String, Class<?>> paramClassCache = new HashMap<>(8, 1L);
        String[] parameterNames = MethodParse.PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (Objects.isNull(parameterNames)) {
            Arrays.fill(parameterNames = new String[genericParameterTypes.length], "");
        }
        int index = 0;
        Map<Type, Type[]> generic = new HashMap<>(4, 1L);
        for (Parameter parameter : method.getParameters()) {
            if (genericParameterTypes[index] instanceof ParameterizedType parameterizedType) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                generic.put(parameterizedType.getRawType(), actualTypeArguments);
            }
            Class<?> type = parameter.getType();

            String typeName = parameterNames[index];
            LogicAlias logicAlias = AnnotationUtils.findAnnotation(parameter, LogicAlias.class);
            if (Objects.nonNull(logicAlias)) {
                typeName = logicAlias.value();
                parameterNames[index] = typeName;
            } else {
                LogicRequestParam requestParam = AnnotationUtils.findAnnotation(parameter, LogicRequestParam.class);
                if (Objects.nonNull(requestParam)) {
                    typeName = requestParam.value();
                    parameterNames[index] = typeName;
                } else {
                    LogicRequestBody requestBody = AnnotationUtils.findAnnotation(parameter, LogicRequestBody.class);
                    if (Objects.nonNull(requestBody)) {
                        typeName = requestBody.value();
                        parameterNames[index] = typeName;
                    }
                }
            }
            paramClassCache.put(typeName, type);
            index++;
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
            return ParamElParserGenerator.build0(el, elList, method, Arrays.asList(parameterNames), paramClassCache, generic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return (stringBuilder, param) -> stringBuilder.toString();
        }
    }


    public static String putElValue(StringBuilder stringBuilder, Object[] params, Method method, String keyValueEl) {
        if (StringUtil.isEmpty(keyValueEl)) {
            return stringBuilder.toString();
        }
        return buildValue(method, keyValueEl).parseValue(stringBuilder, params);
    }

    public static ParamElParser buildValue(Method method, String keyValueEl) {
        if (StringUtil.isEmpty(keyValueEl)) {
            return (builder, param) -> builder.toString();
        }
        return Objects.requireNonNull(cache.computeIfAbsent(method, k -> build(keyValueEl, method)));
    }


}
