package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter;

import io.github.jinlongliao.easy.server.mapper.annotation.Ignore;
import io.github.jinlongliao.easy.server.mapper.annotation.Mapping;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter.InnerConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.ExtraFieldConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.FieldParserBody;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.utils.Objects;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.FieldVisitor;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;


/**
 * 用于生成函数的
 *
 * @author: liaojinlong
 * @date: 2022/5/21 22:01
 */
public abstract class ClassMethodCoreGenerator<C extends IData2Object2> extends AbstractClassMethodGenerator<C> {

    /**
     * 构造函数初始化
     */
    public void initConstruct(ClassWriter classWriter,
                              String owner,
                              boolean searchParentField,
                              Class<? extends ExtraFieldConverter> filedValueConverter) {

        boolean hasConverter = filedValueConverter != null;
        String type = CLassUtils.getClassType(ExtraFieldConverter.class);
        if (hasConverter) {
            FieldVisitor fieldVisitor = classWriter
                    .visitField(ACC_PRIVATE | ACC_FINAL,
                            converter, type, null, null);
            fieldVisitor.visitEnd();
        }

        MethodVisitor construct = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        construct.visitCode();
        construct.visitVarInsn(ALOAD, 0);
        construct.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

        if (hasConverter) {
            String jvmClass = CLassUtils.getJvmClass(filedValueConverter);
            construct.visitVarInsn(ALOAD, 0);
            construct.visitTypeInsn(NEW, jvmClass);
            construct.visitInsn(DUP);
            construct.visitMethodInsn(INVOKESPECIAL, jvmClass, "<init>", "()V", false);
            construct.visitFieldInsn(PUTFIELD, owner, converter, CLassUtils.getClassType(ExtraFieldConverter.class));
        }
        construct.visitInsn(RETURN);
        construct.visitMaxs(3, 1);
        construct.visitEnd();
    }

    @Override
    public <T> List<FieldParserBody> initMethod(ClassWriter cw, String owner, Class<T> tClass, boolean searchParentField,
                                                int javaVersion, Class<? extends ExtraFieldConverter> filedValueConverter) {
        return getObjectFiledParserBody(searchParentField, tClass);
    }

    protected <T> List<FieldParserBody> getObjectFiledParserBody(boolean searchParentField, Class<T> tClass) {
        AtomicInteger index = new AtomicInteger(0);
        return getFields(tClass, searchParentField, index, false);
    }


    protected <T> List<FieldParserBody> getFields(Class<T> tClass, boolean searchParentField, AtomicInteger index,
                                                  boolean isParent) {
        if (Modifier.isInterface(tClass.getModifiers())) {
            return Collections.emptyList();
        }
        if (tClass.equals(Object.class)) {
            return Collections.emptyList();
        }
        final Field[] fields = tClass.getDeclaredFields();
        ArrayList<FieldParserBody> fieldList = new ArrayList<>(fields.length);
        if (searchParentField) {
            Class<?> superclass = tClass.getSuperclass();
            fieldList.addAll(this.getFields(superclass, true, index, true));
        }

        for (Field field : fields) {
            FieldParserBody fieldParserBody = this.addFiled(field, index, isParent);
            if (fieldParserBody == null) {
                continue;
            }
            fieldList.add(fieldParserBody);
        }
        return fieldList;
    }

    protected FieldParserBody addFiled(Field field, AtomicInteger index, boolean isParent) {
        Ignore ignore = field.getAnnotation(Ignore.class);
        if (ignore != null && ignore.value()) {
            return null;
        }
        if (Modifier.isFinal(field.getModifiers())) {
            return null;
        }
        if (Modifier.isStatic(field.getModifiers())) {
            return null;
        }

        final Mapping mapping = field.getAnnotation(Mapping.class);
        final boolean nonNull = Objects.nonNull(mapping);
        String sourceName = nonNull ? mapping.sourceName() : field.getName();
        if (sourceName.length() < 1) {
            sourceName = field.getName();
        }
        String putMethod = nonNull ? mapping.putMethod() : "";
        if (putMethod.length() < 1) {
            String name = field.getName();
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            putMethod = "set" + new String(chars);
        }
        Class<?> className;
        if (nonNull) {
            className = mapping.converterClass();
        } else {
            className = InnerConverter.class;
        }
        String methodName = nonNull ? mapping.converterMethod() : "";
        Class<?> type = field.getType();
        if (methodName.length() < 1) {
            if (InnerConverter.containDateConverter(type)) {
                methodName = InnerConverter.getGlobalConverter(type).getName();
            } else {
                methodName = GET_T;
            }
        }
        return new FieldParserBody(field.getDeclaringClass().getName(), index.getAndIncrement(), sourceName, putMethod, type, className, methodName, isParent);
    }

    @Override
    public String getSuperName() {
        return CLassUtils.OBJECT_SUPER_NAME;
    }
}
