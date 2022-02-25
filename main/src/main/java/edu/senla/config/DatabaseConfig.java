package edu.senla.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.ConnectionHolder;

import java.sql.DriverManager;

@Configuration
@PropertySource(value = "classpath:application.yml")
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @SneakyThrows
    @Bean
    public ConnectionHolder connectionHolder(){
        return new ConnectionHolder(DriverManager.getConnection(url, username, password));
    }

}
