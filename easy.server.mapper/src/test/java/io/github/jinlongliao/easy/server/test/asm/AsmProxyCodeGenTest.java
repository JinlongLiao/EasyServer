package io.github.jinlongliao.easy.server.test.asm;

import io.github.jinlongliao.easy.server.test.asm.servlet.MockHttpServletRequest;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.wrap.ICoreData2Object2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet.IServletData2Object;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet.ServletExtraClassMethodGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class AsmProxyCodeGenTest {
    static {
        System.setProperty("io.github.jinlongliao.commons.mapstruct.core.Proxy.debug", "true");
    }

    private AsmProxyCodeGenerator asmProxyCodeGen = new AsmProxyCodeGenerator();
    Map<String, Object> params = new HashMap<>(2);

    {
        params.put("a", "12345");
        params.put("b", "6789");
        params.put("c", "true");
        params.put("d", "你个老7⃣️");
        params.put("e", "996");
        params.put("f", "996");
        params.put("j", "996");
        params.put("i", "996");
        params.put("k", "996");
        params.put("l", "996");
        params.put("m", "996");
        params.put("n", "996");
    }

    @Test
    public void testOne() {
        ICoreData2Object2<A> proxyObject = asmProxyCodeGen.getProxyObject(A.class, false);
        A a = proxyObject.toMapConverter(params);
        System.out.println("a = \n" + a);
        A a2 = proxyObject.toArrayConverter(new Object[]{"098", "999", "true", "0000"});
        System.out.println("a2 = \n" + a2);
    }

    @Test
    public void testTwo() {
        ICoreData2Object2<B> proxyObject = asmProxyCodeGen.getProxyObject(B.class, true);
        A a = proxyObject.toMapConverter(params);
        System.out.println("b = \n" + a);
        A a2 = proxyObject.toArrayConverter(params.values().toArray());
        System.out.println("b2 = \n" + a2);
    }

    @Test
    public void testThree() {

        ICoreData2Object2<B> proxyObject = asmProxyCodeGen.getProxyObject(AsmProxyCodeGenerator.DEFAULT, B.class, true, 46, FiledValueConverter.class);
        A a = proxyObject.toMapConverter(params);
        System.out.println("b = \n" + a);

    }

    @Test
    public void testFour() {
        ServletExtraClassMethodGenerator classMethodCoreGenerator = new ServletExtraClassMethodGenerator();
        IServletData2Object<B> proxyObject = asmProxyCodeGen.getProxyObject(classMethodCoreGenerator, B.class, true, 46, null);
        MockHttpServletRequest request = new MockHttpServletRequest(params);
        B b = proxyObject.toHttpServletRequestConverter(request);
        System.out.println("b = \n" + b);

    }
}
