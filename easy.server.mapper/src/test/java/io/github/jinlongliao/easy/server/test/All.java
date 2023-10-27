package io.github.jinlongliao.easy.server.test;

import io.github.jinlongliao.easy.server.test.asm.B;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

public class All {
    static {
        MapperStructConfig.setDev(true, "./target/test-classes/", "./target/generated-test-sources/test-classes/");
    }

    @Test
    public void test1() {
        AsmProxyCodeGenerator asmProxyCodeGen = new AsmProxyCodeGenerator();
         int count = 1;
        long l1 = System.nanoTime();

        long l2 = System.nanoTime();
        int size2 = count;
        while (size2-- > 0) {
            asmProxyCodeGen.getProxyObject(B.class, true, Opcodes.V1_7);
        }
        long l3 = System.nanoTime();
        long asm = l3 - l2;
        long javassist = l2 - l1;
        System.out.println("asm = " + asm);
        System.out.println("javassist= " + javassist);
        System.out.println("javassist%asm " + (javassist * 1D) / asm);

    }
}
