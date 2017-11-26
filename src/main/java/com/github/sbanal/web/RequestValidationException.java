package com.github.sbanal.web;

import org.springframework.validation.FieldError;

import java.util.List;

/**
 * RequestValidationException is a runtime exception thrown if a bean validation
 * error has occurred in a request.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
class RequestValidationException extends RuntimeException {

  private final List<FieldError> errors;

  public RequestValidationException(String message, List<FieldError> errors) {
    super(message);
    this.errors = errors;
  }

  public List<FieldError> getErrors() { return errors; }

}
