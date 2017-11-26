package com.github.sbanal.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * ErrorInfo class is represents and error that has occurred during validation.
 * The FieldError class contains information which fields trigger the validation error.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class ErrorInfo {

  public static class FieldError {

    private String field;
    private Object rejectedValue;
    private String message;
    private String code;

    public FieldError() {

    }

    public FieldError(String fieldPath, Object rejectedValue, String message, String code) {
      this.field = fieldPath;
      this.rejectedValue = rejectedValue;
      this.message = message;
      this.code = code;
    }

    public String getField() {
      return field;
    }

    public Object getRejectedValue() {
      return rejectedValue;
    }

    public String getMessage() {
      return message;
    }

    public String getCode() {
      return code;
    }

    public void setField(String field) {
      this.field = field;
    }

    public void setRejectedValue(Object rejectedValue) {
      this.rejectedValue = rejectedValue;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public void setCode(String code) {
      this.code = code;
    }

    @Override
    public String toString() {
      return "FieldError{" +
             "field='" + field + '\'' +
             ", rejectedValue=" + rejectedValue +
             ", message='" + message + '\'' +
             ", code='" + code + '\'' +
             '}';
    }

  }

  private int code;
  private String message;
  private final List<FieldError> fieldErrors = new ArrayList<FieldError>();

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<FieldError> getFieldErrors() {
    return fieldErrors;
  }

  public void addFieldError(String objectName, String fieldName, Object rejectedValue,
                            String message, String code) {

    FieldError error = new FieldError(objectName + "." + fieldName,
                                      rejectedValue,
                                      message,
                                      code);
    this.fieldErrors.add(error);
  }

  @Override
  public String toString() {
    return "ErrorResource{" +
           "code=" + code +
           ", message='" + message + '\'' +
           ", fieldErrors=" + fieldErrors +
           '}';
  }

}
