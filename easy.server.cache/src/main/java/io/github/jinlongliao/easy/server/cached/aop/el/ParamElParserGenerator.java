package io.github.jinlongliao.easy.server.cached.aop.el;

import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator.JAVA_DEF_VERSION;
import static io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator.MAPPER_CLASS_LOADER;
import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;

/**
 * param el 解析
 *
 * @author: liaojinlong
 * @date: 2023/7/5 17:01
 */
public class ParamElParserGenerator {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Map<String, ParamElParser> cache = new ConcurrentHashMap<>(16);
    private static final Class<?>[] interfaces = new Class[]{ParamElParser.class};

    static ParamElParser build0(String el, List<String[]> elList, Method method, Object param) {
        String proxyObjectName = buildProxyClassName(method, el);
        try {
            String dynamicClassName = proxyObjectName.replace('/', '.');
            Class<?> loadClass = MAPPER_CLASS_LOADER.loadClass(dynamicClassName);
            return (ParamElParser) loadClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(JAVA_DEF_VERSION, ACC_PUBLIC, proxyObjectName,
                null,
                CLassUtils.OBJECT_SUPER_NAME,
                Arrays.stream(interfaces).map(CLassUtils::getJvmClass).toArray(String[]::new));
        MethodVisitor construct = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        construct.visitCode();
        construct.visitVarInsn(ALOAD, 0);
        construct.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        construct.visitInsn(RETURN);
        construct.visitMaxs(3, 1);
        construct.visitEnd();





        byte[] classes = classWriter.toByteArray();

        MapperStructConfig.saveClassFile(classes, proxyObjectName);
        return MAPPER_CLASS_LOADER.reLoadInstance(proxyObjectName, classes);


    }

    private static String buildProxyClassName(Method method, String el) {
        StringBuilder builder = new StringBuilder("com.wlzn.web")
                .append(method.getDeclaringClass().getName())
                .append("/")
                .append(method.getName());
        boolean upper = true;
        for (char c : el.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                upper = true;
            } else {
                if (upper) {
                    builder.append(Character.isUpperCase(c));
                } else {
                    builder.append((c));
                }
                upper = false;
            }
        }
        return builder.toString().replace('.', '/');
    }
}
