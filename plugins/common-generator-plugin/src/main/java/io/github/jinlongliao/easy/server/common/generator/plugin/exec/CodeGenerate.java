package io.github.jinlongliao.easy.server.common.generator.plugin.exec;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

/**
 * @date 2022-12-29 11:34
 * @author: liaojinlong
 * @description: 用于生成代码
 **/

public class CodeGenerate {
    private static String classPath = "./target/classes/";
    private static String javaPath = "./target/generated-sources/annotations/";

    public static void run(String[] args, ClassLoader classLoader) {
        if (args.length == 2) {
            classPath = args[0];
            javaPath = args[1];
        }

        System.out.println("classPath = " + classPath);
        System.out.println("javaPath = " + javaPath);
        try {
            Class<?> mapperStructConfigClass = Class.forName("io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig", true, classLoader);
            MethodHandle setDev = MethodHandles.lookup().findStatic(mapperStructConfigClass, "setDev", MethodType.methodType(void.class, boolean.class, String.class, String.class));
            setDev.invoke(true, classPath, javaPath);
            Class<?> generatorHelperClass = Class.forName("io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper", true, classLoader);
            MethodHandle generatorResource = MethodHandles.lookup().findStatic(generatorHelperClass, "generatorResource", MethodType.methodType(void.class, Object[].class));
            generatorResource.invoke(Arrays.stream(args).toArray());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
