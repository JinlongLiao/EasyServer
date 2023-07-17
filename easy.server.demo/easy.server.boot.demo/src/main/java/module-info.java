import io.github.jinlongliao.easy.server.boot.demo.logic.annotation.LogicExtraMethodAnnotationProcess;
import io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodAnnotationProcess;

open module easy.server.boot.demo {
    requires easy.server.bom;

    requires spring.context;
    requires spring.tx;
    requires org.slf4j;
    requires jakarta.servlet;
    requires spring.core;
    requires spring.web;
    requires spring.beans;
    requires jakarta.validation;
    requires jakarta.annotation;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires static lombok;

    provides ExtraMethodAnnotationProcess with LogicExtraMethodAnnotationProcess;

}
