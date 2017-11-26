package com.github.sbanal.web.dao;


import com.github.sbanal.web.dto.Gender;
import com.github.sbanal.web.dto.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomerEntity class is a POJO of a customer record and is used by DAO to read/update/create
 * customer records to/from persistence layer. This is similar to an Entity class
 * used in JAP ORM tools.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class CustomerEntity {

  private Long id;
  private String title;
  private String firstName;
  private String middleName;
  private String surname;
  private String initials;
  private Gender gender;
  private MaritalStatus maritalStatus;
  private int creditRating;
  private boolean existingCustomer;
  private List<AddressEntity> address = new ArrayList<AddressEntity>();

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
    return gender.name();
  }

  public String getGenderCode() {
    return gender.value();
  }

  public void setGender(String gender) {
    this.gender = Gender.valueOf(gender);
  }

  public void setGenderCode(String gender) {
    this.gender = Gender.toEnum(gender);
  }

  public String getMaritalStatus() {
    return maritalStatus.name();
  }

  public String getMaritalStatusCode() {
    return maritalStatus.value();
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = MaritalStatus.valueOf(maritalStatus);
  }

  public void setMaritalStatusCode(String maritalStatus) {
    this.maritalStatus = MaritalStatus.toEnum(maritalStatus);
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

  public List<AddressEntity> getAddress() {
    return address;
  }

  public void setAddress(List<AddressEntity> address) {
    this.address = address;
  }
  public void putAddress(AddressEntity address) {
    this.address.add(address);
  }

  @Override
  public String toString() {
    return "CustomerEntity{" +
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
