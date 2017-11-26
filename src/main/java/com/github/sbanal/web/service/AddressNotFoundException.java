package com.github.sbanal.web.service;

/**
 * AddressNotFoundException is a runtime exception thrown if an address is not found.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class AddressNotFoundException extends Exception {

  public AddressNotFoundException(String message) {
    super(message);
  }
  public AddressNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
