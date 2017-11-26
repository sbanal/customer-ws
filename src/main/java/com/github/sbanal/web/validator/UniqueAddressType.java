package com.github.sbanal.web.validator;

import com.github.sbanal.web.dto.Address;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * UniqueAddressType is a constraint which checks if a list of address
 * objects have a unique set of address types.
 *
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@Documented
@Constraint(validatedBy = UniqueAddressType.Validator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, PARAMETER, CONSTRUCTOR })
@Retention(RUNTIME)
public @interface UniqueAddressType {

  String message() default "Addess type in list is not unique";
  String messageTemplate() default "{com.github.sbanal.web.validator.address.type.unique}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class Validator implements ConstraintValidator<UniqueAddressType, List<Address>> {

    @Override
    public void initialize(UniqueAddressType validEnum) {

    }

    @Override
    public boolean isValid(List<Address> value, ConstraintValidatorContext constraintValidatorContext) {

      if(value==null) {
        return true;
      }

      Set<String> addrType = new HashSet<String>();
      for(Address address : value) {
        if(!addrType.add(address.getType())) {
          return false;
        }
      }

      return true;
    }

  }

}
