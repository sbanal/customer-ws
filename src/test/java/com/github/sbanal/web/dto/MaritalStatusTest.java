package com.github.sbanal.web.dto;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * MaritalStatusTest tests methods of MaritalStatus
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class MaritalStatusTest {

  @Test
  public void testToEnum() throws Exception {
    List<String> enumCodes = new ArrayList<String>();
    for(MaritalStatus ms : MaritalStatus.values()) {
      Assert.assertEquals(ms, MaritalStatus.toEnum(ms.value()));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToEnum_WrongCode() throws Exception {
    MaritalStatus.toEnum("SOME WRONG CODE");
  }

}