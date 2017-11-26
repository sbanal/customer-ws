package com.github.sbanal.web;

import com.github.sbanal.web.dto.Address;
import com.github.sbanal.web.dto.AddressType;
import com.github.sbanal.web.dto.Customer;
import com.github.sbanal.web.dto.Gender;
import com.github.sbanal.web.dto.MaritalStatus;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * TestDaoHelper is a helper class used for retrieving records from database.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class TestDaoHelper {

  static class AddressRowMapper implements RowMapper<Address> {

    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
      Address address = new Address();
      address.setId(rs.getLong("id"));
      address.setType(AddressType.toEnum(rs.getString("address_type")).name());
      address.setStreetType(rs.getString("street_type"));
      address.setStreetName(rs.getString("street_name"));
      address.setSuburb(rs.getString("suburb"));
      address.setState(rs.getString("state"));
      address.setCity(rs.getString("city"));
      address.setCountry(rs.getString("country"));
      address.setPostalCode(rs.getString("postal_code"));
      return address;
    }

  }

  private final DataSource dataSource;

  public TestDaoHelper(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Customer getCustomer(Long id) {
    try {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      Customer customer = jdbcTemplate.queryForObject(
          "SELECT * FROM customer WHERE id = ?",
          new Object[]{id},
          new RowMapper<Customer>() {
            public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {

              Customer customer = new Customer();
              customer.setId(rs.getLong("id"));
              customer.setTitle(rs.getString("title"));
              customer.setMiddleName(rs.getString("middle_name"));
              customer.setFirstName(rs.getString("first_name"));
              customer.setSurname(rs.getString("surname"));
              customer.setGender(Gender.toEnum(rs.getString("gender")).name());
              customer.setMaritalStatus(MaritalStatus.toEnum(rs.getString("marital_status")).name());
              customer.setCreditRating(rs.getInt("credit_rating"));
              customer.setExistingCustomer(rs.getBoolean("existing_customer"));
              customer.setInitials(rs.getString("initials"));

              List<Address> address = getAddress(rs.getLong("id"));
              customer.addAddress(address);

              return customer;
            }
          });
      return customer;
    } catch(EmptyResultDataAccessException e) {
      return null;
    }
  }

  public Address getAddress(Long customerId, Long id) {
    try {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      Address address = jdbcTemplate.queryForObject(
          "SELECT * FROM address WHERE id = ? AND customer_id=?",
          new Object[]{id, customerId},
          new AddressRowMapper());
      return address;
    } catch(EmptyResultDataAccessException e) {
      return null;
    }
  }

  public Address getAddress(Long customerId, AddressType type) {
    try {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      Address address = jdbcTemplate.queryForObject(
          "SELECT * FROM address WHERE customer_id=? AND address_type = ?",
          new Object[]{customerId, type.getCode()},
          new AddressRowMapper());
      return address;
    } catch(EmptyResultDataAccessException e) {
      return null;
    }
  }

  public List<Address> getAddress(Long customerId) {
    try {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      List<Address> address = jdbcTemplate.query(
          "SELECT * FROM address WHERE customer_id = ?",
          new AddressRowMapper(),
          customerId);
      return address;
    } catch(EmptyResultDataAccessException e) {
      return new ArrayList<Address>();
    }
  }

}
