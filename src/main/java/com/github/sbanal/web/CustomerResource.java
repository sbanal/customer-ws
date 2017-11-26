package com.github.sbanal.web;

import com.github.sbanal.web.service.CustomerNotFoundException;
import com.github.sbanal.web.service.CustomerService;
import com.github.sbanal.web.dto.Customer;
import com.github.sbanal.web.service.AddressNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import javax.validation.Valid;

/**
 * CustomerResource is a RESTful web service which handles all the create, read, update and
 * delete operations of a customer resource.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@RestController
@RequestMapping(value = "/api/customers")
public final class CustomerResource {

  private static final Logger LOGGER = LogManager.getLogger(CustomerResource.class);

  @Autowired
  private CustomerService customerService;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Customer>> findCustomer(
      @RequestParam(value="name", defaultValue="") String name,
      @RequestParam(value="offset", defaultValue="0") Integer offset,
      @RequestParam(value="limit", defaultValue="10") Integer limit)
      throws CustomerNotFoundException {

    List<Customer> customerList = customerService.findCustomer(name, offset, limit);
    if(customerList.isEmpty()) {
      return new ResponseEntity<List<Customer>>(customerList, null, HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<Customer>>(customerList, null, HttpStatus.OK);

  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Customer> createCustomer(@RequestBody @Valid Customer customer,
                                                 BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      throw new RequestValidationException("Invalid customer", bindingResult.getFieldErrors());
    }

    Customer newCustomer = customerService.create(customer);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(ServletUriComponentsBuilder
                                .fromCurrentRequest().path("/{id}")
                                .buildAndExpand(newCustomer.getId()).toUri());

    return new ResponseEntity<Customer>(null, httpHeaders, HttpStatus.CREATED);

  }

  @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
  public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId)
      throws CustomerNotFoundException {

    Customer customer = customerService.read(customerId);
    return new ResponseEntity<Customer>(customer, null, HttpStatus.OK);

  }

  @RequestMapping(value = "/{customerId}", method = RequestMethod.PUT)
  public void updateCustomer(@PathVariable Long customerId,
                             @RequestBody @Valid Customer customer,
                             BindingResult bindingResult)
      throws CustomerNotFoundException, AddressNotFoundException {

    if (bindingResult.hasErrors()) {
      throw new RequestValidationException("Invalid customer", bindingResult.getFieldErrors());
    }

    customerService.update(customerId, customer);

  }

  @RequestMapping(value = "/{customerId}", method = RequestMethod.DELETE)
  public void deleteCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {

      customerService.delete(customerId);

  }


}
