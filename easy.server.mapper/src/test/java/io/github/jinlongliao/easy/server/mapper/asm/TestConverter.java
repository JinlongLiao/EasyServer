package io.github.jinlongliao.easy.server.mapper.asm;


public class TestConverter {
    public static String testString(Object obj) {
        return "😁" + obj + "😁";
    }

    public static Object test(Object value, int index, String filedName, String typeName, boolean isParent) {
        System.out.println("value = " + value + ", index = " + index + ", filedName = " + filedName + ", typeName = " + typeName + ", isParent = " + isParent);
        if (String.class.getName().endsWith(typeName) && "d".endsWith(filedName)) {
            return "hihi 老六补充\n" + value;
        }
        return value;
    }
}
