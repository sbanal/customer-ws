package com.github.sbanal.web;

import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.service.AddressExistException;
import com.github.sbanal.web.service.CustomerService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import javax.validation.Valid;

/**
 * AddressResource class is the main controller of all requests related to address management.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@RestController
@RequestMapping(value = "/api/customers/{customerId}/addresses")
public final class AddressResource {

  private static final Logger LOGGER = LogManager.getLogger(AddressResource.class);

  @Autowired
  private CustomerService customerService;


  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Address>> getAddress(@PathVariable Long customerId) {

    List<Address> addressList = customerService.getAddress(customerId);
    if(addressList.isEmpty()) {
      return new ResponseEntity<List<Address>>(addressList, null, HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<Address>>(addressList, null, HttpStatus.OK);

  }

  @RequestMapping(value = "/{addressId}", method = RequestMethod.GET)
  public ResponseEntity<Address> getAddress(@PathVariable Long customerId, @PathVariable Long addressId)
      throws AddressNotFoundException {

    Address address =  customerService.getAddress(customerId, addressId);
    return new ResponseEntity<Address>(address, null, HttpStatus.OK);

  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Address> addAddress(@PathVariable Long customerId,
                                            @RequestBody @Valid Address address,
                                            BindingResult bindingResult)
      throws AddressExistException {

    if (bindingResult.hasErrors()) {
      throw new RequestValidationException("Validation error occurred while processing your request",
                                           bindingResult.getFieldErrors());
    }

    Address newAddress = customerService.addAddress(customerId, address);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(ServletUriComponentsBuilder
                                .fromCurrentRequest().path("/{id}")
                                .buildAndExpand(newAddress.getId()).toUri());

    return new ResponseEntity<Address>(newAddress, httpHeaders, HttpStatus.OK);

  }

  @RequestMapping(value = "/{addressId}", method = RequestMethod.PUT)
  public void updateAddress(@PathVariable Long customerId, @PathVariable Long addressId,
                            @RequestBody @Valid Address address,
                            BindingResult bindingResult) throws AddressNotFoundException {

    if (bindingResult.hasErrors()) {
      throw new RequestValidationException("Validation error occurred while processing your request",
                                           bindingResult.getFieldErrors());
    }

    customerService.updateAddress(customerId, addressId, address);

  }

  @RequestMapping(value = "/{addressId}", method = RequestMethod.DELETE)
  public void deleteAddress(@PathVariable Long customerId, @PathVariable Long addressId)
      throws AddressNotFoundException {

    customerService.deleteAddress(customerId, addressId);

  }

}
