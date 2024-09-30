package org.example.boot.it;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.boot.config.KafkaConfig;
import org.example.boot.config.KafkaTestConfig;
import org.example.boot.config.MongoConfig;
import org.example.dao.adapters.CartJpaAdapter;
import org.example.dao.entity.CartEntity;
import org.example.domain.constant.EventReason;
import org.example.domain.constant.LogReason;
import org.example.fact.CartFactEvent;
import org.example.log.adapters.CartMongoAdapter;
import org.example.log.model.CartDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@Transactional
@SpringBootTest(properties = "KAFKA_TOPICS_LISTENER_TYPE=fact")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Sql(scripts = {"/start.sql", "/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import({KafkaConfig.class, MongoConfig.class, KafkaTestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsumerCartFactIT extends AbstractIT{
    @Autowired
    private KafkaTemplate<String, Object> kafkaFactTemplate;
    @Autowired
    private CartJpaAdapter cartJpaAdapter;
    @Autowired
    private CartMongoAdapter cartMongoAdapter;

    private final String FACT_TOPIC = "outbox.event.cart-fact";

    @Test
    @SneakyThrows
    public void testCreateConsume() {
        // Given
        createTopics(FACT_TOPIC);

        CartFactEvent cartFactEvent = ModelUtils.getCartFactEvent(EventReason.CREATE.name());
        Long id = cartFactEvent.getId();

        final ProducerRecord<String, Object> record =
                new ProducerRecord<>(FACT_TOPIC, null, id.toString(), cartFactEvent);

        // When
        kafkaFactTemplate.send(record).get();
        Thread.sleep(2000);

        // Then
        Optional<CartEntity> optionalCartEntity = cartJpaAdapter.findById(id);
        assertThat(optionalCartEntity.isPresent()).isTrue();
        CartEntity cartEntity = optionalCartEntity.get();
        assertThat(cartEntity.getProducts().size()).isEqualTo(cartFactEvent.getProducts().size());
        assertThat(cartEntity.getDiscounts().size()).isEqualTo(cartFactEvent.getDiscounts().size());

        Optional<CartDocument> optionalCartDocument = cartMongoAdapter.findByCartId(id);
        assertThat(optionalCartDocument.isPresent()).isTrue();
        CartDocument cartDocument = optionalCartDocument.get();
        assertThat(cartDocument.getProducts().size()).isEqualTo(cartFactEvent.getProducts().size());
        assertThat(cartDocument.getDiscounts().size()).isEqualTo(cartFactEvent.getDiscounts().size());
        assertThat(cartDocument.getReason()).isEqualTo(LogReason.CREATE.name());
        assertThat(cartDocument.getCreatedAt()).isNotNull();
        assertThat(cartDocument.getId()).isNotNull();
    }

    @Test
    @SneakyThrows
    public void testUpdateConsume() {
        // Given
        createTopics(FACT_TOPIC);
        CartFactEvent cartFactEvent = ModelUtils.getCartFactEvent(50L, EventReason.UPDATE.name());


        Long id = cartFactEvent.getId();

        final ProducerRecord<String, Object> record =
                new ProducerRecord<>(FACT_TOPIC, null, id.toString(), cartFactEvent);

        // When
        kafkaFactTemplate.send(record).get();
        Thread.sleep(2000);

        // Then
        Optional<CartEntity> optionalCartEntity = cartJpaAdapter.findById(id);
        assertThat(optionalCartEntity.isPresent()).isTrue();
        CartEntity cartEntity = optionalCartEntity.get();
        assertThat(cartEntity.getProducts().size()).isEqualTo(cartFactEvent.getProducts().size());
        assertThat(cartEntity.getDiscounts().size()).isEqualTo(cartFactEvent.getDiscounts().size());

        Optional<CartDocument> optionalCartDocument = cartMongoAdapter.findByCartId(id);
        assertThat(optionalCartDocument.isPresent()).isTrue();
        CartDocument cartDocument = optionalCartDocument.get();
        assertThat(cartDocument.getProducts().size()).isEqualTo(cartFactEvent.getProducts().size());
        assertThat(cartDocument.getDiscounts().size()).isEqualTo(cartFactEvent.getDiscounts().size());
        assertThat(cartDocument.getReason()).isEqualTo(LogReason.UPDATE.name());
        assertThat(cartDocument.getCreatedAt()).isNotNull();
        assertThat(cartDocument.getId()).isNotNull();
    }

    @Test
    @SneakyThrows
    public void testDeleteConsume() {
        // Given
        createTopics(FACT_TOPIC);
        CartFactEvent cartFactEvent = ModelUtils.getCartFactEvent(51L, EventReason.DELETE.name());


        Long id = cartFactEvent.getId();

        final ProducerRecord<String, Object> record =
                new ProducerRecord<>(FACT_TOPIC, null, id.toString(), cartFactEvent);

        // When
        kafkaFactTemplate.send(record).get();
        Thread.sleep(2000);

        // Then
        Optional<CartEntity> optionalCartEntity = cartJpaAdapter.findById(id);
        assertThat(optionalCartEntity.isPresent()).isFalse();

        Optional<CartDocument> optionalCartDocument = cartMongoAdapter.findByCartId(id);
        assertThat(optionalCartDocument.isPresent()).isTrue();
        CartDocument cartDocument = optionalCartDocument.get();
        assertThat(cartDocument.getProducts().size()).isEqualTo(cartFactEvent.getProducts().size());
        assertThat(cartDocument.getDiscounts().size()).isEqualTo(cartFactEvent.getDiscounts().size());
        assertThat(cartDocument.getReason()).isEqualTo(LogReason.DELETE.name());
        assertThat(cartDocument.getCreatedAt()).isNotNull();
        assertThat(cartDocument.getId()).isNotNull();
    }
}

