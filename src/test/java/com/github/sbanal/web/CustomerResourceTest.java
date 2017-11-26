package com.github.sbanal.web;


import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dto.AddressType;
import com.github.sbanal.web.dto.Customer;
import com.github.sbanal.web.dto.ErrorInfo;
import java.net.URI;
import java.util.List;
import javax.sql.DataSource;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

/**
 * CustomerRestControllerTest runs integration test against REST service CustomerRestController.
 * This does not test /customers/<id>/addresses endpoint, check AddressResourceTest.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class,
    properties = {"server.port=9000"},
    webEnvironment = WebEnvironment.DEFINED_PORT)
public class CustomerResourceTest {

  private static final Long TEST_RESOURCE_ID = 1L;
  private static final String CUSTOMER_RESOURCE_URI = "http://localhost:9000/api/customers";
  private static final String CUSTOMER_RESOURCE_FIND_URI = "http://localhost:9000/api/customers?limit={limit}&offset={offset}&name={name}";
  private static final String CUSTOMER_RESOURCE_WITH_PARAM_URI = "http://localhost:9000/api/customers/{id}";
  private static final Logger LOGGER = LogManager.getLogger(CustomerResourceTest.class);

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

  @Test
  public void testCreateCustomer() {

    Customer mockCustomer = TestHelper.createMockCustomerComplete();

    TestRestTemplate restTemplate = new TestRestTemplate();
    URI entityUri = restTemplate.postForLocation(CUSTOMER_RESOURCE_URI, mockCustomer);

    mockCustomer.setId(TestHelper.extractCustomerId(entityUri));
    Customer record = testDaoHelper.getCustomer(mockCustomer.getId());
    TestHelper.assertCustomer(mockCustomer, record);

  }

  @Test
  public void testCreateCustomer_Duplicate_AddressType() {

    Customer mockCustomer = TestHelper.createMockCustomer();
    Address mockAddress1 = TestHelper.createMockAddress(AddressType.RESIDENTIAL);
    Address mockAddress2 = TestHelper.createMockAddress(AddressType.RESIDENTIAL);
    mockCustomer.putAddress(mockAddress1);
    mockCustomer.putAddress(mockAddress2);

    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity<ErrorInfo> response = restTemplate.postForEntity(CUSTOMER_RESOURCE_URI,
                                                                    mockCustomer,
                                                                    ErrorInfo.class);

    Assert.assertEquals(Response.SC_BAD_REQUEST, response.getStatusCode().value());

  }


  @Test
  public void testGetCustomer() throws Exception {

    Customer mockCustomer = TestHelper.createMockCustomerComplete();

    // create a new customer resource
    TestRestTemplate restTemplate = new TestRestTemplate();
    URI entityUri = restTemplate.postForLocation(CUSTOMER_RESOURCE_URI, mockCustomer);
    mockCustomer.setId(TestHelper.extractCustomerId(entityUri));

    // get the created resource
    ResponseEntity<Customer> response = restTemplate.getForEntity(entityUri, Customer.class);

    Customer record = testDaoHelper.getCustomer(mockCustomer.getId());
    TestHelper.assertCustomer(mockCustomer, record);
    TestHelper.assertCustomer(mockCustomer, response.getBody());

  }

  @Test
  public void testFindCustomer() throws Exception {

    TestRestTemplate restTemplate = new TestRestTemplate();

    final String nameSearchKeyword = "go";
    URI uri = new UriTemplate(CUSTOMER_RESOURCE_FIND_URI).expand(1, 0, nameSearchKeyword);

    RequestEntity requestEntity = RequestEntity.get(uri).build();
    ResponseEntity<List<Customer>> responseEntity = restTemplate.exchange(
        requestEntity, new ParameterizedTypeReference<List<Customer>>() {
        });

    List<Customer> listCustomer = responseEntity.getBody();
    Assert.assertEquals(1, listCustomer.size());
    Customer customer = listCustomer.get(0);
    Assert.assertTrue(
        customer.getFirstName().contains(nameSearchKeyword) ||
        customer.getSurname().contains(nameSearchKeyword) ||
        customer.getMiddleName().contains(nameSearchKeyword)
    );

    Customer record = testDaoHelper.getCustomer(2L);
    TestHelper.assertCustomer(record, customer);

  }

  @Test
  public void testGetCustomer_NotFound() throws Exception {

    // create invalid customer resource uri
    URI entityUri =  new URI(CUSTOMER_RESOURCE_URI + "/1111");
    LOGGER.info("URI " + entityUri);

    // get the created resource
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new TestResponseHandler(Response.SC_NOT_FOUND));
    ResponseEntity<String> response = restTemplate.exchange(entityUri, HttpMethod.GET, null,
                                                        String.class);
    Assert.assertEquals(Response.SC_NOT_FOUND, response.getStatusCode().value());

  }

  @Test
  public void testUpdateCustomer() throws Exception {

    Customer recordPreUpdate = testDaoHelper.getCustomer(TEST_RESOURCE_ID);

    TestRestTemplate restTemplate = new TestRestTemplate();
    Customer mockCustomer = TestHelper.createMockCustomerComplete(TEST_RESOURCE_ID);
    mockCustomer.setId(TEST_RESOURCE_ID);

    restTemplate.put(CUSTOMER_RESOURCE_WITH_PARAM_URI, mockCustomer, TEST_RESOURCE_ID);

    Customer recordPostUpdate = testDaoHelper.getCustomer(TEST_RESOURCE_ID);
    Assert.assertEquals(recordPreUpdate.getId(), recordPostUpdate.getId());
    TestHelper.assertCustomer(mockCustomer, recordPostUpdate);

  }

  @Test
  public void testUpdateCustomer_WithError() throws Exception {

    Customer recordPreUpdate = testDaoHelper.getCustomer(TEST_RESOURCE_ID);
    recordPreUpdate.setGender("AA");
    recordPreUpdate.setMaritalStatus("XX");
    recordPreUpdate.setCreditRating(10000);
    recordPreUpdate.setTitle(null);
    recordPreUpdate.setFirstName(null);
    recordPreUpdate.setMiddleName(null);
    recordPreUpdate.setSurname(null);
    recordPreUpdate.setInitials(null);

    TestRestTemplate restTemplate = new TestRestTemplate();
    Customer mockCustomer = TestHelper.createMockCustomerComplete(TEST_RESOURCE_ID);
    mockCustomer.setId(TEST_RESOURCE_ID);

    URI serviceUri = new UriTemplate(CUSTOMER_RESOURCE_WITH_PARAM_URI).expand(TEST_RESOURCE_ID);
    RequestEntity requestEntity = RequestEntity.put(serviceUri).body(recordPreUpdate);
    ResponseEntity<ErrorInfo> responseEntity = restTemplate.exchange(
        requestEntity, ErrorInfo.class);
    ErrorInfo errorInfo = responseEntity.getBody();

    Assert.assertEquals(Response.SC_BAD_REQUEST, errorInfo.getCode());
    TestHelper.assertErrorExist(errorInfo,
                                "customer.title",
                                "customer.firstName",
                                "customer.surname",
                                "customer.middleName",
                                "customer.initials",
                                "customer.gender",
                                "customer.maritalStatus",
                                "customer.creditRating");

  }

  @Test
  public void testDeleteCustomer() throws Exception {

    Customer recordPreUpdate = testDaoHelper.getCustomer(TEST_RESOURCE_ID);
    Assert.assertNotNull(recordPreUpdate);

    TestRestTemplate restTemplate = new TestRestTemplate();
    restTemplate.delete(CUSTOMER_RESOURCE_WITH_PARAM_URI, TEST_RESOURCE_ID);

    Customer recordPostUpdate = testDaoHelper.getCustomer(TEST_RESOURCE_ID);
    Assert.assertNull(recordPostUpdate);

  }

}