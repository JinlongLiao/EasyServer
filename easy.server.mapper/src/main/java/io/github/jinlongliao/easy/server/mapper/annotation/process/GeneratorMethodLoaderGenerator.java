package io.github.jinlongliao.easy.server.mapper.annotation.process;

import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 反向加载  GeneratorCopy
 *
 * @date 2022-12-29 12:34
 * @author: liaojinlong
 * @description: /
 **/

public class GeneratorMethodLoaderGenerator implements LoaderGenerator {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public void loader(Object... args) throws IOException {
        List<String> strings = GeneratorHelper.loadAllConfigClass(GeneratorMethodAnnotationProcessor.PATH);
        strings.forEach(this::configLoader);

    }

    protected void configLoader(String conf) {
        String[] split = conf.split("_:_");
        String className = split[0];
        String methodName = split[1];

        try {
            Class<?> forName = AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(className);
            List<Class<?>> paramClass;
            if (split.length == 2) {
                paramClass = Collections.emptyList();
            } else {
                paramClass = this.parseParamClass(split[2]);
            }
            Method method = forName.getDeclaredMethod(methodName, paramClass.toArray(new Class[0]));
            DirectMethod.valueOf(method);
        } catch (
                Exception e) {
            log.warn(e.getMessage(), e);
        }

    }

    protected List<Class<?>> parseParamClass(String paramConf) throws ClassNotFoundException {
        if (StringUtil.isEmpty(paramConf)) {
            return Collections.emptyList();
        }
        List<Class<?>> paramClasses = new ArrayList<>();

        String[] paramStrs = paramConf.split("@");
        for (String paramStr : paramStrs) {
            String classType = paramStr.split("__")[0].split("<")[0];
            try {
                paramClasses.add(CLassUtils.parseClassType(classType));
            } catch (ClassNotFoundException e) {
                log.warn("paramConf:{} classType: {}", paramConf, classType);
                throw e;
            }
        }
        return paramClasses;
    }

}
