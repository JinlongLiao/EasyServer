package io.github.jinlongliao.easy.server.boot.demo.test;


import groovyjarjarasm.asm.ClassReader;
import groovyjarjarasm.asm.util.ASMifier;
import groovyjarjarasm.asm.util.Printer;
import groovyjarjarasm.asm.util.Textifier;
import groovyjarjarasm.asm.util.TraceClassVisitor;
import org.junit.Test;


import java.io.IOException;
import java.io.PrintWriter;

/**
 * 将一个类的asm代码生成出来
 */
public class ASMPrint {
    @Test
    public void print() throws IOException {
        // (1) 设置参数
        String className = NewUserId.class.getName();
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
}
