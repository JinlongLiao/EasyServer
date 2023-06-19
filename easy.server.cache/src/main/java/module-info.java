module easy.server.cached {
    exports io.github.jinlongliao.easy.server.cached.annotation;
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
    requires easy.server.mapper;
    requires easy.server.core;
}
