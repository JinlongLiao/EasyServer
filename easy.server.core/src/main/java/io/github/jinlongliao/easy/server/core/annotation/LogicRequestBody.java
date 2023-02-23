package io.github.jinlongliao.easy.server.core.annotation;

import java.lang.annotation.*;

/**
 * 参数实体标识
 *
 * @author liaojinlong
 * @since 2021/1/22 23:10
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogicRequestBody {
    /**
     * 默认同Logic ID
     *
     * @return 消息ID
     */
    String logicId() default "";

    String value() default "";

    /**
     * 参数默认值
     *
     * @return /
     */
    String defaultValue() default "";
}
