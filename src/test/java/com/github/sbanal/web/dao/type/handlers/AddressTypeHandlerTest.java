package com.github.sbanal.web.dao.type.handlers;

import com.github.sbanal.web.dto.AddressType;

import org.apache.ibatis.type.JdbcType;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * AddressTypeHandlerTest test.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public class AddressTypeHandlerTest {

  @Test
  public void testSetParameter() throws Exception {

    PreparedStatement mockPrepStmt = Mockito.mock(PreparedStatement.class);

    AddressTypeHandler handler = new AddressTypeHandler();
    handler.setParameter(mockPrepStmt, 0, AddressType.MAILING, JdbcType.VARCHAR);

    Mockito.verify(mockPrepStmt, Mockito.times(1)).setString(
            Mockito.eq(0), Mockito.eq(AddressType.MAILING.getCode()));

  }

  @Test
  public void testGetResult_ByCallableStmt() throws Exception {
    CallableStatement mockStatment = Mockito.mock(CallableStatement.class);
    Mockito.when(mockStatment.getString(Mockito.eq(0))).thenReturn("R");
    AddressTypeHandler handler = new AddressTypeHandler();
    AddressType addressType = handler.getResult(mockStatment, 0);
    Assert.assertEquals(AddressType.RESIDENTIAL, addressType);
  }

  @Test
  public void testGetResult_ByResultSet_Index() throws Exception {
    ResultSet mockResultSet = Mockito.mock(ResultSet.class);
    Mockito.when(mockResultSet.getString(Mockito.eq(0))).thenReturn("R");
    AddressTypeHandler handler = new AddressTypeHandler();
    AddressType addressType = handler.getResult(mockResultSet, 0);
    Assert.assertEquals(AddressType.RESIDENTIAL, addressType);
  }

  @Test
  public void testGetResult_ByResultSet_Column() throws Exception {
    ResultSet mockResultSet = Mockito.mock(ResultSet.class);
    Mockito.when(mockResultSet.getString(Mockito.eq("AddressType"))).thenReturn("R");
    AddressTypeHandler handler = new AddressTypeHandler();
    AddressType addressType = handler.getResult(mockResultSet, "AddressType");
    Assert.assertEquals(AddressType.RESIDENTIAL, addressType);
  }

}