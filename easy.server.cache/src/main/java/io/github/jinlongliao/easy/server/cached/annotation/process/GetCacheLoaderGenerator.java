package io.github.jinlongliao.easy.server.cached.annotation.process;

import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 反向加载  GeneratorCopy
 *
 * @date 2022-12-29 12:34
 * @author: liaojinlong
 * @description: /
 **/

public class GetCacheLoaderGenerator implements LoaderGenerator {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void loader(Object... args) throws IOException {
        List<String> get = GeneratorHelper.loadAllConfigClass(GetCacheAnnotationProcessor.PATH);
        List<String> simpleGet = GeneratorHelper.loadAllConfigClass(SimpleGetCacheAnnotationProcessor.PATH);

    }

}
