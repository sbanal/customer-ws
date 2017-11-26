package com.github.sbanal.web.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintValidatorContext;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * ValidateCountryCodeTest tests ValidateCountryCode.Validator logic.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@RunWith(JUnitParamsRunner.class)
public class ValidateCountryCodeTest {

  private Object[] testData() {
    String[] isoCodes = Locale.getISOCountries();
    List<Object[]> data = new ArrayList<Object[]>();
    for(String code : isoCodes) {
      data.add(new Object[] {code, true});
    }
    data.add(new Object[] {"XX", false});
    data.add(new Object[] {"", false});
    data.add(new Object[] {null, false});
    return data.toArray(new Object[0][2]);
  }

  @Test
  @Parameters(method = "testData")
  public void testIsValid(String value, boolean expectedResult) throws Exception {

    ValidCountryCode.Validator validator = new ValidCountryCode.Validator();
    boolean result = validator.isValid(value, Mockito.mock(ConstraintValidatorContext.class));
    Assert.assertEquals(expectedResult, result);

  }

}