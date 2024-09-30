package org.example.boot.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class KafkaTestConfig {
    @Bean
    public ProducerFactory<String, Object> producerFactFactory(@Value("${kafka.schema.registry.url}") String schemaRegistry,
                                                           @Value("${kafka.bootstrap-servers}") String bootstrapServer) {
        return new DefaultKafkaProducerFactory<>(producerAvroFactConfig(schemaRegistry, bootstrapServer));
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaFactTemplate(ProducerFactory<String, Object> producerFactFactory) {
        return new KafkaTemplate<>(producerFactFactory);
    }

    @Bean
    public ProducerFactory<String, Object> producerDeltaFactory(@Value("${kafka.schema.registry.url}") String schemaRegistry,
                                                           @Value("${kafka.bootstrap-servers}") String bootstrapServer) {
        return new DefaultKafkaProducerFactory<>(producerAvroDeltaConfig(schemaRegistry, bootstrapServer));
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaDeltaTemplate(ProducerFactory<String, Object> producerDeltaFactory) {
        return new KafkaTemplate<>(producerDeltaFactory);
    }

    private Map<String, Object> producerAvroDeltaConfig(String schemaRegistry, String bootstrapServer) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        config.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true);
        config.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, TopicRecordNameStrategy.class.getName());
        return config;
    }

    private Map<String, Object> producerAvroFactConfig(String schemaRegistry, String bootstrapServer) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        config.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true);
        return config;
    }
}
