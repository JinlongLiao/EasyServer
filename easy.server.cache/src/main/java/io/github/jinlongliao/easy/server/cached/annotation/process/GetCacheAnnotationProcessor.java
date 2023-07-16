package io.github.jinlongliao.easy.server.cached.annotation.process;


import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.process.AbstractGeneratorAnnotationProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
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
 * @description: 处理注解 io.github.jinlongliao.easy.server.cached.annotation.GetCache
 **/
@SupportedAnnotationTypes("io.github.jinlongliao.easy.server.cached.annotation.GetCache")
public class GetCacheAnnotationProcessor extends AbstractGeneratorAnnotationProcessor {
    public static final String PATH = "META-INF/io/github/jinlongliao/easy/server/cached/annotation/GetCache.dat";

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GetCache.class;
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        super.process(annotations, roundEnv);
        return true;
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
