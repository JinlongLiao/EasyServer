package io.github.jinlongliao.easy.server.core.annotation.validate;


import io.github.jinlongliao.easy.server.core.annotation.validate.impl.ChineseIdCardValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 身份证号码校验
 *
 * @author liaojinlong
 * @since 2021/3/18 16:46
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChineseIdCardValidator.class)
public @interface ChineseIdCarNo {

    String message() default "invalid id_card format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
