package org.example.consumer.consumer;

import org.example.consumer.ModelUtils;
import org.example.consumer.mapper.CartDeltaEventMapper;
import org.example.consumer.mapper.CartFactEventMapper;
import org.example.delta.DeleteCartDeltaEvent;
import org.example.delta.DiscountCartDeltaEvent;
import org.example.delta.ModifyProductItemCartDeltaEvent;
import org.example.delta.RemoveProductItemCartDeltaEvent;
import org.example.domain.constant.EventReason;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.ProductItemDto;
import org.example.domain.service.CartService;
import org.example.fact.CartFactEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CartConsumerTest {

    @Mock
    private CartService cartService;

    @Mock
    private CartFactEventMapper cartFactEventMapper;

    @Mock
    private CartDeltaEventMapper cartDeltaEventMapper;

    @InjectMocks
    private CartConsumer cartConsumer;

    @Test
    void consumeFact_withCreateReason_shouldSaveCart() {
        // Given
        CartFactEvent event = ModelUtils.getCartFactEvent(EventReason.CREATE.name());
        CartDto cartDto = ModelUtils.getCartDto();

        given(cartFactEventMapper.toDto(event)).willReturn(cartDto);

        // When
        cartConsumer.consumeFact(event);

        // Then
        then(cartFactEventMapper).should().toDto(event);
        then(cartService).should().saveCart(cartDto);
    }

    @Test
    void consumeFact_withUpdateReason_shouldUpdateCart() {
        // Given
        CartFactEvent event = ModelUtils.getCartFactEvent(EventReason.UPDATE.name());
        CartDto cartDto = ModelUtils.getCartDto();

        given(cartFactEventMapper.toDto(event)).willReturn(cartDto);

        // When
        cartConsumer.consumeFact(event);

        // Then
        then(cartFactEventMapper).should().toDto(event);
        then(cartService).should().updateCart(cartDto);
    }

    @Test
    void consumeFact_withDeleteReason_shouldDeleteCart() {
        // Given
        CartFactEvent event = ModelUtils.getCartFactEvent(EventReason.DELETE.name());

        // When
        cartConsumer.consumeFact(event);

        // Then
        then(cartService).should().deleteById(event.getId());
    }

    @Test
    void consumeFact_withUnexpectedReason_shouldThrowIllegalArgumentException() {
        // Given
        CartFactEvent event = ModelUtils.getCartFactEvent("UNEXPECTED_REASON");

        // When/Then
        assertThatThrownBy(() -> cartConsumer.consumeFact(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void consumeFact_withUnexpectedReason_shouldThrowIllegalStateException() {
        // Given
        CartFactEvent event = ModelUtils.getCartFactEvent(EventReason.ADD_DISCOUNT.name());

        // When/Then
        assertThatThrownBy(() -> cartConsumer.consumeFact(event))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void consumeDelta_withCartFactEventAndCreateReason_shouldSaveCart() {
        // Given
        CartFactEvent event = ModelUtils.getCartFactEvent(EventReason.CREATE.name());
        CartDto cartDto = ModelUtils.getCartDto();

        given(cartFactEventMapper.toDto(event)).willReturn(cartDto);

        // When
        cartConsumer.consumeDelta(event);

        // Then
        then(cartFactEventMapper).should().toDto(event);
        then(cartService).should().saveCart(cartDto);
    }

    @Test
    void consumeDelta_withCartFactEventAndUnexpectedReason_shouldThrowIllegalArgumentException() {
        // Given
        CartFactEvent event = ModelUtils.getCartFactEvent("UNEXPECTED_REASON");

        // When/Then
        assertThatThrownBy(() -> cartConsumer.consumeDelta(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void consumeDelta_withCartFactEventAndUnexpectedReason_shouldThrowIllegalStateException() {
        // Given
        CartFactEvent event = ModelUtils.getCartFactEvent(EventReason.ADD_DISCOUNT.name());

        // When/Then
        assertThatThrownBy(() -> cartConsumer.consumeDelta(event))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void consumeDelta_withDeleteCartDeltaEvent_shouldDeleteCart() {
        // Given
        DeleteCartDeltaEvent event = ModelUtils.getDeleteCartDeltaEvent();

        // When
        cartConsumer.consumeDelta(event);

        // Then
        then(cartService).should().deleteById(event.getId());
    }

    @Test
    void consumeDelta_withModifyProductItemCartDeltaEvent_shouldAddProductToCart() {
        // Given
        ModifyProductItemCartDeltaEvent event = ModelUtils.getModifyProductItemCartDeltaEvent(EventReason.ADD_PRODUCT_ITEM.name());
        Set<ProductItemDto> productItemDtos = Set.of(ModelUtils.getProductItemDto());

        given(cartDeltaEventMapper.toProductItemDto(event.getProducts())).willReturn(productItemDtos);

        // When
        cartConsumer.consumeDelta(event);

        // Then
        then(cartDeltaEventMapper).should().toProductItemDto(event.getProducts());
        then(cartService).should().addProductToCartWithId(event.getId(), productItemDtos);
    }

    @Test
    void consumeDelta_withModifyProductItemCartDeltaEventAndUnexpectedReason_shouldThrowIllegalArgumentException() {
        // Given
        ModifyProductItemCartDeltaEvent event = ModelUtils.getModifyProductItemCartDeltaEvent("UNEXPECTED_REASON");

        // When/Then
        assertThatThrownBy(() -> cartConsumer.consumeDelta(event))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void consumeDelta_withModifyProductItemCartDeltaEventAndUnexpectedReason_shouldThrowIllegalStateException() {
        // Given
        ModifyProductItemCartDeltaEvent event = ModelUtils.getModifyProductItemCartDeltaEvent(EventReason.ADD_DISCOUNT.name());

        // When/Then
        assertThatThrownBy(() -> cartConsumer.consumeDelta(event))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void consumeDelta_withRemoveProductItemCartDeltaEvent_shouldRemoveProductFromCart() {
        // Given
        RemoveProductItemCartDeltaEvent event = ModelUtils.getRemoveProductItemCartDeltaEvent();

        // When
        cartConsumer.consumeDelta(event);

        // Then
        then(cartService).should().removeProductFromCartWithId(event.getId(), Set.copyOf(event.getProductIds()));
    }

    @Test
    void consumeDelta_withDiscountCartDeltaEventAndAddDiscountReason_shouldAddDiscountToCart() {
        // Given
        DiscountCartDeltaEvent event = ModelUtils.getDiscountCartDeltaEvent(EventReason.ADD_DISCOUNT.name());

        // When
        cartConsumer.consumeDelta(event);

        // Then
        then(cartService).should().addDiscountToCartWithId(event.getId(), Set.copyOf(event.getDiscounts()));
    }

    @Test
    void consumeDelta_withDiscountCartDeltaEventAndRemoveDiscountReason_shouldRemoveDiscountFromCart() {
        // Given
        DiscountCartDeltaEvent event = ModelUtils.getDiscountCartDeltaEvent(EventReason.REMOVE_DISCOUNT.name());

        // When
        cartConsumer.consumeDelta(event);

        // Then
        then(cartService).should().removeDiscountFromCartWithId(event.getId(), Set.copyOf(event.getDiscounts()));
    }

    @Test
    void consumeDelta_withDiscountCartDeltaEventAndUnexpectedReason_shouldThrowIllegalStateException() {
        // Given
        DiscountCartDeltaEvent event = ModelUtils.getDiscountCartDeltaEvent(EventReason.ADD_PRODUCT_ITEM.name());

        // When/Then
        assertThatThrownBy(() -> cartConsumer.consumeDelta(event))
                .isInstanceOf(IllegalStateException.class);
    }
}

