package io.github.jinlongliao.easy.server.mapper.annotation.process;


import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @date 2022-12-26 14:13
 * @author: liaojinlong
 * @description: 处理注解 @io.github.jinlongliao.easy.server.mapper.annotation.GeneratorMethod
 **/
public class GeneratorMethodAnnotationProcessor extends AbstractGeneratorAnnotationProcessor {
    public static final String PATH = "META-INF/com/common/mapper/mapper/annotation/GeneratorMethod.dat";
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GeneratorMethod.class;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Set<TypeElement> typeElements = new HashSet<>();
        annotations.forEach(annotation -> {
            String className = "io.github.jinlongliao.easy.server.mapper.annotation.GeneratorMethod";
            boolean result = annotation.toString().equals(className);
            if (!result && annotation.getKind() == ElementKind.ANNOTATION_TYPE) {
                List<? extends AnnotationMirror> annotationMirrors = annotation.getAnnotationMirrors();
                boolean com = annotationMirrors.stream().anyMatch(tem -> tem.toString().equals("@" + className));
                if (com) {
                    typeElements.add(annotation);
                }
            } else if (result) {
                typeElements.add(annotation);
            }
        });

        if (typeElements.size() > 0) {
            Set<Element> set = new HashSet<>();
            for (TypeElement typeElement : typeElements) {
                set.addAll(roundEnv.getElementsAnnotatedWith(typeElement));
            }
            if (set.size() > 0) {
                try {
                    this.writeCache(set, roundEnv);
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
        return false;
    }

    private String parseMethod(Element element) {
        ExecutableElement executableElement = (ExecutableElement) element;
        StringBuilder builder = new StringBuilder(element.getEnclosingElement().toString().trim());
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
    protected void writeCache(
            Set<? extends Element> elementsAnnotatedWith,
            RoundEnvironment roundEnv) throws IOException {
        List<String> strings = elementsAnnotatedWith
                .stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
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


    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("*");
    }

}
