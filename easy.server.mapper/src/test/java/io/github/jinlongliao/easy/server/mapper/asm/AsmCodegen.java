package io.github.jinlongliao.easy.server.mapper.asm;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.IData2Object;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.InnerConverter;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.AsmProxyCodeGenerator.MAPPER_CLASS_LOADER;
import static org.objectweb.asm.Opcodes.*;

public class AsmCodegen {
    // @Test
    // public void codeGen() throws IOException {
    //     ClassWriter cw = new ClassWriter(0);
    //     cw.visit(V1_8, ACC_PUBLIC, "AsmCodegenClass", null, "io/github/jinlongliao/commons/mapstruct/core/IData2Object", null);
    //
    //     {
    //         MethodVisitor mv1 = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    //         mv1.visitCode();
    //         mv1.visitVarInsn(ALOAD, 0);
    //         mv1.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
    //         mv1.visitInsn(RETURN);
    //         mv1.visitMaxs(1, 1);
    //         mv1.visitEnd();
    //     }
    //     {
    //         MethodVisitor toMapConverter = cw.visitMethod(ACC_PUBLIC, "toMapConverter", "(Ljava/util/Map;)Ljava/lang/Object;", null, null);
    //         toMapConverter.visitCode();
    //
    //         toMapConverter.visitVarInsn(ALOAD, 1);
    //         toMapConverter.visitInsn(ARETURN);
    //         toMapConverter.visitMaxs(2, 2);
    //         toMapConverter.visitEnd();
    //     }
    //     cw.visitEnd();
    //     File file = new File("./target/AsmCodegenClass.class");
    //     file.createNewFile();
    //     Files.write(file.toPath(), cw.toByteArray());
    // }

    @Test
    public void codeGen2() throws Exception {
        Class ct = A.class;
        String owner = getProxyObjectName(ct);
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_7, ACC_PUBLIC, owner, null, "java/lang/Object", new String[]{CLassUtils.getJvmClass(IData2Object.class)});

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
            MethodVisitor method = cw.visitMethod(ACC_PUBLIC, "toMapConverter", "(Ljava/util/Map;)Ljava/lang/Object;", null, null);
            method.visitCode();
            //
            String data = A.class.getName().replace('.', '/');


            method.visitVarInsn(ALOAD, 1);
            method.visitLdcInsn("a");
            method.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            method.visitVarInsn(ASTORE, 2);

            method.visitVarInsn(ALOAD, 1);
            method.visitLdcInsn("b");
            method.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            method.visitVarInsn(ASTORE, 3);

            method.visitVarInsn(ALOAD, 1);
            method.visitLdcInsn("c");
            method.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            method.visitVarInsn(ASTORE, 4);
            method.visitVarInsn(ALOAD, 1);
            method.visitLdcInsn("d");
            method.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            method.visitVarInsn(ASTORE, 5);
            method.visitVarInsn(ALOAD, 1);
            method.visitLdcInsn("e");
            method.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            method.visitVarInsn(ASTORE, 6);


            method.visitTypeInsn(NEW, data);
            method.visitInsn(DUP);
            method.visitMethodInsn(INVOKESPECIAL, data, "<init>", "()V", false);

            method.visitVarInsn(ASTORE, 7);

            {
                //
                method.visitVarInsn(ALOAD, 7);
                method.visitVarInsn(ALOAD, 2);
                method.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                //
                method.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                method.visitMethodInsn(INVOKEVIRTUAL, data, "setA", "(I)V", false);

            }

            {
                //
                method.visitVarInsn(ALOAD, 7);
                method.visitVarInsn(ALOAD, 3);
                method.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                //
                method.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                method.visitMethodInsn(INVOKEVIRTUAL, data, "setB", "(I)V", false);

            }

            {
                //
                method.visitVarInsn(ALOAD, 7);
                method.visitVarInsn(ALOAD, 4);
                method.visitTypeInsn(CHECKCAST, "java/lang/Boolean");

                //
                method.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
                method.visitMethodInsn(INVOKEVIRTUAL, data, "setC", "(Z)V", false);

            }
            {

                method.visitVarInsn(ALOAD, 5);
                method.visitMethodInsn(INVOKESTATIC, CLassUtils.getJvmClass(InnerConverter.class), "getStr", "(Ljava/lang/Object;)Ljava/lang/String;", false);

                //
                method.visitVarInsn(ASTORE, 8);
                method.visitVarInsn(ALOAD, 7);
                method.visitVarInsn(ALOAD, 8);
                //
                method.visitMethodInsn(INVOKEVIRTUAL, data, "setD", "(Ljava/lang/String;)V", false);

            }
            {
                method.visitVarInsn(ALOAD, 7);

                method.visitVarInsn(ALOAD, 6);
                //
                method.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                method.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                //
                method.visitMethodInsn(INVOKEVIRTUAL, data, "setE", "(S)V", false);

            }


            method.visitVarInsn(ALOAD, 7);

            method.visitInsn(ARETURN);
            method.visitMaxs(2, 9);
            method.visitEnd();
        }
        {
            MethodVisitor method = cw.visitMethod(ACC_PUBLIC, "toArrayConverter", "([Ljava/lang/Object;)Ljava/lang/Object;", null, null);
            method.visitCode();


            String data = A.class.getName().replace('.', '/');
            //
            method.visitTypeInsn(NEW, data);
            method.visitInsn(DUP);
            method.visitMethodInsn(INVOKESPECIAL, data, "<init>", "()V", false);

            method.visitVarInsn(ASTORE, 2);
            method.visitVarInsn(ALOAD, 2);


            method.visitInsn(ARETURN);
            method.visitMaxs(4, 4);
            method.visitEnd();
        }
        {
            MethodVisitor method = cw.visitMethod(ACC_PUBLIC, "toHttpServletRequestConverter", "(Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/Object;", null, null);
            method.visitCode();


            String data = A.class.getName().replace('.', '/');

            //
            method.visitTypeInsn(NEW, data);
            method.visitInsn(DUP);
            method.visitMethodInsn(INVOKESPECIAL, data, "<init>", "()V", false);

            method.visitVarInsn(ASTORE, 2);
            method.visitVarInsn(ALOAD, 2);

            method.visitInsn(ARETURN);
            method.visitMaxs(4, 4);
            method.visitEnd();
        }
        cw.visitEnd();


        byte[] bytes = cw.toByteArray();

        Class aClass = MAPPER_CLASS_LOADER.reLoadClassBySupeClassloader(owner.replace('/', '.'), bytes);
        Object o1 = aClass.getConstructor().newInstance();
        IData2Object iData2Object = (IData2Object) o1;
        Map<String, Object> params = new HashMap<>(8);
        params.put("a", 243243);
        params.put("b", 243243);
        params.put("c", true);
        params.put("d", "你个老六");
        params.put("e", 996);

        Object o = iData2Object.toMapConverter(params);
        System.out.println("o = " + o);


        String[] split = owner.split("/");
        File file = new File("./target/" + split[split.length - 1] + ".class");
        file.createNewFile();
        Files.write(file.toPath(), bytes);
    }

    /**
     * 类名
     *
     * @param tClass
     * @return 实现类名
     */
    private String getProxyObjectName(Class tClass) {
        return tClass.getPackage().getName().replace('.', '/') + "/" + tClass.getSimpleName() + "Data2ObjectImpl"
                // + System.currentTimeMillis();
                ;
    }

    public Integer dyudyu(Map<String, Object> param) {
        Object a = param.get("a");
        Object b = param.get("b");
        Object c = param.get("c");
        Object d = param.get("d");
        Object e = param.get("e");
        A ac = new A();
        ac.setA((Integer) a);
        ac.setB((Integer) b);
        ac.setC((Boolean) c);
        String str = InnerConverter.getStr(d);
        ac.setD(str);
        Integer int2 = InnerConverter.getInt2(e);
        return null;
    }

    public Integer dyudyu(Object[] param) {
        Object a = param[0];
        Object b = param[1];
        Object c = param[2];
        Object d = param[3];
        Object e = param[4];
        Object f = param[5];
        Object i = param[6];
        Object j = param[7];
        Object k = param[8];

        A ac = new A();
        ac.setA((Integer) a);
        ac.setB((Integer) b);
        ac.setC((Boolean) c);
        String str = InnerConverter.getStr(d);
        ac.setD(str);
        Integer int2 = InnerConverter.getInt2(e);
        return null;
    }

    public void dyudyu2(Object[] param) {
        int len = param == null ? 0 : param.length;
        int index = 0;
        boolean b1 = len > index++;
        Object a;
        if (b1) {
            a = param[0];
        } else {
            a = null;
        }
        b1 = len > index++;
        Object b;
        if (b1) {
            b = param[1];
        } else {
            b = null;
        }
        b1 = len > index++;
        Object c;
        if (b1) {
            c = param[2];
        } else {
            c = null;
        }
        b1 = len > index++;
        Object d;
        if (b1) {
            d = param[3];
        } else {
            d = null;
        }
        b1 = len > index++;
        Object e;
        if (b1) {
            e = param[4];
        } else {
            e = null;
        }

    }

    public Object tet(Object[] o, int num) {
        boolean a = true;
        String s = "99999";
        int le;
        if (o == null) {
            le = 0;
        } else {
            le = o.length;
        }
        Object res;
        if (le > num) {
            return 88;
        } else {
            return null;
        }
    }

    public Object httpServlet(HttpServletRequest request) {
        String a = request.getParameter("a");
        String b = request.getParameter("b");

        return null;
    }

}
