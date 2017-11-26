package com.github.sbanal.web.dao.type.handlers;

import com.github.sbanal.web.dto.AddressType;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AddressTypeHandler handles the conversion from address code to/from AddressType.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@MappedTypes(AddressType.class)
public class AddressTypeHandler implements TypeHandler<AddressType> {

  static final Logger LOGGER = LogManager.getLogger(AddressTypeHandler.class);

  @Override
  public void setParameter(PreparedStatement preparedStatement, int paramIndex,
                           AddressType addressType,
                           JdbcType jdbcType) throws SQLException {
    LOGGER.info("set parameter called: " + paramIndex + "=" + addressType);
    preparedStatement.setString(paramIndex, addressType.getCode());
  }

  @Override
  public AddressType getResult(ResultSet resultSet, String s) throws SQLException {
    return AddressType.toEnum(resultSet.getString(s));
  }

  @Override
  public AddressType getResult(ResultSet resultSet, int i) throws SQLException {
    return AddressType.toEnum(resultSet.getString(i));
  }

  @Override
  public AddressType getResult(CallableStatement callableStatement, int i) throws SQLException {
    return AddressType.toEnum(callableStatement.getString(i));
  }

}
