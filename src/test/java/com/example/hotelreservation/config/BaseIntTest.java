package com.example.hotelreservation.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.SQLException;

@TestConfiguration
@EnableJpaRepositories(basePackages = {"com.example.hotelreservation.db"})
public class BaseIntTest {

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1");
        postgreSQLContainer.withDatabaseName("hotel_2");
        postgreSQLContainer.withUsername("postgres");
        postgreSQLContainer.withPassword("admin");
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(final PostgreSQLContainer postgreSQLContainer) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        config.setDriverClassName(postgreSQLContainer.getDriverClassName());
        return new HikariDataSource(config);
    }

    @Bean
    public SpringLiquibase springLiquibase(DataSource dataSource) throws SQLException {
        // here we create the schema first if not yet created before
        tryToCreateSchema(dataSource);
        SpringLiquibase liquibase = new SpringLiquibase();
        // we want to drop the datasbe if it was created before to have immutable version
        liquibase.setDropFirst(true);
        liquibase.setDataSource(dataSource);
        //you set the schema name which will be used into ur integration test
        liquibase.setDefaultSchema("test");
        // the classpath reference for your liquibase changlog
        liquibase.setChangeLog("classpath:/db/changelog/changes/v0001.sql");
        return liquibase;
    }

    private void tryToCreateSchema(DataSource dataSource) throws SQLException {
        String CREATE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS test";
        dataSource.getConnection().createStatement().execute(CREATE_SCHEMA_QUERY);
    }

}
