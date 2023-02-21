package io.github.jinlongliao.easy.server.mapper.annotation.process;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.util.Set;

public abstract class AbstractGeneratorAnnotationProcessor extends AbstractProcessor {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(getAnnotation());
        if (elementsAnnotatedWith.isEmpty()) {
            return false;
        }
        try {
            this.writeCache(elementsAnnotatedWith, roundEnv);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }

    public abstract Class<? extends Annotation> getAnnotation();


    protected abstract void writeCache(Set<? extends Element> elementsAnnotatedWith, RoundEnvironment roundEnv) throws IOException;

    protected abstract String getPath();

    protected OutputStream getOutputStream(Set<? extends Element> elementsAnnotatedWith) throws IOException {
        final FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "",
                getPath(), elementsAnnotatedWith.toArray(new Element[0]));
        return fileObject.openOutputStream();

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
