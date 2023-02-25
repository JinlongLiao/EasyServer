package io.github.jinlongliao.easy.server.netty.demo.logic.annotation;

import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: liaojinlong
 * @date: 2022-09-16 10:00
 */
@LogicRequestParam(value = "tcp_con", isCommon = true, innerParse = true)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TcpCon {

}
