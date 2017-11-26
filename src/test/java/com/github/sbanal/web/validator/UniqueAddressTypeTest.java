package com.github.sbanal.web.validator;

import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dto.AddressType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * UniqueAddressTypeTest class tests UniqueAddressType.Validator logic.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@RunWith(JUnitParamsRunner.class)
public class UniqueAddressTypeTest {


  private Object[] testData() {

    Address addr1 = new Address();
    addr1.setType(AddressType.MAILING.name());
    Address addr2 = new Address();
    addr2.setType(AddressType.MAILING.name());
    Address addr3 = new Address();
    addr3.setType(AddressType.RESIDENTIAL.name());
    List<Address> addressListWithDuplicate = Arrays.asList(addr1, addr2);

    List<Address> addressListWithoutDuplicate = Arrays.asList(addr1, addr3);
    List<Address> addressListEmpty = Arrays.<Address>asList();
    List<Address> addressListOneItem = Arrays.asList(addr1);
    List<Address> addressListNull = null;

    return new Object[]{
        new Object[]{addressListWithDuplicate, false},
        new Object[]{addressListWithoutDuplicate, true},
        new Object[]{addressListEmpty, true},
        new Object[]{addressListOneItem, true},
        new Object[]{addressListNull, true},
    };

  }

  @Test
  @Parameters(method = "testData")
  public void testIsValid(List<Address> addressList, boolean expectedResult) throws Exception {

    UniqueAddressType.Validator validator = new UniqueAddressType.Validator();
    boolean result = validator.isValid(addressList, Mockito.mock(ConstraintValidatorContext.class));
    Assert.assertEquals(expectedResult, result);

  }

}