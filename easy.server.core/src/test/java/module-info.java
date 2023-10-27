open module easy.server.core.test {

    requires easy.server.core;
    requires easy.server.utils;
    requires org.slf4j;
    requires spring.aop;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires easy.server.mapper;
    requires org.hibernate.validator;
    requires static jakarta.servlet;

    requires transitive jakarta.validation;
    requires spring.test;
    requires junit;

}
