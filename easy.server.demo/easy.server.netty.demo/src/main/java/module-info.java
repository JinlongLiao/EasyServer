open module easy.server.netty.demo {
    requires easy.server.bom;

    requires spring.context;
    requires spring.tx;
    requires org.slf4j;
    requires jakarta.annotation;
    requires jakarta.servlet;
    requires spring.core;
    requires spring.beans;
    requires spring.web;

    requires io.netty.transport;
    requires io.netty.common;
    requires io.netty.buffer;
    requires io.netty.transport.unix.common;
    requires io.netty.codec;
    requires org.apache.commons.lang3;
    requires io.netty.handler;
    requires io.netty.transport.classes.epoll;
    requires io.netty.transport.classes.kqueue;
    requires spring.boot;
    requires spring.boot.autoconfigure;
}
