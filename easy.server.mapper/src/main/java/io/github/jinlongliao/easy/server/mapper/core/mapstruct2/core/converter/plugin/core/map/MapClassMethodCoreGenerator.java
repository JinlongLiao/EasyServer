package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.core.map;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.ExtraFieldConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.AbstractExtraClassMethodGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.FieldParserBody;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;

/**
 * 用于生成函数的
 *
 * @author: liaojinlong
 * @date: 2022/5/21 22:01
 */
public class MapClassMethodCoreGenerator extends AbstractExtraClassMethodGenerator<IMapData2Object2> {
    @Override
    public <T> void initExtraMethod(List<FieldParserBody> filedParserBodies,
                                    String owner,
                                    String className,
                                    ClassWriter cw,
                                    Class<? extends ExtraFieldConverter> filedValueConverter) {

        this.initMapMethod(cw, owner, className, filedParserBodies, filedValueConverter);
    }


    protected <T> void initMapMethod(ClassWriter cw,
                                     String owner,
                                     String className,
                                     List<FieldParserBody> fieldParserBodyList,
                                     Class<? extends ExtraFieldConverter> filedValueConverter) {
        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC,
                "toMapConverter",
                "(Ljava/util/Map;)Ljava/lang/Object;",
                null,
                null);

        methodVisitor.visitCode();

        AtomicInteger maxStack = new AtomicInteger(2);
        AtomicInteger maxLocals = new AtomicInteger(2);
        int startLocal = maxLocals.intValue();

        for (FieldParserBody fieldParserBody : fieldParserBodyList) {
            this.loadMapParam(methodVisitor, fieldParserBody, maxStack, maxLocals);

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

    private void loadMapParam(MethodVisitor methodVisitor, FieldParserBody fieldParserBody, AtomicInteger maxStack, AtomicInteger maxLocals) {
        String sourceName = fieldParserBody.getSourceName();
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitLdcInsn(sourceName);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
        methodVisitor.visitVarInsn(ASTORE, maxLocals.getAndIncrement());
    }


    @Override
    public Class<IMapData2Object2> getTargetConverter() {
        return IMapData2Object2.class;
    }


    @Override
    public String getSuperName() {
        return CLassUtils.OBJECT_SUPER_NAME;
    }
}
