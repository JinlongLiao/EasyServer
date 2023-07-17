// //
// // Source code recreated from a .class file by IntelliJ IDEA
// // (powered by FernFlower decompiler)
// //
//
// package io.github.jinlongliao.commons.mapstruct.asm;
//
// import io.github.jinlongliao.commons.mapstruct.core.IData2Object;
// import java.util.Map;
// import jakarta.servlet.http.HttpServletRequest;
//
// public class ACData2ObjectImpl implements IData2Object {
//     public Object toMapConverter(Map var1) {
//         Object var2 = var1.get("a");
//         AC var3 = new AC();
//         var3.setA((Integer) var2);
//         return var3;
//     }
//
//     public Object toArrayConverter(Object[] var1) {
//         AC var2 = new AC();
//         return var2;
//     }
//
//     public Object toHttpServletRequestConverter(HttpServletRequest var1) {
//         AC var2 = new AC();
//         return var2;
//     }
// }
