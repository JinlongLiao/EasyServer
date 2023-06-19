package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter.InnerConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.ExtraFieldConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.FieldParserBody;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Type;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;

import java.util.List;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;


/**
 * 用于生成函数的
 *
 * @author: liaojinlong
 * @date: 2022/5/21 22:01
 */
public abstract class AbstractClassMethodGenerator<C extends IData2Object2> {
    protected static final String GET_T = "__GET_T___";
    protected final String converter = "converter";
    protected final String converterJvmType = CLassUtils.getJvmClass(ExtraFieldConverter.class);
    protected final String converterClassType = CLassUtils.getClassType(ExtraFieldConverter.class);
    protected final String converterDescriptor = "(Ljava/lang/Object;ILjava/lang/String;Ljava/lang/String;Z)Ljava/lang/Object;";


    /**
     * 初始化函数
     *
     * @param cw
     * @param tClass
     * @param searchParentField
     * @param javaVersion
     * @param filedValueConverter
     * @param <T>
     */
    public abstract <T> List<FieldParserBody> initMethod(ClassWriter cw,
                                                         String owner,
                                                         Class<T> tClass,
                                                         boolean searchParentField,
                                                         int javaVersion,
                                                         Class<? extends ExtraFieldConverter> filedValueConverter);

    /**
     * 用于参数赋值
     *
     * @param methodVisitor
     * @param fieldParserBody
     * @param className
     * @param paramStack
     * @param targetStack
     * @param filedValueConverter
     */
    protected void putObjectValue(
            String owner,
            MethodVisitor methodVisitor,
            FieldParserBody fieldParserBody,
            String className,
            int paramStack,
            int targetStack,
            Class<? extends ExtraFieldConverter> filedValueConverter) {
        if (filedValueConverter != null) {
            methodVisitor.visitVarInsn(ALOAD, paramStack);

            int index = fieldParserBody.getIndex();
            String sourceName = fieldParserBody.getSourceName();
            String filedType = fieldParserBody.getFiledType().getName();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, owner,
                    converter, converterClassType);
            boolean parent = fieldParserBody.isParent();
            methodVisitor.visitVarInsn(ALOAD, paramStack);
            CLassUtils.putInt(methodVisitor, index);
            methodVisitor.visitLdcInsn(sourceName);
            methodVisitor.visitLdcInsn(filedType);
            methodVisitor.visitLdcInsn(parent ? ICONST_1 : ICONST_0);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, converterJvmType, converter, converterDescriptor, true);
            methodVisitor.visitVarInsn(ASTORE, paramStack);
        }

        String classType;
        String methodName;
        String descriptor;
        String returnType = CLassUtils.getClassType(fieldParserBody.getFiledType());

        methodVisitor.visitVarInsn(ALOAD, targetStack);

        methodName = fieldParserBody.getConvertMethod();
        Class<?> convertClass = fieldParserBody.getConvertClass();
        classType = CLassUtils.getJvmClass(convertClass);
        if (GET_T.equals(methodName) && InnerConverter.class.equals(convertClass)) {
            methodVisitor.visitLdcInsn(Type.getType(CLassUtils.getClassType(fieldParserBody.getFiledType())));
            methodVisitor.visitLdcInsn(fieldParserBody.getOwnerName() + ":" + fieldParserBody.getSourceName());
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Object");
            methodVisitor.visitVarInsn(ALOAD, paramStack);
            methodVisitor.visitMethodInsn(INVOKESTATIC, CLassUtils.getJvmClass(InnerConverter.class),
                    "getT",
                    "(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    false);
            methodVisitor.visitTypeInsn(CHECKCAST, CLassUtils.getJvmClass(fieldParserBody.getFiledType()));
        } else {
            methodVisitor.visitVarInsn(ALOAD, paramStack);
            descriptor = "(Ljava/lang/Object;)" + returnType;
            methodVisitor.visitMethodInsn(INVOKESTATIC, classType, methodName, descriptor, false);
        }
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, fieldParserBody.getPutMethod(), "(" + returnType + ")V", false);
    }

    /**
     * 用于生成的转换类
     *
     * @return /
     */
    public abstract Class<C> getTargetConverter();

    public Class<?>[] getInterfaces() {
        return new Class<?>[]{getTargetConverter()};
    }

    public abstract String getSuperName();


}
