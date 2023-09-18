
module easy.server.cached {
    exports io.github.jinlongliao.easy.server.cached;
    exports io.github.jinlongliao.easy.server.cached.aop;
    exports io.github.jinlongliao.easy.server.cached.aop.el;
    exports io.github.jinlongliao.easy.server.cached.annotation;
    exports io.github.jinlongliao.easy.server.cached.annotation.process;
    exports io.github.jinlongliao.easy.server.cached.aop.spring;
    exports io.github.jinlongliao.easy.server.cached.aop.spring.handler;
    exports io.github.jinlongliao.easy.server.cached.aop.simple;
    exports io.github.jinlongliao.easy.server.cached.aop.simple.handler;
    exports io.github.jinlongliao.easy.server.cached.config;
    exports io.github.jinlongliao.easy.server.cached.field.simple;
    exports io.github.jinlongliao.easy.server.cached.field.spring;
    exports io.github.jinlongliao.easy.server.cached.util;
    exports io.github.jinlongliao.easy.server.cached.annotation.simple;
    requires org.slf4j;
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.aop;
    requires easy.server.core;
    requires easy.server.utils;
    requires easy.server.mapper;
    requires java.compiler;


    uses io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator;

    provides io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator with
            io.github.jinlongliao.easy.server.cached.annotation.process.GetCacheLoaderGenerator;

    provides javax.annotation.processing.Processor with
            io.github.jinlongliao.easy.server.cached.annotation.process.SimpleGetCacheAnnotationProcessor,
            io.github.jinlongliao.easy.server.cached.annotation.process.GetCacheAnnotationProcessor;

}
