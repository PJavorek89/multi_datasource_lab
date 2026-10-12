package com.javorek.lab.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories( //kde hledat JPA repository
        basePackages = "com.javorek.lab.orders",
        entityManagerFactoryRef = "ordersEntityManagerFactory",
        transactionManagerRef = "ordersTransactionManager"
)
public class OrdersDBConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.orders")
    public DataSourceProperties orderDataSourceProperties() {
        return new DataSourceProperties(); //nacte hodnoty kam a jak se pripojit
    }

    @Bean
    @Primary
    public DataSource ordersDataSource(@Qualifier("orderDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build(); //vytvori objekt, co se bude pripojovat do DB
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean ordersEntityManagerFactory(
            @Qualifier("ordersDataSource")  DataSource dataSource
    ) {
        var emf = new LocalContainerEntityManagerFactoryBean(); //tovarna na tovarnu
        emf.setDataSource(dataSource); //preda datasource
        emf.setPackagesToScan("com.javorek.lab.orders"); //balicek kde ma hledat @Entity
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaPropertyMap(Map.of("hibernate.hbm2ddl.auto", "update")); //jen pro vyvoj
        return emf;
    }

    @Bean
    @Primary
    public PlatformTransactionManager ordersTransactionManager(
            @Qualifier("ordersEntityManagerFactory") EntityManagerFactory emf
            ) {
        return new JpaTransactionManager(emf); //aplikace bude schopna pouzivat @Transactional
    }
}
