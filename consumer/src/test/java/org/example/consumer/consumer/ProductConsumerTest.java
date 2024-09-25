package org.example.consumer.consumer;

import org.example.consumer.ModelUtils;
import org.example.consumer.mapper.ProductFactEventMapper;
import org.example.domain.constant.EventReason;
import org.example.domain.dto.ProductDto;
import org.example.domain.service.ProductService;
import org.example.fact.ProductFactEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductConsumerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductFactEventMapper productFactEventMapper;

    @InjectMocks
    private ProductConsumer productConsumer;

    @Test
    void consumeFact() {
        // Given
        ProductFactEvent event = ModelUtils.getProductFactEvent(EventReason.CREATE.name());
        ProductDto productDto = ModelUtils.getProductDto();

        given(productFactEventMapper.toDto(event)).willReturn(productDto);

        // When
        productConsumer.consumeFact(event);

        // Then
        then(productFactEventMapper).should().toDto(event);
        then(productService).should().save(productDto);
    }

    @Test
    void consumeFact_withUnexpectedReason_shouldThrowIllegalArgumentException() {
        // Given
        ProductFactEvent event = ModelUtils.getProductFactEvent("UNEXPECTED_REASON");

        // When/Then
        assertThatThrownBy(() -> productConsumer.consumeFact(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void consumeFact_withUnexpectedReason_shouldThrowIllegalStateException() {
        // Given
        ProductFactEvent event = ModelUtils.getProductFactEvent(EventReason.ADD_DISCOUNT.name());

        // When/Then
        assertThatThrownBy(() -> productConsumer.consumeFact(event))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void consumeDelta() {
        // Given
        ProductFactEvent event = ModelUtils.getProductFactEvent(EventReason.CREATE.name());
        ProductDto productDto = ModelUtils.getProductDto();

        given(productFactEventMapper.toDto(event)).willReturn(productDto);

        // When
        productConsumer.consumeDelta(event);

        // Then
        then(productFactEventMapper).should().toDto(event);
        then(productService).should().save(productDto);
    }

    @Test
    void consumeDelta_withProductFactEventAndUnexpectedReason_shouldThrowIllegalArgumentException() {
        // Given
        ProductFactEvent event = ModelUtils.getProductFactEvent("UNEXPECTED_REASON");

        // When/Then
        assertThatThrownBy(() -> productConsumer.consumeDelta(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void consumeDelta_withProductFactEventAndUnexpectedReason_shouldThrowIllegalStateException() {
        // Given
        ProductFactEvent event = ModelUtils.getProductFactEvent(EventReason.ADD_DISCOUNT.name());

        // When/Then
        assertThatThrownBy(() -> productConsumer.consumeDelta(event))
                .isInstanceOf(IllegalStateException.class);
    }
}
