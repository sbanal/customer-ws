package com.github.sbanal.web.service;

import com.github.sbanal.web.dao.CustomerEntity;
import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dao.AddressEntity;
import com.github.sbanal.web.dto.Customer;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ServiceHelper class is a utility class used to transform entity to/from
 * dto DTOs.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
final class ServiceHelper {

  public static CustomerEntity toEntity(Customer model) {
    CustomerEntity entity = new CustomerEntity();
    BeanUtils.copyProperties(model, entity, "address");
    for(Address address : model.getAddress()) {
      entity.putAddress(ServiceHelper.toEntity(address));
    }
    return entity;
  }

  public static AddressEntity toEntity(Address address) {
    AddressEntity entity = new AddressEntity();
    BeanUtils.copyProperties(address, entity);
    return entity;
  }

  public static Customer toModel(CustomerEntity entity) {
    Customer model = new Customer();
    BeanUtils.copyProperties(entity, model, "address");
    for(AddressEntity address : entity.getAddress()) {
      model.putAddress(ServiceHelper.toModel(address));
    }
    return model;
  }

  public static Address toModel(AddressEntity address) {
    Address entity = new Address();
    BeanUtils.copyProperties(address, entity);
    return entity;
  }

  public static List<Address> toModel(List<AddressEntity> addressList) {
    List<Address> modelList = new ArrayList<Address>();
    for(AddressEntity addressEntity : addressList) {
      modelList.add(toModel(addressEntity));
    }
    return modelList;
  }

}
