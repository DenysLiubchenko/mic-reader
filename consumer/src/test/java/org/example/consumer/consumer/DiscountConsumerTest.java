package org.example.consumer.consumer;

import org.example.consumer.ModelUtils;
import org.example.consumer.mapper.DiscountFactEventMapper;
import org.example.domain.constant.EventReason;
import org.example.domain.dto.DiscountDto;
import org.example.domain.service.DiscountService;
import org.example.fact.DiscountFactEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DiscountConsumerTest {

    @Mock
    private DiscountService discountService;

    @Mock
    private DiscountFactEventMapper discountFactEventMapper;

    @InjectMocks
    private DiscountConsumer discountConsumer;

    @Test
    void consumeFact() {
        // Given
        DiscountFactEvent event = ModelUtils.getDiscountFactEvent(EventReason.CREATE.name());
        DiscountDto discountDto = ModelUtils.getDiscountDto();

        given(discountFactEventMapper.toDto(event)).willReturn(discountDto);

        // When
        discountConsumer.consumeFact(event);

        // Then
        then(discountFactEventMapper).should().toDto(event);
        then(discountService).should().save(discountDto);
    }

    @Test
    void consumeFact_withUnexpectedReason_shouldThrowIllegalArgumentException() {
        // Given
        DiscountFactEvent event = ModelUtils.getDiscountFactEvent("UNEXPECTED_REASON");

        // When/Then
        assertThatThrownBy(() -> discountConsumer.consumeFact(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void consumeFact_withUnexpectedReason_shouldThrowIllegalStateException() {
        // Given
        DiscountFactEvent event = ModelUtils.getDiscountFactEvent(EventReason.ADD_DISCOUNT.name());

        // When/Then
        assertThatThrownBy(() -> discountConsumer.consumeFact(event))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void consumeDelta() {
        // Given
        DiscountFactEvent event = ModelUtils.getDiscountFactEvent(EventReason.CREATE.name());
        DiscountDto discountDto = ModelUtils.getDiscountDto();

        given(discountFactEventMapper.toDto(event)).willReturn(discountDto);

        // When
        discountConsumer.consumeDelta(event);

        // Then
        then(discountFactEventMapper).should().toDto(event);
        then(discountService).should().save(discountDto);
    }

    @Test
    void consumeDelta_withDiscountFactEventAndUnexpectedReason_shouldThrowIllegalArgumentException() {
        // Given
        DiscountFactEvent event = ModelUtils.getDiscountFactEvent("UNEXPECTED_REASON");

        // When/Then
        assertThatThrownBy(() -> discountConsumer.consumeDelta(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void consumeDelta_withDiscountFactEventAndUnexpectedReason_shouldThrowIllegalStateException() {
        // Given
        DiscountFactEvent event = ModelUtils.getDiscountFactEvent(EventReason.ADD_DISCOUNT.name());

        // When/Then
        assertThatThrownBy(() -> discountConsumer.consumeDelta(event))
                .isInstanceOf(IllegalStateException.class);
    }
}

