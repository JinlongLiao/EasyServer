module easy.server.mapper {
    exports io.github.jinlongliao.easy.server.mapper.constant;
    exports io.github.jinlongliao.easy.server.mapper.core.field;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.core;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct.annotation;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.core.map;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.core.array;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.core.wrap;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.servlet;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.method;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter;
    exports io.github.jinlongliao.easy.server.mapper.core.mapstruct2.loader;
    exports io.github.jinlongliao.easy.server.mapper.exception;
    exports io.github.jinlongliao.easy.server.mapper.spring;
    exports io.github.jinlongliao.easy.server.mapper.utils;
    exports io.github.jinlongliao.easy.server.mapper.annotation.process;
    exports io.github.jinlongliao.easy.server.mapper.annotation;
    exports io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm;


    requires spring.beans;

    requires static java.compiler;
    requires static javax.servlet.api;
    requires static org.slf4j;

    uses io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator;
    uses io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.InnerConverter;
    uses io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.IDataConverter;
    uses io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.ClassVersion;
    provides io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator with
            io.github.jinlongliao.easy.server.mapper.annotation.process.GeneratorCopyLoaderGenerator,
            io.github.jinlongliao.easy.server.mapper.annotation.process.GeneratorMethodLoaderGenerator;
}
