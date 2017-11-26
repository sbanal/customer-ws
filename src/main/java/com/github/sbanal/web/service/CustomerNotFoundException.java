package com.github.sbanal.web.service;

/**
 * CustomerNotFoundException is a checked exception that is thrown if a customer
 * is not found.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class CustomerNotFoundException extends Exception {

  public CustomerNotFoundException(String message) {
    super(message);
  }

  protected CustomerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
