package com.github.sbanal.web;

import com.github.sbanal.web.dao.AddressEntity;
import com.github.sbanal.web.dao.CustomerEntity;
import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dto.AddressType;
import com.github.sbanal.web.dto.Customer;
import com.github.sbanal.web.dto.ErrorInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * TestHelper class contains custom assert methods and util methods used by unit/integration tests.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public final class TestHelper {

  public static Customer createMockCustomerComplete() {
    Customer mockCustomer = TestHelper.createMockCustomer();
    Address mockMailingAddress = TestHelper.createMockAddress(AddressType.MAILING);
    Address mockAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);
    mockCustomer.putAddress(mockMailingAddress);
    mockCustomer.putAddress(mockAddress);
    return mockCustomer;
  }

  public static Customer createMockCustomerComplete(Long id) {
    Customer mockCustomer = createMockCustomerComplete();
    mockCustomer.setId(id);
    return mockCustomer;
  }

  public static void assertBeanEquals(Customer obj1, CustomerEntity obj2, String... ignoreFields) {

    try {

      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

      FilterProvider filters = new SimpleFilterProvider().addFilter(
          "filter properties by name", SimpleBeanPropertyFilter.serializeAllExcept(ignoreFields));
      ObjectWriter writer = mapper.writer(filters);
      String jsonObj1 = writer.writeValueAsString(obj1);
      String jsonObj2 = writer.writeValueAsString(obj2);

      Assert.assertEquals("Beans not the equal.\nobj1=" + jsonObj1 + ",\nobj2=" + jsonObj2,
                          jsonObj1, jsonObj2);

    } catch (JsonProcessingException e) {
      throw new AssertionError("Test failed due to json processing error", e);
    }

  }

  public static Customer createMockCustomer() {
    Customer customer = new Customer();
    int randomId = (new Random()).nextInt(1000);
    int creditRating = (new Random()).nextInt(100);
    customer.setTitle("Dr");
    customer.setFirstName("Pedro " + randomId);
    customer.setMiddleName("S");
    customer.setSurname("Penduko " + randomId);
    customer.setInitials("PSP");
    customer.setExistingCustomer(false);
    customer.setCreditRating(creditRating);
    customer.setExistingCustomer(false);
    customer.setGender("MALE");
    customer.setMaritalStatus("SINGLE");
    return customer;
  }

  public static Address createMockAddress(AddressType addressType) {
    Address address = new Address();
    address.setStreetType("St");
    address.setStreetName((new Random()).nextInt(10) + " Flinders");
    address.setSuburb("Suburb " + (new Random()).nextInt(10));
    address.setCity("Melbourne");
    address.setState("VIC");
    address.setType(addressType.name());
    address.setPostalCode(String.valueOf(1000 + (new Random()).nextInt(999)));
    address.setCountry("AU");
    return address;
  }

  public static AddressEntity createMockAddressEntity(AddressType addressType) {
    AddressEntity addressEntity = new AddressEntity();
    BeanUtils.copyProperties(createMockAddress(addressType), addressEntity);
    return addressEntity;
  }

  public static ErrorInfo toErrorResource(ClientHttpResponse clientHttpResponse)
      throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    IOUtils.copy(clientHttpResponse.getBody(), bos);
    String str = bos.toString();
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(str, ErrorInfo.class);
  }

  public static Long extractCustomerId(URI entityUri) {
    Pattern pattern = Pattern.compile("^/api/customers/([0-9]+)");
    java.util.regex.Matcher matcher = pattern.matcher(entityUri.getPath());
    if(matcher.matches()) {
      return Long.valueOf(matcher.group(1));
    }
    throw new IllegalArgumentException("Invalid uri " + entityUri);
  }

  public static Long[] extractCustomerAndAddressId(URI entityUri) {
    Pattern pattern = Pattern.compile("^/api/customers/([0-9]+)/addresses/([0-9]+)");
    java.util.regex.Matcher matcher = pattern.matcher(entityUri.getPath());
    if(matcher.matches()) {
      return new Long[] {
          Long.valueOf(matcher.group(1)),
          Long.valueOf(matcher.group(2))
      };
    }
    throw new IllegalArgumentException("Invalid uri " + entityUri);
  }

  public static void assertCustomer(Customer expectedCustomer, CustomerEntity record) {
    Customer customer = new Customer();
    BeanUtils.copyProperties(record, customer);
    assertCustomer(expectedCustomer, customer);
  }

  public static void assertCustomer(Customer expectedCustomer, Customer record) {
    Assert.assertEquals(expectedCustomer.getId(), record.getId());
    Assert.assertEquals(expectedCustomer.getTitle(), record.getTitle());
    Assert.assertEquals(expectedCustomer.getFirstName(), record.getFirstName());
    Assert.assertEquals(expectedCustomer.getSurname(), record.getSurname());
    Assert.assertEquals(expectedCustomer.getMiddleName(), record.getMiddleName());
    Assert.assertEquals(expectedCustomer.getMaritalStatus(), record.getMaritalStatus());
    Assert.assertEquals(expectedCustomer.getGender(), record.getGender());
    Assert.assertEquals(expectedCustomer.getCreditRating(), record.getCreditRating());
    Assert.assertEquals(expectedCustomer.isExistingCustomer(), record.isExistingCustomer());
  }

  public static void assertAddressExist(Customer expectedCustomer, Address address) {
    for(Address expAddress : expectedCustomer.getAddress()) {
      if(address.getType().equals(expAddress.getType())) {
        assertAddress(expAddress, address);
        return;
      }
    }
    Assert.assertFalse("Address " + address + " not found", true);
  }

  public static void assertEquals(List<Address> expected, List<Address> input) {
    for(Address address : expected) {
      boolean found = false;
      for(Address inputAddress : input) {
        if(address.getType().equals(inputAddress.getType())) {
          TestHelper.assertAddress(address, inputAddress);
          found = true;
        }
      }
      Assert.assertEquals(expected.size(), input.size());
      Assert.assertTrue("Address " + address + " not found in result.\n\t"
                        + "Result=" + input, found);
    }
  }

  public static void assertAddressTypeExist(AddressType addressType, List<Address> addressesToCheck,
                                            Address expectedAddress) {

    boolean found = false;
    for(Address address : addressesToCheck) {
      if(address.getType().equals(addressType.name())) {
        TestHelper.assertAddress(address, expectedAddress);
        found = true;
        break;
      }
    }

    Assert.assertTrue("Address " + expectedAddress + " not found in input addresses.\n\t"
                      + "addressesToCheck=" + addressesToCheck, found);

  }

  public static void assertAddress(AddressEntity expectedAddress, Address address) {
    Address addressEntity = new Address();
    BeanUtils.copyProperties(expectedAddress, addressEntity);
    assertAddress(addressEntity, address);
  }

  public static void assertAddress(Address expectedAddress, AddressEntity addressEntity) {
    Address address = new Address();
    BeanUtils.copyProperties(addressEntity, address);
    assertAddress(expectedAddress, address);
  }

  public static void assertAddress(Address expectedAddress, Address result) {
    Assert.assertEquals(expectedAddress.getCity(), result.getCity());
    Assert.assertEquals(expectedAddress.getCountry(), result.getCountry());
    Assert.assertEquals(expectedAddress.getPostalCode(), result.getPostalCode());
    Assert.assertEquals(expectedAddress.getState(), result.getState());
    Assert.assertEquals(expectedAddress.getStreetName(), result.getStreetName());
    Assert.assertEquals(expectedAddress.getStreetType(), result.getStreetType());
    Assert.assertEquals(expectedAddress.getSuburb(), result.getSuburb());
  }

  public static void assertErrorExist(ErrorInfo error, String... expectedErrorFields ) {
    for(String errorFieldPath : expectedErrorFields) {
      boolean found = false;
      Assert.assertEquals(expectedErrorFields.length, error.getFieldErrors().size());
      for(ErrorInfo.FieldError fldError : error.getFieldErrors()) {
        if(fldError.getField().equals(errorFieldPath)) {
          found = true;
        }
      }
      Assert.assertTrue("Error " + errorFieldPath + " not found in error " + error, found);
    }
  }


}
