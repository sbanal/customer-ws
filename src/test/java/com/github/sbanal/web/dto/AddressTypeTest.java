package com.github.sbanal.web.dto;

import org.junit.Assert;
import org.junit.Test;

/**
 * AddressTypeTest test methods of AddressType enum.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class AddressTypeTest {

  @Test
  public void testToEnum() throws Exception {
    for(AddressType ms : AddressType.values()) {
      Assert.assertEquals(ms, AddressType.toEnum(ms.getCode()));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToEnum_WrongCode() throws Exception {
    AddressType.toEnum("SOME WRONG CODE");
  }

}