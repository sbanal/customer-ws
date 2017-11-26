package com.github.sbanal.web.service;

/**
 * ServiceException is a runtime exception thrown by a service if an unexpected
 * exception is caught while processing a request.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class ServiceException extends RuntimeException {

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceException(String message) {
    super(message);
  }

}
