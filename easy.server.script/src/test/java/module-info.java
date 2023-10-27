open module easy.server.script.test {
    requires org.slf4j;
    requires spring.core;
    requires spring.aop;
    requires spring.context;
    requires spring.beans;
    requires spring.integration.scripting;


    requires easy.server.script;
    requires easy.server.mapper;
    requires jakarta.annotation;
    requires junit;
}
