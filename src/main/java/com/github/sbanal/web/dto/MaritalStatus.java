package com.github.sbanal.web.dto;

/**
 * MaritalStatus enum defines the supported marital status values.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public enum MaritalStatus {
  SINGLE("S"),
  MARRIED("M"),
  WIDOWED("W"),
  DIVORCED("D"),
  SEPARATED("E");

  final String code;

  MaritalStatus(String code) {
    this.code = code;
  }

  public String value() {
    return this.code;
  }

  public static MaritalStatus toEnum(String code) {
    for(MaritalStatus status : MaritalStatus.values()) {
      if(status.code.equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid Marital status code " + code);
  }

}
