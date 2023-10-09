package io.github.jinlongliao.easy.server.cached.annotation.process;

import io.github.jinlongliao.easy.server.cached.aop.el.ParamElParserBuilder;
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.process.GeneratorMethodLoaderGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 反向加载  GetCache
 *
 * @author: liaojinlong
 * @date: 2023/7/16 15:24
 */
public class GetCacheLoaderGenerator extends GeneratorMethodLoaderGenerator {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void loader(Object... args) throws IOException {
        List<String> get = GeneratorHelper.loadAllConfigClass(GetCacheAnnotationProcessor.PATH);
        List<String> simpleGet = GeneratorHelper.loadAllConfigClass(SimpleGetCacheAnnotationProcessor.PATH);
        get.forEach(this::configLoader);
        simpleGet.forEach(this::configLoader);
    }

    @Override
    protected void configLoader(String conf) {
        String[] split = conf.split("_:_");
        String elStr = split[0];
        if (elStr.isEmpty()) {
            return;
        }
        String className = split[2];
        String methodName = split[3];
        try {
            Class<?> forName = AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(className);
            List<Class<?>> paramClass = this.parseParamClass(split[4]);
            Method method = forName.getDeclaredMethod(methodName, paramClass.toArray(new Class[0]));
            ParamElParserBuilder.buildValue(method, elStr);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

    }

}
