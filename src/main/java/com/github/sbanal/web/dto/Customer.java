package com.github.sbanal.web.dto;

import com.github.sbanal.web.validator.UniqueAddressType;
import com.github.sbanal.web.validator.ValidEnum;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

/**
 * Customer class encapsulate customer details.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class Customer {

  private Long id;
  @NotEmpty(message="Invalid customer title '${validatedValue}'")
  private String title;
  @NotEmpty(message="Invalid customer first name '${validatedValue}'")
  private String firstName;
  @NotEmpty(message="Invalid customer middle name '${validatedValue}'")
  private String middleName;
  @NotEmpty(message="Invalid customer surname '${validatedValue}'")
  private String surname;
  @NotEmpty(message="Invalid customer initials '${validatedValue}'")
  private String initials;
  @ValidEnum(enumType=Gender.class, message="Invalid customer gender '${validatedValue}'")
  private String gender;
  @ValidEnum(enumType=MaritalStatus.class, message="Invalid customer marital status '${validatedValue}'")
  private String maritalStatus;
  @Range(min=0, max=100, message="Invalid customer credit rating '${validatedValue}'. Min={min} and Max={max}")
  private int creditRating;
  private boolean existingCustomer;

  @Valid
  @UniqueAddressType
  private final List<Address> address = new ArrayList<Address>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getInitials() {
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public int getCreditRating() {
    return creditRating;
  }

  public void setCreditRating(int creditRating) {
    this.creditRating = creditRating;
  }

  public boolean isExistingCustomer() {
    return existingCustomer;
  }

  public void setExistingCustomer(boolean existingCustomer) {
    this.existingCustomer = existingCustomer;
  }

  public List<Address> getAddress() {
    return address;
  }

  public void addAddress(List<Address> addressList) {
    if(addressList==null) {
      return;
    }
    address.addAll(addressList);
  }

  public void putAddress(Address address) {
    this.address.add(address);
  }

  @Override
  public String toString() {
    return "Customer{" +
           "id=" + id +
           ", title='" + title + '\'' +
           ", firstName='" + firstName + '\'' +
           ", middleName='" + middleName + '\'' +
           ", surname='" + surname + '\'' +
           ", initials='" + initials + '\'' +
           ", gender='" + gender + '\'' +
           ", maritalStatus='" + maritalStatus + '\'' +
           ", creditRating=" + creditRating +
           ", existingCustomer=" + existingCustomer +
           ", address=" + address +
           '}';
  }
}
