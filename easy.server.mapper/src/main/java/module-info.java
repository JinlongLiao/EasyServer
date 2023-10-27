open module easy.server.mapper {
    exports io.github.jinlongliao.easy.server.mapper.constant;
    exports io.github.jinlongliao.easy.server.mapper.core.field;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.map;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.array;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.wrap;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.loader;
    exports io.github.jinlongliao.easy.server.mapper.exception;
    exports io.github.jinlongliao.easy.server.mapper.spring;
    exports io.github.jinlongliao.easy.server.mapper.utils;
    exports io.github.jinlongliao.easy.server.mapper.annotation.process;
    exports io.github.jinlongliao.easy.server.mapper.annotation;
    exports io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm;

    requires org.slf4j;
    requires spring.beans;

    requires static java.compiler;
    requires static jakarta.servlet;

    uses io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator;
    uses io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter.InnerConverter;
    uses io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter.IDataConverter;
    uses io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.ClassVersion;
    provides io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator with
            io.github.jinlongliao.easy.server.mapper.annotation.process.GeneratorCopyLoaderGenerator,
            io.github.jinlongliao.easy.server.mapper.annotation.process.GeneratorMethodLoaderGenerator;

    provides javax.annotation.processing.Processor with
            io.github.jinlongliao.easy.server.mapper.annotation.process.GeneratorCopyAnnotationProcessor,
            io.github.jinlongliao.easy.server.mapper.annotation.process.GeneratorMethodAnnotationProcessor;

}
