package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.ExtraFieldConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.AbstractExtraClassMethodGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.FieldParserBody;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;

/**
 * Javax Servlet
 *
 * @author: liaojinlong
 * @date: 2022/5/22 10:28
 */
public class ServletExtraClassMethodGenerator extends AbstractExtraClassMethodGenerator<IServletData2Object> {
    public <T> void initJavaxServletExtraMethod(List<FieldParserBody> fieldParserBodyList,
                                                String owner,
                                                String className,
                                                ClassWriter cw,
                                                Class<? extends ExtraFieldConverter> filedValueConverter) {
        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "toHttpServletRequestConverter", "(Ljakarta/servlet/http/HttpServletRequest;)Ljava/lang/Object;", null, null);

        methodVisitor.visitCode();

        AtomicInteger maxStack = new AtomicInteger(2);
        AtomicInteger maxLocals = new AtomicInteger(2);
        int startLocal = maxLocals.intValue();

        for (FieldParserBody fieldParserBody : fieldParserBodyList) {
            this.loadServletParam(methodVisitor, fieldParserBody, maxStack, maxLocals);
        }
        // new Object
        methodVisitor.visitTypeInsn(NEW, className);
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, className, "<init>", "()V", false);
        int endLocal = maxLocals.getAndIncrement();
        methodVisitor.visitVarInsn(ASTORE, endLocal);
        // start Object.setXX(value)
        for (FieldParserBody fieldParserBody : fieldParserBodyList) {
            this.putObjectValue(owner, methodVisitor, fieldParserBody, className, startLocal++, endLocal, filedValueConverter);
        }
        methodVisitor.visitVarInsn(ALOAD, endLocal);
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitMaxs(maxStack.intValue(), maxLocals.intValue());
        methodVisitor.visitEnd();
    }

    private void loadServletParam(MethodVisitor methodVisitor, FieldParserBody fieldParserBody, AtomicInteger maxStack, AtomicInteger maxLocals) {
        String sourceName = fieldParserBody.getSourceName();
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitLdcInsn(sourceName);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "jakarta/servlet/http/HttpServletRequest", "getParameter", "(Ljava/lang/String;)Ljava/lang/String;", true);
        methodVisitor.visitVarInsn(ASTORE, maxLocals.getAndIncrement());
    }

    @Override
    public <T> void initExtraMethod(List<FieldParserBody> filedParserBodies,
                                    String owner,
                                    String className,
                                    ClassWriter cw,
                                    Class<? extends ExtraFieldConverter> filedValueConverter) {
        this.initJavaxServletExtraMethod(filedParserBodies, owner, className, cw, filedValueConverter);
    }

    @Override
    public Class<IServletData2Object> getTargetConverter() {
        return IServletData2Object.class;
    }
}
