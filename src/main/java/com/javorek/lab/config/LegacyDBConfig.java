package com.javorek.lab.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "com.javorek.lab.legacy.mapper", //kde hledat myBattis mapper rozhrani
        sqlSessionFactoryRef = "legacySqlSessionFactory"
)
public class LegacyDBConfig {

    //adresa a prihlasovaci udaje
    @Bean
    @ConfigurationProperties("app.datasource.legacy")
    public DataSourceProperties legacyDataSource() {
        return new DataSourceProperties();
    }

    //connection pool ddo oracle
    @Bean
    public DataSource legacyDatasource(
            @Qualifier("legacyDataSource") DataSourceProperties dataSourceProperties
    ) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    //MyBatis nad poolem obdoba entity manager factory
    @Bean
    public SqlSessionFactory legacySqlSessionFactory(
            @Qualifier("legacyDatasource") DataSource dataSource) throws Exception {
            var factory = new SqlSessionFactoryBean();
            factory.setDataSource(dataSource);
            factory.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResource("classpath:mapper/legacy/*.xml")); //kde lezi XML s SQL

            var config = new org.apache.ibatis.session.Configuration(); //plny naazev koliduje s @Configuration
            config.setMapUnderscoreToCamelCase(true); //PART_NO -> partNo
            factory.setConfiguration(config);

            return factory.getObject();
    }

    //nastaveni transactional na JDBC
    @Bean
    public PlatformTransactionManager legacyTransactionalManager(
            @Qualifier("legacyDataSource") DataSource dataSource
    ) {
        return new DataSourceTransactionManager(dataSource);
    }

}
