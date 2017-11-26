package com.github.sbanal.web.service;

import com.github.sbanal.web.dao.CustomerEntity;
import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dto.AddressType;
import com.github.sbanal.web.dao.AddressDao;
import com.github.sbanal.web.dao.AddressEntity;
import com.github.sbanal.web.dao.CustomerDao;
import com.github.sbanal.web.dto.Customer;

import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * CustomerServiceImpl class provides all the business logic processing
 * related to customer records management.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public final class CustomerServiceImpl implements CustomerService {

  private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class);

  private static final int MAX_FIND_RESULT_LIMIT = 20;

  @Autowired
  private CustomerDao customerDao;

  @Autowired
  private AddressDao addressDao;


  public CustomerDao getCustomerDao() {
    return customerDao;
  }

  public void setCustomerDao(CustomerDao customerDao) {
    this.customerDao = customerDao;
  }

  public AddressDao getAddressDao() {
    return addressDao;
  }

  public void setAddressDao(AddressDao addressDao) {
    this.addressDao = addressDao;
  }

  @Override
  @Transactional
  public Customer create(Customer customer) {

    try {
      CustomerEntity customerEntity = ServiceHelper.toEntity(customer);
      customerDao.create(customerEntity);

      for(Address address : customer.getAddress()) {
        AddressEntity addressEntity = ServiceHelper.toEntity(address);
        addressEntity.setCustomerId(customerEntity.getId());
        addressDao.create(addressEntity);
      }

      customer.setId(customerEntity.getId());
      return customer;
    } catch(Exception e) {
      throw new ServiceException("Unable to process insert request", e);
    }

  }

  @Override
  public Customer read(Long id) throws CustomerNotFoundException {
    try {
      CustomerEntity entity = customerDao.read(id);
      if (entity == null) {
        throw new CustomerNotFoundException("Customer " + id + " not found");
      }
      return ServiceHelper.toModel(entity);
    } catch(CustomerNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ServiceException("Unable to process read request", e);
    }
  }

  @Override
  public List<Customer> findCustomer(String name, Integer offset, Integer limit) {

    try {

      if(limit>MAX_FIND_RESULT_LIMIT) {
        limit = MAX_FIND_RESULT_LIMIT;
      }

      RowBounds rowBounds = new RowBounds(offset, limit);
      List<CustomerEntity> customerEntityList = null;
      if (name == null || name.isEmpty()) {
        customerEntityList = customerDao.findAll(rowBounds);
      } else {
        customerEntityList = customerDao.find(name + "%", rowBounds);
      }

      List<Customer> customerList = new ArrayList<Customer>();
      for (CustomerEntity entity : customerEntityList) {
        customerList.add(ServiceHelper.toModel(entity));
      }

      return customerList;
    } catch(Exception e) {
      throw new ServiceException("Unable to process proces search request", e);
    }

  }

  @Override
  @Transactional
  public void update(Long id, Customer customer) throws CustomerNotFoundException,
                                                        AddressNotFoundException {

    try {
      CustomerEntity customerEntity = ServiceHelper.toEntity(customer);
      customerEntity.setId(id);

      int customerUpdate = customerDao.update(customerEntity);
      if (customerUpdate < 1) {
        throw new CustomerNotFoundException("Customer " + id + " not found");
      }

      for(Address address : customer.getAddress()) {
        AddressEntity addressEntity = ServiceHelper.toEntity(address);
        addressEntity.setCustomerId(customerEntity.getId());
        int addressUpdate = addressDao.update(addressEntity);
        if (addressUpdate < 1) {
          throw new AddressNotFoundException("Address " + addressEntity.getId() + " not updated,"
                                             + " result=" + addressUpdate);
        }
      }

    } catch(CustomerNotFoundException e) {
      throw e;
    } catch(AddressNotFoundException e) {
      throw e;
    } catch(Exception e) {
      throw new ServiceException("Unable to process update request", e);
    }

  }

  @Override
  @Transactional
  public void delete(Long id) throws CustomerNotFoundException {
    try {

      addressDao.deleteByCustomerId(id);

      int result = customerDao.delete(id);
      if (result < 1) {
        throw new CustomerNotFoundException("Customer " + id + " not found");
      }

    } catch(CustomerNotFoundException e) {
      throw e;
    } catch(Exception e) {
      e.printStackTrace();
      throw new ServiceException("Unable to process delete request", e);
    }
  }

  @Override
  @Transactional
  public Address addAddress(Long customerId, Address address) throws AddressExistException {
    try {

      AddressEntity inputAddress = ServiceHelper.toEntity(address);
      inputAddress.setCustomerId(customerId);

      AddressType type = AddressType.valueOf(address.getType());
      AddressEntity existingAddress = addressDao.readAddressByType(customerId, type);
      if(existingAddress!=null) {
        throw new AddressExistException("Address of type " + address.getType() + " already exist.");
      }

      addressDao.create(inputAddress);
      address.setId(inputAddress.getId());

      return address;
    } catch(AddressExistException e) {
      throw e;
    } catch(Exception e) {
      throw new ServiceException("Unable to process add address request", e);
    }
  }

  @Override
  @Transactional
  public void updateAddress(Long customerId, Long addressId, Address address)
      throws AddressNotFoundException {
    try {
      AddressEntity entity = ServiceHelper.toEntity(address);
      entity.setId(addressId);
      entity.setCustomerId(customerId);
      int result = addressDao.update(entity);
      if (result < 1) {
        throw new AddressNotFoundException("Address " + addressId + " not found");
      }
    } catch(AddressNotFoundException e) {
      throw e;
    } catch(Exception e) {
      throw new ServiceException("Unable to process update address request", e);
    }
  }

  @Override
  @Transactional
  public void deleteAddress(Long customerId, Long addressId) throws AddressNotFoundException {
    try {
      int result = addressDao.delete(addressId, customerId);
      if (result < 1) {
        throw new AddressNotFoundException("Address " + addressId + " not found");
      }
    } catch(AddressNotFoundException e) {
      throw e;
    } catch(Exception e) {
      throw new ServiceException("Unable to process update address request", e);
    }
  }

  @Override
  public List<Address> getAddress(Long customerId) {
    try {
      List<AddressEntity> addressList = customerDao.getAddress(customerId);
      return ServiceHelper.toModel(addressList);
    } catch(Exception e) {
      throw new ServiceException("Unable to process get address list request", e);
    }
  }

  @Override
  public Address getAddress(Long customerId, Long addressId) throws AddressNotFoundException {
    try {
      AddressEntity addressEntity = addressDao.read(addressId, customerId);
      if(addressEntity==null) {
        throw new AddressNotFoundException("Address " + addressId + " not found");
      }
      return ServiceHelper.toModel(addressEntity);
    } catch(AddressNotFoundException e) {
      throw e;
    } catch(Exception e) {
      throw new ServiceException("Unable to process get address request", e);
    }
  }

}
