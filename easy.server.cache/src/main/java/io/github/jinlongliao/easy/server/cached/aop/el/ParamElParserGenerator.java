package io.github.jinlongliao.easy.server.cached.aop.el;

import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator.JAVA_DEF_VERSION;
import static io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator.MAPPER_CLASS_LOADER;
import static org.springframework.asm.Opcodes.*;

/**
 * param el 解析
 *
 * @author: liaojinlong
 * @date: 2023/7/5 17:01
 */
public class ParamElParserGenerator {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Class<?>[] INTERFACES = new Class[]{ParamElParser.class};

    static ParamElParser build0(String el, List<String[]> elList, Method method, Map<Type, Type[]> generic,
                                Class<?> paramClass) throws MethodInvokeException {
        String proxyObjectName = buildProxyClassName(method, el);
        try {
            String dynamicClassName = proxyObjectName.replace('/', '.');
            Class<?> loadClass = MAPPER_CLASS_LOADER.loadClass(dynamicClassName);
            return (ParamElParser) loadClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(JAVA_DEF_VERSION, ACC_PUBLIC, proxyObjectName,
                null,
                CLassUtils.OBJECT_SUPER_NAME,
                Arrays.stream(INTERFACES).map(CLassUtils::getJvmClass).toArray(String[]::new));
        MethodVisitor construct = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        construct.visitCode();
        construct.visitVarInsn(ALOAD, 0);
        construct.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        construct.visitInsn(RETURN);
        construct.visitMaxs(3, 1);
        construct.visitEnd();

        Map<Class<?>, List<ElField>> fieldCache = new HashMap<>(8, 1L);
        List<List<ElField>> elFields = new ArrayList<>(el.length());
        for (String[] elFieldsStr : elList) {
            List<ElField> elField = new ArrayList<>();
            elFields.add(elField);
            Class<?> root = paramClass;
            for (String elFieldStr : elFieldsStr) {
                List<ElField> fields = parserField(fieldCache, root, generic);
                ElField parserElField = fields.stream()
                        .filter(field -> field.getFieldName().equals(elFieldStr)).findFirst()
                        .orElseThrow(() -> new MethodInvokeException("not found field: " + elFieldStr));
                elField.add(parserElField);
                root = parserElField.getFieldClass();
            }
        }
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "parseValue", "(Ljava/lang/StringBuilder;Ljava/lang/Object;)Ljava/lang/String;", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitTypeInsn(CHECKCAST, CLassUtils.getJvmClass(paramClass));
        methodVisitor.visitVarInsn(ASTORE, 3);
        AtomicInteger localIndex = new AtomicInteger(3);

        for (List<ElField> elField : elFields) {
            buildGetCode(methodVisitor, localIndex, elField);
        }
        int index = 0;
        for (List<ElField> elField : elFields) {
            index = Math.max(index, elField.size());
        }
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitMaxs(index + 2, localIndex.get());
        methodVisitor.visitEnd();

        classWriter.visitEnd();

        byte[] classes = classWriter.toByteArray();
        MapperStructConfig.saveClassFile(classes, proxyObjectName);
        return MAPPER_CLASS_LOADER.reLoadInstance(proxyObjectName, classes);


    }

    private static void buildGetCode(MethodVisitor methodVisitor, AtomicInteger localIndex, List<ElField> elField) {
        if (elField.isEmpty()) {
            return;
        }
        Class<?> type = null;
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitVarInsn(ALOAD, 3);
        for (ElField field : elField) {
            type = field.getFieldClass();

            if (field.isGeneric()) {
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                        CLassUtils.getJvmClass(field.getField().getDeclaringClass()),
                        getGetMethod(field),
                        "()" + CLassUtils.getClassType(Object.class),
                        false);
                methodVisitor.visitTypeInsn(CHECKCAST, CLassUtils.getJvmClass(type));
            } else {
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                        CLassUtils.getJvmClass(field.getField().getDeclaringClass()),
                        getGetMethod(field),
                        "()" + CLassUtils.getClassType(type),
                        false);
            }

        }
        boolean baseType = CLassUtils.isBaseType(type);
        String desc;
        if (baseType) {
            desc = CLassUtils.getJvmClass(type);
        } else {
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false);
            desc = "Ljava/lang/String;";
        }
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(" + desc + ")Ljava/lang/StringBuilder;", false);
        methodVisitor.visitInsn(POP);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitLdcInsn(":");

        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        methodVisitor.visitInsn(POP);

    }


    private static String getGetMethod(ElField field) {
        String fieldName = field.getFieldName();
        Class<?> type = field.getFieldClass();
        boolean bool = CLassUtils.isBool(type);
        String getName;
        char[] charArray = fieldName.toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        if (bool) {
            getName = "is" + new String(charArray);
        } else {
            getName = "get" + new String(charArray);
        }
        return getName;
    }

    private static List<ElField> parserField(Map<Class<?>, List<ElField>> fieldCache, Class<?> tC, Map<Type, Type[]> generic) {
        if (tC.equals(Object.class)) {
            return Collections.emptyList();
        }
        return fieldCache.computeIfAbsent(tC, key -> {
            Field[] declaredFields = tC.getDeclaredFields();
            List<ElField> fields = new ArrayList<>(declaredFields.length);
            Type[] types = generic.get(tC);
            int index = 0;
            for (Field declaredField : declaredFields) {
                Class<?> type = declaredField.getType();
                String fieldName = declaredField.getName();
                boolean genericFlag = false;
                if (type == Object.class && Objects.nonNull(types)) {
                    type = (Class<?>) types[index++];
                    genericFlag = true;
                }
                fields.add(new ElField(declaredField, type, fieldName, genericFlag));
            }
            fields.addAll(parserField(fieldCache, tC.getSuperclass(), generic));
            return fields;
        });

    }


    private static String buildProxyClassName(Method method, String el) {
        StringBuilder builder = new StringBuilder(ParamElParserGenerator.class.getPackageName())
                .append(method.getDeclaringClass().getName())
                .append("/")
                .append(method.getName());
        boolean upper = true;
        for (char c : el.toCharArray()) {
            if (c == '.') {
                continue;
            }
            if (Character.isSpaceChar(c)) {
                upper = true;
            } else {
                if (upper) {
                    builder.append(Character.isUpperCase(c));
                } else {
                    builder.append((c));
                }
                upper = false;
            }
        }
        return builder.toString().replace('.', '/');
    }
}