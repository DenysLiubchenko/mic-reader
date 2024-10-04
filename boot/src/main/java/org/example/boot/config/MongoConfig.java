package org.example.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.example.log")
@EnableMongoAuditing
public class MongoConfig {
    @Bean("documentDurationTimeout")
    public String documentDurationTimeout(@Value("${mongodb.document.timeout}") String timeout) {
        return timeout;
    }

    @Bean
    public boolean isReadingTypeFact(@Value("${kafka.listener.type}") String type) {
        return type.equals("fact");
    }

    @Bean
    public boolean isReadingTypeDelta(@Value("${kafka.listener.type}") String type) {
        return type.equals("delta");
    }
}
