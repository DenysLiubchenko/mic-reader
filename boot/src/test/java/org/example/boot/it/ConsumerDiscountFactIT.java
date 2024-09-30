package org.example.boot.it;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.boot.config.KafkaConfig;
import org.example.boot.config.KafkaTestConfig;
import org.example.boot.config.MongoConfig;
import org.example.dao.adapters.DiscountJpaAdapter;
import org.example.dao.entity.DiscountEntity;
import org.example.domain.constant.EventReason;
import org.example.domain.constant.LogReason;
import org.example.fact.DiscountFactEvent;
import org.example.log.adapters.DiscountMongoAdapter;
import org.example.log.model.DiscountDocument;
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
@SpringBootTest(properties = "KAFKA_TOPICS_LISTENER_TYPE=fact")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Sql(scripts = {"/start.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import({KafkaConfig.class, MongoConfig.class, KafkaTestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsumerDiscountFactIT extends AbstractIT{
    @Autowired
    private KafkaTemplate<String, Object> kafkaFactTemplate;
    @Autowired
    private DiscountJpaAdapter discountJpaAdapter;
    @Autowired
    private DiscountMongoAdapter discountMongoAdapter;

    private final String FACT_TOPIC = "outbox.event.discount-fact";

    @Test
    @SneakyThrows
    public void testConsume() {
        // Given
        createTopics(FACT_TOPIC);
        DiscountFactEvent discountFactEvent = ModelUtils.getDiscountFactEvent(EventReason.CREATE.name());
        String code = discountFactEvent.getCode();

        final ProducerRecord<String, Object> record =
                new ProducerRecord<>(FACT_TOPIC, null, code, discountFactEvent);

        // When
        kafkaFactTemplate.send(record).get();
        Thread.sleep(2000);

        // Then
        Optional<DiscountEntity> optionalDiscountEntity = discountJpaAdapter.findById(code);
        assertThat(optionalDiscountEntity.isPresent()).isTrue();
        DiscountEntity discountEntity = optionalDiscountEntity.get();
        assertThat(discountEntity.getDue()).isEqualTo(discountFactEvent.getDue());

        Optional<DiscountDocument> optionalDiscountDocument = discountMongoAdapter.findByCode(code);
        assertThat(optionalDiscountDocument.isPresent()).isTrue();
        DiscountDocument discountDocument = optionalDiscountDocument.get();
        assertThat(discountDocument.getDue()).isEqualTo(discountFactEvent.getDue());
        assertThat(discountDocument.getReason()).isEqualTo(LogReason.CREATE.name());
        assertThat(discountDocument.getCreatedAt()).isNotNull();
        assertThat(discountDocument.getId()).isNotNull();
    }
}
