package io.github.jinlongliao.easy.server.cached;

import io.github.jinlongliao.easy.server.cached.aop.el.ParamElParserBuilder;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 将一个类的asm代码生成出来
 */
public class ASMPrint {
    @Test
    public void print() throws IOException {
        // (1) 设置参数
        String className = TestParamElParser.class.getName();
        //        String className = AsmCodegen.class.getName();
        int parsingOptions = ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG;
        boolean asmCode = true;

        // (2) 打印结果
        // ASMifier打印asm代码。Textifier打印字节码信息
        Printer printer = asmCode ? new ASMifier() : new Textifier();
        PrintWriter printWriter = new PrintWriter(System.out, true);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, printWriter);
        new ClassReader(className).accept(traceClassVisitor, parsingOptions);
    }

    public void testMethod0(A a) {

    }

    @Test
    public void testMethod() throws NoSuchMethodException {
        MapperStructConfig.setDev(true, "./target/", "./target/");
        A param = new A();
        param.setB(new B());
        param.setD("");
        ParamElParserBuilder.putElValue(new StringBuilder(), param, 0, ASMPrint.class.getDeclaredMethod("testMethod0", A.class), "a and b.b1 and b.b2 and c and d");
    }


}
