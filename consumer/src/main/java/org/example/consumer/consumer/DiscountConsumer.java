package org.example.consumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.example.consumer.mapper.DiscountFactEventMapper;
import org.example.domain.constant.EventReason;
import org.example.domain.service.DiscountService;
import org.example.fact.DiscountFactEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscountConsumer {
    private final DiscountService discountService;
    private final DiscountFactEventMapper discountFactEventMapper;

    @KafkaListener(topics = "${kafka.topics.fact.discount}", groupId = "discount-fact-group")
    public void consumeFact(@Payload final DiscountFactEvent event) {
        log.info("Received fact: {} message", event);

        switch (EventReason.valueOf(event.getReason())) {
            case EventReason.CREATE -> discountService.save(discountFactEventMapper.toDto(event));
            // Add more reasons if needed
            default -> throw new IllegalStateException("Unexpected reason: " + event.getReason());
        }
    }

    @KafkaListener(topics = "${kafka.topics.delta.discount}", groupId = "discount-delta-group")
    public void consumeDelta(@Payload final SpecificRecord message) {
        log.info("Received delta: {} message", message);

        switch (message) {
            case DiscountFactEvent event -> {
                if (EventReason.valueOf(event.getReason()) == EventReason.CREATE) {
                    discountService.save(discountFactEventMapper.toDto(event));
                } else {
                    throw new IllegalStateException("Unexpected reason: " + event.getReason());
                }
            }
            // Add more delta events if needed
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}
