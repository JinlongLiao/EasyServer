package io.github.jinlongliao.easy.server.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务处理逻辑Message
 *
 * @author liaojinlong
 * @since 2021/1/22 15:20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface LogicController {
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * 消息描述
     *
     * @return 消息描述
     */
    String desc() default "消息描述";
}
