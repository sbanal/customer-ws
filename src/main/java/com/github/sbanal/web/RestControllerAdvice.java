package com.github.sbanal.web;

import com.github.sbanal.web.service.CustomerNotFoundException;
import com.github.sbanal.web.dto.ErrorInfo;
import com.github.sbanal.web.service.AddressExistException;
import com.github.sbanal.web.service.AddressNotFoundException;
import com.github.sbanal.web.service.ServiceException;

import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * CustomerRestControllerAdvice class is the main controller handling the transformation
 * of exceptions to HTTP response.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@ControllerAdvice(annotations=RestController.class)
final class RestControllerAdvice {

  private static final org.apache.logging.log4j.Logger
      LOGGER = LogManager.getLogger(RestControllerAdvice.class);

  @ResponseBody
  @ExceptionHandler(CustomerNotFoundException.class)
  ResponseEntity<ErrorInfo> handleException(CustomerNotFoundException ex) {
    LOGGER.error("CustomerNotFoundException Caught", ex);

    ErrorInfo error = new ErrorInfo();
    error.setCode(HttpStatus.NOT_FOUND.value());
    error.setMessage(ex.getMessage());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .build()
                                .toUri());
    return new ResponseEntity<ErrorInfo>(error, httpHeaders, HttpStatus.NOT_FOUND);
  }

  @ResponseBody
  @ExceptionHandler(AddressNotFoundException.class)
  ResponseEntity<ErrorInfo> handleException(AddressNotFoundException ex) {
    LOGGER.error("AddressNotFoundException Caught", ex);

    ErrorInfo error = new ErrorInfo();
    error.setCode(HttpStatus.NOT_FOUND.value());
    error.setMessage(ex.getMessage());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .build()
                                .toUri());

    return new ResponseEntity<ErrorInfo>(error, httpHeaders, HttpStatus.NOT_FOUND);
  }

  @ResponseBody
  @ExceptionHandler(AddressExistException.class)
  ResponseEntity<ErrorInfo> handleException(AddressExistException ex) {

    LOGGER.error("AddressExistException Caught", ex);

    ErrorInfo error = new ErrorInfo();
    error.setCode(HttpStatus.CONFLICT.value());
    error.setMessage(ex.getMessage());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .build()
                                .toUri());

    return new ResponseEntity<ErrorInfo>(error, httpHeaders, HttpStatus.CONFLICT);
  }

  @ResponseBody
  @ExceptionHandler(RequestValidationException.class)
  ResponseEntity<ErrorInfo> handleException(RequestValidationException ex) {

    LOGGER.error("RequestValidationException Caught", ex);

    ErrorInfo error = new ErrorInfo();
    error.setCode(HttpStatus.BAD_REQUEST.value());
    error.setMessage(ex.getMessage());
    for(FieldError fieldError : ex.getErrors()) {
      error.addFieldError(fieldError.getObjectName(),
                          fieldError.getField(),
                          fieldError.getRejectedValue(),
                          fieldError.getDefaultMessage(),
                          fieldError.getCode());
    }

    return new ResponseEntity<ErrorInfo>(error, null, HttpStatus.BAD_REQUEST);
  }

  @ResponseBody
  @ExceptionHandler(ServiceException.class)
  ResponseEntity<ErrorInfo> handleException(ServiceException ex) {

    LOGGER.error("ServiceException Caught", ex);
    ErrorInfo error = new ErrorInfo();
    error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.setMessage("Service error: " + ex.getMessage());

    return new ResponseEntity<ErrorInfo>(error, null, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseBody
  @ExceptionHandler(Exception.class)
  ResponseEntity<ErrorInfo> handleException(Exception ex) {

    LOGGER.error("Unknown exception Caught", ex);
    ErrorInfo error = new ErrorInfo();
    error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.setMessage(ex.getMessage());

    return new ResponseEntity<ErrorInfo>(error, null, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
