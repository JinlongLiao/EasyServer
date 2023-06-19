package io.github.jinlongliao.easy.server.core.annotation.validate;
/**
 * 手机号校验
 *
 * @author liaojinlong
 * @since 2021/3/11 16:34
 */

import io.github.jinlongliao.easy.server.core.annotation.validate.impl.PhoneNumberValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface Phone {

    String message() default "invalid phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
