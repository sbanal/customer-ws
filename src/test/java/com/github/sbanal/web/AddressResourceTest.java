package com.github.sbanal.web;

import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dto.AddressType;
import com.github.sbanal.web.dto.ErrorInfo;

import org.apache.catalina.connector.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriTemplate;


import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.sql.DataSource;

/**
 * Tests AddressResource RESTful web services endpoint /api/customers/<id>/addresses.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,
    properties = {"server.port=9000"},
    webEnvironment = WebEnvironment.DEFINED_PORT)
public class AddressResourceTest {

  private static final Long TEST_CUSTOMER_ID = 1L;
  private static final Long TEST_CUSTOMER_ID_NO_ADDRESS = 2L;
  private static final Long TEST_ADDRESS_ID = 2L;
  private static final String ADDRESS_RESOURCE_WITH_PARAM_URI = "http://localhost:9000/api/customers/{customerId}/addresses/{addressId}";
  private static final Logger LOGGER = LogManager.getLogger(AddressResourceTest.class);

  @Autowired
  @Qualifier("dataSource")
  private DataSource dataSource;
  private TestDaoHelper testDaoHelper;

  @BeforeClass
  public static void initTest() {
    System.setProperty("spring.profiles.active","test");
  }

  @Before
  public void beforeTest() {
    testDaoHelper = new TestDaoHelper(dataSource);
  }

  static URI getServiceUri(Long customerId, Long addressId) {
    return new UriTemplate(ADDRESS_RESOURCE_WITH_PARAM_URI).expand(customerId, addressId);
  }

  static URI doCreateAddress(Long customerId, Address address) {
    TestRestTemplate restTemplate = new TestRestTemplate();
    return restTemplate.postForLocation(getServiceUri(customerId, null), address);
  }

  static ErrorInfo doHttpPostAddressWithErrors(Long customerId, Address address) {
    TestRestTemplate restTemplate = new TestRestTemplate();
    URI uri = getServiceUri(customerId, null);
    ResponseEntity<ErrorInfo> responseEntity = restTemplate.postForEntity(
        uri, address, ErrorInfo.class);
    return responseEntity.getBody();
  }

  static List<Address> doHttpGetAddressResource(Long customerId, Long addressId) {

    TestRestTemplate restTemplate = new TestRestTemplate();
    URI uri = getServiceUri(customerId, addressId);

    if(addressId==null) {
      RequestEntity requestEntity = RequestEntity.get(uri).build();
      ResponseEntity<List<Address>> responseEntity = restTemplate.exchange(
          requestEntity, new ParameterizedTypeReference<List<Address>>() {});
      return responseEntity.getBody();
    } else {
      ResponseEntity<Address> responseEntity = restTemplate.getForEntity(uri, Address.class);
      return Arrays.asList(responseEntity.getBody());
    }

  }

  @Test
  public void testGetAddress() throws Exception {

    // retrieve all records in db
    List<Address> addressesInDb = testDaoHelper.getAddress(TEST_CUSTOMER_ID);
    Assert.assertEquals(2, addressesInDb.size());

    // do a HTTP get to retrieve all customer addresses
    List<Address> addressList = doHttpGetAddressResource(TEST_CUSTOMER_ID, null);

    // check that all addresses in db are in the result list
    TestHelper.assertEquals(addressesInDb, addressList);

  }

  @Test
  public void testGetAddress_ById() throws Exception {

    // retrieve record in db
    Address addressInDb = testDaoHelper.getAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);
    Assert.assertNotNull(addressInDb);

    // do a HTTP get to retrieve all customer addresses
    List<Address> addressList = doHttpGetAddressResource(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);

    // check that all addresses in db are in the result list
    Assert.assertEquals(1, addressList.size());
    TestHelper.assertAddress(addressInDb, addressList.get(0));

  }

  @Test
  public void testAddAddress() throws Exception {

    Address mockResAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);

    // retrieve all records in db
    List<Address> addressesInDb = testDaoHelper.getAddress(TEST_CUSTOMER_ID_NO_ADDRESS);
    Assert.assertEquals(0, addressesInDb.size());

    // create address
    URI addressUri = doCreateAddress(TEST_CUSTOMER_ID_NO_ADDRESS, mockResAddress);
    Assert.assertNotNull(addressUri);
    Long[] ids = TestHelper.extractCustomerAndAddressId(addressUri);
    Assert.assertEquals(ids[0], TEST_CUSTOMER_ID_NO_ADDRESS);
    Long resultAddressId = ids[1];

    // do a HTTP get to retrieve all customer addresses
    List<Address> addressList = doHttpGetAddressResource(TEST_CUSTOMER_ID_NO_ADDRESS, null);

    // retrieve addresses after http post
    List<Address> addressesInDbPostCreate = testDaoHelper.getAddress(TEST_CUSTOMER_ID_NO_ADDRESS);

    Assert.assertEquals("Address list size mismatch. input=" + addressList,
                        1, addressList.size());
    Assert.assertEquals(1, addressesInDbPostCreate.size());
    TestHelper.assertAddressTypeExist(AddressType.RESIDENTIAL, addressList, mockResAddress);

  }

  @Test
  public void testAddAddress_ExistingAddressType() throws Exception {

    Address mockResAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);

    // retrieve all records in db
    List<Address> addressesInDb = testDaoHelper.getAddress(TEST_CUSTOMER_ID);
    addressesInDb.removeIf(new Predicate<Address>() {
      @Override
      public boolean test(Address address) {
        return address.getType().equals(AddressType.RESIDENTIAL.name());
      }
    });
    Assert.assertEquals(1, addressesInDb.size());

    // create address with errors
    TestRestTemplate restTemplate = new TestRestTemplate();
    RequestEntity requestEntity = RequestEntity.post(getServiceUri(TEST_CUSTOMER_ID, null))
        .body(mockResAddress);
    ResponseEntity<ErrorInfo> responseEntity = restTemplate.exchange(
        requestEntity, ErrorInfo.class);

    // expect a conflict
    Assert.assertEquals(Response.SC_CONFLICT, responseEntity.getStatusCode().value());

  }

  @Test
  public void testAddAddress_WithErrors() throws Exception {

    Address mockResAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);
    mockResAddress.setCountry("XX");
    mockResAddress.setState("00");
    mockResAddress.setType("AA");

    // create address with errors
    ErrorInfo errorInfo = doHttpPostAddressWithErrors(TEST_CUSTOMER_ID, mockResAddress);

    Assert.assertEquals(Response.SC_BAD_REQUEST, errorInfo.getCode());
    TestHelper.assertErrorExist(errorInfo, "address.country", "address.state", "address.type");
  }

  @Test
  public void testUpdateAddress() throws Exception {

    Address mockResAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);

    // retrieve all records in db
    Address addressInDb = testDaoHelper.getAddress(TEST_CUSTOMER_ID, AddressType.RESIDENTIAL);
    Assert.assertNotNull(addressInDb);
    Assert.assertEquals(AddressType.RESIDENTIAL.name(), addressInDb.getType());

    // update address
    TestRestTemplate restTemplate = new TestRestTemplate();
    restTemplate.put(getServiceUri(TEST_CUSTOMER_ID, addressInDb.getId()), mockResAddress);

    // do a HTTP get to retrieve all customer addresses
    List<Address> addressList = doHttpGetAddressResource(TEST_CUSTOMER_ID, addressInDb.getId());

    // retrieve addresses after http post
    Address addressInDbPostUpdate = testDaoHelper.getAddress(TEST_CUSTOMER_ID, AddressType.RESIDENTIAL);

    Assert.assertEquals(1, addressList.size());
    TestHelper.assertAddressTypeExist(AddressType.RESIDENTIAL, addressList, mockResAddress);
    TestHelper.assertAddress(mockResAddress, addressInDbPostUpdate);

  }

  @Test
  public void testUpdateAddress_WithErrors() throws Exception {

    Address mockResAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);
    mockResAddress.setCountry("XX");
    mockResAddress.setState("00");
    mockResAddress.setType("AA");

    // update address with defective address fields
    TestRestTemplate restTemplate = new TestRestTemplate();
    URI uri = getServiceUri(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);
    RequestEntity requestEntity = RequestEntity.put(uri).body(mockResAddress);
    ResponseEntity<ErrorInfo> responseEntity = restTemplate.exchange(
        requestEntity, ErrorInfo.class);
    ErrorInfo errorInfo = responseEntity.getBody();

    Assert.assertEquals(Response.SC_BAD_REQUEST, errorInfo.getCode());
    TestHelper.assertErrorExist(errorInfo, "address.country", "address.state", "address.type");
  }

  @Test
  public void testDeleteAddress() throws Exception {

    Address mockResAddress = TestHelper.createMockAddress(AddressType.MAILING);

    // create address
    URI addressUri = doCreateAddress(TEST_CUSTOMER_ID_NO_ADDRESS, mockResAddress);
    Assert.assertNotNull(addressUri);
    Long[] ids = TestHelper.extractCustomerAndAddressId(addressUri);
    Assert.assertEquals(ids[0], TEST_CUSTOMER_ID_NO_ADDRESS);
    Long resultAddressId = ids[1];

    // retrieve record in db
    Address addressInDb = testDaoHelper.getAddress(TEST_CUSTOMER_ID_NO_ADDRESS, resultAddressId);
    Assert.assertNotNull(addressInDb);

    // delete address
    TestRestTemplate restTemplate = new TestRestTemplate();
    restTemplate.delete(getServiceUri(TEST_CUSTOMER_ID_NO_ADDRESS, resultAddressId));

    // record should be deleted in db
    Address deletedAddressInDb = testDaoHelper.getAddress(TEST_CUSTOMER_ID_NO_ADDRESS,
                                                          resultAddressId);
    Assert.assertNull(deletedAddressInDb);

  }

  @Test
  public void testDeleteAddress_NotFound() throws Exception {

    Address mockResAddress = TestHelper.createMockAddress(AddressType.MAILING);

    final Long SOME_NONE_EXISTENT_ADDRESS_ID = 10000L;
    URI addressUri = getServiceUri(TEST_CUSTOMER_ID_NO_ADDRESS, SOME_NONE_EXISTENT_ADDRESS_ID);

    // delete address
    TestRestTemplate restTemplate = new TestRestTemplate();
    RequestEntity requestEntity = RequestEntity.delete(addressUri).build();
    ResponseEntity<ErrorInfo> responseEntity = restTemplate.exchange(
        requestEntity, ErrorInfo.class);
    ErrorInfo errorInfo = responseEntity.getBody();

    Assert.assertEquals(Response.SC_NOT_FOUND, responseEntity.getStatusCode().value());

  }


}