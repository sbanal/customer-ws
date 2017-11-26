package com.github.sbanal.web.dto;

/**
 * AddressType enum defines the supported address types.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public enum AddressType {
  MAILING("M"),
  RESIDENTIAL("R");

  final String code;

  AddressType(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }

  public static AddressType toEnum(String code) {
    for(AddressType status : AddressType.values()) {
      if(status.code.equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid Address type code " + code);
  }

}
