/**
 * Copyright 2020-2021 JinlongLiao
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core;

import io.github.jinlongliao.easy.server.mapper.constant.ParamType;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.annotation.Ignore;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.annotation.Mapping;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.exception.ConverterException;
import io.github.jinlongliao.easy.server.mapper.exception.ConverterNotFountException;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import io.github.jinlongliao.easy.server.mapper.utils.Objects;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IData2Object 代理类实现
 *
 * @author liaojinlong
 * @since 2020/11/23 21:48
 */
public class Proxy {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String DOT = ".";
    /**
     * 数据值转换 函数名
     */
    private static final Map<Type, String> VALUE_CONVERTER;

    static {
        VALUE_CONVERTER = new ConcurrentHashMap<>(1 << 8);
        final Class<InnerCoreDataConverter> innerCoreDataConverterClass = InnerCoreDataConverter.class;
        final String className = innerCoreDataConverterClass.getName();
        final Method[] methods = innerCoreDataConverterClass.getDeclaredMethods();
        for (Method method : methods) {
            final Class<?> type = method.getReturnType();
            final String methodName = method.getName();
            VALUE_CONVERTER.put(type, className + DOT + methodName + "(");
        }
    }

    private static final ClassPool POOL = ClassPool.getDefault();

    private static final String PROXY_SUFFIX = "Data2ObjectImpl";
    private static final String INTERFACE_NAME = IData2Object.class.getName();

    static {
        ClassClassPath classPath = new ClassClassPath(IData2Object.class);
        POOL.insertClassPath(classPath);
    }

    /**
     * 新增全局自定义转换函数
     *
     * @param key
     * @param fullMethod
     */
    public static void addValueConverter(Type key, String fullMethod) {
        VALUE_CONVERTER.put(key, fullMethod);
    }

    /**
     * 生成代理对象
     *
     * @return io.github.jinlonghliao.commons.mapstruct.IData2Object
     */
    public IData2Object getProxyObject(Class tClass) {
        return this.getProxyObject(tClass, false);
    }

    /**
     * 生成代理对象
     *
     * @param tClass
     * @param searchParentField
     * @return io.github.jinlonghliao.commons.mapstruct.IData2Object
     */
    public IData2Object getProxyObject(Class tClass, boolean searchParentField) {
        IData2Object proxyObject;
        // 创建代理类对象
        String proxyObjectName = getProxyObjectName(tClass);
        try {
            String dynamicClassName = proxyObjectName.replace('/', '.');
            Class<? extends IData2Object> loadClass = (Class<? extends IData2Object>) AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(dynamicClassName);

            return loadClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            if (log.isDebugEnabled()){
                log.debug(e.getMessage(),e);
            }
         }
        CtClass ctClass = POOL.makeClass(proxyObjectName);
        try {
            // 设置代理类的接口
            CtClass aClass = POOL.getCtClass(INTERFACE_NAME);
            ctClass.setInterfaces(new CtClass[]{aClass});
            // 代理类的所有方法
            CtMethod[] methods = aClass.getDeclaredMethods();
            for (CtMethod method : methods) {
                ParamType paramType;
                if ("toMapConverter".equals(method.getName())) {
                    paramType = ParamType.MAP;
                } else if ("toArrayConverter".equals(method.getName())) {
                    paramType = ParamType.ARRAY;
                } else if ("toHttpServletRequestConverter".equals(method.getName())) {
                    paramType = ParamType.SERVLET;
                } else {
                    throw new ConverterException("参数异常：");
                }
                buildToConverterMethod(paramType, searchParentField, method, ctClass, tClass);
            }

            MapperStructConfig.saveClassFile(ctClass.toBytecode(), proxyObjectName);
            proxyObject = (IData2Object) ctClass.toClass().newInstance();
        } catch (Exception e) {
            throw new ConverterException(e);
        }

        return proxyObject;
    }


    /**
     * 构建Map/Array/SERVLET 参数函数实现
     *
     * @param paramType
     * @param searchParentField
     * @param method
     * @param ctClass
     * @param tClass
     * @throws CannotCompileException
     * @throws NotFoundException
     */
    private void buildToConverterMethod(ParamType paramType, boolean searchParentField, CtMethod method, CtClass ctClass, Class tClass) throws CannotCompileException, NotFoundException {
        String methodName = method.getName();
        CtMethod cm = new CtMethod(method.getReturnType(), methodName, method.getParameterTypes(), ctClass);
        final String methodBody = getMethodBodyStr(tClass, searchParentField, paramType);
        cm.setBody(methodBody);
        ctClass.addMethod(cm);
    }


    /**
     * 函数实现内容（Map/Array）
     *
     * @param tClass
     * @param searchParentField 关联父属性
     * @param paramType
     * @return 接口函数实现内容 （Map/Array）
     */
    private String getMethodBodyStr(Class tClass, boolean searchParentField, ParamType paramType) {
        StringBuffer buffer = new StringBuffer("{");
        final List<Field> fieldList = getFields(tClass, searchParentField);
        final String objectName = tClass.getName();
        buffer.append(objectName + " tmp= new " + objectName + "();");
        int fieldIndex = 0;
        for (Field field : fieldList) {
            Ignore ignore = field.getAnnotation(Ignore.class);
            if (ignore != null && ignore.value()) {
                continue;
            }
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            final Mapping mapping = field.getAnnotation(Mapping.class);
            final boolean nonNull = Objects.nonNull(mapping);
            final String setMethodName = nonNull
                    ? mapping.method().length() > 0
                    ? mapping.method()
                    : "set" + firstToUpper(field.getName())
                    : "set" + firstToUpper(field.getName());
            buffer.append("tmp.");
            buffer.append(setMethodName);
            buffer.append("(");
            buffer.append(getParamTypeConverterMethod(field, mapping));
            if (ParamType.MAP.equals(paramType)) {
                final String fieldName = nonNull
                        ? mapping.source().length() > 0
                        ? mapping.source()
                        : field.getName()
                        : field.getName();
                buffer.append("$1.get(\"");
                buffer.append(fieldName);
                buffer.append("\"))");
            } else if (ParamType.ARRAY.equals(paramType)) {
                buffer.append("$1[");
                buffer.append(fieldIndex);
                buffer.append("])");
            } else if (ParamType.SERVLET.equals(paramType)) {
                final String fieldName = nonNull
                        ? mapping.source().length() > 0
                        ? mapping.source()
                        : field.getName()
                        : field.getName();
                Class<?> type = field.getType();
                if (type == List.class) {
                    buffer.append(" java.util.Arrays.asList($1.getParameterValues(\"");
                    buffer.append(fieldName);
                    buffer.append("\")))");
                } else if (type.isArray()) {
                    buffer.append("$1.getParameterValues(\"");
                    buffer.append(fieldName);
                    buffer.append("\"))");
                } else {
                    buffer.append("$1.getParameter(\"");
                    buffer.append(fieldName);
                    buffer.append("\"))");
                }

            } else {
                throw new ConverterNotFountException(ParamType.class.getName() + "Not Found: " + paramType);
            }

            buffer.append(");");
            fieldIndex++;
        }

        buffer.append("return tmp;");
        buffer.append("}");
        return buffer.toString();
    }

    private List<Field> getFields(Class tClass, boolean searchParentField) {
        boolean anInterface = Modifier.isInterface(tClass.getModifiers());
        if (anInterface) {
            return Collections.emptyList();
        }
        if (tClass.equals(Object.class)) {
            return Collections.emptyList();
        }
        final Field[] fields = tClass.getDeclaredFields();
        ArrayList<Field> fieldList = new ArrayList<>(fields.length);
        if (searchParentField) {
            Class superclass = tClass.getSuperclass();
            fieldList.addAll(this.getFields(superclass, searchParentField));
        }
        fieldList.addAll(Arrays.asList(fields));
        return fieldList;
    }

    /**
     * 参数类型转换
     *
     * @param field
     * @return 赋值函数
     */
    private String getParamTypeConverterMethod(Field field, Mapping mapping) {
        final Class<?> type = field.getType();
        String converterMethod;
        final boolean exist = Objects.nonNull(mapping) &&
                mapping.className().trim().length() > 0;
        if (exist) {
            converterMethod = mapping.className() + DOT + mapping.methodName() + "(";
        } else {
            String name = type.getName();
            if (VALUE_CONVERTER.containsKey(type)) {
                converterMethod = VALUE_CONVERTER.get(type);
            } else {
                converterMethod = " ((" + field.getType().getName() + ")";
            }
        }
        return converterMethod;
    }

    /**
     * 类名
     *
     * @param tClass
     * @return 实现类名
     */
    private String getProxyObjectName(Class tClass) {
        return tClass.getPackage().getName()
                + DOT
                + "dynamic"
                + DOT
                + "mapstruct"
                + DOT
                + tClass.getSimpleName()
                + PROXY_SUFFIX;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return 首字母大写
     */
    private String firstToUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
