package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.core.array;


import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.ExtraFieldConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.AbstractExtraClassMethodGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.FieldParserBody;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Label;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 用于生成函数的
 *
 * @author: liaojinlong
 * @date: 2022/5/21 22:01
 */
public class ArrayClassMethodCoreGenerator extends AbstractExtraClassMethodGenerator {

    @Override
    public <T> void initExtraMethod(List<FieldParserBody> filedParserBodies,
                                    String owner,
                                    String className,
                                    ClassWriter cw,
                                    Class<? extends ExtraFieldConverter> filedValueConverter) {
        this.initArrayMethod(cw, owner, className, filedParserBodies, filedValueConverter);
    }


    protected <T> void initArrayMethod(ClassWriter cw,
                                       String owner,
                                       String className,
                                       List<FieldParserBody> fieldParserBodyList,
                                       Class<? extends ExtraFieldConverter> filedValueConverter) {
        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC,
                "toArrayConverter",
                "([Ljava/lang/Object;)Ljava/lang/Object;",
                null,
                null);

        methodVisitor.visitCode();

        AtomicInteger maxStack = new AtomicInteger(2);
        AtomicInteger maxLocals = new AtomicInteger(2);
        // start Map.get("KEY")
        methodVisitor.visitVarInsn(ALOAD, 1);
        Label ifNull = new Label();
        maxStack.getAndIncrement();
        methodVisitor.visitJumpInsn(IFNONNULL, ifNull);
        methodVisitor.visitInsn(ICONST_0);
        methodVisitor.visitVarInsn(ISTORE, 2);
        Label goTo = new Label();
        methodVisitor.visitJumpInsn(GOTO, goTo);
        methodVisitor.visitLabel(ifNull);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitInsn(ARRAYLENGTH);
        methodVisitor.visitVarInsn(ISTORE, 2);
        methodVisitor.visitLabel(goTo);
        int startLocal = maxLocals.incrementAndGet();

        for (FieldParserBody fieldParserBody : fieldParserBodyList) {
            this.loadArrayParam(methodVisitor, fieldParserBody, maxStack, maxLocals);
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


    private void loadArrayParam(MethodVisitor methodVisitor, FieldParserBody fieldParserBody, AtomicInteger maxStack, AtomicInteger maxLocals) {
        int index = fieldParserBody.getIndex();
        CLassUtils.putInt(methodVisitor, index);
        methodVisitor.visitVarInsn(ILOAD, 2);
        Label bg = new Label();
        methodVisitor.visitJumpInsn(IF_ICMPGE, bg);
        methodVisitor.visitVarInsn(ALOAD, 1);
        CLassUtils.putInt(methodVisitor, index);
        methodVisitor.visitInsn(AALOAD);
        Label goTo = new Label();
        methodVisitor.visitJumpInsn(GOTO, goTo);
        methodVisitor.visitLabel(bg);
        methodVisitor.visitInsn(ACONST_NULL);
        methodVisitor.visitLabel(goTo);

        methodVisitor.visitVarInsn(ASTORE, maxLocals.getAndIncrement());
    }

    @Override
    public Class getTargetConverter() {
        return IArrayData2Object2.class;
    }


    @Override
    public String getSuperName() {
        return CLassUtils.OBJECT_SUPER_NAME;
    }
}
