package com.github.sbanal.web.service;

/**
 * AddressExistException is a checked exception thrown if an address of
 * the same type already exist.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class AddressExistException extends Exception {

  public AddressExistException(String message) {
    super(message);
  }

  public AddressExistException(String message, Throwable cause) {
    super(message, cause);
  }

}
