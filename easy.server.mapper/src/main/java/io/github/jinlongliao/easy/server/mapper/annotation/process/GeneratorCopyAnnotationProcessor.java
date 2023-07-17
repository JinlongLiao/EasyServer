package io.github.jinlongliao.easy.server.mapper.annotation.process;


import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorCopy;
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @date 2022-12-26 14:13
 * @author: liaojinlong
 * @description: 处理注解 @io.github.jinlongliao.easy.server.mapper.annotation.GeneratorCopy
 **/
@SupportedAnnotationTypes("io.github.jinlongliao.easy.server.mapper.annotation.GeneratorCopy")
public class GeneratorCopyAnnotationProcessor extends AbstractGeneratorAnnotationProcessor {
    public static final String PATH = "META-INF/io/github/jinlongliao/easy/server/mapper/annotation/GeneratorCopy.dat";
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GeneratorCopy.class;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        super.process(annotations, roundEnv);
        return true;
    }

    @Override
    protected void writeCache(Set<? extends Element> elementsAnnotatedWith, RoundEnvironment roundEnv) throws IOException {

        List<String> strings = elementsAnnotatedWith.stream().map(element -> {
            GeneratorCopy annotation = element.getAnnotation(GeneratorCopy.class);
            return element + ";" + annotation.genMap() + ";" + annotation.genArray() + ";" + annotation.genServlet();
        }).collect(Collectors.toList());
        if (strings.size() == 0) {
            return;
        }
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
