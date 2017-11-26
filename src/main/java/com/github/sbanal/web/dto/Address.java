package com.github.sbanal.web.dto;


import com.github.sbanal.web.validator.ValidCountryCode;
import com.github.sbanal.web.validator.ValidEnum;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Address class encapsulates address details.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class Address {

  private Long id;
  @ValidEnum(enumType=AddressType.class, message = "Invalid address type '${validatedValue}'")
  private String type;
  @Size(min=1, message="Invalid address street type '${validatedValue}'. Must not be empty.")
  private String streetType;
  @Size(min=1, message="Invalid address street name '${validatedValue}'. Must not be empty.")
  private String streetName;
  @Size(min=1, message="Invalid address suburb '${validatedValue}'. Must not be empty.")
  private String suburb;
  @Size(min=1, message="Invalid address city '${validatedValue}'. Must not be empty.")
  private String city;
  @ValidEnum(enumType=AddressState.class, message = "Invalid state '${validatedValue}'. Must not be empty.")
  private String state;
  @ValidCountryCode(message = "Invalid ISO2 country code '${validatedValue}'")
  private String country;
  @Pattern(regexp="[0-9]{4}", message = "Invalid postal code '${validatedValue}'. Must be a 4 digit number")
  private String postalCode;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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
    return state;
  }

  public void setState(String addressState) {
    this.state = addressState;
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

  @Override
  public String toString() {
    return "Address{" +
           "id=" + id +
           ", type='" + type + '\'' +
           ", streetType='" + streetType + '\'' +
           ", streetName='" + streetName + '\'' +
           ", suburb='" + suburb + '\'' +
           ", city='" + city + '\'' +
           ", state='" + state + '\'' +
           ", country='" + country + '\'' +
           ", postalCode='" + postalCode + '\'' +
           '}';
  }
}
