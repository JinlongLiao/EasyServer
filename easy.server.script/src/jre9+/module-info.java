module easy.server.script {
    requires org.slf4j;
    requires spring.core;
    requires spring.aop;
    requires spring.context;
    requires spring.beans;
    requires spring.integration.scripting;


    requires easy.server.mapper;
    requires static javax.annotation;

    exports io.github.jinlongliao.easy.server.script.groovy;
    exports io.github.jinlongliao.easy.server.script.groovy.config;
    exports io.github.jinlongliao.easy.server.script.groovy.config.dynamic;
    exports io.github.jinlongliao.easy.server.script.groovy.bean;
    exports io.github.jinlongliao.easy.server.script.groovy.annotation;
    exports io.github.jinlongliao.easy.server.script.groovy.constant;
}
