package io.github.jinlongliao.easy.server.extend.annotation.process;


import io.github.jinlongliao.easy.server.mapper.annotation.process.AbstractGeneratorAnnotationProcessor;
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.extend.annotation.GeneratorResponse;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import java.io.*;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @date 2022-12-26 14:13
 * @author: liaojinlong
 * @description: 处理注解 @io.github.jinlongliao.easy.server.extend.annotation.GeneratorSource
 **/
@SupportedAnnotationTypes("io.github.jinlongliao.easy.server.extend.annotation.GeneratorResponse")
public class GeneratorResponseAnnotationProcessor extends AbstractGeneratorAnnotationProcessor {
    public static final String PATH = "META-INF/io/github/jinlongliao/easy/server/extend/annotation/GeneratorResponse.dat";

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GeneratorResponse.class;
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
