package org.example.service.service;


import org.example.domain.constant.LogReason;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;
import org.example.domain.historyRepository.CartHistoryRepository;
import org.example.domain.repository.CartRepository;
import org.example.service.ModelUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartHistoryRepository cartHistoryRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private final CartDto cartDto = ModelUtils.getCartDto();
    private final Long cartId = 1L;
    private final Set<String> discountCodes = Set.of("DISCOUNT10");
    private final Set<ProductItemDto> productItems = Set.of(ModelUtils.getProductItemDto());
    private final PageableDto pageableDto = ModelUtils.getPageableDto();

    @Test
    void addDiscountToCartWithIdTest() {
        // Given
        given(cartRepository.getCartDtoById(cartId)).willReturn(cartDto);

        // When
        cartService.addDiscountToCartWithId(cartId, discountCodes);

        // Then
        then(cartRepository).should().addDiscountToCart(cartId, discountCodes);
        then(cartRepository).should().getCartDtoById(cartId);
        then(cartHistoryRepository).should().save(cartDto, LogReason.UPDATE);
    }

    @Test
    void addProductToCartWithIdTest() {
        // Given
        given(cartRepository.getCartDtoById(cartId)).willReturn(cartDto);

        // When
        cartService.addProductToCartWithId(cartId, productItems);

        // Then
        then(cartRepository).should().addProductToCart(cartId, productItems);
        then(cartRepository).should().getCartDtoById(cartId);
        then(cartHistoryRepository).should().save(cartDto, LogReason.UPDATE);
    }

    @Test
    void deleteByIdTest() {
        // Given
        given(cartRepository.getCartDtoById(cartId)).willReturn(cartDto);

        // When
        cartService.deleteById(cartId);

        // Then
        then(cartRepository).should().getCartDtoById(cartId);
        then(cartRepository).should().deleteCart(cartId);
        then(cartHistoryRepository).should().save(cartDto, LogReason.DELETE);
    }

    @Test
    void saveCartTest() {
        // Given
        given(cartRepository.saveCart(cartDto)).willReturn(cartDto);

        // When
        cartService.saveCart(cartDto);

        // Then
        then(cartRepository).should().saveCart(cartDto);
        then(cartHistoryRepository).should().save(cartDto, LogReason.CREATE);
    }

    @Test
    void updateCartTest() {
        // Given
        given(cartRepository.updateCart(cartDto)).willReturn(cartDto);

        // When
        cartService.updateCart(cartDto);

        // Then
        then(cartRepository).should().updateCart(cartDto);
        then(cartHistoryRepository).should().save(cartDto, LogReason.UPDATE);
    }

    @Test
    void removeDiscountFromCartWithIdTest() {
        // Given
        given(cartRepository.getCartDtoById(cartId)).willReturn(cartDto);

        // When
        cartService.removeDiscountFromCartWithId(cartId, discountCodes);

        // Then
        then(cartRepository).should().removeDiscountFromCart(cartId, discountCodes);
        then(cartRepository).should().getCartDtoById(cartId);
        then(cartHistoryRepository).should().save(cartDto, LogReason.UPDATE);
    }

    @Test
    void removeProductFromCartWithIdTest() {
        Set<Long> productIds = Set.of(1L, 2L); // Example product IDs
        given(cartRepository.getCartDtoById(cartId)).willReturn(cartDto);

        // When
        cartService.removeProductFromCartWithId(cartId, productIds);

        // Then
        then(cartRepository).should().removeProductFromCart(cartId, productIds);
        then(cartRepository).should().getCartDtoById(cartId);
        then(cartHistoryRepository).should().save(cartDto, LogReason.UPDATE);
    }

    @Test
    void findAllCartsTest() {
        // Given
        PageDto<CartDto> expectedPageDto = ModelUtils.pageDtoOf(cartDto);
        String productNameSearchQuery = "example";
        BigDecimal totalCostFrom = BigDecimal.ZERO;
        BigDecimal totalCostTo = BigDecimal.TEN;
        given(cartRepository.findAllBy(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto)).willReturn(expectedPageDto);

        // When
        PageDto<CartDto> result = cartService.findAll(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);

        // Then
        then(cartRepository).should().findAllBy(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);
        assertThat(result).isEqualTo(expectedPageDto);
    }
}

