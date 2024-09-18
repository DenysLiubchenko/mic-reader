package org.example.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    private @Value("${kafka.schema.registry.url}") String schemaRegistry;
    private @Value("${kafka.bootstrap-servers}") String bootstrapServer;


}
