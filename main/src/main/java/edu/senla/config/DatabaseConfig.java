package edu.senla.config;

import edu.senla.helper.ConnectionHolder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.sql.DriverManager;

@Configuration
@PropertySource(value = "classpath:liquibase.properties", encoding="UTF-8")
public class DatabaseConfig {

    @Value("${url}")
    private String url;

    @Value("${user}")
    private String username;

    @Value("${password}")
    private String password;

    @SneakyThrows
    @Bean
    public ConnectionHolder connectionHolder(){
        return new ConnectionHolder(DriverManager.getConnection(url, username, password));
    }

}
