package io.github.jinlongliao.easy.server.mapper.annotation.process;

import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.spring.BeanMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;

/**
 * 反向加载  GeneratorCopy
 *
 * @date 2022-12-29 12:34
 * @author: liaojinlong
 * @description: /
 **/

public class GeneratorCopyLoaderGenerator implements LoaderGenerator {

    private static final BeanMapperFactory BEAN_MAPPER_FACTORY = new BeanMapperFactory(false);
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public void loader(Object... args) throws IOException {
        List<String> strings = GeneratorHelper.loadAllConfigClass(GeneratorCopyAnnotationProcessor.PATH);
        strings.forEach(string -> {
            String[] split = string.split(";");
            this.toHandlerGenerator(split[0], Boolean.parseBoolean(split[1]), Boolean.parseBoolean(split[2]), Boolean.parseBoolean(split[3]));
        });

    }

    /**
     * 处理配置的注解
     *
     * @param className  类名
     * @param genMap     是否生成Map Copy
     * @param genArray   是否生成Array Copy
     * @param genServlet 是否生成Servlet  Copy
     */
    private void toHandlerGenerator(String className, boolean genMap, boolean genArray, boolean genServlet) {
        Class<?> aClass;
        try {
            aClass = AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(className);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return;
        }
        if (genArray) {
            BEAN_MAPPER_FACTORY.common(aClass, BeanMapperFactory.ARRAY_CLASS_METHOD_CORE_GENERATOR, new HashMap<>(2, 1L), false);
        }
        if (genMap) {
            BEAN_MAPPER_FACTORY.common(aClass, BeanMapperFactory.MAP_CLASS_METHOD_CORE_GENERATOR, new HashMap<>(2, 1L), false);
        }
        if (genServlet) {
            BEAN_MAPPER_FACTORY.common(aClass, BeanMapperFactory.SERVLET_EXTRA_CLASS_METHOD_GENERATOR, new HashMap<>(2, 1L), false);
        }
    }
}
