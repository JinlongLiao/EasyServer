package io.github.jinlongliao.easy.server.core.annotation;

import java.lang.annotation.*;

/**
 * 解析 客户端IP
 *
 * @author: liaojinlong
 * @date: 2022-09-16 11:13
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@LogicRequestParam(value = "__CLIENT_IP__", innerParse = true)
public @interface LogicRequestIp {
}
