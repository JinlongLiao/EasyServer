package io.github.jinlongliao.easy.server.cached.annotation.process;


import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache;
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.process.AbstractGeneratorAnnotationProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @date 2022-12-26 14:13
 * @author: liaojinlong
 * @description: 处理注解 @io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache
 **/
@SupportedAnnotationTypes("io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache")
public class SimpleGetCacheAnnotationProcessor extends AbstractGeneratorAnnotationProcessor {
    public static final String PATH = "META-INF/io/github/jinlongliao/easy/server/cached/annotation/SimpleGetCache.dat";

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return SimpleGetCache.class;
    }

    @Override
    protected void writeCache(Set<? extends Element> elementsAnnotatedWith, RoundEnvironment roundEnv) throws IOException {
        List<String> strings = elementsAnnotatedWith.stream().map(Element::toString).collect(Collectors.toList());
        OutputStream outputStream = null;
        try {
            outputStream = getOutputStream(elementsAnnotatedWith);
            if (Objects.nonNull(outputStream)) {
                GeneratorHelper.writeToFile(outputStream, strings);
            }
        } finally {
            if (Objects.nonNull(outputStream)) {
                outputStream.close();
            }
        }
    }

    @Override
    protected String getPath() {
        return PATH;
    }
}
