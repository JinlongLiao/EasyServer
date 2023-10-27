open module easy.server.core {

    requires easy.server.utils;
    exports io.github.jinlongliao.easy.server.core;
    exports io.github.jinlongliao.easy.server.core.annotation;
    exports io.github.jinlongliao.easy.server.core.annotation.validate;
    exports io.github.jinlongliao.easy.server.core.annotation.validate.impl;
    exports io.github.jinlongliao.easy.server.core.core;
    exports io.github.jinlongliao.easy.server.core.core.spring;
    exports io.github.jinlongliao.easy.server.core.core.spring.register;
    exports io.github.jinlongliao.easy.server.core.exception;
    exports io.github.jinlongliao.easy.server.core.model;
    exports io.github.jinlongliao.easy.server.core.parser;
    exports io.github.jinlongliao.easy.server.core.parser.inner;

    requires org.slf4j;
    requires spring.aop;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires easy.server.mapper;
    requires org.hibernate.validator;
    requires static javax.servlet;

    uses io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodAnnotationProcess;
    requires static javax.validation;
}
