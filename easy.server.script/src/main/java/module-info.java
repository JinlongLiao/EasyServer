module easy.server.script {
    requires org.slf4j;
    requires spring.core;
    requires spring.aop;
    requires spring.context;
    requires spring.beans;
    requires spring.integration.scripting;
    requires easy.server.mapper;
    requires static jakarta.annotation;

    exports io.github.jinlongliao.easy.server.script.groovy.config;
    exports io.github.jinlongliao.easy.server.script.groovy.annotation;

}
