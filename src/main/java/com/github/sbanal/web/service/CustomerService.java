package com.github.sbanal.web.service;

import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dto.Customer;

import java.util.List;

/**
 * CustomerService interface defines the common methods of a service which manages customer
 * profile and address details.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public interface CustomerService {

  Customer create(Customer customer);
  Customer read(Long customerId) throws CustomerNotFoundException;
  List<Address> getAddress(Long customerId);
  void update(Long customerId, Customer customer) throws CustomerNotFoundException,
                                                         AddressNotFoundException;
  void delete(Long customerId) throws CustomerNotFoundException;
  List<Customer> findCustomer(String name, Integer limit, Integer offset);

  Address addAddress(Long customerId, Address address) throws AddressExistException;
  void updateAddress(Long customerId, Long addressId, Address address) throws AddressNotFoundException;
  void deleteAddress(Long customerId, Long addressId) throws AddressNotFoundException;
  Address getAddress(Long customerId, Long addressId) throws AddressNotFoundException;



}
