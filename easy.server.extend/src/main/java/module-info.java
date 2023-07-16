open module easy.server.extend {
    exports io.github.jinlongliao.easy.server.extend.annotation;
    exports io.github.jinlongliao.easy.server.extend.annotation.process;
    exports io.github.jinlongliao.easy.server.extend.exception;
    exports io.github.jinlongliao.easy.server.extend.parser;
    exports io.github.jinlongliao.easy.server.extend.response;
    exports io.github.jinlongliao.easy.server.extend.response.proxy;

    requires easy.server.core;
    requires easy.server.mapper;

    requires static java.compiler;
    requires static org.slf4j;
    requires static jakarta.servlet;

    uses io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator;

    provides io.github.jinlongliao.easy.server.mapper.annotation.LoaderGenerator with
            io.github.jinlongliao.easy.server.extend.annotation.process.GeneratorResponseLoaderGenerator;

    provides javax.annotation.processing.Processor with
            io.github.jinlongliao.easy.server.extend.annotation.process.GeneratorResponseAnnotationProcessor;
}
