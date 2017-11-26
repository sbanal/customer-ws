package com.github.sbanal.web.service;

import com.github.sbanal.web.TestHelper;
import com.github.sbanal.web.dao.AddressDao;
import com.github.sbanal.web.dao.AddressEntity;
import com.github.sbanal.web.dao.CustomerDao;
import com.github.sbanal.web.dao.CustomerEntity;
import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dto.AddressType;
import com.github.sbanal.web.dto.Customer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;

/**
 * CustomerServiceImplTest tests the customer RESTful web service method handlers.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@RunWith(JUnitParamsRunner.class)
public class CustomerServiceImplTest {

  private static final Long TEST_CUSTOMER_ID = 1L;
  private static final Long TEST_ADDRESS_ID = 2L;

  public static CustomerService createMockService(CustomerDao mockCustomerDao,
                                                  AddressDao mockAddressDao) {

    CustomerServiceImpl mockService = new CustomerServiceImpl();
    mockService.setCustomerDao(mockCustomerDao);
    mockService.setAddressDao(mockAddressDao);
    return mockService;
  }

  @Test
  public void testCreate() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.doNothing().when(mockCustomerDao).create(Mockito.<CustomerEntity>any());

    // simulate the call
    Customer customer = TestHelper.createMockCustomerComplete(TEST_CUSTOMER_ID);
    mockCustomerService.create(customer);

    // check how many calls to customer dao
    ArgumentCaptor<CustomerEntity> customerCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
    Mockito.verify(mockCustomerDao, Mockito.times(1)).create(customerCaptor.capture());
    TestHelper.assertCustomer(customer, customerCaptor.getValue());

    // check how many calls to address dao
    ArgumentCaptor<AddressEntity> addressCaptor = ArgumentCaptor.forClass(AddressEntity.class);
    Mockito.verify(mockAddressDao, Mockito.times(customer.getAddress().size()))
        .create(addressCaptor.capture());

    Assert.assertEquals(customer.getAddress().size(), addressCaptor.getAllValues().size());
    for(Address address : customer.getAddress()) {
      boolean addressFound = false;
      for(AddressEntity addressEntity : addressCaptor.getAllValues()) {
        if (address.getType().equals(addressEntity.getType())) {
          TestHelper.assertAddress(address, addressEntity);
          addressFound = true;
        }
      }
      Assert.assertTrue("Address " + address + " not found in captured params", addressFound);
    }

  }

  @Test(expected=ServiceException.class)
  public void testCreate_ServiceException() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.doThrow(new RuntimeException("MyBatis exception")).when(
        mockCustomerDao).create(Mockito.<CustomerEntity>any());

    // simulate the call
    Customer customer = TestHelper.createMockCustomerComplete(TEST_CUSTOMER_ID);
    mockCustomerService.create(customer);

  }

  @Test
  public void testRead() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    CustomerEntity mockEnityNoAddress = Mockito.mock(CustomerEntity.class);
    Mockito.when(mockEnityNoAddress.getId()).thenReturn(TEST_CUSTOMER_ID);
    Mockito.when(mockEnityNoAddress.getTitle()).thenReturn("Dr");
    Mockito.when(mockEnityNoAddress.getFirstName()).thenReturn("Some first name");
    Mockito.when(mockEnityNoAddress.getSurname()).thenReturn("Some surname");
    Mockito.when(mockEnityNoAddress.getMiddleName()).thenReturn("Some Mid Name");
    Mockito.when(mockEnityNoAddress.getInitials()).thenReturn("SLBB");
    Mockito.when(mockEnityNoAddress.getMaritalStatus()).thenReturn("M");
    Mockito.when(mockEnityNoAddress.getGender()).thenReturn("F");
    Mockito.when(mockEnityNoAddress.getCreditRating()).thenReturn(99);
    Mockito.when(mockEnityNoAddress.isExistingCustomer()).thenReturn(true);

    Mockito.when(mockCustomerDao.read(Mockito.eq(TEST_CUSTOMER_ID))).thenReturn(mockEnityNoAddress);

    // simulate the call
    Customer customer = mockCustomerService.read(TEST_CUSTOMER_ID);

    // check expected result
    Assert.assertEquals(Long.valueOf(TEST_CUSTOMER_ID), customer.getId());
    Assert.assertEquals("Dr", customer.getTitle());
    Assert.assertEquals("Some first name", customer.getFirstName());
    Assert.assertEquals("Some surname", customer.getSurname());
    Assert.assertEquals("Some Mid Name", customer.getMiddleName());
    Assert.assertEquals("SLBB", customer.getInitials());
    Assert.assertEquals("M", customer.getMaritalStatus());
    Assert.assertEquals("F", customer.getGender());
    Assert.assertEquals(99, customer.getCreditRating());
    Assert.assertEquals(true, customer.isExistingCustomer());

  }

  @Test(expected = CustomerNotFoundException.class)
  public void testRead_NotFound() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockCustomerDao.read(Mockito.eq(TEST_CUSTOMER_ID))).thenReturn(null);

    // simulate the call
    Customer customer = mockCustomerService.read(TEST_CUSTOMER_ID);

  }

  @Test(expected = ServiceException.class)
  public void testRead_ServiceException() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockCustomerDao.read(Mockito.eq(TEST_CUSTOMER_ID)))
        .thenThrow(new RuntimeException("test exception"));

    // simulate the call
    Customer customer = mockCustomerService.read(TEST_CUSTOMER_ID);

  }

  @Test
  public void testUpdate() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockCustomerDao.update(Mockito.<CustomerEntity>any())).thenReturn(1);
    Mockito.when(mockAddressDao.update(Mockito.<AddressEntity>any())).thenReturn(1);

    // simulate the call
    Customer customer = TestHelper.createMockCustomerComplete(TEST_CUSTOMER_ID);
    mockCustomerService.update(1L, customer);

    // check how many calls to customer dao
    ArgumentCaptor<CustomerEntity> customerCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
    Mockito.verify(mockCustomerDao, Mockito.times(1)).update(customerCaptor.capture());
    TestHelper.assertCustomer(customer, customerCaptor.getValue());

    // check how many calls to address dao
    ArgumentCaptor<AddressEntity> addressCaptor = ArgumentCaptor.forClass(AddressEntity.class);
    Mockito.verify(mockAddressDao, Mockito.times(customer.getAddress().size()))
        .update(addressCaptor.capture());

    Assert.assertEquals(customer.getAddress().size(), addressCaptor.getAllValues().size());
    for(Address address : customer.getAddress()) {
      boolean addressFound = false;
      for(AddressEntity addressEntity : addressCaptor.getAllValues()) {
        if (address.getType().equals(addressEntity.getType())) {
          TestHelper.assertAddress(address, addressEntity);
          addressFound = true;
        }
      }
      Assert.assertTrue("Address " + address + " not found in captured params", addressFound);
    }

  }

  @Test
  public void testUpdate_UpdateCustomerFailed() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockCustomerDao.update(Mockito.<CustomerEntity>any())).thenReturn(0);
    Mockito.when(mockAddressDao.update(Mockito.<AddressEntity>any())).thenReturn(1);

    try {

      // simulate the call
      Customer customer = TestHelper.createMockCustomerComplete(TEST_CUSTOMER_ID);
      mockCustomerService.update(1L, customer);
      Assert.assertFalse("Expected to fail but succeeded", true);

    } catch(CustomerNotFoundException ex) {

      Mockito.verify(mockCustomerDao, Mockito.times(1)).update(Mockito.<CustomerEntity>any());
      Mockito.verify(mockAddressDao, Mockito.times(0)).update(Mockito.<AddressEntity>any());

    }

  }

  @Test
  public void testUpdate_UpdateAddressFailed() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Customer customer = TestHelper.createMockCustomerComplete(TEST_CUSTOMER_ID);

    Mockito.when(mockCustomerDao.update(Mockito.<CustomerEntity>any())).thenReturn(1);
    Mockito.when(mockAddressDao.update(Mockito.<AddressEntity>any())).thenReturn(0);

    Assert.assertTrue(customer.getAddress().size() > 0);

    try {

      // simulate the call
      mockCustomerService.update(1L, customer);
      Assert.assertFalse("Expected to fail but succeeded", true);

    } catch(AddressNotFoundException ex) {

      Mockito.verify(mockCustomerDao, Mockito.times(1)).update(Mockito.<CustomerEntity>any());
      Mockito.verify(mockAddressDao, Mockito.times(1)).update(Mockito.<AddressEntity>any());

    }

  }

  @Test
  public void testUpdate_RuntimeException() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Customer customer = TestHelper.createMockCustomerComplete(TEST_CUSTOMER_ID);

    Mockito.when(mockCustomerDao.update(Mockito.<CustomerEntity>any()))
        .thenThrow(new RuntimeException("Test exception"));

    Assert.assertTrue(customer.getAddress().size() > 0);

    try {

      // simulate the call
      mockCustomerService.update(1L, customer);
      Assert.assertFalse("Expected to fail but succeeded", true);

    } catch(ServiceException ex) {

      Mockito.verify(mockCustomerDao, Mockito.times(1)).update(Mockito.<CustomerEntity>any());
      Mockito.verify(mockAddressDao, Mockito.times(0)).update(Mockito.<AddressEntity>any());

    }

  }

  @Test
  public void testDelete() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockCustomerDao.delete(Mockito.eq(TEST_CUSTOMER_ID))).thenReturn(1);

    // simulate the call
    mockCustomerService.delete(TEST_CUSTOMER_ID);

    // verify
    Mockito.verify(mockCustomerDao, Mockito.times(1)).delete(TEST_CUSTOMER_ID);
    Mockito.verify(mockAddressDao, Mockito.times(1)).deleteByCustomerId(TEST_CUSTOMER_ID);

  }

  @Test
  public void testDelete_CustomerNotFound() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockCustomerDao.delete(Mockito.eq(TEST_CUSTOMER_ID))).thenReturn(0);

    try {
      // simulate the call
      mockCustomerService.delete(TEST_CUSTOMER_ID);
      Assert.assertFalse("Expected to fail but succeeded", true);
    } catch(CustomerNotFoundException ex) {
      // verify
      Mockito.verify(mockCustomerDao, Mockito.times(1)).delete(TEST_CUSTOMER_ID);
      Mockito.verify(mockAddressDao, Mockito.times(1)).deleteByCustomerId(TEST_CUSTOMER_ID);
    }

  }

  @Test
  public void testDelete_RuntimeException() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockCustomerDao.delete(Mockito.eq(TEST_CUSTOMER_ID))).thenThrow(
        new RuntimeException("test exception"));

    try {
      // simulate the call
      mockCustomerService.delete(TEST_CUSTOMER_ID);
      Assert.assertFalse("Expected to fail but succeeded", true);
    } catch(ServiceException ex) {
      // verify
      Mockito.verify(mockCustomerDao, Mockito.times(1)).delete(TEST_CUSTOMER_ID);
      Mockito.verify(mockAddressDao, Mockito.times(1)).deleteByCustomerId(TEST_CUSTOMER_ID);
    }

  }

  @Test
  public void testAddAddress() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    Address mockAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);

    Mockito.doNothing().when(mockAddressDao).create(Mockito.<AddressEntity>any());

    // simulate the call
    Address resultAddress = mockCustomerService.addAddress(TEST_CUSTOMER_ID, mockAddress);

    // verify
    ArgumentCaptor<AddressEntity> captor = ArgumentCaptor.forClass(AddressEntity.class);
    Mockito.verify(mockAddressDao, Mockito.times(1)).create(captor.capture());
    AddressEntity addressEntity = captor.getValue();

    TestHelper.assertAddress(mockAddress, addressEntity);

  }

  @Test(expected = ServiceException.class)
  public void testAddAddress_Exception() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    Address mockAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);

    Mockito.doThrow(new RuntimeException("test exception")).when(mockAddressDao)
        .create(Mockito.<AddressEntity>any());

    // simulate the call
    mockCustomerService.addAddress(TEST_CUSTOMER_ID, mockAddress);

  }

  @Test
  public void testUpdateAddress() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    Address mockAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);

    Mockito.when(mockAddressDao.update(Mockito.<AddressEntity>any())).thenReturn(1);

    // simulate the call
    mockCustomerService.updateAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID, mockAddress);

    // verify
    ArgumentCaptor<AddressEntity> captor = ArgumentCaptor.forClass(AddressEntity.class);
    Mockito.verify(mockAddressDao, Mockito.times(1)).update(captor.capture());
    AddressEntity addressEntity = captor.getValue();

    TestHelper.assertAddress(mockAddress, addressEntity);

  }

  @Test
  public void testUpdateAddress_AddressNotFound() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    Address mockAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);

    Mockito.when(mockAddressDao.update(Mockito.<AddressEntity>any())).thenReturn(0);

    try {
      // simulate the call
      mockCustomerService.updateAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID, mockAddress);
      Assert.assertFalse("Expected to fail but succeeded", true);
    } catch(AddressNotFoundException ex) {
      // verify
      Mockito.verify(mockAddressDao, Mockito.times(1)).update(Mockito.<AddressEntity>any());
    }

  }

  @Test
  public void testUpdateAddress_RuntimeException() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    Address mockAddress = TestHelper.createMockAddress(AddressType.RESIDENTIAL);

    Mockito.when(mockAddressDao.update(Mockito.<AddressEntity>any()))
        .thenThrow(new RuntimeException("test exception"));

    try {
      // simulate the call
      mockCustomerService.updateAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID, mockAddress);
      Assert.assertFalse("Expected to fail but succeeded", true);
    } catch(ServiceException ex) {
      // verify
      Mockito.verify(mockAddressDao, Mockito.times(1)).update(Mockito.<AddressEntity>any());
    }

  }

  @Test
  public void testDeleteAddress() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockAddressDao.delete(
        Mockito.eq(TEST_ADDRESS_ID),
        Mockito.eq(TEST_CUSTOMER_ID))).thenReturn(1);

    // simulate the call
    mockCustomerService.deleteAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);

    // verify
    Mockito.verify(mockAddressDao, Mockito.times(1)).delete(TEST_ADDRESS_ID, TEST_CUSTOMER_ID);

  }

  @Test
  public void testDeleteAddress_AddressNotFound() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockAddressDao.delete(
        Mockito.eq(TEST_ADDRESS_ID),
        Mockito.eq(TEST_CUSTOMER_ID))).thenReturn(0);

    try {

      // simulate the call
      mockCustomerService.deleteAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);
      Assert.assertFalse("Expected to fail but succeeded", true);

    } catch(AddressNotFoundException ex) {

      Mockito.verify(mockAddressDao, Mockito.times(1)).delete(
          Mockito.eq(TEST_ADDRESS_ID),
          Mockito.eq(TEST_CUSTOMER_ID));

    }

  }

  @Test
  public void testDeleteAddress_RuntimeException() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);

    Mockito.when(mockAddressDao.delete(
        Mockito.eq(TEST_ADDRESS_ID),
        Mockito.eq(TEST_CUSTOMER_ID))).thenThrow(new RuntimeException("test exception"));

    try {

      // simulate the call
      mockCustomerService.deleteAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);
      Assert.assertFalse("Expected to fail but succeeded", true);

    } catch(ServiceException ex) {

      Mockito.verify(mockAddressDao, Mockito.times(1)).delete(
          Mockito.eq(TEST_ADDRESS_ID),
          Mockito.eq(TEST_CUSTOMER_ID));

    }

  }

  @Test
  public void testGetAddress() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    AddressEntity mockAddress = TestHelper.createMockAddressEntity(AddressType.RESIDENTIAL);

    Mockito.when(mockAddressDao.read(
        Mockito.eq(TEST_ADDRESS_ID),
        Mockito.eq(TEST_CUSTOMER_ID))).thenReturn(mockAddress);

    // simulate the call
    Address result =mockCustomerService.getAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);

    TestHelper.assertAddress(mockAddress, result);

  }

  @Test
  public void testGetAddress_AddressNotFound() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    AddressEntity mockAddress = TestHelper.createMockAddressEntity(AddressType.RESIDENTIAL);

    Mockito.when(mockAddressDao.read(
        Mockito.eq(TEST_ADDRESS_ID),
        Mockito.eq(TEST_CUSTOMER_ID))).thenReturn(null);

    try {

      // simulate the call
      Address result = mockCustomerService.getAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);
      Assert.assertFalse("Expected to fail but succeeded", true);

    } catch(AddressNotFoundException ex) {

      Mockito.verify(mockAddressDao, Mockito.times(1)).read(
          Mockito.eq(TEST_ADDRESS_ID),
          Mockito.eq(TEST_CUSTOMER_ID));

    }

  }

  @Test
  public void testGetAddress_RuntimeException() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    AddressEntity mockAddress = TestHelper.createMockAddressEntity(AddressType.RESIDENTIAL);

    Mockito.when(mockAddressDao.read(
        Mockito.eq(TEST_ADDRESS_ID),
        Mockito.eq(TEST_CUSTOMER_ID))).thenThrow(new RuntimeException("test exception"));

    try {

      // simulate the call
      Address result = mockCustomerService.getAddress(TEST_CUSTOMER_ID, TEST_ADDRESS_ID);
      Assert.assertFalse("Expected to fail but succeeded", true);

    } catch(ServiceException ex) {

      Mockito.verify(mockAddressDao, Mockito.times(1)).read(
          Mockito.eq(TEST_ADDRESS_ID),
          Mockito.eq(TEST_CUSTOMER_ID));

    }

  }

  @Test
  public void testGetAddressById() throws Exception {

    // create mocks
    CustomerDao mockCustomerDao = Mockito.mock(CustomerDao.class);
    AddressDao mockAddressDao = Mockito.mock(AddressDao.class);
    CustomerService mockCustomerService = createMockService(mockCustomerDao, mockAddressDao);
    AddressEntity mockAddress1 = TestHelper.createMockAddressEntity(AddressType.RESIDENTIAL);
    AddressEntity mockAddress2 = TestHelper.createMockAddressEntity(AddressType.MAILING);

    List<AddressEntity> mockEntityResult = Arrays.asList(mockAddress1, mockAddress2);
    Mockito.when(mockCustomerDao.getAddress(Mockito.eq(TEST_CUSTOMER_ID)))
        .thenReturn(mockEntityResult);

    // simulate the call
    List<Address> result = mockCustomerService.getAddress(TEST_CUSTOMER_ID);

    Assert.assertEquals(mockEntityResult.size(), result.size());
    for(Address address : result) {
      boolean found = false;
      for(AddressEntity entity : mockEntityResult) {
        if (address.getType().equals(entity.getType())) {
          TestHelper.assertAddress(address, entity);
          found = true;
          break;
        }
      }
      Assert.assertTrue("Address " + address + " not found in entity list", found);
    }

  }

}