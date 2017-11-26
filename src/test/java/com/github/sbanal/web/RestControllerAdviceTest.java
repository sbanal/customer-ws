package com.github.sbanal.web;

import com.github.sbanal.web.dto.ErrorInfo;
import com.github.sbanal.web.service.AddressExistException;
import com.github.sbanal.web.service.AddressNotFoundException;
import com.github.sbanal.web.service.CustomerNotFoundException;
import com.github.sbanal.web.service.ServiceException;

import org.apache.catalina.connector.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * CustomerRestControllerAdviceTest tests
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@PrepareForTest(ServletUriComponentsBuilder.class)
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class RestControllerAdviceTest {

  static void mockServeletUriComponentBuilder() throws URISyntaxException {

    PowerMockito.mockStatic(ServletUriComponentsBuilder.class);

    UriComponents mockUriComp = Mockito.mock(UriComponents.class);
    Mockito.when(mockUriComp.toUri()).thenReturn(new URI("http://localhost/api/customers/1"));

    ServletUriComponentsBuilder mockUriBlder = Mockito.mock(ServletUriComponentsBuilder.class);
    Mockito.when(mockUriBlder.build()).thenReturn(mockUriComp);

    Mockito.when(ServletUriComponentsBuilder.fromCurrentRequest()).thenReturn(mockUriBlder);

  }

  @Before
  public void beforeTest() throws URISyntaxException {
    mockServeletUriComponentBuilder();
  }

  static void assertResponseEntity(ResponseEntity<ErrorInfo> response,
                                   int httpCode, int errorCode, String msg) {

    Assert.assertEquals(httpCode,response.getStatusCode().value());
    Assert.assertEquals(errorCode, response.getBody().getCode());
    if (msg!=null) {
      Assert.assertTrue("Error message keyword '" + msg + "' not found in response",
                        response.getBody().getMessage().contains(msg));
    }
  }

  @Test
  public void testNotFoundExceptionHandler_Address() throws Exception {

    RestControllerAdvice advice = new RestControllerAdvice();
    ResponseEntity<ErrorInfo> response = advice.handleException(
        new AddressNotFoundException("Some exception"));

    assertResponseEntity(response, Response.SC_NOT_FOUND, Response.SC_NOT_FOUND, null);
  }

  @Test
  public void testNotFoundExceptionHandler_Customer() throws Exception {

    UriComponents mockUriComp = Mockito.mock(UriComponents.class);
    Mockito.when(mockUriComp.toUri()).thenReturn(new URI("http://localhost/api/customers/1"));

    ServletUriComponentsBuilder mockUriBlder = Mockito.mock(ServletUriComponentsBuilder.class);
    Mockito.when(mockUriBlder.build()).thenReturn(mockUriComp);

    PowerMockito.mockStatic(ServletUriComponentsBuilder.class);
    Mockito.when(ServletUriComponentsBuilder.fromCurrentRequest()).thenReturn(mockUriBlder);


    RestControllerAdvice advice = new RestControllerAdvice();
    ResponseEntity<ErrorInfo> response = advice.handleException(
        new CustomerNotFoundException("Some exception"));

    assertResponseEntity(response, Response.SC_NOT_FOUND, Response.SC_NOT_FOUND, null);

  }

  @Test
  public void testAddressExistExceptionHandler_AddressExist() throws Exception {

    RestControllerAdvice advice = new RestControllerAdvice();
    ResponseEntity<ErrorInfo> response = advice.handleException(
        new AddressExistException("Some exception"));

    assertResponseEntity(response, Response.SC_CONFLICT, Response.SC_CONFLICT, null);

  }

  @Test
  public void testValidationExceptionHandler() throws Exception {

    FieldError fld1 = Mockito.mock(FieldError.class);
    Mockito.when(fld1.getField()).thenReturn("fld1");
    Mockito.when(fld1.getObjectName()).thenReturn("testObject");
    Mockito.when(fld1.getDefaultMessage()).thenReturn("fld 1 error");
    Mockito.when(fld1.getRejectedValue()).thenReturn("1");

    FieldError fld2 = Mockito.mock(FieldError.class);
    Mockito.when(fld2.getField()).thenReturn("fld2");
    Mockito.when(fld2.getObjectName()).thenReturn("testObject");
    Mockito.when(fld2.getDefaultMessage()).thenReturn("fld 2 error");
    Mockito.when(fld2.getRejectedValue()).thenReturn("2");

    List<FieldError> validationFldErrors = Arrays.asList(fld1, fld2);
    RestControllerAdvice advice = new RestControllerAdvice();
    ResponseEntity<ErrorInfo> response = advice.handleException(
        new RequestValidationException("Some exception", validationFldErrors));

    assertResponseEntity(response, Response.SC_BAD_REQUEST, Response.SC_BAD_REQUEST, null);
    Assert.assertEquals(2, response.getBody().getFieldErrors().size());

    for(FieldError fldError : validationFldErrors) {
      boolean found = false;
      for(ErrorInfo.FieldError responseFldError : response.getBody().getFieldErrors()) {
        if(responseFldError.getField().endsWith(fldError.getField())) {
          Assert.assertEquals(fldError.getObjectName() + "." + fldError.getField(),
                              responseFldError.getField());
          Assert.assertEquals(fldError.getRejectedValue(), responseFldError.getRejectedValue());
          Assert.assertEquals(fldError.getCode(), responseFldError.getCode());
          Assert.assertEquals(fldError.getDefaultMessage(), responseFldError.getMessage());
          found = true;
          break;
        }
      }
      Assert.assertTrue("Field " + fldError.getObjectName() + "." + fldError.getField()
                        + " not found in response " + response.getBody().getFieldErrors(),
                        found);
    }

  }

  @Test
  public void testServiceExceptionHandler() throws Exception {

    RestControllerAdvice advice = new RestControllerAdvice();
    ResponseEntity<ErrorInfo> response = advice.handleException(
        new ServiceException("Some exception"));

    assertResponseEntity(response, Response.SC_INTERNAL_SERVER_ERROR,
                         Response.SC_INTERNAL_SERVER_ERROR, null);

  }

  @Test
  public void testCatchAllExceptionHandler() throws Exception {

    RestControllerAdvice advice = new RestControllerAdvice();
    ResponseEntity<ErrorInfo> response = advice.handleException(
        new Exception("Some exception"));

    assertResponseEntity(response, Response.SC_INTERNAL_SERVER_ERROR,
                         Response.SC_INTERNAL_SERVER_ERROR, null);

  }

}