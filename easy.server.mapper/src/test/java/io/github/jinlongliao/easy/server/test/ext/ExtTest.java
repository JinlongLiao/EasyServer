package io.github.jinlongliao.easy.server.test.ext;


import io.github.jinlongliao.easy.server.mapper.core.mapstruct.BeanCopierUtils;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import org.junit.Test;

public class ExtTest {
    static {
        MapperStructConfig.setDev(true, "./target/test-classes/", "./target/generated-test-sources/annotations/");
    }

    private Object[] data = new Object[]{new A(true), false};

    @Test
    @org.junit.Ignore
    public void convertTest() {
        B b = BeanCopierUtils.getData2Object(B.class).toArrayConverter(data);
        System.out.println(b);
    }

    // {
    //     io.github.jinlongliao.commons.mapstruct.ext.B tmp = new io.github.jinlongliao.commons.mapstruct.ext.B();
    //     tmp.setA(((io.github.jinlongliao.commons.mapstruct.ext.A) $1.get("a")));
    //     tmp.setB(io.github.jinlongliao.commons.mapstruct.core.InnerCoreDataConverter.getInt($1.get("b")));
    //     return tmp;
    // }
}
