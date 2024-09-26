package org.example.consumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.example.consumer.mapper.ProductFactEventMapper;
import org.example.domain.constant.EventReason;
import org.example.domain.service.ProductService;
import org.example.fact.ProductFactEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductConsumer {
    private final ProductService productService;
    private final ProductFactEventMapper productFactEventMapper;

    @KafkaListener(topics = "${kafka.topics.fact.product}", groupId = "product-fact-group", autoStartup = "#{@isReadingTypeFact}")
    public void consumeFact(@Payload final ProductFactEvent event) {
        log.info("Received fact: {} message", event);

        switch (EventReason.valueOf(event.getReason())) {
            case EventReason.CREATE -> productService.save(productFactEventMapper.toDto(event));
            // Add more reasons if needed
            default -> throw new IllegalStateException("Unexpected reason: " + event.getReason());
        }
    }

    @KafkaListener(topics = "${kafka.topics.delta.product}", groupId = "product-delta-group", autoStartup = "#{@isReadingTypeDelta}")
    public void consumeDelta(@Payload final SpecificRecord message) {
        log.info("Received delta: {} message", message);

        switch (message) {
            case ProductFactEvent event -> {
                if (EventReason.valueOf(event.getReason()) == EventReason.CREATE) {
                    productService.save(productFactEventMapper.toDto(event));
                } else {
                    throw new IllegalStateException("Unexpected reason: " + event.getReason());
                }
            }
            // Add more delta events if needed
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}
