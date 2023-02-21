package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.method;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import io.github.jinlongliao.easy.server.mapper.utils.UnpackDesc;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Label;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;

/**
 * @author: liaojinlong
 * @date: 2022/8/16 22:48
 */
public final class MethodInvokeFactory {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Map<Method, MethodInvoke> FACTORY = new HashMap<>(32);
    private static final String JVM_CLASS = CLassUtils.getJvmClass(MethodInvokeException.class);

    public static MethodInvoke buildMethodInvoke(Method method) {
        MethodInvoke methodInvoke = FACTORY.get(method);
        if (methodInvoke == null) {
            synchronized (method) {
                methodInvoke = FACTORY.get(method);
                if (methodInvoke == null) {
                    methodInvoke = build0(method);
                    FACTORY.put(method, methodInvoke);
                }
            }
        }
        return methodInvoke;
    }

    private static MethodInvoke build0(Method method) {
        String methodName = method.getName();
        char[] className = methodName.toCharArray();
        className[0] = Character.toUpperCase(className[0]);
        Class<?> declaringClass = method.getDeclaringClass();
        String owner = declaringClass.getPackage().getName().replace('.', '/')
                + "/dynamic/method/"
                + declaringClass.getSimpleName()
                + new String(className)
                + "MethodInvoke";
        try {
            String dynamicClassName = owner.replace('/', '.');
            Class<? extends MethodInvoke> loadClass = (Class<? extends MethodInvoke>) AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(dynamicClassName);
            return loadClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
        }
        MethodVisitor methodVisitor;
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(AsmProxyCodeGenerator.JAVA_DEF_VERSION, ACC_PUBLIC | ACC_SUPER, owner, null, "java/lang/Object", new String[]{CLassUtils.getJvmClass(MethodInvoke.class)});
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            Parameter[] parameters = method.getParameters();
            int length = parameters.length;
            String methodJvmClass = CLassUtils.getJvmClass(declaringClass);
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_VARARGS,
                    "invoke",
                    "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;",
                    null, new String[]{JVM_CLASS});
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, methodJvmClass);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 3);
            buildCast(methodVisitor, parameters);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, methodJvmClass, methodName, getMethodDesc(method), false);
            buildReturn(methodVisitor, method.getReturnType());

            methodVisitor.visitLabel(label1);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitTypeInsn(NEW, JVM_CLASS);
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, JVM_CLASS, "<init>", "(Ljava/lang/Throwable;)V", false);
            methodVisitor.visitInsn(ATHROW);
            methodVisitor.visitMaxs(length + 1, length + 2);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();
        byte[] classes = classWriter.toByteArray();
        MapperStructConfig.saveClassFile(classes, owner);
        return AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.reLoadInstance(owner, classes);
    }

    private static void buildReturn(MethodVisitor methodVisitor, Class<?> type) {
        if (type == null || type == Void.class || type == Void.TYPE) {
            methodVisitor.visitInsn(ACONST_NULL);
            return;
        }
        if (CLassUtils.isBaseType(type)) {
            UnpackDesc unpackDesc = CLassUtils.getUnpackDesc(type);
            methodVisitor.visitMethodInsn(INVOKESTATIC, unpackDesc.getOwner(), unpackDesc.getPackMethodName(), unpackDesc.getPackDescriptor(), false);
        }

    }

    private static void buildCast(MethodVisitor methodVisitor, Parameter[] parameters) {
        int flag = 0;
        for (Parameter parameter : parameters) {
            methodVisitor.visitVarInsn(ALOAD, 2);
            buildCastItem(methodVisitor, parameter, flag++);
        }
    }

    private static void buildCastItem(MethodVisitor methodVisitor, Parameter parameter, int index) {
        CLassUtils.putInt(methodVisitor, index);
        methodVisitor.visitInsn(AALOAD);
        Class<?> type = parameter.getType();
        Class<?> packageType = CLassUtils.getPackageType(type);
        methodVisitor.visitTypeInsn(CHECKCAST, CLassUtils.getJvmClass(packageType));
        // 函数里为基本类型
        boolean isBaseType = packageType != type;
        if (isBaseType) {
            UnpackDesc unpackDesc = CLassUtils.getUnpackDesc(packageType);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, unpackDesc.getOwner(), unpackDesc.getBaseMethodName(), unpackDesc.getBaseDescriptor(), false);
        }
    }

    private static String getMethodDesc(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder builder = new StringBuilder(32);
        builder.append("(");
        for (Class<?> parameterType : parameterTypes) {
            builder.append(CLassUtils.getClassType(parameterType));
        }
        builder.append(")");
        builder.append(CLassUtils.getClassType(returnType));
        return builder.toString();
    }


}
