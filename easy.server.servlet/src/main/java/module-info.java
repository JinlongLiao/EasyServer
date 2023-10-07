module easy.server.servlet {
    requires spring.beans;
    requires org.slf4j;
    requires easy.server.mapper;
    requires spring.context;
    requires spring.core;
    exports io.github.jinlongliao.easy.server;
    exports io.github.jinlongliao.easy.server.servlet;
    exports io.github.jinlongliao.easy.server.servlet.config;
    exports io.github.jinlongliao.easy.server.servlet.extra;
    exports io.github.jinlongliao.easy.server.servlet.match;
    requires static jakarta.servlet;
}
