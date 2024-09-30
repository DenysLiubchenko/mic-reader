package org.example.boot.it;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.example.boot.testcontainers.SchemaRegistryContainer;
import org.junit.jupiter.api.AfterAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

@Testcontainers
public abstract class AbstractIT {
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    public static GenericContainer<?> mongoContainer = new GenericContainer<>(DockerImageName.parse("mongo:5.0"))
        .withEnv("MONGO_INITDB_ROOT_USERNAME", "mongodb")
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", "mongodb")
        .withEnv("MONGO_INITDB_DATABASE", "mongodb-reader")
        .withExposedPorts(27017);

    private static final Network KAFKA_NETWORK = Network.newNetwork();
    public static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
            .withNetwork(KAFKA_NETWORK)
            .withKraft()
            .withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1")
            .withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1");

    public static SchemaRegistryContainer schemaRegistryContainer = new SchemaRegistryContainer("7.4.0")
            .withStartupTimeout(Duration.ofMinutes(2));

    static {
        postgresContainer.start();
        mongoContainer.start();
        kafkaContainer.start();
        schemaRegistryContainer.withKafka(kafkaContainer).start();
        schemaRegistryContainer.withEnv("SCHEMA_REGISTRY_LISTENERS", schemaRegistryContainer.getSchemaUrl());
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        // Connect our Spring application to our Testcontainers Kafka instance
        registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.producer.properties.schema.registry.url", schemaRegistryContainer::getSchemaUrl);
        registry.add("kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("kafka.schema.registry.url", schemaRegistryContainer::getSchemaUrl);

        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        registry.add("spring.data.mongodb.host", mongoContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", ()->"mongodb");
        registry.add("spring.data.mongodb.password", ()->"mongodb");

    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @AfterAll
    public static void stopContainers() {
        schemaRegistryContainer.stop();
        kafkaContainer.stop();
        mongoContainer.stop();
        postgresContainer.stop();
    }

    public static void createTopics(String... topics) {
        var newTopics =
                Arrays.stream(topics)
                        .map(topic -> new NewTopic(topic, 1, (short) 1))
                        .collect(Collectors.toList());
        try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers()))) {
            admin.createTopics(newTopics);
        }
    }
}

