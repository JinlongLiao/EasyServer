package io.github.jinlongliao.easy.server.cached.annotation.process;


import io.github.jinlongliao.easy.server.cached.annotation.GetCache;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;


/**
 * @date 2022-12-26 14:13
 * @author: liaojinlong
 * @description: 处理注解 io.github.jinlongliao.easy.server.cached.annotation.GetCache
 **/
@SupportedAnnotationTypes("io.github.jinlongliao.easy.server.cached.annotation.GetCache")
public class GetCacheAnnotationProcessor extends SimpleGetCacheAnnotationProcessor {
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
    protected String parseMethod(Element element) {
        GetCache annotation = element.getAnnotation(GetCache.class);
        return parseMethod(element, annotation.argsIndex(), annotation.keyValueEl());
    }

    @Override
    protected String getPath() {
        return PATH;
    }
}
