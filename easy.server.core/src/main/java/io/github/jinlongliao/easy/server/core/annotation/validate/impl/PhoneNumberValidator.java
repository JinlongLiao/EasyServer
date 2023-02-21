package io.github.jinlongliao.easy.server.core.annotation.validate.impl;


import io.github.jinlongliao.easy.server.core.annotation.validate.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * validate mobile phone number
 *
 * @author mahongxu
 * @since 1.0
 */
public class PhoneNumberValidator implements ConstraintValidator<Phone, String> {
    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^((13|14|15|16|17|18|19)[0-9]{9})$")
                && (value.length() > 8) && (value.length() < 14);
    }
}
