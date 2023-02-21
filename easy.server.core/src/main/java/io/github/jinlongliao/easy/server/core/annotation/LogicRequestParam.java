package io.github.jinlongliao.easy.server.core.annotation;

import java.lang.annotation.*;

/**
 * @author liaojinlong
 * @since 2021/1/22 17:08
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogicRequestParam {
    /**
     * 参数名称
     *
     * @return 参数名称
     */
    String value() default "";

    /**
     * @return 参数 长度
     */
    int length() default 0;

    /**
     * 动态长度
     * 只支持String
     */
    boolean dynamicLength() default false;


    /**
     * 参数默认值
     *
     * @return /
     */
    String defaultValue() default "";

    /**
     * 公共属性
     *
     * @return /
     */
    boolean isCommon() default false;

    /**
     * 表明此属性位 内部解析
     *
     * @return /
     */
    boolean innerParse() default false;
}
