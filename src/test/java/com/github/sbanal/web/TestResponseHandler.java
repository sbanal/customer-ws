package com.github.sbanal.web;

import com.github.sbanal.web.dto.ErrorInfo;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * TestResponseHandler is a custom response exception handler used in
 * integration test codes to extract body of response as ErrorResource when
 * a service exception occurs.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
class TestResponseHandler implements ResponseErrorHandler {

  private ErrorInfo errorInfo;
  private int httpCode;
  private final int expectedHttpCode;

  public TestResponseHandler(int expectedHttpCode) {
    this.expectedHttpCode = expectedHttpCode;
  }

  public ErrorInfo getErrorInfo() {
    return errorInfo;
  }

  public int getHttpCode() {
    return httpCode;
  }

  public int getExpectedHttpCode() {
    return expectedHttpCode;
  }


  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    httpCode = clientHttpResponse.getStatusCode().value();
    return (expectedHttpCode!=httpCode);
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    errorInfo = TestHelper.toErrorResource(clientHttpResponse);
    throw new RuntimeException("Error response received from service: " + errorInfo);
  }

}
