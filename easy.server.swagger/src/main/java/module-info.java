module easy.server.swagger {
    exports io.github.jinlongliao.easy.server.swagger.config;
    exports io.github.jinlongliao.easy.server.swagger.model;
    exports io.github.jinlongliao.easy.server.swagger.knife4j.auth;
    exports io.github.jinlongliao.easy.server.swagger.knife4j.parse;
    exports io.github.jinlongliao.easy.server.swagger.servlet;
    exports io.github.jinlongliao.easy.server.swagger.servlet.help;

    requires easy.server.core;
    requires easy.server.utils;
    requires easy.server.mapper;
    requires easy.server.servlet;
    requires org.slf4j;

    requires spring.core;
    requires spring.aop;
    requires spring.beans;
    requires spring.context;
    requires static jakarta.servlet;
}
