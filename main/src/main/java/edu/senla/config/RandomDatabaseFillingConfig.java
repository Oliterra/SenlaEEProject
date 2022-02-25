package edu.senla.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource(value = "classpath:data.yml", encoding="UTF-8" )
@Getter
public class RandomDatabaseFillingConfig {

    @Value("${female-first-names:name}")
    private List<String> femaleFirstNames;

    @Value("${male-first-names:name}")
    private List<String> maleFirstNames;

    @Value("${female-last-names:surname}")
    private List<String> femaleLastNames;

    @Value("${male-last-names:surname}")
    private List<String> maleLastNames;
}
