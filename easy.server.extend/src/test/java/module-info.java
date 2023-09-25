open module easy.server.extend.test {
    requires easy.server.utils;
    requires easy.server.core;
    requires easy.server.mapper;
    requires easy.server.extend;

    requires static java.compiler;
    requires static org.slf4j;
    requires jakarta.servlet;
    requires org.objectweb.asm;
    requires junit;
    requires org.objectweb.asm.util;
}
