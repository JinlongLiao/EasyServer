//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.jinlongliao.easy.server.mapper.asm;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.InnerConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.IData2Object2;

import java.util.Map;

public class ACData2ObjectImpl2 implements IData2Object2 {
    public ACData2ObjectImpl2() {
    }

    public Object toMapConverter(Map var1) {
        Object var2 = var1.get("a");
        Object var3 = var1.get("b");
        Object var4 = var1.get("c");
        Object var5 = var1.get("d");
        Object var6 = var1.get("e");
        Object var7 = var1.get("f");
        Object var8 = var1.get("i");
        Object var9 = var1.get("j");
        Object var10 = var1.get("k");
        Object var11 = var1.get("l");
        A var12 = new A();
        var12.setA(InnerConverter.getInt(var2));
        var12.setB(InnerConverter.getInt(var3));
        var12.setC(InnerConverter.getBoolean(var4));
        var12.setD(InnerConverter.getStr(var5));
        var12.setE(InnerConverter.getShort(var6));
        var12.setF(InnerConverter.getShort(var7));
        var12.setI(InnerConverter.getShort(var8));
        var12.setJ(InnerConverter.getShort(var9));
        var12.setK(InnerConverter.getShort(var10));
        var12.setL(InnerConverter.getShort(var11));
        return var12;
    }

    public Object toArrayConverter(Object[] var1) {
        int maxLen = var1 == null ? 0 : var1.length;
        Object var2 = maxLen > 0 ? var1[0] : null;
        Object var3 = maxLen > 1 ? var1[1] : null;
        Object var4 = maxLen > 2 ? var1[2] : null;
        Object var5 = maxLen > 3 ? var1[3] : null;
        Object var6 = maxLen > 4 ? var1[4] : null;
        Object var7 = maxLen > 5 ? var1[5] : null;
        Object var8 = maxLen > 6 ? var1[6] : null;
        Object var9 = maxLen > 7 ? var1[7] : null;
        Object var10 = maxLen > 8 ? var1[8] : null;
        Object var11 = maxLen > 9 ? var1[9] : null;
        A var12 = new A();
        var12.setA(InnerConverter.getInt(var2));
        var12.setB(InnerConverter.getInt(var3));
        var12.setC(InnerConverter.getBoolean(var4));
        var12.setD(InnerConverter.getStr(var5));
        var12.setE(InnerConverter.getShort(var6));
        var12.setF(InnerConverter.getShort(var7));
        var12.setI(InnerConverter.getShort(var8));
        var12.setJ(InnerConverter.getShort(var9));
        var12.setK(InnerConverter.getShort(var10));
        var12.setL(InnerConverter.getShort(var11));
        return var12;
    }
}
