package com.github.sbanal.web.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * ValidEnum is an annotation used to validate that a string value is a valid name of a specific Enum type.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@Documented
@Constraint(validatedBy = ValidEnum.Validator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, PARAMETER, CONSTRUCTOR })
@Retention(RUNTIME)
@NotNull
public @interface ValidEnum {

  String message() default "{com.github.sbanal.web.validator.gender}";
  String messageTemplate() default "{com.github.sbanal.web.validator.gender}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends Enum<?>> enumType();

  class Validator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumType;

    void setEmumType(Class<? extends Enum<?>> enumType) {
        this.enumType = enumType;
    }

    @Override
    public void initialize(ValidEnum validEnum) {
      enumType = validEnum.enumType();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
      for(Enum<?> enumValue : enumType.getEnumConstants()) {
        if(enumValue.name().equals(s)) {
          return true;
        }
      }
      return false;
    }

  }
}
