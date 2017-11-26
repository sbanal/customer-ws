package com.github.sbanal.web.dto;

import org.junit.Assert;
import org.junit.Test;

/**
 * GenderTest tests methods of Gender enum
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class GenderTest {

  @Test
  public void testToEnum() throws Exception {
    for(Gender ms : Gender.values()) {
      Assert.assertEquals(ms, Gender.toEnum(ms.value()));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToEnum_WrongCode() throws Exception {
    Gender.toEnum("SOME WRONG CODE");
  }

}