package io.github.jinlongliao.easy.server.cached.annotation.process;


import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache;
 import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.process.AbstractGeneratorAnnotationProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
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
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        super.process(annotations, roundEnv);
        return true;
    }

    @Override
    protected void writeCache(Set<? extends Element> elementsAnnotatedWith, RoundEnvironment roundEnv) throws IOException {

        List<String> strings = elementsAnnotatedWith
                .stream()
                .map(this::parseMethod)
                .collect(Collectors.toList());
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

    protected String parseMethod(Element element) {
        SimpleGetCache annotation = element.getAnnotation(SimpleGetCache.class);
        return parseMethod(element, annotation.argsIndex(), annotation.keyValueEl());
    }

    protected String parseMethod(Element element, int argIndex, String el) {
        ExecutableElement executableElement = (ExecutableElement) element;
        StringBuilder builder = new StringBuilder(el);
        builder.append("_:_");
        builder.append(argIndex);
        builder.append("_:_");
        builder.append(element.getEnclosingElement().toString().trim());
        builder.append("_:_");
        builder.append(executableElement.getSimpleName().toString().trim());
        builder.append("_:_");
        List<? extends VariableElement> parameters = executableElement.getParameters();
        int size = parameters.size();
        int index = 0;
        for (VariableElement parameter : parameters) {
            String str = parameter.asType().toString().trim();
            String[] split = str.split(" ");
            str = split[split.length - 1].trim();
            str = str.replace(")", "");
            builder.append(str);
            builder.append("__");
            builder.append(parameter.toString().trim());
            if (size > ++index) {
                builder.append("@");
            }
        }
        return builder.toString();
    }

    @Override
    protected String getPath() {
        return PATH;
    }
}
