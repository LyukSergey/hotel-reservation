package com.example.hotelreservation.config;

import com.github.dockerjava.api.model.Bind;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@TestConfiguration
@EnableJpaRepositories(basePackages = {"com.example.hotelreservation.db"})
public class BaseIntTest {

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1");
        postgreSQLContainer.withDatabaseName("hotel_2");
        postgreSQLContainer.withUsername("postgres");
        postgreSQLContainer.withPassword("admin");
        postgreSQLContainer.withExposedPorts();
        ArrayList<String> portBindings = new ArrayList<String>();
        portBindings.add("5432");
        postgreSQLContainer.setPortBindings(portBindings);
        ArrayList<Integer> exposedPorts = new ArrayList<>();
        exposedPorts.add(5432);
        postgreSQLContainer.setExposedPorts(exposedPorts);
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
    public Flyway flyway(DataSource dataSource){
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        return flyway;
    }

    @Bean
    public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway){
        return new FlywayMigrationInitializer(flyway, null);
    }

   /* @Bean
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
*/
    private void tryToCreateSchema(DataSource dataSource) throws SQLException {
        String CREATE_SCHEMA_QUERY = "CREATE SCHEMA IF NOT EXISTS test";
        dataSource.getConnection().createStatement().execute(CREATE_SCHEMA_QUERY);
    }

}
