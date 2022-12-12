package com.my.app.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;
 

@Configuration
@MapperScan(basePackages = "com.my.app.mapper")
public class MyBatisConfig {
   private final ApplicationContext appCtx;

   public MyBatisConfig(ApplicationContext appCtx) {
       this.appCtx = appCtx;
   }

   @Bean
   public SqlSessionFactory sqlSessionFactory(DataSource hikariDataSource) throws Exception {
       SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
       sqlSessionFactoryBean.setDataSource(hikariDataSource);
       sqlSessionFactoryBean.setMapperLocations( appCtx.getResources("classpath:/com/my/app/mapper/*.xml") );
       return sqlSessionFactoryBean.getObject();
   }

   @Bean
   public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
       return new SqlSessionTemplate(sqlSessionFactory);
   }

}