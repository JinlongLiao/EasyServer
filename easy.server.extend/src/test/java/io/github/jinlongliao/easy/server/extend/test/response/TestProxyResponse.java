package io.github.jinlongliao.easy.server.extend.test.response;

import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory;
import io.github.jinlongliao.easy.server.extend.response.proxy.InnerWriteResponseProxyMethod;
import io.github.jinlongliao.easy.server.extend.response.proxy.ProxyResponse;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

public class TestProxyResponse implements ProxyResponse {
    @Override
    public byte[] genResHex(IResponseStreamFactory factory, ICommonResponse response) {
        TestCommonResponse testResponse = (TestCommonResponse) response;
        InnerWriteResponseProxyMethod.writeInt(factory, testResponse.getRestFreeNum());
        InnerWriteResponseProxyMethod.writeString(factory, "", testResponse.getRestFreeNum());
        return factory.toBytes();
    }

    public byte[] genResHex2(IResponseStreamFactory factory, ICommonResponse response) {
        TestCommonResponse2 testResponse = (TestCommonResponse2) response;
        InnerWriteResponseProxyMethod.writeInt(factory, testResponse.getRestFreeNum());
        InnerWriteResponseProxyMethod.writeString(factory, testResponse.getDynamicStr(), 20);
        return factory.toBytes();
    }

    @Test
    public void print() throws IOException {
        // (1) 设置参数
        String className = TestRequestParseRule.class.getName();
        //        String className = AsmCodegen.class.getName();
        int parsingOptions = ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG;
        boolean asmCode = true;

        // (2) 打印结果
        // ASMifier打印asm代码。Textifier打印字节码信息
        Printer printer = new ASMifier();
        PrintWriter printWriter = new PrintWriter(System.out, true);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, printWriter);
        new ClassReader(className).accept(traceClassVisitor, parsingOptions);
    }
}
