package com.github.sbanal.web.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * ValidEnumTest class tests ValidEnumTest.Valid validator.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@RunWith(JUnitParamsRunner.class)
public class ValidEnumTest {

  public enum SampleEnum1 {
    A,
    B,
    C
  }

  private Object[] testData() {

    List<Object[]> data = new ArrayList<Object[]>();
    for(SampleEnum1 enumElem : SampleEnum1.values()) {
      data.add(new Object[] {SampleEnum1.class, enumElem.name(), true});
    }
    data.add(new Object[] {SampleEnum1.class, "XX", false});
    data.add(new Object[] {SampleEnum1.class, "", false});
    data.add(new Object[] {SampleEnum1.class, (String) null, false});

    return data.toArray(new Object[0][3]);
  }

  @Test
  @Parameters(method = "testData")
  public void testIsValid(Class<?> enumType, String value, boolean expectedResult) throws Exception {

    ValidEnum.Validator validator = new ValidEnum.Validator();
    validator.setEmumType((Class<? extends Enum<?>> ) enumType);
    boolean result = validator.isValid(value, Mockito.mock(ConstraintValidatorContext.class));
    Assert.assertEquals(expectedResult, result);

  }

}