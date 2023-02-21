package io.github.jinlongliao.easy.server.extend.response.proxy;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Ignore2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Mapping2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.InnerConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.FieldParserBody;
import io.github.jinlongliao.easy.server.mapper.exception.ConverterNotFountException;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import io.github.jinlongliao.easy.server.mapper.utils.Objects;
import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;

/**
 * @date 2022-12-21 16:06
 * @author: liaojinlong
 * @description: 用于代理生成的
 **/

public class ProxyResponseFactory {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String RESPONSE＿STREAM＿FACTORY＿CLASS_TYPE = CLassUtils.getClassType(IResponseStreamFactory.class);
    private static final Map<Class<? extends ICommonResponse>, ProxyResponse> PROXY_RESPONSE_MAP = new ConcurrentHashMap<>(32, 1L);

    public static ProxyResponse getProxyResponse(ICommonResponse response) {
        return PROXY_RESPONSE_MAP.computeIfAbsent(response.getClass(), key -> buildProxyResponse0(response));
    }

    private static ProxyResponse buildProxyResponse0(ICommonResponse response) {
        Class<? extends ICommonResponse> responseClass = response.getClass();
        String proxyObjectName = getProxyObjectName(responseClass);
        try {
            String dynamicClassName = proxyObjectName.replace('/', '.');
            Class<? extends ProxyResponse> loadClass = (Class<? extends ProxyResponse>) AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(dynamicClassName);
            return loadClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
        }

        ClassWriter classWriter = CLassUtils.buildObjConstruct(proxyObjectName, ProxyResponse.class);
        List<ExtraFieldParserBody> fields = getResponseFields(response, responseClass);

        buildImplMethod(classWriter, fields, responseClass);

        byte[] classes = classWriter.toByteArray();
        MapperStructConfig.saveClassFile(classes, proxyObjectName);
        return AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.reLoadInstance(proxyObjectName, classes);
    }

    private static void buildImplMethod(ClassWriter classWriter, List<ExtraFieldParserBody> fields,
                                        Class<? extends ICommonResponse> responseClass) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "genResHex",
                "(" + RESPONSE＿STREAM＿FACTORY＿CLASS_TYPE + CLassUtils.getClassType(ICommonResponse.class) + ")[B",
                null,
                null);

        methodVisitor.visitCode();
        fields.sort(Comparator.comparingInt(FieldParserBody::getIndex));
        String jvmClass = CLassUtils.getJvmClass(responseClass);
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitTypeInsn(CHECKCAST, jvmClass);
        methodVisitor.visitVarInsn(ASTORE, 3);
        for (ExtraFieldParserBody field : fields) {
            toBuildField(methodVisitor, jvmClass, field);
        }
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, CLassUtils.getJvmClass(IResponseStreamFactory.class), "toBytes", "()[B", true);
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitMaxs(3, 4);
        methodVisitor.visitEnd();
    }

    private static void toBuildField(MethodVisitor methodVisitor, String jvmClass, ExtraFieldParserBody fieldParserBody) {
        Class<?> convertClass = fieldParserBody.getConvertClass();
        String convertMethod = fieldParserBody.getConvertMethod();
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitVarInsn(ALOAD, 3);
        String method = fieldParserBody.getPutMethod();
        String classType = CLassUtils.getClassType(fieldParserBody.getFiledType());
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, jvmClass, method, "()" + classType, false);
        if (fieldParserBody.isStr() && !fieldParserBody.isDynamic()) {
            CLassUtils.putInt(methodVisitor, fieldParserBody.getStrLen());
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    CLassUtils.getJvmClass(convertClass),
                    convertMethod,
                    "(" + RESPONSE＿STREAM＿FACTORY＿CLASS_TYPE + classType + "I)V",
                    false);
        } else {
            methodVisitor.visitMethodInsn(INVOKESTATIC,
                    CLassUtils.getJvmClass(convertClass),
                    convertMethod,
                    "(" + RESPONSE＿STREAM＿FACTORY＿CLASS_TYPE + classType + ")V",
                    false);
        }
    }


    private static List<ExtraFieldParserBody> getResponseFields(ICommonResponse response, Class<? extends
            ICommonResponse> responseClass) {
        List<Field> parentField = response.headerAppender();
        List<ExtraFieldParserBody> fields = new ArrayList<>(parentField.size());
        int index = 0;
        for (Field declaredField : parentField) {
            index = getIndex(fields, index, declaredField, true);
        }
        Field[] declaredFields = responseClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            index = getIndex(fields, index, declaredField, false);
        }
        return fields;
    }

    private static int getIndex(List<ExtraFieldParserBody> fields, int index, Field declaredField,
                                boolean parentField) {
        Ignore2 ignore = declaredField.getAnnotation(Ignore2.class);
        if (Objects.nonNull(ignore) && ignore.value()) {
            return index;
        }
        if (Modifier.isStatic(declaredField.getModifiers())) {
            return index;
        }
        final Mapping2 mapping = declaredField.getAnnotation(Mapping2.class);
        final boolean nonNull = Objects.nonNull(mapping);
        String sourceName = nonNull ? mapping.sourceName() : declaredField.getName();
        if (sourceName.length() < 1) {
            sourceName = declaredField.getName();
        }
        String putMethod = nonNull ? mapping.putMethod() : "";
        Class<?> fieldType = declaredField.getType();
        if (putMethod.length() < 1) {
            char[] chars = sourceName.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            if (CLassUtils.isBoolType(fieldType)) {
                putMethod = "is" + new String(chars);
            } else {
                putMethod = "get" + new String(chars);
            }
        }
        Class<?> className;
        if (nonNull) {
            className = mapping.converterClass();
        } else {
            className = InnerWriteResponseProxyMethod.class;
        }
        boolean isDynamic = false;
        boolean isStr = false;
        int strLen = 0;
        String methodName = nonNull ? mapping.converterMethod() : "";
        if (methodName.length() < 1) {
            Method converter;
            if (fieldType == String.class) {
                isStr = true;
                strLen = 20;
                LogicRequestParam requestParam = declaredField.getAnnotation(LogicRequestParam.class);
                if (java.util.Objects.isNull(requestParam)) {
                    log.warn("Field:{} not use {}, use def len 20 ", declaredField.getName(), LogicRequestParam.class.getName());
                    converter = InnerWriteResponseProxyMethod.strMethod;
                } else {
                    boolean dynamicLength = requestParam.dynamicLength();
                    strLen = requestParam.length();
                    if (dynamicLength) {
                        converter = InnerWriteResponseProxyMethod.dynamicStrMethod;
                        isDynamic = true;
                    } else {
                        converter = InnerWriteResponseProxyMethod.strMethod;
                    }
                }
            } else {
                converter = InnerWriteResponseProxyMethod.getGlobalConverter(fieldType);
            }
            if (converter == null) {
                throw new ConverterNotFountException("Type Converter Not Found " + fieldType.getName());
            }
            methodName = converter.getName();
        }
        fields.add(new ExtraFieldParserBody(declaredField.getDeclaringClass().getName(),
                index++,
                sourceName,
                putMethod,
                fieldType,
                className,
                methodName,
                parentField,
                isStr,
                isDynamic,
                strLen));
        return index;
    }


    /**
     * 类名
     *
     * @param tClass
     * @return 实现类名
     */
    private static String getProxyObjectName(Class<? extends ICommonResponse> tClass) {
        return tClass.getPackage().getName().replace('.', '/') +
                "/dynamic/" +
                "response/" +
                tClass.getSimpleName() +
                "ProxyResponseDynamicImpl";
    }
}
