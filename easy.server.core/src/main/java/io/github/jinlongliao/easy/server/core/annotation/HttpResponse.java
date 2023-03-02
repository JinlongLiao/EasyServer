package io.github.jinlongliao.easy.server.core.annotation;

import java.lang.annotation.*;

/**
 * Http servlet requyest
 *
 * @author: liaojinlong
 * @date: 2023/3/2 22:34
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@LogicRequestParam(value = "http_response", innerParse = true)
public @interface HttpResponse {
}
