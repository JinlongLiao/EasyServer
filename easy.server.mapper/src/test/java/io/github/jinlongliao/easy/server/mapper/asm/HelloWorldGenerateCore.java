package io.github.jinlongliao.easy.server.mapper.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.nio.file.Files;

import static org.objectweb.asm.Opcodes.*;

public class HelloWorldGenerateCore {
    public static void main(String[] args) throws Exception {
        String relative_path = "sample/HelloWorld.class";

        // (1) 生成byte[]内容
        byte[] bytes = dump();
        File file = new File("./target/HelloWorld.class");
        file.createNewFile();
        Files.write(file.toPath(), bytes);
    }

    public static byte[] dump() throws Exception {
        // (1) 创建ClassWriter对象
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // (2) 调用visitXxx()方法
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, "sample/HelloWorld", null, "java/lang/Object", null);

        {
            MethodVisitor mv1 = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv1.visitCode();
            mv1.visitVarInsn(ALOAD, 0);
            mv1.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv1.visitInsn(RETURN);
            mv1.visitMaxs(1, 1);
            mv1.visitEnd();
        }

        {
            MethodVisitor mv2 = cw.visitMethod(ACC_PUBLIC, "test", "()Lsample/GoodChild", null, null);
            mv2.visitCode();
            mv2.visitTypeInsn(NEW, "sample/GoodChild");
            mv2.visitInsn(DUP);
            mv2.visitLdcInsn("Lucy");
            mv2.visitIntInsn(BIPUSH, 8);
            mv2.visitMethodInsn(INVOKESPECIAL, "sample/GoodChild", "<init>", "(Ljava/lang/String;I)V", false);
            mv2.visitVarInsn(ASTORE, 1);
            mv2.visitVarInsn(ALOAD, 1);
            mv2.visitInsn(ARETURN);
            mv2.visitMaxs(2, 2);
            mv2.visitEnd();
        }

        cw.visitEnd();

        // (3) 调用toByteArray()方法
        return cw.toByteArray();
    }
}
