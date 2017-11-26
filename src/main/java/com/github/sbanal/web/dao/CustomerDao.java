package com.github.sbanal.web.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * CustomerDao interface defines the methods used to create, read, update and delete
 * customer records to/from persistence layer.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public interface CustomerDao {

  @Insert("INSERT INTO customer (title, first_name, middle_name, surname, initials, gender, "
          + "marital_status, existing_customer, credit_rating, date_created, date_updated)"
          + " VALUES(#{title}, #{firstName}, #{middleName}, #{surname}, #{initials}, #{genderCode}, "
          + "#{maritalStatusCode}, #{existingCustomer}, #{creditRating}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
  @Options(useGeneratedKeys=true, keyProperty="id")
  void create(CustomerEntity customer);

  @Select("SELECT * FROM customer WHERE id = #{id}")
  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "firstName", column = "first_name"),
      @Result(property = "middleName", column = "middle_name"),
      @Result(property = "surname", column = "surname"),
      @Result(property = "genderCode", column = "gender"),
      @Result(property = "maritalStatusCode", column = "marital_status"),
      @Result(property = "existingCustomer", column = "existing_customer"),
      @Result(property = "creditRating", column = "credit_rating"),
      @Result(property="address", javaType=List.class, column="id",
          many=@Many(select="getAddress"))
  })
  CustomerEntity read(Long id);

  @Select("SELECT * FROM customer "
          + "WHERE first_name LIKE #{name} OR surname LIKE #{name} OR middle_name LIKE #{name}")
  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "firstName", column = "first_name"),
      @Result(property = "middleName", column = "middle_name"),
      @Result(property = "surname", column = "surname"),
      @Result(property = "genderCode", column = "gender"),
      @Result(property = "maritalStatusCode", column = "marital_status"),
      @Result(property = "existingCustomer", column = "existing_customer"),
      @Result(property = "creditRating", column = "credit_rating"),
      @Result(property="address", javaType=List.class, column="id",
          many=@Many(select="getAddress"))
  })
  @Options(resultSetType= ResultSetType.SCROLL_INSENSITIVE)
  List<CustomerEntity> find(@Param("name") String name, RowBounds rowBound);

  @Select("SELECT * FROM customer")
  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "firstName", column = "first_name"),
      @Result(property = "middleName", column = "middle_name"),
      @Result(property = "surname", column = "surname"),
      @Result(property = "genderCode", column = "gender"),
      @Result(property = "maritalStatusCode", column = "marital_status"),
      @Result(property = "existingCustomer", column = "existing_customer"),
      @Result(property = "creditRating", column = "credit_rating"),
      @Result(property="address", javaType=List.class, column="id",
          many=@Many(select="getAddress"))
  })
  @Options(resultSetType= ResultSetType.SCROLL_INSENSITIVE)
  List<CustomerEntity> findAll(RowBounds rowBound);

  @Select("SELECT * FROM address WHERE customer_id = #{id}")
  @Results({
      @Result(property = "typeCode", column = "address_type"),
      @Result(property = "streetName", column = "street_name"),
      @Result(property = "streetType", column = "street_type"),
      @Result(property = "postalCode", column = "postal_code")
  })
  List<AddressEntity> getAddress(Long id);

  @Update("UPDATE customer SET "
          + "title = #{title}, "
          + "first_name = #{firstName}, "
          + "middle_name = #{middleName}, "
          + "surname = #{surname}, "
          + "initials = #{initials}, "
          + "gender = #{genderCode}, "
          + "marital_status = #{maritalStatusCode}, "
          + "existing_customer = #{existingCustomer}, "
          + "credit_rating = #{creditRating}, "
          + "date_updated = CURRENT_TIMESTAMP "
          + "WHERE id = #{id}")
  int update(CustomerEntity customer);

  @Delete("DELETE FROM customer WHERE id = #{id}")
  int delete(Long id);

}
