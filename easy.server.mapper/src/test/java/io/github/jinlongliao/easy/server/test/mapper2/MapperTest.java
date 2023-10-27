package io.github.jinlongliao.easy.server.test.mapper2;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.BeanCopierUtils;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.wrap.ICoreData2Object2;
import org.junit.Test;

import java.util.Date;

public class MapperTest {
    @Test
    public void mapperTest() {
        ICoreData2Object2<Mapper> data2Object = BeanCopierUtils.getData2Object(Mapper.class);
        byte b = 1;
        boolean bool = false;
        char c = 65;
        short s = 22;
        int i = 3423;
        long l = 3423424;
        float f = 1022f;
        double d = 1.84968;
        Date date = new Date();
        Mapper mapper = data2Object.toArrayConverter(new Object[]{b, b, bool, bool, c, c, s, s, i, i, l, l, f, f, d, d,date});
        System.out.println("mapper = " + mapper);

    }
}
