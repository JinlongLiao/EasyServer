package io.github.jinlongliao.easy.server.extend.annotation.process;

import io.github.jinlongliao.easy.server.extend.parser.StaticRequestParseRule;
import io.github.jinlongliao.easy.server.extend.response.proxy.ProxyResponseFactory;
import io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator;
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.process.GeneratorMethodAnnotationProcessor;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.core.core.MethodInfo;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * 反向加载  GeneratorCopy
 *
 * @date 2022-12-29 12:34
 * @author: liaojinlong
 * @description: /
 **/

public class GeneratorResponseLoaderGenerator implements LoaderGenerator {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final MethodParse methodParse = new MethodParse();

    @Override
    public void loader(Object... args) throws IOException {
        List<String> strings = GeneratorHelper.loadAllConfigClass(GeneratorResponseAnnotationProcessor.PATH);
        strings.forEach(this::toHandlerGenerator);
        List<String> logicConf = GeneratorHelper.loadAllConfigClass(GeneratorMethodAnnotationProcessor.PATH);
        Set<String> classNames = new HashSet<>();
        logicConf.forEach(conf -> this.parseLogicRequest(conf, classNames));
        this.generatorRequest(classNames);
    }

    private void generatorRequest(Set<String> classNames) {
        for (String className : classNames) {
            try {
                Class<?> aClass = AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(className);
                Map<String, MethodInfo> logic = methodParse.getLogic(aClass);
                for (MethodInfo methodInfo : logic.values()) {
                    LogicModel logicModel = new LogicModel(null,
                            methodInfo.getDirectMethod(),
                            methodInfo.getMsgModels(),
                            methodInfo.getDesc(),
                            "beanName");
                    logicModel.setSourceClass(aClass);
                    new StaticRequestParseRule(logicModel);
                }
            } catch (ClassNotFoundException e) {
                log.warn(e.getMessage(), e);

            }
        }
    }

    private void parseLogicRequest(String conf, Set<String> classNames) {
        classNames.add(conf.split("_:_")[0]);
    }

    /**
     * 处理配置的注解
     *
     * @param className 类名
     */
    private void toHandlerGenerator(String className) {
        try {
            Class<? extends ICommonResponse> aClass = (Class<? extends ICommonResponse>) AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(className);
            Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
            Constructor<?> declaredConstructor = declaredConstructors[0];
            Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
            ICommonResponse response = (ICommonResponse) declaredConstructor.newInstance(Arrays.stream(parameterTypes).map(CLassUtils::getDefaultValue).toArray());
            ProxyResponseFactory.getProxyResponse(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
