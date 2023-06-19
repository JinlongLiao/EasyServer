package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.ExtraFieldConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.ClassMethodCoreGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.IData2Object2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.wrap.ICoreData2Object2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.wrap.WrapClassMethodCoreGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.loader.MapperClassLoader;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.ACC_PUBLIC;

/**
 * @author: liaojinlong
 * @date: 2022/5/21 21:17
 */
public class AsmProxyCodeGenerator {
    private static final Logger log = LoggerFactory.getLogger(AsmProxyCodeGenerator.class);
    public static final ClassMethodCoreGenerator DEFAULT = new WrapClassMethodCoreGenerator();

    public static final MapperClassLoader MAPPER_CLASS_LOADER = new MapperClassLoader();
    public static final int JAVA_DEF_VERSION = ClassVersion.CLASS_VERSION.version();

    /**
     * 生成代理对象
     *
     * @return io.github.jinlonghliao.commons.mapstruct.IData2Object
     */
    public <T> ICoreData2Object2<T> getProxyObject(Class<T> tClass) {
        return this.getProxyObject(DEFAULT, tClass, false, JAVA_DEF_VERSION, null);
    }

    public <T> ICoreData2Object2<T> getProxyObject(Class<T> tClass, boolean searchParentField) {
        return this.getProxyObject(DEFAULT, tClass, searchParentField, JAVA_DEF_VERSION, null);
    }

    public <T> ICoreData2Object2<T> getProxyObject(Class<T> tClass, boolean searchParentField, int javaVersion) {
        return this.getProxyObject(DEFAULT, tClass, searchParentField, javaVersion, null);
    }

    /**
     * 生成代理对象
     *
     * @param tClass
     * @param searchParentField
     * @return io.github.jinlonghliao.commons.mapstruct.IData2Object
     */
    public <T, C extends IData2Object2<T>> C getProxyObject(ClassMethodCoreGenerator classMethodCoreGenerator,
                                                            Class<T> tClass,
                                                            boolean searchParentField,
                                                            int javaVersion,
                                                            Class<? extends ExtraFieldConverter> filedValueConverter) {
        Class<?>[] interfaces = classMethodCoreGenerator.getInterfaces();
        String proxyObjectName = getProxyObjectName(tClass, interfaces);
        try {
            String dynamicClassName = proxyObjectName.replace('/', '.');
            Class<C> loadClass = (Class<C>) AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(dynamicClassName);
            return loadClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(javaVersion, ACC_PUBLIC, proxyObjectName, null, classMethodCoreGenerator.getSuperName(), Arrays.stream(interfaces).map(CLassUtils::getJvmClass).toArray(String[]::new));
        classMethodCoreGenerator.initConstruct(cw, proxyObjectName, searchParentField, filedValueConverter);
        classMethodCoreGenerator.initMethod(cw,
                proxyObjectName,
                tClass,
                searchParentField,
                javaVersion,
                filedValueConverter);
        byte[] classes = cw.toByteArray();

        MapperStructConfig.saveClassFile(classes, proxyObjectName);
        return MAPPER_CLASS_LOADER.reLoadInstance(proxyObjectName, classes);
        // return mapperClassLoader.reLoadInstance(getClass().getClassLoader(), proxyObjectName, classes);
    }


    /**
     * 类名
     *
     * @param tClass
     * @param interfaces
     * @return 实现类名
     */
    private String getProxyObjectName(Class tClass, Class<?>[] interfaces) {
        String className;
        if (interfaces.length < 1) {
            className = tClass.getSimpleName() + "Data2ObjectImpl2Dynamic";
        } else {
            className = tClass.getSimpleName() + interfaces[0].getSimpleName().substring(1) + "Dynamic";
        }
        return tClass.getPackage().getName().replace('.', '/') +
                "/dynamic/" +
                "mapstruct2/"
                + className;
    }
}
