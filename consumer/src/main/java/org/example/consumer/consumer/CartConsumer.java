package org.example.consumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.example.consumer.mapper.CartDeltaEventMapper;
import org.example.consumer.mapper.CartFactEventMapper;
import org.example.delta.DeleteCartDeltaEvent;
import org.example.delta.DiscountCartDeltaEvent;
import org.example.delta.ModifyProductItemCartDeltaEvent;
import org.example.delta.RemoveProductItemCartDeltaEvent;
import org.example.domain.constant.EventReason;
import org.example.domain.service.CartService;
import org.example.fact.CartFactEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.example.domain.constant.EventReason.CREATE;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartConsumer {
    private final CartService cartService;
    private final CartFactEventMapper cartFactEventMapper;
    private final CartDeltaEventMapper cartDeltaEventMapper;

    @KafkaListener(topics = "${kafka.topics.fact.cart}", groupId = "cart-fact-group", autoStartup = "#{@isReadingTypeFact}")
    public void consumeFact(@Payload final CartFactEvent event) {
        log.info("Received fact: {} message", event);

        switch (EventReason.valueOf(event.getReason())) {
            case CREATE -> cartService.saveCart(cartFactEventMapper.toDto(event));
            case UPDATE -> cartService.updateCart(cartFactEventMapper.toDto(event));
            case DELETE -> cartService.deleteById(event.getId());
            default -> throw new IllegalStateException("Unexpected reason: " + event.getReason());
        }
    }

    @KafkaListener(topics = "${kafka.topics.delta.cart}", groupId = "cart-delta-group", autoStartup = "#{@isReadingTypeDelta}")
    public void consumeDelta(@Payload final SpecificRecord message) {
        log.info("Received delta: {} message", message);

        switch (message) {
            case CartFactEvent event -> {
                if (EventReason.valueOf(event.getReason()) == CREATE) {
                    cartService.saveCart(cartFactEventMapper.toDto(event));
                } else {
                    throw new IllegalStateException("Unexpected reason: " + event.getReason());
                }
            }
            case DeleteCartDeltaEvent event -> cartService.deleteById(event.getId());
            case ModifyProductItemCartDeltaEvent event -> {
                switch (EventReason.valueOf(event.getReason())) {
                    case ADD_PRODUCT_ITEM ->
                            cartService.addProductToCartWithId(event.getId(), cartDeltaEventMapper.toProductItemDto(event.getProducts()));
                    case CHANGE_QUANTITY_OF_PRODUCT_ITEM ->
                            cartService.addProductToCartWithId(event.getId(), cartDeltaEventMapper.toProductItemDto(event.getProducts()));
                    default -> throw new IllegalStateException("Unexpected reason: " + event.getReason());
                }
            }
            case RemoveProductItemCartDeltaEvent event ->
                    cartService.removeProductFromCartWithId(event.getId(), Set.copyOf(event.getProductIds()));
            case DiscountCartDeltaEvent event -> {
                switch (EventReason.valueOf(event.getReason())) {
                    case ADD_DISCOUNT ->
                            cartService.addDiscountToCartWithId(event.getId(), Set.copyOf(event.getDiscounts()));
                    case REMOVE_DISCOUNT ->
                            cartService.removeDiscountFromCartWithId(event.getId(), Set.copyOf(event.getDiscounts()));
                    default -> throw new IllegalStateException("Unexpected reason: " + event.getReason());
                }
            }
            // Add more delta events if needed
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}
