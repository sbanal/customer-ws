package com.github.sbanal.web.validator;

import org.hibernate.validator.constraints.NotEmpty;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.Size;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ValidCountryCode is a constraint used to check if a string value
 * is a valid country ISO code (e.g. US, AU). The country
 * codes used for validation is from the list returned
 * by Locale.getISOCountries() method.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@Documented
@Constraint(validatedBy = ValidCountryCode.Validator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, PARAMETER, CONSTRUCTOR })
@Retention(RUNTIME)
@NotEmpty
@Size(min=2,max=2)
public @interface ValidCountryCode {

  String message() default "{com.github.sbanal.web.validator.country}";
  String messageTemplate() default "{com.github.sbanal.web.validator.country}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<ValidCountryCode, String> {

    @Override
    public void initialize(ValidCountryCode validEnum) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
      for(String isoCountry : Locale.getISOCountries()) {
        if(isoCountry.equals(s)) {
          return true;
        }
      }
      return false;
    }

  }

}
