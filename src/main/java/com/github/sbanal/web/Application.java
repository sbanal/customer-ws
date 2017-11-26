package com.github.sbanal.web;

import com.github.sbanal.web.dao.type.handlers.AddressTypeHandler;
import com.github.sbanal.web.service.CustomerService;
import com.github.sbanal.web.service.CustomerServiceImpl;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application class main entry point for running this application
 * as standalone jar. It also contains all the code based Spring bean definitions.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.github.sbanal.web.dao")
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CustomerService customerService() {
    return new CustomerServiceImpl();
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sqlFactory = new SqlSessionFactoryBean();
    sqlFactory.setDataSource(dataSource());
    sqlFactory.setTypeHandlersPackage(AddressTypeHandler.class.getPackage().getName());
    return sqlFactory.getObject();
  }

  @Bean(destroyMethod = "shutdown")
  public EmbeddedDatabase dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    return builder.setType(EmbeddedDatabaseType.H2)
        .addScript("create-db.sql")
        .build();
  }

  @Bean
  public DataSourceTransactionManager transactionManager() {
    DataSourceTransactionManager txMgr = new DataSourceTransactionManager();
    txMgr.setDataSource(dataSource());
    return txMgr;
  }

}
