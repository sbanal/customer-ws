package com.github.sbanal.web.dto;

/**
 * Gender enum.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public enum Gender {
  MALE("M"),
  FEMALE("F"),
  UNSPECIFIED("U");

  final String code;

  Gender(String code) {
    this.code = code;
  }

  public String value() {
    return this.code;
  }

  public static Gender toEnum(String code) {
    for(Gender gender : Gender.values()) {
      if(gender.code.equals(code)) {
        return gender;
      }
    }
    throw new IllegalArgumentException("Invalid gender code " + code);
  }

}
