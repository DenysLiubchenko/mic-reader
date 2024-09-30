package org.example.boot.it;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.boot.config.KafkaConfig;
import org.example.boot.config.KafkaTestConfig;
import org.example.boot.config.MongoConfig;
import org.example.dao.adapters.ProductJpaAdapter;
import org.example.dao.entity.ProductEntity;
import org.example.domain.constant.EventReason;
import org.example.domain.constant.LogReason;
import org.example.fact.ProductFactEvent;
import org.example.log.adapters.ProductMongoAdapter;
import org.example.log.model.ProductDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(properties = "KAFKA_TOPICS_LISTENER_TYPE=delta")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Sql(scripts = {"/start.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import({KafkaConfig.class, MongoConfig.class, KafkaTestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsumerProductDeltaIT extends AbstractIT{
    @Autowired
    private KafkaTemplate<String, Object> kafkaDeltaTemplate;
    @Autowired
    private ProductJpaAdapter productJpaAdapter;
    @Autowired
    private ProductMongoAdapter productMongoAdapter;

    private final String DELTA_TOPIC = "outbox.event.product-delta";

    @Test
    @SneakyThrows
    public void testConsume() {
        // Given
        createTopics(DELTA_TOPIC);
        ProductFactEvent productFactEvent = ModelUtils.getProductFactEvent(EventReason.CREATE.name());
        Long id = productFactEvent.getId();

        final ProducerRecord<String, Object> record =
                new ProducerRecord<>(DELTA_TOPIC, null, id.toString(), productFactEvent);

        // When
        kafkaDeltaTemplate.send(record).get();
        Thread.sleep(2000);

        // Then
        Optional<ProductEntity> optionalProductEntity = productJpaAdapter.findById(id);
        assertThat(optionalProductEntity.isPresent()).isTrue();
        ProductEntity productEntity = optionalProductEntity.get();
        assertThat(productEntity.getName()).isEqualTo(productFactEvent.getName());
        assertThat(productEntity.getCost()).isEqualTo(productFactEvent.getCost());

        Optional<ProductDocument> optionalProductDocument = productMongoAdapter.findByProductId(id);
        assertThat(optionalProductDocument.isPresent()).isTrue();
        ProductDocument productDocument = optionalProductDocument.get();
        assertThat(productDocument.getName()).isEqualTo(productFactEvent.getName());
        assertThat(productDocument.getCost()).isEqualTo(productFactEvent.getCost());
        assertThat(productDocument.getReason()).isEqualTo(LogReason.CREATE.name());
        assertThat(productDocument.getCreatedAt()).isNotNull();
        assertThat(productDocument.getId()).isNotNull();
    }
}
