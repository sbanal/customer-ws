package com.github.sbanal.web.dao;

import com.github.sbanal.web.dto.AddressType;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * AddressDao interface defines the methods used to create, read, update and delete
 * address records from persistence layer.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public interface AddressDao {

  @Insert("INSERT INTO address (customer_id, address_type, street_type, street_name, suburb, city, "
          + "state, country, postal_code, date_created, date_updated) "
          + "VALUES(#{customerId}, #{typeCode}, #{streetType}, #{streetName}, #{suburb}, #{city}, "
          + "#{state}, #{country}, #{postalCode}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
  @Options(useGeneratedKeys=true, keyProperty="id")
  void create(AddressEntity address);

  @Select("SELECT * FROM address WHERE id = #{id} AND customer_id = #{customerId}")
  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "customerId", column = "customer_id"),
      @Result(property = "typeCode", column = "address_type"),
      @Result(property = "streetType", column = "street_type"),
      @Result(property = "streetName", column = "street_name"),
      @Result(property = "suburb", column = "suburb"),
      @Result(property = "city", column = "city"),
      @Result(property = "state", column = "state"),
      @Result(property = "country", column = "country"),
      @Result(property = "postalCode", column = "postal_code")
  })
  AddressEntity read(@Param("id") Long id, @Param("customerId") Long customerId);

  @Select("SELECT * FROM address WHERE customer_id = #{customerId} AND address_type = #{type}")
  @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "customerId", column = "customer_id"),
      @Result(property = "typeCode", column = "address_type"),
      @Result(property = "streetType", column = "street_type"),
      @Result(property = "streetName", column = "street_name"),
      @Result(property = "suburb", column = "suburb"),
      @Result(property = "city", column = "city"),
      @Result(property = "state", column = "state"),
      @Result(property = "country", column = "country"),
      @Result(property = "postalCode", column = "postal_code")
  })
  AddressEntity readAddressByType(@Param("customerId") Long customerId,
                                  @Param("type") AddressType type);

  @Update("UPDATE address SET "
          + "address_type = #{typeCode}, "
          + "street_type = #{streetType}, "
          + "street_name = #{streetName}, "
          + "suburb = #{suburb}, "
          + "city = #{city}, "
          + "state = #{state}, "
          + "country = #{country}, "
          + "postal_code = #{postalCode},"
          + "date_updated = CURRENT_TIMESTAMP "
          + "WHERE id = #{id} AND customer_id = #{customerId} AND address_type = #{typeCode}")
  int update(AddressEntity address);

  @Delete("DELETE FROM address WHERE id = #{id} AND customer_id = #{customerId}")
  int delete(@Param("id") Long id, @Param("customerId") Long customerId);

  @Delete("DELETE FROM address WHERE customer_id = #{customerId}")
  int deleteByCustomerId(@Param("customerId") Long customerId);

}
