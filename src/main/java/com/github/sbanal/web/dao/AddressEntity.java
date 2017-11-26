package com.github.sbanal.web.dao;

import com.github.sbanal.web.dto.AddressState;
import com.github.sbanal.web.dto.AddressType;

/**
 * AddressEntity class is a POJO of an address record and is used by DAO to read/update/create
 * address records to/from persistence layer. This is similar to an Entity class
 * used in JAP ORM tools.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class AddressEntity {

  private Long id;
  private Long customerId;
  private AddressType type;
  private String streetType;
  private String streetName;
  private String suburb;
  private String city;
  private AddressState state;
  private String country;
  private String postalCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getType() {
    return type.name();
  }

  public void setType(String type) {
    this.type = AddressType.valueOf(type);
  }

  public String getTypeCode() {
    return type.getCode();
  }

  public void setTypeCode(String type) {
    this.type = AddressType.toEnum(type);
  }


  public String getStreetType() {
    return streetType;
  }

  public void setStreetType(String streetType) {
    this.streetType = streetType;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public String getSuburb() {
    return suburb;
  }

  public void setSuburb(String suburb) {
    this.suburb = suburb;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state.name();
  }

  public void setState(String state) {
    this.state = AddressState.valueOf(state);
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

}
