package dev.anandbose.todo.authorization;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataSourceConfig {
    @Bean
    @Profile("development")
    public DataSource developmentDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/todo_dev")
                .username("todo_dev")
                .password("todo_dev_password")
                .build();
    }

    @Bean
    @Profile("production")
    public DataSource productionDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/todo_prod")
                .username("todo_prod")
                .password("todo_prod_password")
                .build();
    }
}
