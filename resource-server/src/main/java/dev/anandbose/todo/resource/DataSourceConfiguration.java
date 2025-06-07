package dev.anandbose.todo.resource;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataSourceConfiguration {
    @Bean
    @Profile("development")
    public DataSource developmentDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/todo_resource_dev")
                .username("todo_resource_dev")
                .password("todo_resource_dev_password")
                .build();
    }

    @Bean
    @Profile("production")
    public DataSource productionDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/todo_resource_prod")
                .username("todo_resource_prod")
                .password("todo_resource_prod_password")
                .build();
    }
}
