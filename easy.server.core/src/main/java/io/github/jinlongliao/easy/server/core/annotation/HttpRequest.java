package io.github.jinlongliao.easy.server.core.annotation;

import java.lang.annotation.*;


/**
 * Http servlet response
 *
 * @author: liaojinlong
 * @date: 2023/3/2 22:34
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@LogicRequestParam(value = "http_request", innerParse = true)
public @interface HttpRequest {
}
