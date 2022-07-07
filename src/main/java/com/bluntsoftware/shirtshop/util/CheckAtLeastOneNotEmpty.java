package com.bluntsoftware.shirtshop.util;

import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckAtLeastOneNotEmpty.CheckAtLeastOneNotEmptyValidator.class)
@Documented
public @interface CheckAtLeastOneNotEmpty {

    String message() default "at least one field should not be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fieldNames();

    public static class CheckAtLeastOneNotEmptyValidator implements ConstraintValidator<CheckAtLeastOneNotEmpty, Object> {

        private String[] fieldNames;
        private String message;
        public void initialize(CheckAtLeastOneNotEmpty constraintAnnotation) {
            this.fieldNames = constraintAnnotation.fieldNames();
            this.message = String.format("at least one field should not be empty %s", Arrays.toString(this.fieldNames));
        }

        public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {

            if (object == null) {
                return true;
            }
            try {
                for (String fieldName:fieldNames){
                    Object property = PropertyUtils.getProperty(object, fieldName);

                    if (property != null && !property.toString().isEmpty()) return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
